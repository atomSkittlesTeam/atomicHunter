package net.vniia.skittles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.readers.EmployeeReader;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.repositories.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final EmailService emailService;

    private final VacancyRespondRepository vacancyRespondRepository;

    private final InterviewRepository interviewRepository;
    private final EmployeeTimeMapRepository employeeTimeMapRepository;
    private final PlaceTimeMapRepository placeTimeMapRepository;

    private final InterviewEmployeeRepository interviewEmployeeRepository;

    private final InterviewReader interviewReader;

    private final EmployeeReader employeeReader;

    @Transactional
    public void createInterview(Long vacancyRespondId, InterviewDto interviewDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondId)
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        Interview interview = new Interview(vacancyRespondId, interviewDto);
        interview = interviewRepository.save(interview);

        if (interviewDto.getEmployees() != null && !interviewDto.getEmployees().isEmpty()) {
            List<EmployeeTimeMap> employeeTimes = new ArrayList<>();
            for (EmployeeDto employee : interviewDto.getEmployees()) {
                InterviewEmployee interviewEmployee = new InterviewEmployee(interview.getId(), employee.getId());
                interviewEmployeeRepository.save(interviewEmployee);
                employeeTimeMapRepository.save(new EmployeeTimeMap(employee, interviewDto));
            }
        }

        placeTimeMapRepository.save(new PlaceTimeMap(interviewDto));

        List<EmployeeDto> employeeDtoList = employeeReader
                .getInterviewEmployees(interview.getId());

        // сотрудникам
        List<String> emails = new ArrayList<>(employeeDtoList.stream().map(EmployeeDto::getEmail).toList());
        // кандидату
        emails.add(vacancyRespond.getEmail());

        this.sendInviteForInterview(emails,
                Date.from(interview.getDateStart()), Date.from(interview.getDateEnd()));
    }

    public void sendInviteForInterview(List<String> emails, Date interviewStartDate, Date interviewEndDate)
            throws Exception {
        this.emailService.sendInterviewInvite("Приглашение на собеседование!",
                emails,
                "Собеседование",
                "Собеседование в компании Атомпродукт",
                interviewStartDate,
                interviewEndDate
        );
    }

    @Transactional
    public InterviewDto updateInterviewById(Long interviewId, InterviewDto interviewDto) {
        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
            () -> new RuntimeException("Собеседование не найдено!")
        );
        interview.update(interviewDto);
        interviewRepository.saveAndFlush(interview);
        if (interviewDto.getEmployees() != null) {
            if (!interviewDto.getEmployees().isEmpty()) {
                this.mergeEmployees(interviewId, interviewDto.getEmployees());
            } else {
                List<InterviewEmployee> interviewEmployeeList = interviewEmployeeRepository
                        .findAllByInterviewId(interviewId);
                this.interviewEmployeeRepository.deleteAll(interviewEmployeeList);
            }
        }
        return interviewReader.getInterviewById(interviewId);
    }

    public void mergeEmployees(Long interviewId, List<EmployeeDto> employees) {
        List<InterviewEmployee> interviewEmployeeList = interviewEmployeeRepository
                .findAllByInterviewId(interviewId);

        Map<UUID, EmployeeDto> newMap = employees.stream()
                .collect(Collectors.toMap(EmployeeDto::getId,
                        Function.identity()));

        List<InterviewEmployee> deletedInterviewEmployees = new ArrayList<>();
        for (InterviewEmployee interviewEmployee : interviewEmployeeList) {
            if (newMap.containsKey(interviewEmployee.getEmployeeId())) {
                // ok, it's still here
                newMap.remove(interviewEmployee.getEmployeeId());
            } else {
                deletedInterviewEmployees.add(interviewEmployee);
            }
        }

        interviewEmployeeRepository.deleteAll(deletedInterviewEmployees);
        List<InterviewEmployee> list = newMap.values().stream()
                .map(e -> new InterviewEmployee(interviewId, e.getId()))
                .toList();
        interviewEmployeeRepository.saveAll(list);
    }
}

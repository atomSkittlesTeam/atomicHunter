package net.vniia.skittles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

        // @TODO send to candidate
//        this.emailService.sendInterviewInvite("Приглашение на собеседование!",
//                vacancyRespond.getEmail(), vacancyRespond.getId());

        // send to employees
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

    @Transactional
    public String validateInterview(InterviewDto interviewDto) {
        String message = "";
//        interviewDto.getPlace().getId();
        return "";
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

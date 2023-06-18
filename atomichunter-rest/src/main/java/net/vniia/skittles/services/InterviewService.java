package net.vniia.skittles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.*;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.readers.EmployeeReader;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.readers.PositionReader;
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

    private final PositionReader positionReader;

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
                employeeTimeMapRepository.save(
                        new EmployeeTimeMap(employee, interviewDto, interview.getId()));
            }
        }

        placeTimeMapRepository.save(new PlaceTimeMap(interviewDto, interview.getId()));

        this.notifyAboutInterview(interview, vacancyRespond);
    }

    private void notifyAboutInterview(Interview interview, VacancyRespond vacancyRespond) throws Exception {
        // notifications
        List<EmployeeDto> employeeDtoList = employeeReader
                .getInterviewEmployees(interview.getId());

        List<String> emails = new ArrayList<>(employeeDtoList.stream()
                .map(EmployeeDto::getEmail)
                .filter(Objects::nonNull)
                .toList());

        // кандидату
        if (vacancyRespond.getEmail() != null) {
            this.sendInviteForInterviewForRespond(vacancyRespond, vacancyRespond.getEmail(),
                    Date.from(interview.getDateStart()), Date.from(interview.getDateEnd()));
        }

        // сотрудникам
        this.sendInviteForInterviewForEmployees(
                vacancyRespond,
                emails,
                Date.from(interview.getDateStart()),
                Date.from(interview.getDateEnd()));
    }

    public void sendInviteForInterviewForRespond(VacancyRespond vacancyRespond,
                                                 String email,
                                                 Date interviewStartDate,
                                                 Date interviewEndDate)
            throws Exception {

        PositionDto position = positionReader.getPositionDtoByVacancyId(vacancyRespond.getVacancyId());

        this.emailService.sendInterviewInviteForRespond("Приглашение на собеседование!",
                Collections.singletonList(email),
                "Собеседование",
                "Собеседование в компании Атомпродукт",
                interviewStartDate,
                interviewEndDate,
                position.getName()
        );
    }

    public void sendInviteForInterviewForEmployees(VacancyRespond vacancyRespond,
                                                   List<String> emails,
                                                   Date interviewStartDate,
                                                   Date interviewEndDate)
            throws Exception {

        PositionDto position = positionReader.getPositionDtoByVacancyId(vacancyRespond.getVacancyId());

        this.emailService.sendInterviewInviteForEmployee("Участие в собеседовании!",
                emails,
                "Собеседование",
                "Собеседование кандидата " +
                        vacancyRespond.getFullName() +
                        " в компанию Атомпродукт",
                interviewStartDate,
                interviewEndDate,
                position.getName()
        );
    }

    @Transactional
    public InterviewDto updateInterviewById(Long interviewId, InterviewDto interviewDto) throws Exception {
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
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(interview.getVacancyRespondId())
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));
        this.notifyAboutInterview(interview, vacancyRespond);
        return interviewReader.getInterviewById(interviewId);
    }

    @Transactional
    public void deleteInterviewById(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId).orElseThrow(
                () -> new RuntimeException("Собеседование не найдено!")
        );
        interviewRepository.delete(interview);
        interviewEmployeeRepository.deleteAllByInterviewId(interviewId);
        employeeTimeMapRepository.deleteAllByInterviewId(interviewId);
        placeTimeMapRepository.deleteAllByInterviewId(interviewId);
    }

    @Transactional
    public List<String> validateInterview(InterviewDto interviewDto) {
        String message = "";
        interviewDto.getPlace().getId();
        if (interviewDto.getPlace() != null && interviewDto.getDateStart() != null
                && interviewDto.getDateEnd() != null) {
            message += validatePlace(interviewDto);
        }
        if (interviewDto.getEmployees() != null && !interviewDto.getEmployees().isEmpty()
                && interviewDto.getDateStart() != null && interviewDto.getDateEnd() != null) {
            message += validateEveryEmployees(interviewDto);
        }
        return Collections.singletonList(message);
    }

    @Transactional
    public String validatePlace(InterviewDto interviewDto) {
        String result = "";
        List<PlaceTimeMapDto> placeTimeMapDtos = interviewReader
                .getAllPlaceTimeMapById(interviewDto.getPlace().getId(), interviewDto.getId());
        for (PlaceTimeMapDto placeTimeMapDto : placeTimeMapDtos) {
            if (!((placeTimeMapDto.getDateStart().isAfter(interviewDto.getDateEnd()) &&
            placeTimeMapDto.getDateEnd().isAfter(interviewDto.getDateEnd()))
            ||
            (placeTimeMapDto.getDateStart().isBefore(interviewDto.getDateStart()) &&
            placeTimeMapDto.getDateEnd().isBefore(interviewDto.getDateStart()))
            )) {
                result = "Площадка " + interviewDto.getPlace().getName() + " занята на указанное время \n";
                break;
            }
        }
        return result;
    }

    @Transactional
    public String validateEveryEmployees(InterviewDto interviewDto) {
        String result = "";
        for (EmployeeDto employee : interviewDto.getEmployees()) {
            List<EmployeeTimeMapDto> employeeTimeMapDtos = interviewReader
                    .getAllEmployeeTimeMapById(employee.getId(), interviewDto.getId());
            for (EmployeeTimeMapDto employeeTimeMapDto : employeeTimeMapDtos) {
                if (!((employeeTimeMapDto.getDateStart().isAfter(interviewDto.getDateEnd()) &&
                        employeeTimeMapDto.getDateEnd().isAfter(interviewDto.getDateEnd()))
                        ||
                        (employeeTimeMapDto.getDateStart().isBefore(interviewDto.getDateStart()) &&
                                employeeTimeMapDto.getDateEnd().isBefore(interviewDto.getDateStart()))
                )) {
                    result += "Эксперт " + employee.getLastName() + " " + employee.getFirstName()
                            + " занят(а) на указанное время \n";
                    break;
                }
            }
        }
        return result;
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

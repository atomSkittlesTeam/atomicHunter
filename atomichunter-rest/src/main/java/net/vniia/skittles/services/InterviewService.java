package net.vniia.skittles.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.InterviewDto;
import net.vniia.skittles.entities.Interview;
import net.vniia.skittles.entities.InterviewEmployee;
import net.vniia.skittles.entities.VacancyCompetence;
import net.vniia.skittles.entities.VacancyRespond;
import net.vniia.skittles.readers.InterviewReader;
import net.vniia.skittles.repositories.InterviewEmployeeRepository;
import net.vniia.skittles.repositories.InterviewRepository;
import net.vniia.skittles.repositories.VacancyRespondRepository;
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

    private final InterviewEmployeeRepository interviewEmployeeRepository;

    private final InterviewReader interviewReader;

    @Transactional
    public void createInterview(Long vacancyRespondId, InterviewDto interviewDto) throws Exception {
        VacancyRespond vacancyRespond = vacancyRespondRepository.findById(vacancyRespondId)
                .orElseThrow(() -> new RuntimeException("Отклик на вакансию не найден!"));

        Interview interview = new Interview(vacancyRespondId, interviewDto);
        interview = interviewRepository.save(interview);

        if (interviewDto.getEmployees() != null && !interviewDto.getEmployees().isEmpty()) {
            for (EmployeeDto employee : interviewDto.getEmployees()) {
                InterviewEmployee interviewEmployee = new InterviewEmployee(interview.getId(), employee.getId());
                interviewEmployeeRepository.save(interviewEmployee);
            }
        }

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
        if (!interviewDto.getEmployees().isEmpty()) {

            List<InterviewEmployee> interviewEmployeeList = interviewEmployeeRepository
                    .findAllByInterviewId(interviewId);

            Map<UUID, EmployeeDto> newMap = interviewDto.getEmployees().stream()
                    .collect(Collectors.toMap(EmployeeDto::getId,
                    Function.identity()));

            List<InterviewEmployee> deletedInterviewEmployees = new ArrayList<>();
            for (InterviewEmployee interviewEmployee : interviewEmployeeList) {
                if (newMap.containsKey(interviewEmployee.getEmployeeId())) {
                    // ok, it's still here
                } else {
                    deletedInterviewEmployees.add(interviewEmployee);
                }
            }

            interviewEmployeeRepository.deleteAll(deletedInterviewEmployees);

//            vacancyCompetenceRepository.deleteAll(deletedCompetence);
//            vacancyCompetences.removeAll(deletedCompetence);
//            vacancyCompetences.addAll(newMap.values());
//            vacancyCompetenceRepository.saveAll(vacancyCompetences);


//            Map<Long, VacancyCompetence> newMap = vacancyDto.getCompetenceWeight()
//                    .stream()
//                    .map(c -> new VacancyCompetence(vacancy.getId(), c))
//                    .collect(Collectors.toMap(VacancyCompetence::getCompetenceId, Function.identity()));
//
//            List<VacancyCompetence> deletedCompetence = new ArrayList<>();
//            for (VacancyCompetence existingVacancyCompetence : vacancyCompetences) {
//                if (newMap.containsKey(existingVacancyCompetence.getCompetenceId())) {
//                    VacancyCompetence competence = newMap.get(existingVacancyCompetence.getCompetenceId());
//                    existingVacancyCompetence.setWeight(competence.getWeight());
//                    newMap.remove(existingVacancyCompetence.getCompetenceId());
//                } else {
//                    deletedCompetence.add(existingVacancyCompetence);
//                }
//            }


        } else {
            List<InterviewEmployee> interviewEmployeeList = interviewEmployeeRepository
                    .findAllByInterviewId(interviewId);
            this.interviewEmployeeRepository.deleteAll(interviewEmployeeList);
        }
        return interviewReader.getInterviewById(interviewId);
    }

    public void mergeEmployees() {

    }
}

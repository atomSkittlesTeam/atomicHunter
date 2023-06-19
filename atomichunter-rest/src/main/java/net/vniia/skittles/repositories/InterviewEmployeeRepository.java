package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.EmployeeTimeMap;
import net.vniia.skittles.entities.InterviewEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InterviewEmployeeRepository extends JpaRepository<InterviewEmployee, Long> {
    List<InterviewEmployee> findAllByInterviewId(Long interviewId);
    void deleteAllByInterviewId(Long interviewId);
    void deleteAllByEmployeeId(UUID employeeId);
    List<InterviewEmployee> findAllByEmployeeId(UUID employeeId);
}

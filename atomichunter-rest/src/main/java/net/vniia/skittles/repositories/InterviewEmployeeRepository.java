package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.InterviewEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewEmployeeRepository extends JpaRepository<InterviewEmployee, Long> {
    List<InterviewEmployee> findAllByInterviewId(Long interviewId);
    void deleteAllByInterviewId(Long interviewId);
}

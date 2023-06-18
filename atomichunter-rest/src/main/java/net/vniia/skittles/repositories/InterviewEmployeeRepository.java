package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.InterviewEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewEmployeeRepository extends JpaRepository<InterviewEmployee, Long> {
}

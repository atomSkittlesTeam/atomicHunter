package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.EmployeeTimeMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeTimeMapRepository extends JpaRepository<EmployeeTimeMap, Long> {
    void deleteAllByInterviewId(Long interviewId);
}

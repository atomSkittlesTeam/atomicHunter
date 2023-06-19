package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.EmployeeTimeMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeTimeMapRepository extends JpaRepository<EmployeeTimeMap, Long> {
    void deleteAllByInterviewId(Long interviewId);
    void deleteAllByEmployeeId(UUID employeeId);
    List<EmployeeTimeMap> findAllByEmployeeId(UUID employeeId);
    List<EmployeeTimeMap> findAllByInterviewId(Long interviewId);
}

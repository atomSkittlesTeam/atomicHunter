package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByOrderByLastNameAsc();
}

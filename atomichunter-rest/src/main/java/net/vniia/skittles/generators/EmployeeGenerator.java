package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Employee;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeGenerator {
    @Value("${generators.employee}")
    private Boolean generatorEmployeeEnable;

    private final EmployeeRepository employeeRepository;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorEmployeeEnable) {
            List<Employee> employees = employeeRepository.findAll();
            if (employees.isEmpty()) {
                this.generateEmployees();
            }
        }
    }

    private void generateEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(
                UUID.randomUUID(),
                "DEVOPS",
                UUID.randomUUID(),
                "Иванов",
                "Иван",
                "Иванович"
        ));
        employees.add(new Employee(
                UUID.randomUUID(),
                "PROGER",
                UUID.randomUUID(),
                "Сидоров",
                "Сидорв",
                "Сидорович"
        ));
        employees.add(new Employee(
                UUID.randomUUID(),
                "PM",
                UUID.randomUUID(),
                "Петров",
                "Петр",
                "Иванович"
        ));
        employeeRepository.saveAll(employees);
    }
}

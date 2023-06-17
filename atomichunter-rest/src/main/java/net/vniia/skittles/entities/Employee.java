package net.vniia.skittles.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private UUID id;
    private String positionId;
    private UUID staffUnitId;
    private String firstName;
    private String lastName;
    private String email;

    public Employee(EmployeeDto employeeDto) {
        this.id = employeeDto.getId();
        this.positionId = employeeDto.getPositionId();
        this.staffUnitId = employeeDto.getStaffUnitId();
        this.firstName = employeeDto.getFirstName();
        this.lastName = employeeDto.getLastName();
        this.email = employeeDto.getEmail();
    }
}

package net.vniia.skittles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vniia.skittles.entities.Employee;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private UUID id;
    private String positionId;
    private UUID staffUnitId;
    private String firstName;
    private String lastName;
    private String email;
    private String employeeFullName;

    public EmployeeDto(Employee employee) {
        this.id = employee.getId();
        this.positionId = employee.getPositionId();
        this.staffUnitId = employee.getStaffUnitId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
    }
}

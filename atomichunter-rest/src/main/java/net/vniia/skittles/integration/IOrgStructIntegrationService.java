package net.vniia.skittles.integration;

import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.entities.VacancyRespond;

import java.util.List;

public interface IOrgStructIntegrationService {
    List<StaffUnitDto> getAllStaffUnits();
    List<StaffUnitDto> getAllStaffUnitsByStatus(String status);
    public StaffUnitDto getStaffUnitById(String staffUnitId);
    List<EmployeeDto> getAllEmployees();
    List<EmployeeDto> getAllEmployeesByPositionId(String positionId);
    EmployeeDto getAllEmployeeById(String employeeId);
    void registerEmployee(Vacancy vacancy, VacancyRespond vacancyRespond);
    List<PositionDto> getAllPositions();
    PositionDto getPositionById(String positionId);
}

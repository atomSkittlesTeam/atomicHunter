package net.vniia.skittles.integration;

import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Vacancy;
import net.vniia.skittles.entities.VacancyRespond;

import java.util.ArrayList;
import java.util.List;

public class DummyOrgStructIntegrationService implements IOrgStructIntegrationService {

    @Override
    public List<StaffUnitDto> getAllStaffUnits() {
        return new ArrayList<>();
    }

    @Override
    public List<StaffUnitDto> getAllStaffUnitsByStatus(String status) {
        return new ArrayList<>();
    }

    @Override
    public StaffUnitDto getStaffUnitById(String staffUnitId) {
        return null;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return new ArrayList<>();
    }

    @Override
    public List<EmployeeDto> getAllEmployeesByPositionId(String positionId) {
        return new ArrayList<>();
    }

    @Override
    public EmployeeDto getAllEmployeeById(String employeeId) {
        return null;
    }

    @Override
    public void registerEmployee(Vacancy vacancy, VacancyRespond vacancyRespond) {

    }

    @Override
    public List<PositionDto> getAllPositions() {
        return new ArrayList<>();
    }

    @Override
    public PositionDto getPositionById(String positionId) {
        return null;
    }
}

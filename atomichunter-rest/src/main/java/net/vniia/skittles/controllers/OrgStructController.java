package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Employee;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.readers.EmployeeReader;
import net.vniia.skittles.readers.PositionReader;
import net.vniia.skittles.readers.StaffUnitReader;
import net.vniia.skittles.repositories.EmployeeRepository;
import net.vniia.skittles.repositories.PositionRepository;
import net.vniia.skittles.repositories.StaffUnitRepository;
import net.vniia.skittles.services.OrgStructService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("org-struct")
@RequiredArgsConstructor
public class OrgStructController {

    private final OrgStructService orgStructService;

    private final StaffUnitReader staffUnitReader;

    private final EmployeeReader employeeReader;

    @GetMapping("staff-units")
    public List<StaffUnitDto> getAllStaffUnits() {
        return staffUnitReader.getAllStaffUnits();
    }

    @GetMapping("staff-units/open")
    public List<StaffUnitDto> getOpenStaffUnits() {
        return staffUnitReader.getOpenStaffUnits();
    }

    @GetMapping("employees")
    public List<EmployeeDto> getAllEmployees() {
        return orgStructService.getAllEmployees();
    }

    @GetMapping("employees/interview/{interviewId}")
    public List<EmployeeDto> getEmployeesForInterview(@PathVariable Long interviewId) {
        return orgStructService.getEmployeesForInterview(interviewId);
    }

    @GetMapping("employees/hr")
    public List<EmployeeDto> getHrEmployees() {
        return employeeReader.getHrEmployees();
    }

    @GetMapping("positions")
    public List<PositionDto> getAllPositions() {
        return orgStructService.getAllPositions();
    }



}

package net.vniia.skittles.controllers;

import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Employee;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.readers.PositionReader;
import net.vniia.skittles.repositories.EmployeeRepository;
import net.vniia.skittles.repositories.PositionRepository;
import net.vniia.skittles.repositories.StaffUnitRepository;
import net.vniia.skittles.services.OrgStructService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("org-struct")
@RequiredArgsConstructor
public class OrgStructController {

    private final OrgStructService orgStructService;

    @GetMapping("staff-units")
    public List<StaffUnitDto> getAllStaffUnits() {
        return orgStructService.getAllStaffUnits();
    }

    @GetMapping("employees")
    public List<EmployeeDto> getAllEmployees() {
        return orgStructService.getAllEmployees();
    }

    @GetMapping("positions")
    public List<PositionDto> getAllPositions() {
        return orgStructService.getAllPositions();
    }



}

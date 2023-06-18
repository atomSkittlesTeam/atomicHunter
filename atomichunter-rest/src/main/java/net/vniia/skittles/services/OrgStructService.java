package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Employee;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.integration.OrgStructIntegrationService;
import net.vniia.skittles.repositories.EmployeeRepository;
import net.vniia.skittles.repositories.PositionRepository;
import net.vniia.skittles.repositories.StaffUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrgStructService {

    private final OrgStructIntegrationService orgStructIntegrationService;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final StaffUnitRepository staffUnitRepository;


    @Scheduled(fixedDelayString = "${scheduled.staff-units}")
    public void getAllStaffUnitsFromRestAndSave() {
        List<StaffUnitDto> allStaffUnits = orgStructIntegrationService.getAllStaffUnits();
        List<StaffUnit> allStaffUnitsEntities = new ArrayList<>();
        allStaffUnits.forEach(e -> allStaffUnitsEntities.add(new StaffUnit(e)));
        staffUnitRepository.saveAll(allStaffUnitsEntities);
        log.info("staff units saved");
    }

    @Scheduled(fixedDelayString = "${scheduled.employees}")
    public void getAllEmployeesFromRestAndSave() {
        List<EmployeeDto> allEmployees = orgStructIntegrationService.getAllEmployees();
        List<Employee> allEmployeesEntities = new ArrayList<>();
        allEmployees.forEach(e -> allEmployeesEntities.add(new Employee(e)));
        employeeRepository.saveAll(allEmployeesEntities);
        log.info("employees saved");
    }

    @Scheduled(fixedDelayString = "${scheduled.positions}")
    public void getAllPositionsFromRestAndSave() {
        List<PositionDto> allPositions = orgStructIntegrationService.getAllPositions();
        List<Position> allPositionsEntities = new ArrayList<>();
        allPositions.forEach(e -> allPositionsEntities.add(new Position(e)));
        positionRepository.saveAll(allPositionsEntities);
        log.info("positions saved");
    }

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAllByOrderByLastNameAsc();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employees.forEach(e -> {
            EmployeeDto employeeDto = new EmployeeDto(e);
            employeeDto.setEmployeeFullName(employeeDto.getLastName()
                    + " "
                    + employeeDto.getFirstName());
            employeeDtos.add(employeeDto);
        });
        return employeeDtos;
    }

    public List<PositionDto> getAllPositions() {
        List<Position> positions = positionRepository.findAll();
        List<PositionDto> positionDtos = new ArrayList<>();
        positions.forEach(e -> positionDtos.add(new PositionDto(e)));
        return positionDtos;
    }
}

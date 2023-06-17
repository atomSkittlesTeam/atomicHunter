package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.Employee;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.entities.RequestPosition;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.integration.OrgStructIntegrationService;
import net.vniia.skittles.readers.RequestReader;
import net.vniia.skittles.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrgStructService {

    private final OrgStructIntegrationService orgStructIntegrationService;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final StaffUnitRepository staffUnitRepository;

    @Autowired
    private JPAQueryFactory queryFactory;


    @Scheduled(fixedDelay = 30000)
    public void getAllStaffUnitsAndSave() {
        List<StaffUnitDto> allStaffUnits = orgStructIntegrationService.getAllStaffUnits();
        List<StaffUnit> allStaffUnitsEntities = new ArrayList<>();
        allStaffUnits.forEach(e -> allStaffUnitsEntities.add(new StaffUnit(e)));
        staffUnitRepository.saveAll(allStaffUnitsEntities);
        log.info("staff units saved");
    }

    @Scheduled(fixedDelay = 30000)
    public void getAllEmployeesAndSave() {
        List<EmployeeDto> allEmployees = orgStructIntegrationService.getAllEmployees();
        List<Employee> allEmployeesEntities = new ArrayList<>();
        allEmployees.forEach(e -> allEmployeesEntities.add(new Employee(e)));
        employeeRepository.saveAll(allEmployeesEntities);
        log.info("employees saved");
    }

    @Scheduled(fixedDelay = 30000)
    public void getAllPositionsAndSave() {
        List<PositionDto> allPositions = orgStructIntegrationService.getAllPositions();
        List<Position> allPositionsEntities = new ArrayList<>();
        allPositions.forEach(e -> allPositionsEntities.add(new Position(e)));
        positionRepository.saveAll(allPositionsEntities);
        log.info("positions saved");
    }

}

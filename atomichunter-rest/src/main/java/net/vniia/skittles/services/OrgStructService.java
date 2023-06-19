package net.vniia.skittles.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.dto.PositionDto;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.*;
import net.vniia.skittles.integration.OrgStructIntegrationService;
import net.vniia.skittles.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrgStructService {

    private final OrgStructIntegrationService orgStructIntegrationService;
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeTimeMapRepository employeeTimeMapRepository;
    private final PlaceTimeMapRepository placeTimeMapRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewEmployeeRepository interviewEmployeeRepository;
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
    @Transactional
    public void getAllEmployeesFromRestAndSave() {
        List<EmployeeDto> allEmployees = orgStructIntegrationService.getAllEmployees();
        List<Employee> actualEmployees = new ArrayList<>();
        allEmployees.forEach(e -> actualEmployees.add(new Employee(e)));
        List<Employee> employeesInBase = employeeRepository.findAll();
        List<UUID> actualEmployeesIds = actualEmployees.stream().map(Employee::getId).toList();
        List<UUID> employeesFromDBIds = new ArrayList<>(employeesInBase.stream().map(Employee::getId).toList());
        employeesFromDBIds.removeAll(actualEmployeesIds);
        List<UUID> firedEmployees = employeesFromDBIds;
        deleteEmployeeFromEveryDBOnFire(firedEmployees);
        employeeRepository.saveAll(actualEmployees);

        log.info("employees saved");
    }

    @Transactional
    public void deleteEmployeeFromEveryDBOnFire(List<UUID> firedEmployees) {
        for (UUID firedEmployee : firedEmployees) {
            log.info("Сотрудник " + firedEmployee + " уволен. Удаление из таблиц...");
            List<InterviewEmployee> listInterviewEmployee = interviewEmployeeRepository.findAllByEmployeeId(firedEmployee);
            List<Long> interviewIds = listInterviewEmployee.stream().map(InterviewEmployee::getInterviewId).toList();
            listInterviewEmployee = null;
            employeeTimeMapRepository.deleteAllByEmployeeId(firedEmployee);
            employeeTimeMapRepository.flush();
            log.info("Сотрудник удален из таблицы расписания времени");
            interviewEmployeeRepository.deleteAllByEmployeeId(firedEmployee);
            interviewEmployeeRepository.flush();
            log.info("Сотрудник удален из таблицы собеседующих");
            for (Long interviewId : interviewIds) {
                if(employeeTimeMapRepository.findAllByInterviewId(interviewId).size() == 0) {
                    interviewRepository.deleteById(interviewId);
                    log.info("Удалено интервью, так как в нем не осталось собеседующих");
                    placeTimeMapRepository.deleteAllByInterviewId(interviewId);
                    log.info("Место собеседования удалено из таблицы расписания времени");
                }
            }
        }
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

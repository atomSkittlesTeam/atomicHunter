package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.QEmployee;
import net.vniia.skittles.entities.QPosition;
import net.vniia.skittles.entities.QStaffUnit;
import net.vniia.skittles.entities.QVacancy;
import net.vniia.skittles.enums.StaffUnitStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StaffUnitReader {

    private static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    private static final QEmployee employee = QEmployee.employee;

    private static final QPosition position = QPosition.position;

    private static final QVacancy vacancy = QVacancy.vacancy;

    private final JPAQueryFactory queryFactory;

    public static QBean<StaffUnitDto> getMappedSelectForStaffUnitDto() {
        return Projections.bean(StaffUnitDto.class,
                staffUnit.id,
                staffUnit.employeeId,
                EmployeeReader.getMappedSelectForEmployeeDto().as("employee"),
                staffUnit.positionId,
                PositionReader.getMappedSelectForPositionDto().as("position"),
                staffUnit.closeTime,
                staffUnit.status,
                vacancy.id.as("vacancyId"));
    }

    public List<StaffUnitDto> getAllStaffUnits() {
        return queryFactory.from(staffUnit)
                .leftJoin(employee).on(employee.id.eq(staffUnit.employeeId))
                .leftJoin(position).on(position.id.eq(staffUnit.positionId))
                .leftJoin(vacancy).on(vacancy.staffUnitId.eq(staffUnit.id))
                .select(getMappedSelectForStaffUnitDto())
                .fetch();
    }

    public List<StaffUnitDto> getOpenStaffUnits() {
        return queryFactory.from(staffUnit)
                .leftJoin(employee).on(employee.id.eq(staffUnit.employeeId))
                .leftJoin(position).on(position.id.eq(staffUnit.positionId))
                .leftJoin(vacancy).on(vacancy.staffUnitId.eq(staffUnit.id))
                .where(staffUnit.status.eq(StaffUnitStatus.Opened))
                .select(getMappedSelectForStaffUnitDto())
                .fetch();
    }
}

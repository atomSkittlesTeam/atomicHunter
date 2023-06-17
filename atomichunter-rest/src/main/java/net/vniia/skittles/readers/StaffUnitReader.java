package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.QEmployee;
import net.vniia.skittles.entities.QPosition;
import net.vniia.skittles.entities.QStaffUnit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StaffUnitReader {

    private static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    private static final QEmployee employee = QEmployee.employee;

    private static final QPosition position = QPosition.position;

    private final JPAQueryFactory queryFactory;

    public static QBean<StaffUnitDto> getMappedSelectForStaffUnitDto() {
        return Projections.bean(StaffUnitDto.class,
                staffUnit.id,
                staffUnit.employeeId,
                EmployeeReader.getMappedSelectForEmployeeDto().as("employee"),
                staffUnit.positionId,
                PositionReader.getMappedSelectForPositionDto().as("position"),
                staffUnit.closeTime,
                staffUnit.status);
    }

    public List<StaffUnitDto> getAllStaffUnits() {
        return queryFactory.from(staffUnit)
                .leftJoin(employee).on(employee.id.eq(staffUnit.employeeId))
                .leftJoin(position).on(position.id.eq(staffUnit.positionId))
                .select(getMappedSelectForStaffUnitDto())
                .fetch();
    }
}

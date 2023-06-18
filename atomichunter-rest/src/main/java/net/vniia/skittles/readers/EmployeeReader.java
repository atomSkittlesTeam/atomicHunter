package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.entities.QEmployee;
import net.vniia.skittles.entities.QInterviewEmployee;
import net.vniia.skittles.entities.QStaffUnit;
import net.vniia.skittles.enums.StaffUnitStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeReader {
    private static final QEmployee employee = QEmployee.employee;

    private static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    private static final QInterviewEmployee interviewEmployee = QInterviewEmployee.interviewEmployee;

    private final JPAQueryFactory queryFactory;

    public static QBean<EmployeeDto> getMappedSelectForEmployeeDto() {
        return Projections.bean(EmployeeDto.class,
                employee.id,
                employee.positionId,
                employee.staffUnitId,
                employee.firstName,
                employee.lastName,
                employee.email);
    }

    public List<EmployeeDto> getHrEmployees() {
        List<EmployeeDto> hrs = queryFactory.from(employee)
                .innerJoin(staffUnit).on(staffUnit.id.eq(employee.staffUnitId).and(staffUnit.status
                        .ne(StaffUnitStatus.Opened).or(staffUnit.closeTime.isNotNull())))
                .where(employee.positionId.eq("hr"))
                .select(getMappedSelectForEmployeeDto())
                .fetch();

        for (EmployeeDto hr : hrs) {
            hr.setEmployeeFullName(hr.getLastName() + " " + hr.getFirstName());
        }

        return hrs;
    }

    public List<EmployeeDto> getInterviewEmployees(Long interviewId) {
        List<EmployeeDto> hrs = queryFactory.from(employee)
                .innerJoin(interviewEmployee).on(interviewEmployee.employeeId.eq(employee.id)
                        .and(interviewEmployee.interviewId.eq(interviewId)))
                .select(getMappedSelectForEmployeeDto())
                .fetch();

        for (EmployeeDto hr : hrs) {
            hr.setEmployeeFullName(hr.getLastName() + " " + hr.getFirstName());
        }
        return hrs;
    }
}

package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.entities.QEmployee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeReader {
    private static final QEmployee employee = QEmployee.employee;

    private final JPAQueryFactory queryFactory;

    public static QBean<EmployeeDto> getMappedSelectForEmployeeDto() {
        StringExpression expression = employee.lastName.concat(" ").concat(employee.firstName);
        return Projections.bean(EmployeeDto.class,
                employee.id,
                employee.positionId,
                employee.staffUnitId,
                employee.firstName,
                employee.lastName,
                employee.email,
                expression.as("fullName"));
    }

    public List<EmployeeDto> getHrEmployees() {
        return queryFactory.from(employee)
                .where(employee.positionId.eq("hr"))
                .select(getMappedSelectForEmployeeDto())
                .fetch();
    }
}

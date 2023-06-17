package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import net.vniia.skittles.dto.EmployeeDto;
import net.vniia.skittles.entities.QEmployee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeReader {
    private static final QEmployee employee = QEmployee.employee;

    public static QBean<EmployeeDto> getMappedSelectForEmployeeDto() {
        return Projections.bean(EmployeeDto.class,
                employee.id,
                employee.positionId,
                employee.staffUnitId,
                employee.firstName,
                employee.lastName,
                employee.email);
    }
}

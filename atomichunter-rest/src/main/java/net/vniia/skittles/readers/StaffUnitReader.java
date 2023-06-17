package net.vniia.skittles.readers;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.dto.StaffUnitDto;
import net.vniia.skittles.entities.QStaffUnit;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StaffUnitReader {

    private static final QStaffUnit staffUnit = QStaffUnit.staffUnit;

    public static QBean<StaffUnitDto> getMappedSelectForStaffUnitDto() {
        return Projections.bean(StaffUnitDto.class,
                staffUnit.id,
                staffUnit.employeeId,
                staffUnit.positionId,
                staffUnit.closeTime,
                staffUnit.status);
    }
}

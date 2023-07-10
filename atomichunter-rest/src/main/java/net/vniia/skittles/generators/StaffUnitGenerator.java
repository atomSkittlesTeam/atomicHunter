package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.enums.StaffUnitStatus;
import net.vniia.skittles.repositories.StaffUnitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffUnitGenerator {
    @Value("${generators.staffunit}")
    private Boolean generatorStaffunitEnable;

    private final StaffUnitRepository staffUnitRepository;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorStaffunitEnable) {
            List<StaffUnit> staffUnits = staffUnitRepository.findAll();
            if (staffUnits.isEmpty()) {
                this.generateStaffUnits();
            }
        }
    }

    private void generateStaffUnits() {
        List<StaffUnit> staffUnits = new ArrayList<>();

        staffUnits.add(new StaffUnit(
                UUID.randomUUID(),
                "PROGER",
                UUID.randomUUID(),
                StaffUnitStatus.Opened,
                null
        ));

        staffUnits.add(new StaffUnit(
                UUID.randomUUID(),
                "DEVOPS",
                UUID.randomUUID(),
                StaffUnitStatus.Opened,
                null
        ));

        staffUnits.add(new StaffUnit(
                UUID.randomUUID(),
                "PM",
                UUID.randomUUID(),
                StaffUnitStatus.Opened,
                null
        ));

       staffUnitRepository.saveAll(staffUnits);
    }
}

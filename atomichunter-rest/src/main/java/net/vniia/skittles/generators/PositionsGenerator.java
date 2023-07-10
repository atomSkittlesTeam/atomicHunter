package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.entities.StaffUnit;
import net.vniia.skittles.enums.StaffUnitStatus;
import net.vniia.skittles.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionsGenerator {
    @Value("${generators.positions}")
    private Boolean generatorPositionsEnable;

    private final PositionRepository positionRepository;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorPositionsEnable) {
            List<Position> positions = positionRepository.findAll();
            if (positions.isEmpty()) {
                this.generateStaffUnits();
            }
        }
    }

    private void generateStaffUnits() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(
                "PROGER",
                "Программист"
        ));
        positions.add(new Position(
                "DEVOPS",
                "devOps"
        ));
        positions.add(new Position(
                "PM",
                "Project Manager"
        ));

        positionRepository.saveAll(positions);
    }
}

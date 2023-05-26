package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.repositories.PositionRepository;
import net.vniia.skittles.templates.VacancyTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionGenerator {

    private final PositionRepository positionRepository;

    @Value("${generators.positions}")
    private Boolean generatorPositionEnable;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorPositionEnable) {
            List<Position> positions = positionRepository.findAll();
            if (positions.isEmpty()) {
                List<Position> generatedPositions = this.generatePositions();
            }
        }
    }

    private List<Position> generatePositions() {
        ArrayList<Position> generatedList = new ArrayList<>(10);
        long id = 1L;
        for (String positionName : VacancyTemplate.allPositionNames) {
            Position position = new Position(id++, positionName, positionName + " " + Math.random());
            generatedList.add(position);
            positionRepository.saveAllAndFlush(generatedList);
        }
        return generatedList;
    }
}

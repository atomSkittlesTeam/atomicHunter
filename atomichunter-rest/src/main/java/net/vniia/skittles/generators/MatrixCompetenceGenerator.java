package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.vniia.skittles.entities.Competence;
import net.vniia.skittles.entities.MatrixCompetence;
import net.vniia.skittles.entities.Position;
import net.vniia.skittles.repositories.CompetenceRepository;
import net.vniia.skittles.repositories.MatrixCompetenceRepository;
import net.vniia.skittles.repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatrixCompetenceGenerator {
    private final MatrixCompetenceRepository matrixCompetenceRepository;

    private final CompetenceRepository competenceRepository;

    private final PositionRepository positionRepository;

    @Value("${generators.matrixCompetence}")
    private Boolean generatorMatrixCompetenceEnable;

    @PostConstruct
    public void runGenerator() {
        if (this.generatorMatrixCompetenceEnable) {
            List<MatrixCompetence> matrixCompetences = matrixCompetenceRepository.findAll();
            if (matrixCompetences.isEmpty()) {
                List<MatrixCompetence> generatedMatrix = this.generatorMatrixCompetence();
            }
        }
    }

    private List<MatrixCompetence> generatorMatrixCompetence() {
        ArrayList<MatrixCompetence> matrix = new ArrayList<>();
        List<Competence> competences = competenceRepository.findAll();
        if (competences.isEmpty()) {
            System.out.println("competences is empty");
        } else {
            List <Position> positions = positionRepository.findAll();
            if (positions.isEmpty()) {
                System.out.println("positions is empty");
            } else {
                long id = 1L;
                for (Position position : positions) {
                    if (position.getName().equals("Программист")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 4L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 7L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 8L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Уборщик")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 9L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Повар")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 9L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Охранник")) {
                        matrix.add(new MatrixCompetence(id++, 6L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Водитель")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 6L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Бухгалтер")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 9L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Электрик")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 2L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Монтер")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Слесарь")) {
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Токарь")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 2L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 3L, Long.parseLong(position.getId())));
                        matrix.add(new MatrixCompetence(id++, 5L, Long.parseLong(position.getId())));
                    }
                    if (position.getName().equals("Курьер")) {
                        matrix.add(new MatrixCompetence(id++, 1L, Long.parseLong(position.getId())));
                    }
                }
                matrixCompetenceRepository.saveAll(matrix);
            }
        }
        return matrix;
    }
}

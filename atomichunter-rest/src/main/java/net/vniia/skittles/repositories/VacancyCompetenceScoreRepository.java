package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.VacancyCompetenceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VacancyCompetenceScoreRepository extends JpaRepository<VacancyCompetenceScore, Long> {
    List<VacancyCompetenceScore> findAllByVacancyCompetenceId(Long vacancyCompetenceId);
    List<VacancyCompetenceScore> findAllByEmployeeId(Long employeeId);
    List<VacancyCompetenceScore> findAllByEmployeeIdAndVacancyRespondId(UUID employeeId, Long vacancyRespondId);
}

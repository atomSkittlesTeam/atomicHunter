package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.VacancyCompetenceScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyCompetenceScoreRepository extends JpaRepository<VacancyCompetenceScore, Long> {
    List<VacancyCompetenceScore> findAllByVacancyCompetenceId(Long vacancyCompetenceId);
    List<VacancyCompetenceScore> findAllByMaintainerId(Long maintainerId);
}

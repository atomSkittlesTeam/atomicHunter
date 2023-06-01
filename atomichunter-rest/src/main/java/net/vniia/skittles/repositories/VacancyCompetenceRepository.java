package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.VacancyCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyCompetenceRepository extends JpaRepository<VacancyCompetence, Long> {
    List<VacancyCompetence> findAllByVacancyId(Long vacancyId);
}

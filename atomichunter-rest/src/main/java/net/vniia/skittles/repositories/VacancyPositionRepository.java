package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.VacancyRespond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyPositionRepository extends JpaRepository<VacancyRespond, Long> {
}

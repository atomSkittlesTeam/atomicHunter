package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.VacancyPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyPositionRepository extends JpaRepository<VacancyPosition, Long> {
}

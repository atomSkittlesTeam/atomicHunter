package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}

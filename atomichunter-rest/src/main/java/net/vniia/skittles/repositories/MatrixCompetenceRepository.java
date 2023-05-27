package net.vniia.skittles.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixCompetenceRepository extends JpaRepository<net.vniia.skittles.entities.MatrixCompetence, Long> {
}

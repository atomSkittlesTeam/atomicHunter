package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}

package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.RequestPosition;
import org.springframework.data.jpa.repository.JpaRepository;

@Deprecated
public interface RequestPositionRepository extends JpaRepository<RequestPosition, Long> {
}

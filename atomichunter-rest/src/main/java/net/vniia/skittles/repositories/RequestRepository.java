package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface RequestRepository extends JpaRepository<Request, Long> {
}

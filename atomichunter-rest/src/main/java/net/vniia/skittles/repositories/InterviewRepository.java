package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}

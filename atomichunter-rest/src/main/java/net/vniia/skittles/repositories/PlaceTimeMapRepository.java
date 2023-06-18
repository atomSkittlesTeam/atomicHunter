package net.vniia.skittles.repositories;

import net.vniia.skittles.entities.PlaceTimeMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTimeMapRepository extends JpaRepository<PlaceTimeMap, Long> {
    void deleteAllByInterviewId(Long interviewId);
}

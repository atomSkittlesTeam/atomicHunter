package net.vniia.skittles.repositories;


import net.vniia.skittles.entities.TelegramMailingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMailingHistoryRepository extends JpaRepository<TelegramMailingHistory, Long> {
}

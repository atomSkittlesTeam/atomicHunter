package net.vniia.skittles.repositories;


import net.vniia.skittles.entities.TelegramChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramChatHistoryRepository extends JpaRepository<TelegramChatHistory, Long> {
    TelegramChatHistory findByMessageId(Long messageId);

    TelegramChatHistory findByUserId(Long userId);

    TelegramChatHistory findByUserName(String userName);
}

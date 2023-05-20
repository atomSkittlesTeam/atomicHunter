package net.vniia.skittles.repositories;


import net.vniia.skittles.entities.TelegramSubscriber;
import net.vniia.skittles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramSubscriberRepository extends JpaRepository<TelegramSubscriber, Long> {
    TelegramSubscriber findByLogin(String login);
    TelegramSubscriber findByRole(String role);
    List<TelegramSubscriber> findAllByRole(String role);
    TelegramSubscriber findByTelegramId(Long telegramId);
    TelegramSubscriber findByTelegramName(String telegramName);
}

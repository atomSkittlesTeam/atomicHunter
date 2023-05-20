package net.vniia.skittles.repositories;


import net.vniia.skittles.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByRole(String role);
    List<User> findAllByRole(String role);
}

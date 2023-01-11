package com.example.tgbot.repositories;

import com.example.tgbot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //todo А по СhatId?
    Optional<User> findUserByName(String name);
}

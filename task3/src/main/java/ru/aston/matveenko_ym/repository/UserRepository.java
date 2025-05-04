package ru.aston.matveenko_ym.repository;

import ru.aston.matveenko_ym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
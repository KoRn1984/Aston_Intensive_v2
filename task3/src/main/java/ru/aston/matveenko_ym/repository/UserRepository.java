package ru.aston.matveenko_ym.repository;

import org.springframework.stereotype.Repository;
import ru.aston.matveenko_ym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
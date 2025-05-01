package ru.aston.matveenko_ym.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUser {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUser.class);

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.error("Name cannot be empty!");
            throw new IllegalArgumentException("Name cannot be empty!");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            logger.error("Invalid email format!");
            throw new IllegalArgumentException("Invalid email format!");
        }
    }
}
package ru.aston.matveenko_ym;

import ru.aston.matveenko_ym.service.UserService;

public class UserApplication {
    UserService userService;

    {
        userService = new UserService();
    }

    public static void main(String[] args) {
        new UserService().runApplication();
    }
}
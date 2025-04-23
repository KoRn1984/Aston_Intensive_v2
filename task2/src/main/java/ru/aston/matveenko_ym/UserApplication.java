package ru.aston.matveenko_ym;

import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

public class UserApplication {
    UserServiceImpl userServiceImpl;

    {
        userServiceImpl = new UserServiceImpl();
    }

    public static void main(String[] args) {
        new UserServiceImpl().runApplication();
    }
}
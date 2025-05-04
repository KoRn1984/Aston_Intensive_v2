package ru.aston.matveenko_ym.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.matveenko_ym.UserDao;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.service.UserService;
import ru.aston.matveenko_ym.util.HibernateUtil;
import ru.aston.matveenko_ym.validation.ValidationUser;

import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final Scanner scanner;

    public UserServiceImpl() {
        this.userDao = new UserDao();
        this.scanner = new Scanner(System.in);
    }

    public UserServiceImpl(UserDao userDao, Scanner scanner) {
        this.userDao = userDao;
        this.scanner = scanner;
    }

    public void runApplication() {
        logger.info("Application started!");
        while (true) {
            printMenu();
            int option = getUserInputInt("Choose an option: ");
            switch (option) {
                case 1:
                    createUser();
                    break;
                case 2:
                    getUserById();
                    break;
                case 3:
                    getAllUsers();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    deleteUser();
                    break;
                case 6:
                    shutdownApplication();
                    return;
                default:
                    logger.error("Invalid option!");
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Create User.");
        System.out.println("2. Get User by ID.");
        System.out.println("3. Get All Users.");
        System.out.println("4. Update User.");
        System.out.println("5. Delete User.");
        System.out.println("6. Exit.");
    }

    public void createUser() {
        String name;
        while (true) {
            System.out.print("Enter name: ");
            name = scanner.next();
            try {
                ValidationUser.validateName(name);
                break;
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
            }
        }
        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.next();
            try {
                ValidationUser.validateEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
            }
        }
        int age = getUserInputInt("Enter age: ");
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setAge(age);
        userDao.createUser(newUser);
    }

    public void getUserById() {
        Long id = getUserInputLong("Enter user ID: ");
        User user = userDao.getUserById(id);
        System.out.println(user != null ? user : "User not found!");
    }

    public void getAllUsers() {
        List<User> users = userDao.getAllUsers();
        users.forEach(System.out::println);
    }

    public void updateUser() {
        Long userId = getUserInputLong("Enter user ID: ");
        User userToUpdate = userDao.getUserById(userId);
        if (userToUpdate != null) {
            String newName;
            while (true) {
                System.out.print("Enter new name: ");
                newName = scanner.next();
                try {
                    ValidationUser.validateName(newName);
                    break;
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage());
                }
            }
            userToUpdate.setName(newName);
            String newEmail;
            while (true) {
                System.out.print("Enter new email: ");
                newEmail = scanner.next();
                try {
                    ValidationUser.validateEmail(newEmail);
                    break;
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage());
                }
            }
            userToUpdate.setEmail(newEmail);
            userToUpdate.setAge(getUserInputInt("Enter new age: "));
            userDao.updateUser(userToUpdate);
        } else {
            logger.error("User not found!");
        }
    }

    public void deleteUser() {
        Long userId = getUserInputLong("Enter user ID to delete: ");
        userDao.deleteUser(userId);
    }

    private void shutdownApplication() {
        HibernateUtil.shutdown();
        System.out.println("Exiting...");
        logger.info("Application finished!");
    }

    public int getUserInputInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (Exception e) {
                logger.error("Invalid input! Please enter a valid number.");
                scanner.next(); // чистка некорректного ввода
            }
        }
    }

    public Long getUserInputLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextLong();
            } catch (Exception e) {
                logger.error("Invalid input! Please enter a valid ID.");
                scanner.next(); // чистка некорректного ввода
            }
        }
    }
}
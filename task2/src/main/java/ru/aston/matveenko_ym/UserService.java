package ru.aston.matveenko_ym;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.matveenko_ym.dao.UserDao;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static void main(String[] args) {
        logger.info("Application started!");
        UserDao userDao = new UserDao();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Create User.");
            System.out.println("2. Get User by ID.");
            System.out.println("3. Get All Users.");
            System.out.println("4. Update User.");
            System.out.println("5. Delete User.");
            System.out.println("6. Exit.");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.next();
                    System.out.print("Enter email: ");
                    String email = scanner.next();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();

                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setAge(age);
                    userDao.createUser(newUser);
                    break;

                case 2:
                    System.out.print("Enter user ID: ");
                    Long id = scanner.nextLong();
                    User user = userDao.getUserById(id);
                    System.out.println(user != null ? user : "User not found!");
                    break;

                case 3:
                    List<User> users = userDao.getAllUsers();
                    users.forEach(System.out::println);
                    break;

                case 4:
                    System.out.print("Enter user ID: ");
                    Long userIdToUpdate = scanner.nextLong();
                    User userToUpdate = userDao.getUserById(userIdToUpdate);
                    if (userToUpdate != null) {
                        System.out.print("Enter new name: ");
                        userToUpdate.setName(scanner.next());
                        System.out.print("Enter new email: ");
                        userToUpdate.setEmail(scanner.next());
                        System.out.print("Enter new age: ");
                        userToUpdate.setAge(scanner.nextInt());
                        userDao.updateUser(userToUpdate);
                    } else {
                        System.out.println("User not found!");
                    }
                    break;

                case 5:
                    System.out.print("Enter user ID to delete: ");
                    Long userIdToDelete = scanner.nextLong();
                    userDao.deleteUser(userIdToDelete);
                    break;

                case 6:
                    HibernateUtil.shutdown();
                    System.out.println("Exiting...");
                    logger.info("Application finished!");
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}
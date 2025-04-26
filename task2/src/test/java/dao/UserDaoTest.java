package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.matveenko_ym.dao.UserDao;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.util.HibernateUtil;
import util.AbstractTestContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDaoTest extends AbstractTestContainer {

    private final UserDao userDao = new UserDao();

    @BeforeEach
    void cleanUpDatabase() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNativeMutationQuery("TRUNCATE TABLE users RESTART IDENTITY").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("Yury Matveenko");
        user.setEmail("korn1984@narod.ru");
        user.setAge(40);
        userDao.createUser(user);
        assertNotNull(user.getId());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setName("Bogdan Matveenko");
        user.setEmail("bogdi2012@mail.ru");
        user.setAge(12);
        userDao.createUser(user);
        User retrievedUser = userDao.getUserById(user.getId());
        assertEquals("Bogdan Matveenko", retrievedUser.getName());
        assertEquals("bogdi2012@mail.ru", retrievedUser.getEmail());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@gmail.com");
        user1.setAge(20);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@gmail.com");
        user2.setAge(32);

        userDao.createUser(user1);
        userDao.createUser(user2);
        List<User> users = userDao.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old.email@gmail.com");
        user.setAge(40);
        userDao.createUser(user);

        user.setName("New Name");
        user.setEmail("new.email@gmail.com");
        user.setAge(45);
        userDao.updateUser(user);

        User updatedUser = userDao.getUserById(user.getId());
        assertEquals("New Name", updatedUser.getName());
        assertEquals("new.email@gmail.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("User Deleted");
        user.setEmail("delete@gmail.com");
        user.setAge(50);
        userDao.createUser(user);
        userDao.deleteUser(user.getId());
        User deletedUser = userDao.getUserById(user.getId());
        assertNull(deletedUser);
    }
}
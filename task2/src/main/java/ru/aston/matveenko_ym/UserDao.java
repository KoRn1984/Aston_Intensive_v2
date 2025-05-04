package ru.aston.matveenko_ym;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.util.HibernateUtil;

import java.util.List;

public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created successfully: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error while creating user: {}", e.getMessage(), e);
        }
    }

    public User getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            logger.info("Retrieved user by ID {}: {}", id, user);
            return user;
        } catch (Exception e) {
            logger.error("Error while retrieving user by ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            logger.info("Retrieved all users: {}", users);
            return users;
        } catch (Exception e) {
            logger.error("Error while retrieving all users: {}", e.getMessage(), e);
            return null;
        }
    }

    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User existingUser = session.get(User.class, user.getId());
            if (existingUser == null) {
                throw new IllegalArgumentException("User with ID " + user.getId() + " does not exist!");
            }
            session.merge(user);
            transaction.commit();
            logger.info("User updated successfully: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error while updating user: {}", e.getMessage(), e);
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("User deleted successfully: {}", user);
            } else {
                throw new IllegalArgumentException("User with ID " + id + " does not exist!");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error while deleting user: {}", e.getMessage(), e);
        }
    }
}
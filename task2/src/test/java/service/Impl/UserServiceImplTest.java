package service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.aston.matveenko_ym.dao.UserDao;
import ru.aston.matveenko_ym.model.User;
import ru.aston.matveenko_ym.service.Impl.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userDao, scanner);
    }

    @Test
    void testCreateUser() {
        when(scanner.next()).thenReturn("Yury", "korn1984@narod.ru");
        userService.createUser();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).createUser(userCaptor.capture());
        User createdUser = userCaptor.getValue();

        assertEquals("Yury", createdUser.getName());
        assertEquals("korn1984@narod.ru", createdUser.getEmail());
    }

    @Test
    void testGetUserById_UserFound() {
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Yury");
        mockUser.setEmail("korn1984@narod.ru");
        mockUser.setAge(40);

        when(scanner.nextLong()).thenReturn(userId);
        when(userDao.getUserById(userId)).thenReturn(mockUser);
        userService.getUserById();
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Yury");
        user1.setEmail("korn1984@narod.ru");
        user1.setAge(40);

        User user2 = new User();
        user1.setId(2L);
        user2.setName("Nastya");
        user1.setEmail("nasty1985@mail.ru");
        user1.setAge(39);

        List<User> users = Arrays.asList(user1, user2);
        when(userDao.getAllUsers()).thenReturn(users);
        userService.getAllUsers();
    }

    @Test
    void testUpdateUser_UserFound() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Yury");
        existingUser.setEmail("korn1984@narod.ru");
        existingUser.setAge(40);

        when(scanner.nextLong()).thenReturn(userId);
        when(scanner.next()).thenReturn("Bogdan", "bogdi@gmail.com");
        when(userDao.getUserById(userId)).thenReturn(existingUser);
        userService.updateUser();
        verify(userDao).updateUser(existingUser);

        assertEquals("Bogdan", existingUser.getName());
        assertEquals("bogdi@gmail.com", existingUser.getEmail());
    }

    @Test
    void testUpdateNonexistentUser() {
        Long userId = 999L;
        when(scanner.nextLong()).thenReturn(userId);
        when(userDao.getUserById(userId)).thenReturn(null);
        userService.updateUser();
        verify(userDao, never()).updateUser(any(User.class));
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        when(scanner.nextLong()).thenReturn(userId);
        userService.deleteUser();
        verify(userDao).deleteUser(userId);
    }

    @Test
    void testDeleteNonexistentUser() {
        Long userId = 999L;
        when(scanner.nextLong()).thenReturn(userId);
        doThrow(new IllegalArgumentException("User with ID " + userId + " does not exist!"))
                .when(userDao).deleteUser(userId);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser());
        assertEquals("User with ID 999 does not exist!", exception.getMessage());
        verify(userDao).deleteUser(userId);
    }
}
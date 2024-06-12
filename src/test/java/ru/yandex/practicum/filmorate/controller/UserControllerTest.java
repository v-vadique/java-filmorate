package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    UserController userController;
    User user1;
    User user2;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user1 = new User("test@test", "test", "test", LocalDate.of(2010, 2, 2));
        user2 = new User("test@test", "test", "", LocalDate.of(2010, 2, 2));
    }

    @Test
    public void getUsersTest() {
        userController.create(user1);
        userController.create(user2);

        Collection<User> users = userController.getUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
    }

    @Test
    public void createUserTest() {
        User newUser1 = userController.create(user1);
        User newUser2 = userController.create(user2);

        assertTrue(userController.getUsers().contains(newUser1));
        assertEquals(newUser2.getLogin(), newUser2.getName());
    }

    @Test
    public void updateUserTest() {
        User oldUser = userController.create(user1);
        user2.setId(1L);
        User newUser = userController.update(user2);

        assertTrue(userController.getUsers().contains(newUser));
        assertEquals(1, userController.getUsers().size());
    }
}

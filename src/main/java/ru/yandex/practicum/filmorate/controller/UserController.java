package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь был успешно создан");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь не был найден");
            throw new NotFoundException("Пользователя с таким ID не существует");
        }

        User oldUser = users.get(newUser.getId());
        if (newUser.getEmail() != null) {
            log.debug("Смена мейла пользователя");
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null) {
            log.debug("Смена логина пользователя");
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getName() != null) {
            log.debug("Смена имени пользователя");
            oldUser.setName(newUser.getName());
        }
        if (newUser.getBirthday() != null) {
            log.debug("Смена дня рождения пользователя");
            oldUser.setBirthday(newUser.getBirthday());
        }
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validate(user);

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь был успешно создан");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
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
        validate(oldUser);
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

    private void validate(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Пользователь не прошел валидацию");
            throw new ValidateException("Электронная почта должна содержать \"@\"");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Пользователь не прошел валидацию");
            throw new ValidateException(("Логин не должен содержать пробелов"));
        }
        if (user.getName() == null) {
            log.debug("Смена имени пользователя на логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Пользователь не прошел валидацию");
            throw new ValidateException("Дата рождения не может быть позже текущей даты");
        }
    }
}

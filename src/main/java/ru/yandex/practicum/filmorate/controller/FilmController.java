package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController("/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм был успешно создан");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм не был найден");
            throw new NotFoundException("Фильма с таким ID не существует");
        }

        Film oldFilm = films.get(newFilm.getId());
        if (newFilm.getName() != null) {
            log.debug("Смена названия фильма");
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            log.debug("Смена описания фильма");
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getDuration() != null) {
            log.debug("Смена длительности фильма");
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getReleaseDate() != null) {
            log.debug("Смена даты выхода фильма");
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        validate(oldFilm);
        log.info("Фильм был успешно обновлен");
        return oldFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Фильм не прошел валидацию");
            throw new ValidateException("Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Фильм не прошел валидацию");
            throw new ValidateException("Описание должно содержать не более 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Фильм не прошел валидацию");
            throw new ValidateException("Дата выпуска должна быть не ранее 28.12.1895");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.warn("Фильм не прошел валидацию");
            throw new ValidateException("Продолжительность должна быть положительным числом");
        }
    }
}

package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм был успешно создан");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
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
}

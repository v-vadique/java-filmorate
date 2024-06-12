package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController;
    Film film1;
    Film film2;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film1 = new Film("name", "description", LocalDate.of(2015, 1, 1),
                Duration.ofMinutes(90));
        film2 = new Film("name1", "description1", LocalDate.of(2017, 1, 1),
                Duration.ofMinutes(60));
    }

    @Test
    public void getFilmsTest() {
        filmController.create(film1);
        filmController.create(film2);

        Collection<Film> films = filmController.getFilms();
        assertNotNull(films);
        assertTrue(films.contains(film2));
        assertEquals(2, films.size());
    }

    @Test
    public void createFilmTest() {
        Film newFilm = filmController.create(film1);

        assertEquals(film1, newFilm);
        assertNotEquals(film2, newFilm);
    }

    @Test
    public void updateFilmTest() {
        Film oldFilm = filmController.create(film1);
        film2.setId(1L);
        Film newFilm = filmController.updateFilm(film2);

        assertEquals(film2, newFilm);
        assertEquals(1, filmController.getFilms().size());
    }
}

package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FilmDbStorageTest {

    @Autowired
    private FilmDbStorage filmDbStorage;

    @Test
    void createAndGetFilm() {
        Film film = new Film();
        film.setName("DB Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020,1,1));
        film.setDuration(100);
        MpaRating mpa = new MpaRating();
        mpa.setId(1);
        film.setMpa(mpa);

        Film created = filmDbStorage.create(film);
        assertThat(created.getId()).isPositive();

        Film retrieved = filmDbStorage.getById(created.getId()).orElseThrow();
        assertThat(retrieved.getName()).isEqualTo("DB Test Film");
    }
}

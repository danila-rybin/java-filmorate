package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.film.MpaDbStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final MpaDbStorage mpaStorage;
    private final GenreDbStorage genreStorage;

    private static final LocalDate EARLIEST_RELEASE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserService userService,
                       MpaDbStorage mpaStorage,
                       GenreDbStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        setMpaAndGenres(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.exists(film.getId())) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        validateFilm(film);
        setMpaAndGenres(film);
        return filmStorage.update(film);
    }

    public void addLike(int filmId, int userId) {
        getFilmById(filmId);
        userService.getUserById(userId);

        if (filmStorage instanceof FilmDbStorage dbStorage) {
            dbStorage.addLike(filmId, userId);
        }
    }

    public void removeLike(int filmId, int userId) {
        getFilmById(filmId);
        userService.getUserById(userId);

        if (filmStorage instanceof FilmDbStorage dbStorage) {
            dbStorage.removeLike(filmId, userId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        log.debug("Получение {} популярных фильмов", count);

        if (filmStorage instanceof FilmDbStorage dbStorage) {
            List<Film> films = dbStorage.getMostPopular(count);
            log.info("Возвращено {} популярных фильмов", films.size());
            return films;
        }

        log.warn("FilmStorage не является FilmDbStorage");
        return List.of();
    }


    public boolean filmExists(int id) {
        return filmStorage.exists(id);
    }

    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.getAllMpaRatings();
    }

    public MpaRating getMpaRatingById(int id) {
        return mpaStorage.getMpaRatingById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма не может быть больше 200 символов");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(EARLIEST_RELEASE)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private void setMpaAndGenres(Film film) {
        if (film.getMpa() != null && film.getMpa().getId() > 0) {
            MpaRating mpa = mpaStorage.getMpaRatingById(film.getMpa().getId())
                    .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + film.getMpa().getId() + " не найден"));
            film.setMpa(mpa);
        }

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> {
                if (genre.getId() > 0) {
                    genreStorage.getGenreById(genre.getId())
                            .orElseThrow(() -> new NotFoundException("Жанр с id " + genre.getId() + " не найден"));
                }
            });
        }
    }
}

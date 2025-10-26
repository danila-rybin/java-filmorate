package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImplementation implements FilmService {

    private final FilmStorage filmStorage;

    @Override
    public Film create(Film film) {
        validate(film);
        filmStorage.create(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validate(film);
        filmStorage.findById(film.getId())
                .orElseThrow(() -> new ru.yandex.practicum.filmorate.exception.NotFoundException("Фильм с id=" + film.getId() + " не найден"));

        return filmStorage.update(film);
    }

    @Override
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @Override
    public Optional<Film> findById(Long id) {
        return filmStorage.findById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new ru.yandex.practicum.filmorate.exception.NotFoundException("Фильм с id=" + filmId + " не найден"));
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new ru.yandex.practicum.filmorate.exception.NotFoundException("Фильм с id=" + filmId + " не найден"));
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }


    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Описание фильма не должно превышать 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ru.yandex.practicum.filmorate.exception.ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}

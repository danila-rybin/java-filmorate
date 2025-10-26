package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmService {

    Film create(Film film);
    Film update(Film film);
    List<Film> findAll();
    Optional<Film> findById(Long id);
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    List<Film> getPopularFilms(int count);
}

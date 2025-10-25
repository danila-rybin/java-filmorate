package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")

public class FilmController {

    private final FilmService filmService;


    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        film = filmService.create(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
      film = filmService.update(film);
        log.info("Фильм обновлён: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }


}

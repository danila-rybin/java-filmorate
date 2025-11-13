package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.dao.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbStorage mpaStorage;

    public MpaController(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping
    public List<MpaDto> getAllMpaRatings() {
        return mpaStorage.getAllMpaRatings().stream().map(MpaMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable int id) {
        return mpaStorage.getMpaRatingById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }

}

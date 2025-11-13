package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmDto> addFilm(@Valid @RequestBody FilmDto filmDto) throws ValidationException {
        validateFilm(filmDto);
        Film film = filmService.createFilm(FilmMapper.toEntity(filmDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(FilmMapper.toDto(film));
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody FilmDto filmDto) throws ValidationException {
        validateFilm(filmDto);
        Film updatedFilm = filmService.updateFilm(FilmMapper.toEntity(filmDto));
        return ResponseEntity.ok(FilmMapper.toDto(updatedFilm));
    }

    @GetMapping
    public List<FilmDto> getAllFilms() {
        List<Film> films = filmService.getAllFilms();
        return films.stream().map(FilmMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilm(@PathVariable int id) {
        Film film = filmService.getFilmById(id);
        return ResponseEntity.ok(FilmMapper.toDto(film));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> films = filmService.getPopularFilms(count);
        return films.stream().map(FilmMapper::toDto).collect(Collectors.toList());
    }

    private void validateFilm(FilmDto film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }
}

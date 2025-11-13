package ru.yandex.practicum.filmorate.dto.mapper;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import java.util.stream.Collectors;

public class FilmMapper {

    public static FilmDto toDto(Film film) {
        if (film == null) return null;

        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(MpaMapper.toDto(film.getMpa()));
        dto.setGenres(film.getGenres().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toSet()));
        return dto;
    }

    public static Film toEntity(FilmDto dto) {
        if (dto == null) return null;

        Film film = new Film();
        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());
        film.setMpa(MpaMapper.toEntity(dto.getMpa()));
        if (dto.getGenres() != null) {
            film.setGenres(dto.getGenres().stream()
                    .map(GenreMapper::toEntity)
                    .collect(Collectors.toSet()));
        }
        return film;
    }
}

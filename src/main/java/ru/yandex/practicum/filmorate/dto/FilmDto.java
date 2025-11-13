package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmDto {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaDto mpa;
    private Set<GenreDto> genres;
}

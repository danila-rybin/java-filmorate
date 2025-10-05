package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}

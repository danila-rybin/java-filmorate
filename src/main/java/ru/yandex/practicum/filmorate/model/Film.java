package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    private final Set<Long> likes = new HashSet<>();
}

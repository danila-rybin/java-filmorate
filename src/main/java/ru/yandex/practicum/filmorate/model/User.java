package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Builder;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы")
    private String login;

    @Email(message = "Почта должна быть корректной")
    @NotBlank(message = "Почта не может быть пустой")
    private String email;

    @PastOrPresent(message = "День рождения не может быть в будущем")
    private LocalDate birthday;

    private String name;

    private final Set<Long> friends = new HashSet<>();

    // Метод для корректировки имени
    public void fixName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}

package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmorateApplicationTests {

	private FilmController filmController;
	private UserController userController;

	@BeforeEach
	void setup() {
		filmController = new FilmController();
		userController = new UserController();
	}

	// Film validation tests
	@Test
	void shouldThrowWhenFilmNameIsEmpty() {
		Film film = Film.builder()
				.name("")
				.description("Desc")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(120)
				.build();

		assertThrows(ResponseStatusException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenFilmDescriptionTooLong() {
		String longDesc = "x".repeat(201);
		Film film = Film.builder()
				.name("Film")
				.description(longDesc)
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(120)
				.build();

		assertThrows(ResponseStatusException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenFilmReleaseDateTooEarly() {
		Film film = Film.builder()
				.name("Film")
				.description("Desc")
				.releaseDate(LocalDate.of(1800, 1, 1))
				.duration(120)
				.build();

		assertThrows(ResponseStatusException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenFilmDurationNegative() {
		Film film = Film.builder()
				.name("Film")
				.description("Desc")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(-5)
				.build();

		assertThrows(ResponseStatusException.class, () -> filmController.create(film));
	}

	// User validation tests
	@Test
	void shouldThrowWhenUserEmailInvalid() {
		User user = new User(null, "invalidEmail", "login", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(ResponseStatusException.class, () -> userController.create(user));
	}

	@Test
	void shouldThrowWhenUserLoginHasSpace() {
		User user = new User(null, "email@example.com", "log in", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(ResponseStatusException.class, () -> userController.create(user));
	}

	@Test
	void shouldThrowWhenUserBirthdayInFuture() {
		User user = new User(null, "email@example.com", "login", "Name", LocalDate.now().plusDays(1));
		assertThrows(ResponseStatusException.class, () -> userController.create(user));
	}

	// Optional: тест обновления несуществующего пользователя или фильма
	@Test
	void shouldThrowWhenUpdatingNonExistentUser() {
		User user = new User(999L, "email@example.com", "login", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(ResponseStatusException.class, () -> userController.update(user));
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentFilm() {
		Film film = Film.builder()
				.id(999L)
				.name("Film")
				.description("Desc")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();
		assertThrows(ResponseStatusException.class, () -> filmController.update(film));
	}
}

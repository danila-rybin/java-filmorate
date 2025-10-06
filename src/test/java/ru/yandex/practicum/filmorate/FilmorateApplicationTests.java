package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

	@Test
	void shouldThrowWhenFilmNameIsEmpty() {
		Film film = Film.builder()
				.name("")
				.description("Desc")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(120)
				.build();

		assertThrows(ValidationException.class, () -> filmController.create(film));
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

		assertThrows(ValidationException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenFilmReleaseDateTooEarly() {
		Film film = Film.builder()
				.name("Film")
				.description("Desc")
				.releaseDate(LocalDate.of(1800, 1, 1))
				.duration(120)
				.build();

		assertThrows(ValidationException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenFilmDurationNegative() {
		Film film = Film.builder()
				.name("Film")
				.description("Desc")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(-5)
				.build();

		assertThrows(ValidationException.class, () -> filmController.create(film));
	}

	@Test
	void shouldThrowWhenUserEmailInvalid() {
		User user = new User(null, "invalidEmail", "login", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(ValidationException.class, () -> userController.create(user));
	}

	@Test
	void shouldThrowWhenUserLoginHasSpace() {
		User user = new User(null, "email@example.com", "log in", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(ValidationException.class, () -> userController.create(user));
	}

	@Test
	void shouldThrowWhenUserBirthdayInFuture() {
		User user = new User(null, "email@example.com", "login", "Name", LocalDate.now().plusDays(1));
		assertThrows(ValidationException.class, () -> userController.create(user));
	}

	@Test
	void shouldThrowWhenUpdatingNonExistentUser() {
		User user = new User(999L, "email@example.com", "login", "Name", LocalDate.of(2000, 1, 1));
		assertThrows(NotFoundException.class, () -> userController.update(user));
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
		assertThrows(NotFoundException.class, () -> filmController.update(film));
	}
}

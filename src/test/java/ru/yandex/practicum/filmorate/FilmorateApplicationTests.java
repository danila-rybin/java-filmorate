package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmServiceImplementation;
import ru.yandex.practicum.filmorate.service.UserServiceImplementation;
import ru.yandex.practicum.filmorate.storage.FilmStorageImplementation;
import ru.yandex.practicum.filmorate.storage.UserStorageImplementation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmorateApplicationTests {

	private FilmController filmController;
	private UserController userController;

	@BeforeEach
	void setup() {
		var userStorage = new UserStorageImplementation();
		var filmStorage = new FilmStorageImplementation();
		var userService = new UserServiceImplementation(userStorage);
		var filmService = new FilmServiceImplementation(filmStorage, userStorage);

		userController = new UserController(userService);
		filmController = new FilmController(filmService);
	}

	@Test
	void shouldCreateValidFilm() {
		Film film = Film.builder()
				.name("Valid Film")
				.description("Описание фильма")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(120)
				.build();

		Film created = filmController.create(film);
		assertEquals("Valid Film", created.getName());
	}

	@Test
	void shouldUpdateFilm() {
		Film film = Film.builder()
				.name("Film To Update")
				.description("Описание")
				.releaseDate(LocalDate.of(2001, 1, 1))
				.duration(100)
				.build();
		Film created = filmController.create(film);

		created.setName("Updated Film");
		Film updated = filmController.update(created);

		assertEquals("Updated Film", updated.getName());
	}

	@Test
	void shouldCreateValidUser() {
		User user = User.builder()
				.email("email@example.com")
				.login("login")
				.name("Name")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		User created = userController.createUser(user);
		assertEquals("login", created.getLogin());
	}

	@Test
	void shouldUpdateUser() {
		User user = User.builder()
				.email("email2@example.com")
				.login("user2")
				.name("User2")
				.birthday(LocalDate.of(1995, 5, 5))
				.build();
		User created = userController.createUser(user);

		created.setName("Updated Name");
		User updated = userController.updateUser(created);

		assertEquals("Updated Name", updated.getName());
	}
}

package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDbStorageTest {

    @Autowired
    private UserDbStorage userDbStorage;

    @Test
    void createAndGetUser() {
        User user = new User();
        user.setEmail("dbtest@example.com");
        user.setLogin("dbtest");
        user.setName("DB Test");
        user.setBirthday(LocalDate.of(1990,1,1));

        User created = userDbStorage.create(user);
        assertThat(created.getId()).isPositive();

        User retrieved = userDbStorage.getById(created.getId()).orElseThrow();
        assertThat(retrieved.getLogin()).isEqualTo("dbtest");
    }
}

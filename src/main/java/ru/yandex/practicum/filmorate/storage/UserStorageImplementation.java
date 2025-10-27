package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UserStorageImplementation implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 0;

    @Override
    public User create(User user) {
        user.setId(++idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}

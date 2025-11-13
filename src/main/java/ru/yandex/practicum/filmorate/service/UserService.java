package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exception.NotFoundException;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (!userStorage.exists(user.getId())) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        validateUser(user);
        return userStorage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        ((UserDbStorage) userStorage).addFriend(userId, friendId, FriendshipStatus.PENDING);
    }

    public void confirmFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        ((UserDbStorage) userStorage).updateFriendshipStatus(friendId, userId, FriendshipStatus.CONFIRMED);
    }

    public void removeFriend(int userId, int friendId) {
        getUserById(userId);
        getUserById(friendId);
        ((UserDbStorage) userStorage).removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        getUserById(userId);
        return ((UserDbStorage) userStorage).getFriends(userId);
    }


    public List<User> getFriendRequests(int userId) {
        return ((UserDbStorage) userStorage).getFriendRequests(userId);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        List<User> friends1 = getFriends(userId1);
        List<User> friends2 = getFriends(userId2);
        return friends1.stream().filter(friends2::contains).collect(Collectors.toList());
    }

    public boolean userExists(int id) {
        return userStorage.exists(id);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный login");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}

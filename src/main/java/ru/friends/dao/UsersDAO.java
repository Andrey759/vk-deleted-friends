package ru.friends.dao;

import ru.friends.model.User;

import java.util.List;

public interface UsersDAO {

    User getById(int id);
    List<User> getAll();
    void save(User user);

}

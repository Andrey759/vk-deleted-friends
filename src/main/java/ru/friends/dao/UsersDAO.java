package ru.friends.dao;

import ru.friends.model.User;

public interface UsersDAO {

    User getById(int id);
    void save(User user);

}

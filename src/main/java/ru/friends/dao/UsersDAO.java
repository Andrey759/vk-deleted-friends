package ru.friends.dao;

import ru.friends.model.User;

public interface UsersDAO {

    public User getById(int id);

    public void save(User user);

}

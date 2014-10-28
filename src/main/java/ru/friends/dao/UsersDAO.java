package ru.friends.dao;

import ru.friends.model.Users;

public interface UsersDAO {

    public Users getById(int id);

    public void save(Users user);

}

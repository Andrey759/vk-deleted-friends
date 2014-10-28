package ru.friends.dao;

import ru.friends.model.Friends;



public interface FriendsDAO {

    public Friends getById(int id);

    public void save(Friends f);

}

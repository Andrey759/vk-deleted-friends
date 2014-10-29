package ru.friends.dao;

import ru.friends.model.Friend;


public interface FriendsDAO {

    public Friend getById(int id);

    public void save(Friend f);

}

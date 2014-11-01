package ru.friends.dao;

import ru.friends.model.Friend;

import java.util.List;


public interface FriendsDAO {

    Friend getById(int id);
    void save(Friend f);
    List<Friend> getFriends(int user_id);
    List<Friend> getDeletedFriends(int user_id);

}

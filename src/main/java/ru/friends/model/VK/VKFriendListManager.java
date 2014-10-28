package ru.friends.model.VK;

import java.util.List;

public class VKFriendListManager {

    private VKFriendsDAO vkFriendsDAO = new VKFriendsDAO();
    private List friends = null;

    private List getDifference(List a, List b) {
        return null;
    }

    private List getFriendsFromDB() {
        return friends;
    }

    private List getDeletedFriendsFromDB() {
        return null;
    }

    public void update() {

    }

    public List getFriends() {
        return friends;
    }

    public List getDeletedFriends() {
        return null;
    }

}

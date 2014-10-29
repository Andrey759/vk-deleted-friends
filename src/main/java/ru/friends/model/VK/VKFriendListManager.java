package ru.friends.model.VK;

import org.springframework.beans.factory.annotation.Autowired;
import ru.friends.dao.FriendsDAO;
import ru.friends.model.*;
import java.util.List;

public class VKFriendListManager {

    private VKFriendsDAO vkFriendsDAO = new VKFriendsDAO();
    private User user;
    private List<Friend> friends;

    @Autowired
    private FriendsDAO friendsDAO;

    public VKFriendListManager(User user) {
        this.user = user;
    }

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

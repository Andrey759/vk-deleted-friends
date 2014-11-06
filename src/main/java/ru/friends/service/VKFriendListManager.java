package ru.friends.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.friends.dao.FriendsDAO;
import ru.friends.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VKFriendListManager {

    private static Logger log = Logger.getLogger(VKFriendListManager.class);

    @Autowired
    private FriendsDAO friendsDAO;

    //Возвращает результат множества a за вычетом множества b
    private List<Friend> getDifference(List<Friend> a, List<Friend> b) {
        List<Friend> difference = new ArrayList<>();
        difference.addAll(a);
        difference.removeAll(b);
        return difference;
    }

    private List<Friend> getFriendsFromDB(User user) {
        return friendsDAO.getFriends(user.getId());
    }

    private List<Friend> getDeletedFriendsFromDB(User user) {
        return friendsDAO.getDeletedFriends(user.getId());
    }

    public List<Friend> getFriendsFromVK(User user) throws IOException {
        return null;//requestDAO.getResponseAsList("" + user.getId());
    }

    public void update() {

    }

    public List<Friend> getFriends(User user) {
        return getFriendsFromDB(user);
    }

    public List<Friend> getDeletedFriends(User user) {
        return getDeletedFriendsFromDB(user);
    }

}

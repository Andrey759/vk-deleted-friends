package ru.friends.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.friends.dao.FriendsDAO;
import ru.friends.model.*;

import java.io.IOException;
import java.util.List;

public class VKFriendListManager {

    private static Logger log = Logger.getLogger(VKFriendListManager.class);
    private RequestDAO<Friend> requestDAO = new VKFriendsDAOImpl();
    private User user = null;

    @Autowired
    private FriendsDAO friendsDAO;

    public VKFriendListManager() {

    }

    public VKFriendListManager(User user) {
        add(user);
    }

    public void add(User user) {
        if(this.user != null)
            log.error("VKFriendListManager.add(user): User already exist and will be changed " + this.user.getId() + " -> " + user.getId());
        this.user = user;
        //friendsDAO.
    }

    private List<Friend> getDifference(List<Friend> a, List<Friend> b) {
        return null;
    }

    private List<Friend> getFriendsFromDB() {
        return null;
    }

    private List<Friend> getDeletedFriendsFromDB() {
        return null;
    }

    public List<Friend> getFriendsFromVK() throws IOException {
        return requestDAO.getResponseAsList("" + user.getId());
    }

    public void update() {

    }

    public List<Friend> getFriends() {
        return getFriendsFromDB();
    }

    public List<Friend> getDeletedFriends() {
        return getDeletedFriendsFromDB();
    }

}

package ru.friends.service;

import ru.friends.model.User;

import java.util.List;

public class VKFriendListManagerComposite extends VKFriendListManager {

    List<VKFriendListManager> users;

    public VKFriendListManagerComposite() { }

    public VKFriendListManagerComposite(User user) {
        users.add(new VKFriendListManager(user));
    }

    @Override
    public void add(User user) {
        users.add(new VKFriendListManager(user));
    }

    @Override
    public void update() {
        users.forEach(e -> e.update());
    }
}

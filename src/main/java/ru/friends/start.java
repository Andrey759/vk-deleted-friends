package ru.friends;

import java.sql.SQLException;

public class start {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RequestManager.getInstance().run();
        //ru.friends.RequestManager.getInstance().getDeletedFriends();
    }

}

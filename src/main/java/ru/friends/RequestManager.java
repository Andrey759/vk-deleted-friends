package ru.friends;

import java.sql.SQLException;
import java.util.Timer;

@Deprecated
public class RequestManager {

    private final String url = "http://vk.com/al_friends.php?act=load_friends_silent&al=1&gid=0&id=";
    private final String cookie = "";

    private static RequestManager INSTANCE = new RequestManager();
    //private JDBC jdbc = new JDBC();
    private volatile boolean working = false;
    private Timer timer = new Timer();

    public static RequestManager getInstance() {
        return INSTANCE;
    }



    public void run() {
        if(!working) {
            working = true;
            //timer.schedule(new RequestFriendsFromVk(client, initialMethod, jdbc), 0, 1000);
        }
    }

    public void stop() {
        if(working) {
            timer.cancel();
            timer.purge();
            working = false;
        }
    }

    public void getDeletedFriends() throws SQLException, ClassNotFoundException {
        //jdbc.executeDelUsers("SELECT * FROM " + jdbc.getTableFrineds() + " WHERE(del_date IS NOT NULL) ORDER BY del_date ASC").forEach(System.out::println);
    }
}

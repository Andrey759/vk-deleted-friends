package ru.friends;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.sql.SQLException;
import java.util.Timer;

public class RequestManager {

    private final String url = "http://vk.com/al_friends.php?act=load_friends_silent&al=1&gid=0&id=";
    private final String cookie = "";

    private static RequestManager INSTANCE = new RequestManager();
    private HttpClient client = new HttpClient();
    private PostMethod initialMethod = new PostMethod(url);
    private JDBC jdbc = new JDBC();
    private volatile boolean working = false;
    private Timer timer = new Timer();

    public static RequestManager getInstance() {
        return INSTANCE;
    }

    private RequestManager() {
        initialMethod.addRequestHeader("Referer", "http://vk.com/friends?section=all");
        initialMethod.addRequestHeader("Host", "vk.com");
        initialMethod.addRequestHeader("ru.friends.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        initialMethod.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        initialMethod.addRequestHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        //initialMethod.addRequestHeader("Accept-Encoding", "gzip, deflate");
        initialMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        initialMethod.addRequestHeader("X-Requested-With", "XMLHttpRequest");
        initialMethod.addRequestHeader("Cookie", cookie);
        //initialMethod.addRequestHeader("Content-Length", "45");
        initialMethod.addRequestHeader("Pragma", "no-cache");
        initialMethod.addRequestHeader("Cache-control", "no-cache");
    }

    public void run() {
        if(!working) {
            working = true;
            timer.schedule(new RequestFriendsFromVk(client, initialMethod, jdbc), 0, 1000);
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
        jdbc.executeDelUsers("SELECT * FROM " + jdbc.getTableFrineds() + " WHERE(del_date IS NOT NULL) ORDER BY del_date ASC").forEach(System.out::println);
    }
}

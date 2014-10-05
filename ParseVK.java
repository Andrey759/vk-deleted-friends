import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class ParseVK {

    private static String url = "http://vk.com/al_friends.php?act=load_friends_silent&al=1&gid=0&id=165428827";
    private static String cookie = "remixstid=855287052_ce92e85b99ecfc1cac; remixscreen_depth=24; audio_vol=13; remixrefkey=51c0165d2aaeb8ebf5; remixflash=15.0.0; remixlang=0; remixdt=0; remixseenads=1; remixtst=1090caaf; remixsid=8cd78f3002f6724e1260bd8da778aa79b786b52c4bc6e4cc1a294";

    private static JDBC jdbc = new JDBC();

    private static List<User> parseJSON(String json) {
        Gson gson = new Gson();
        Map root = gson.fromJson(json, Map.class);
        if (!root.containsKey("all"))
            return null;
        List friends = (ArrayList) root.get("all");
        List<User> result = new ArrayList<>(friends.size());
        for (List<String> friend : (List<List<String>>) friends) {
            int id = Integer.valueOf(friend.get(0));
            String pic = friend.get(1);
            String page = friend.get(2);
            boolean isFemale = "1".equals(friend.get(3));
            String name = friend.get(5);
            result.add(new User(id, pic, page, isFemale ? User.Gender.FEMALE : User.Gender.MALE, name));
        }
        return result;
    }

    public static void showDeleted() throws SQLException, ClassNotFoundException {
        jdbc.executeDelUsers("SELECT * FROM " + jdbc.getTableFrineds() + " WHERE(del_date IS NOT NULL) ORDER BY del_date ASC").forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        HttpClient client = new HttpClient();
        PostMethod initialMethod = new PostMethod(url);

        initialMethod.addRequestHeader("Referer", "http://vk.com/friends?section=all");
        initialMethod.addRequestHeader("Host", "vk.com");
        initialMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        initialMethod.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        initialMethod.addRequestHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        //initialMethod.addRequestHeader("Accept-Encoding", "gzip, deflate");
        initialMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        initialMethod.addRequestHeader("X-Requested-With", "XMLHttpRequest");
        initialMethod.addRequestHeader("Cookie", cookie);
        //initialMethod.addRequestHeader("Content-Length", "45");
        initialMethod.addRequestHeader("Pragma", "no-cache");
        initialMethod.addRequestHeader("Cache-control", "no-cache");
        int status = client.executeMethod(initialMethod);
        String resp = initialMethod.getResponseBodyAsString();
        /*
        resp = resp.split("<!>\\{" + '"' + "all" + '"' + ":\\[\\['")[1];
        resp = resp.split("'\\]\\]}<!>")[0];
        String[] lines = Pattern.compile("']\n,\\['").split(resp);
        List<User> friends = new ArrayList<User>();
        Pattern pattern = Pattern.compile("','");
        for(String line : lines) {
            String[] fields = pattern.split(line);
            friends.add(new User(Integer.parseInt(fields[0]),fields[1],fields[2],"1".equals(fields[3]) ? User.Gender.FEMALE : User.Gender.MALE,fields[5]));
        }
        */

        int p1 = resp.indexOf("{");
        int p2 = resp.indexOf("}", p1);
        resp = resp.substring(p1, p2 + 1);
        final Collection<User> friends = parseJSON(resp);

        List<User> newFriends = new ArrayList<>();
        final List<User> oldFriends = jdbc.executeUsers("SELECT * FROM friends WHERE(del_date IS NULL)");
        newFriends.addAll(friends);
        newFriends.removeAll(oldFriends);

        //Добавляем новых друзей в базу
        for(User u : newFriends) {
            jdbc.execute("DELETE FROM " + jdbc.getTableFrineds() + " WHERE(id = '" + u.getId() + "')");
            jdbc.execute("INSERT INTO " + jdbc.getTableFrineds() + " VALUES(" + u.getId() + ", '" + u.getName() + "', " + u.isFemale() + ", '" + u.getPage() + "', '" + u.getPic() + "', NULL)");
        }

        List<User> deletedFriends = new ArrayList<>();
        deletedFriends.addAll(oldFriends);
        deletedFriends.removeAll(friends);
        //Заносим в базу удалившихся друзей
        for(User u : deletedFriends) {
            jdbc.execute("UPDATE " + jdbc.getTableFrineds() + " SET del_date = '" + String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS", Calendar.getInstance()) + "' WHERE(id = " + u.getId() + ")");
        }

        showDeleted();
    }

}

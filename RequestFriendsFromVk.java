import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.*;

public class RequestFriendsFromVk extends TimerTask {

    private HttpClient client;
    private PostMethod initialMethod;
    private JDBC jdbc;

    private int count = 10;

    public RequestFriendsFromVk(HttpClient client, PostMethod initialMethod, JDBC jdbc) {
        this.client = client;
        this.initialMethod = initialMethod;
        this.jdbc = jdbc;
    }

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

    @Override
    public void run() {
        try {
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
            for (User u : newFriends) {
                jdbc.execute("DELETE FROM " + jdbc.getTableFrineds() + " WHERE(id = '" + u.getId() + "')");
                jdbc.execute("INSERT INTO " + jdbc.getTableFrineds() + " VALUES(" + u.getId() + ", '" + u.getName() + "', " + u.isFemale() + ", '" + u.getPage() + "', '" + u.getPic() + "', NULL)");
            }

            List<User> deletedFriends = new ArrayList<>();
            deletedFriends.addAll(oldFriends);
            deletedFriends.removeAll(friends);
            //Заносим в базу удалившихся друзей
            for (User u : deletedFriends) {
                jdbc.execute("UPDATE " + jdbc.getTableFrineds() + " SET del_date = '" + String.format("%tY-%<tm-%<td %<tH:%<tM:%<tS", Calendar.getInstance()) + "' WHERE(id = " + u.getId() + ")");
            }

            count--;
            System.out.println(count);
            if(count == 0)
                RequestManager.getInstance().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

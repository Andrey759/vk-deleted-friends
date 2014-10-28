package ru.friends.model.VK;

import com.google.gson.Gson;
import ru.friends.User;
import ru.friends.model.Friends;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VKFriendsDAO {

    private VKRequestsManager vkRequestsManager = new VKRequestsManager();
    private static final String url = "http://vk.com/al_friends.php?act=load_friends_silent&al=1&gid=0&id=";

    private static List<User> parseJSON(String json) {
        Gson gson = new Gson();
        Map root = gson.fromJson(json, Map.class);
        if (!root.containsKey("all"))
            return null;
        List friends = (ArrayList) root.get("all");
        List<User> result = new ArrayList<>(friends.size());
        List<Friends> result2 = new ArrayList<>(friends.size());
        for (List<String> friend : (List<List<String>>) friends) {
            int id = Integer.valueOf(friend.get(0));
            String pic = friend.get(1);
            String page = friend.get(2);
            boolean isFemale = "1".equals(friend.get(3));
            String name = friend.get(5);
            result.add(new User(id, pic, page, isFemale ? User.Gender.FEMALE : User.Gender.MALE, name));
            //result.add(new Friends());
        }
        return result;
    }

    public List getFriendList(int id) throws IOException {
        String response = vkRequestsManager.executeReqest(url + id);
        /*
        resp = resp.split("<!>\\{" + '"' + "all" + '"' + ":\\[\\['")[1];
        resp = resp.split("'\\]\\]}<!>")[0];
        String[] lines = Pattern.compile("']\n,\\['").split(resp);
        List<ru.friends.User> friends = new ArrayList<ru.friends.User>();
        Pattern pattern = Pattern.compile("','");
        for(String line : lines) {
            String[] fields = pattern.split(line);
            friends.add(new ru.friends.User(Integer.parseInt(fields[0]),fields[1],fields[2],"1".equals(fields[3]) ? ru.friends.User.Gender.FEMALE : ru.friends.User.Gender.MALE,fields[5]));
        }
        */

        int p1 = response.indexOf("{");
        int p2 = response.indexOf("}", p1);
        response = response.substring(p1, p2 + 1);
        final Collection<User> friends = parseJSON(response);
        return null;
    }

}

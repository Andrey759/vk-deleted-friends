package ru.friends.service;

import com.google.gson.Gson;
import ru.friends.model.Friend;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class VKFriendsDAO {

    private static final String url = "http://vk.com/al_friends.php?act=load_friends_silent&al=1&gid=0&id=";

    private VKFriendsDAO() { }

    private static List<Friend> parseJSON(String json) {
        Gson gson = new Gson();
        Map root = gson.fromJson(json, Map.class);
        if (!root.containsKey("all"))
            return null;
        List friends = (ArrayList) root.get("all");
        List<Friend> result = new ArrayList<>(friends.size());
        for (List<String> friend : (List<List<String>>) friends)
            result.add(new Friend(friend.get(0), friend.get(1), friend.get(2), friend.get(3), friend.get(5)));
        return result;
    }

    private static List<Friend> parseByReg(String json) {
        json = json.split("<!>\\{" + '"' + "all" + '"' + ":\\[\\['")[1];
        json = json.split("'\\]\\]}<!>")[0];
        String[] lines = Pattern.compile("']\n,\\['").split(json);
        List<Friend> friends = new ArrayList<>();
        Pattern pattern = Pattern.compile("','");
        for(String line : lines) {
            String[] fields = pattern.split(line);
            friends.add(new Friend(fields[0], fields[1], fields[2], fields[3], fields[5]));
        }
        return friends;
    }

    public static List<Friend> getResponseAsList(String page) throws IOException {
        String response = RequestManager.executeReqest(url + page);
        int p1 = response.indexOf("{");
        int p2 = response.indexOf("}", p1);
        response = response.substring(p1, p2 + 1);
        final List<Friend> friends = parseJSON(response);
        //final Collection<Friend> friends = parseByReg(response);  //Асболютно эквивалентно строке выше
        return friends;
    }

}

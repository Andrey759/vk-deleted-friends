package ru.friends.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.dao.*;
import ru.friends.model.*;
import ru.friends.service.VKFriendListManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

@Controller
//@RequestMapping
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class);

    @Autowired
    private FriendsDAO friendsDAO;
    @Autowired
    private TransactionsDAO transactionsDAO;
    @Autowired
    private UsersDAO usersDAO;
    @Autowired
    private VKFriendListManager vkFriendListManager;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public void begin(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //response.setCharacterEncoding("UTF-8");   //Не работает
        response.getWriter().println("Трололо!<br> Трололо!<br> Я водитель нло!");
        //friendsDAO.getById(1974730);
        //response.getWriter().println(usersDAO.getById(1974730).getInterval().getTime());
        //response.getWriter().println(vkFriendListManager.getFriends(usersDAO.getById(1974730)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/friends")
    public void friends(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam int id
                     ) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(friendsDAO.getFriends(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/deleted")
    public void deleted(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam int id
    ) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(friendsDAO.getDeletedFriends(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public void transactions(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam int id
    ) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(transactionsDAO.getTransactions(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/settings")
    public void settings(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam int id
    ) throws IOException {
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reg")
    public void reg(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam int id,
                     @RequestParam String pass,
                     @RequestParam int interval
    ) throws IOException {
        try {
            //User user = new User();
            //user.setId(id);
            //user.setPass(pass);
            //user.setInterval(new Timestamp(interval * 60 * 1000));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(interval * 60 * 1000);
            User user = new User(id, pass, calendar);
            //user.setLastUpdate();
            usersDAO.save(new User(id, pass, calendar));
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("Пользователь успешно зарегистрирован.");
            response.getWriter().println();
            response.getWriter().println(user);
            response.getWriter().println("Интервал: " + user.getInterval().getTimeInMillis());
            response.getWriter().println();
            response.getWriter().println(usersDAO.getById(1974730));
            response.getWriter().println("Интервал: " + usersDAO.getById(1974730).getInterval().getTimeInMillis());
        } catch (Throwable ex) {
            log.fatal(ex);
        }
    }





    /*
    public void show(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam(defaultValue = "@null", required = false, value = "var2") String var1
    ) throws IOException {
    */



    /*
    Map parameterMap = request.getParameterMap();
    for (Object o: parameterMap.values()) {
        System.out.println(((String[])o)[0]);
        //response.getWriter().println((String)o);
    }
    */



    /*

    ViewController
    /friend
    /music

    @Controller
    @RequestMapping(value = "/friend")
    FriendController
    {
        @RequestMapping(value = "/get/{0}")
        void...l

        @RequestMethod(value = "/put")
        void...
    }

    /friend/12211
    /friend/get
    friend/put
    music
    music/get
    music/add
     */

}

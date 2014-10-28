package ru.friends.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.friends.dao.*;
import ru.friends.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

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

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public void show(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam(defaultValue = "@null", required = false, value = "var2") String var1
                     ) throws IOException {
        Friends f = new Friends();
        f.setGender((byte) 1);
        f.setName("Masha");
        f.setPage("page");
        f.setPic("/pic");
        // workers
        //friendsDAO.save(f);
        response.getWriter().println("Hello World! " + var1);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reg")
    public void reg(HttpServletRequest request, HttpServletResponse response,
                     @RequestParam int id,
                     @RequestParam String pass,
                     @RequestParam int interval
    ) throws IOException {
        try {
            Users user = new Users();
            user.setId(id);
            user.setPass(pass);
            user.setInterval(new Timestamp(interval * 60 * 1000));
            usersDAO.save(user);
            response.getWriter().println(user);
            /*Friends friend = new Friends();
            friend.setId(id);
            friend.setName("Trololo");
            friend.setGender((byte)1);*/
            response.getWriter().println("<br><br><br>");
            response.getWriter().println(friendsDAO.getById(1050));
        } catch (Throwable ex) {
            log.fatal(ex);
        }
    }







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
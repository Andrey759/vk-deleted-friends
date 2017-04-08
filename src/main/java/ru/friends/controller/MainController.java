package ru.friends.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

//@Controller
//@RequestMapping
@Deprecated
public class MainController {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public void begin(HttpServletResponse response) throws IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //response.setCharacterEncoding("UTF-8");   //Не работает
        //log.debug("test1");
        response.getWriter().println("Трололо!<br> Трололо!<br> Я водитель нло!");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/friends")
    public void friends(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam int id
                     ) throws IOException {

        //log.debug("test2");
        response.setContentType("text/html;charset=UTF-8");
        //response.getWriter().println(friendsDAO.getAddedFriendsByUserId(id));
        //log.debug("Trololo");
        //response.getWriter().println(VKFriendsDAO.getResponseAsList("1974730"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/deleted")
    public void deleted(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam int id
    ) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //response.getWriter().println(friendsDao.getDeletedFriendsByUserId(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public void transactions(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam int id
    ) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //response.getWriter().println(transactionsDao.getTransactionsByUserId(id));
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
            response.setContentType("text/html;charset=UTF-8");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(interval * 1000);
            //if(usersDAO.getById(id) == null) {
                //usersDAO.save(new User(id, pass, calendar));
                response.getWriter().println("Пользователь успешно зарегистрирован.<br>");
            //} else
                response.getWriter().println("Пользователь с id = " + id + " уже существует.");
        } catch (Throwable ex) {
            //log.fatal(ex);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/start")
    public void start(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //vkFriendListManager.init();
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

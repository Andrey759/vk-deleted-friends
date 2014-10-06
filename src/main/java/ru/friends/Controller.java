package ru.friends;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    private int counter = 0;
    private String text = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //counter++;
        //req.setAttribute("counter", counter);
        //req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}

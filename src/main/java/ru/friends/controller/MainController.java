package ru.friends.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
//@RequestMapping
public class MainController {

    @RequestMapping(method = RequestMethod.GET, value = "/show")
    public void show(HttpServletResponse response,
                     @RequestParam(defaultValue = "@null", required = false, value = "var2") String var1
                     ) throws IOException {
        response.getWriter().println("Hello World! " + var1);
    }

}

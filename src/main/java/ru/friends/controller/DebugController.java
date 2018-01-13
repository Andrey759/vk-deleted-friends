package ru.friends.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class DebugController {

    @GetMapping(value = "/debug")
    public String getIndexPage() {
        return "debug";
    }

}

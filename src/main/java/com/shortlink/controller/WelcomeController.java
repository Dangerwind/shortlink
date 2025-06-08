package com.shortlink.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
public class WelcomeController {
    @GetMapping(path = "")
    public String welcome(HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");

        return "";
    }
}

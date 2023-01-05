package com.example.tgbot.security;

import com.example.tgbot.domain.User;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Authentication {
    private final UserService userService;

    @Autowired
    public Authentication(UserService userService){
        this.userService = userService;
    }

    public String getPermission(String name){
        User user = userService.getUserByName(name);
        String role = user.getRole();
        switch (role.toLowerCase()){
            case "viewer":
                return "low";
            case "student":
                return "medium";
            case "teacher":
                return "high";
        }
        throw new NullPointerException();
    }
}

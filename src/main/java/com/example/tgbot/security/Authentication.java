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

    public String getPermission(Long id){
        if (userService.existsById(id)){
            User user = userService.getUserById(id);
            String role = user.getRole();
            switch (role) {
                case "Viewer":
                    return "low";
                case "Student":
                    return "medium";
                case "Teacher":
                    return "high";
            }
        } else {
            return "low";
        }
        throw new NullPointerException();
    }
}

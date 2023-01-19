package com.example.tgbot.security;

import com.example.tgbot.entity.Role;
import com.example.tgbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationManager {
    private final UserService userService;

    @Autowired
    public AuthenticationManager(UserService userService){
        this.userService = userService;
    }

    public String getPermission(Long id){
        var user = userService.getUserById(id);
        if (user != null){
            Role role = user.getRole();
            switch (role) {
                case VIEWER:
                    return "low";
                case STUDENT:
                    return "medium";
                case TEACHER:
                    return "high";
            }
        }
        return "low";
    }
}

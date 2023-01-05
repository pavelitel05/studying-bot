package com.example.tgbot.security;

import org.springframework.stereotype.Component;

@Component
public class Authorization {

    private final Integer adminPassword = Integer.getInteger(System.getenv("ADMIN_ID"));

    private final Integer studentPassword = Integer.getInteger(System.getenv("STUDENT_PASSWORD"));

    public String authorization(Integer password){
        if (password == adminPassword){
            return "Teacher";
        } else if (password == studentPassword) {
            return "Student";
        } else {
            return "Viewer";
        }
    }
}

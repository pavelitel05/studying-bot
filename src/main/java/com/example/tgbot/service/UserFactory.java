package com.example.tgbot.service;

import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class UserFactory {
    private final UserService userService;

    @Autowired
    public UserFactory(UserService userService) {
        this.userService = userService;
    }

    public boolean newUser(Message message){
        User user = new User();
        user.setName("Not authorized");
        user.setChatId(message.getChatId());
        user.setStatus(Status.REQUEST_PASSWORD);
        userService.setUser(user);
        return true;
    }
}

package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageHandler {
    @Autowired
    private Authorization authorization;

    @Autowired
    private UserService userService;

    public BotApiMethod<?> answerMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message receivedMessage = update.getMessage();
        if (userService.existsById(receivedMessage.getChatId())){
            String status = userService.getUserById(receivedMessage.getChatId()).getStatus();
            if ("request-password".equals(status)){
                return authorization.checkPassword(receivedMessage);
            } else if ("request-name".equals(status)){
                return authorization.setName(receivedMessage);
            } else if ("request-module".equals(status)) {
                return authorization.setModule(update);
            }
        }
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText(receivedMessage.getText());
        return sendMessage;
    }
}
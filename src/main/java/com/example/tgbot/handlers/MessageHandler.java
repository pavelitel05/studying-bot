package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
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

    public BotApiMethod<?> answerMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message receivedMessage = update.getMessage();
        if (receivedMessage.getMessageId() == 1){
            return authorization.checkPassword(receivedMessage);
        } else if (receivedMessage.getMessageId() == 2) {
            return authorization.setName(receivedMessage);
        }
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText(receivedMessage.getText());
        return sendMessage;
    }
}

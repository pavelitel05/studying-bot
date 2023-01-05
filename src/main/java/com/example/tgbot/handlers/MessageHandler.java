package com.example.tgbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageHandler {
    public BotApiMethod<?> answerMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message m = update.getMessage();
        sendMessage.setChatId(m.getChatId());
        sendMessage.setText(m.getText());
        return sendMessage;
    }
}

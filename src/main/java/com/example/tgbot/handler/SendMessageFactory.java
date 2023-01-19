package com.example.tgbot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SendMessageFactory {

    public SendMessage getSendMessage(BotApiObject botApiObject){
        SendMessage sendMessage = new SendMessage();
        if (botApiObject.getClass().equals(Message.class)){
            sendMessage.setChatId(((Message) botApiObject).getChatId());
        } else {
            sendMessage.setChatId(((CallbackQuery) botApiObject).getFrom().getId());
        }
        return sendMessage;
    }

}

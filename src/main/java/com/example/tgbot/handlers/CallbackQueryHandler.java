package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackQueryHandler {
    @Autowired
    private Authorization authorization;
    public BotApiMethod<?> answerCallbackQuery(Update update) {;
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        if ("ЕГЭ".equals(callbackData)
                | "ОГЭ".equals(callbackData)
                | "Python".equals(callbackData)
                |"Java".equals(callbackData)
                |"Школьная программа".equals(callbackData)){
            return authorization.setModule(update);
        }
        throw new UnsupportedOperationException();
    }
}

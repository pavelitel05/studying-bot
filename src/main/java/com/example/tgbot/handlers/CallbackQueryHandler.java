package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CallbackQueryHandler {
    @Autowired
    private Authorization authorization;
    @Autowired
    private UserService userService;
    public BotApiMethod<?> answerCallbackQuery(Update update) {
        if ("request-module".equals(userService.getUserById(update.getCallbackQuery().getMessage().getChatId()).getStatus())){
            return authorization.setModule(update);
        }
        return null;
    }
}

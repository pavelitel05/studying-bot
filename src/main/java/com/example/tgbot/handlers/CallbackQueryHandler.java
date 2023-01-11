package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

//todo Если авторишь сервис, лучше кидать анатацию @Service
//todo И придерживайся одного стиля внедрения бинов (@Autowired на поле/Конструктор/Cеттер или без @Autowired)
//todo Если прям по уму, то сделать interface аля CallbackHandler и имплементить его здесь
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

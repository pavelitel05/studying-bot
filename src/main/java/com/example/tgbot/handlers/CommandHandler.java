package com.example.tgbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandHandler {
    public BotApiMethod<?> answerCommand(Update update) {
        throw new UnsupportedOperationException();
    }
}

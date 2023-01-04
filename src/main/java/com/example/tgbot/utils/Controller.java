package com.example.tgbot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@Slf4j
public class Controller {

    private final TelegramBot telegramBot;

    @Autowired
    public Controller(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostMapping
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }
}

package com.example.tgbot.utils;

import com.example.tgbot.config.TelegramBotConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramWebhookBot {
    private final TelegramBotConfigurationProperties config;
    @Autowired
    public TelegramBot(TelegramBotConfigurationProperties config) {
        this.config = config;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        SendMessage message = new SendMessage();
        if (update.hasMessage()){
            Message mes = update.getMessage();
            message.setText(mes.getText());
            message.setChatId(mes.getChatId());
            return message;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWebhook(SetWebhook setWebhook) throws TelegramApiException {
        setWebhook.setUrl(config.getWebhookPath());
    }

    @Override
    public String getBotPath() {
        return config.getWebhookPath();
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}

package com.example.tgbot.utils;

import com.example.tgbot.config.TelegramBotConfigurationProperties;
import com.example.tgbot.handlers.CallbackQueryHandler;
import com.example.tgbot.handlers.CommandHandler;
import com.example.tgbot.handlers.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramWebhookBot {
    private final TelegramBotConfigurationProperties config;
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final CommandHandler commandHandler;
    @Autowired
    public TelegramBot(TelegramBotConfigurationProperties config,
                       CallbackQueryHandler callbackQueryHandler,
                       MessageHandler messageHandler,
                       CommandHandler commandHandler) {
        this.config = config;
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            return callbackQueryHandler.answerCallbackQuery(update);
        }
        if (update.hasMessage()) {
            if (update.getMessage().getText().charAt(0) == '/'){
                return commandHandler.answerCommand(update);
            } else if (update.getMessage().hasText()){
                return messageHandler.answerMessage(update);
            }
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

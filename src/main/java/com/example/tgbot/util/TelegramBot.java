package com.example.tgbot.util;

import com.example.tgbot.config.TelegramBotConfigurationProperties;
import com.example.tgbot.handler.CallbackQueryHandler;
import com.example.tgbot.handler.CommandHandler;
import com.example.tgbot.handler.MessageHandler;
import com.example.tgbot.handler.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramWebhookBot {
    private final TelegramBotConfigurationProperties config;
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final CommandHandler commandHandler;

    private final SendMessageFactory sendMessageFactory;
    @Autowired
    public TelegramBot(TelegramBotConfigurationProperties config,
                       CallbackQueryHandler callbackQueryHandler,
                       MessageHandler messageHandler,
                       CommandHandler commandHandler,
                       SendMessageFactory sendMessageFactory) {
        this.config = config;
        this.callbackQueryHandler = callbackQueryHandler;
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
        this.sendMessageFactory = sendMessageFactory;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            return callbackQueryHandler.answerCallbackQuery(update);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText().charAt(0) == '/'){
                return commandHandler.answerCommand(update);
            } else if (update.getMessage().hasText()){
                return messageHandler.answerMessage(update);
            }
        }
        SendMessage sendMessage = sendMessageFactory.getSendMessage(update.getMessage());
        sendMessage.setText("Я не понимаю :(");
        return sendMessage;
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

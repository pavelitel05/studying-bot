package com.example.tgbot.handlers;

import com.example.tgbot.domain.User;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandHandler {
    public BotApiMethod<?> answerCommand(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        switch (message.getText()){
            case "/start":
                sendMessage.setText("Приветствую в NerzonStudyingBot!\n" +
                        "*\n" +
                        "Он создан для коммуникации учеников с их преподавателем.\n" +
                        "*\n" +
                        "Вы можете ознакомиться со своим расписанием и изменить его статус\n");
                return sendMessage;
            case "/help":
                sendMessage.setText("Список команд:\n" +
                                    "1. /help\n" +
                                    "2. /start\n" +
                                    "3. /authorization\n" +
                                    "Доступные после авторизации:\n" +
                                    "coming soon");
                return sendMessage;
            case "/authorization":
                //Creating not authorized user
                User user = new User();
                user.setName("Not authorized");
                user.setChatId(message.getChatId());
                sendMessage.setText("Введите вам выданный пароль");
                sendMessage.setReplyToMessageId(1);
                return sendMessage;
            default:
                sendMessage.setText("Не знаю такой команды!");
                return sendMessage;
        }
    }
}

package com.example.tgbot.handlers;

import com.example.tgbot.domain.User;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandHandler {
    @Autowired
    private UserService userService;
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
                                    "4. /logout");
                return sendMessage;
            case "/authorization":
                // валидация
                if (userService.existsById(message.getChatId())){
                    sendMessage.setText("❗Вы уже авторизированы❗\n" +
                                        "Если хотите выйти из учетной записи воспользуйтесь:\n" +
                                        "/logout\n");
                    return sendMessage;
                }
                //Creating not authorized user
                User user = new User();
                user.setName("Not authorized");
                user.setChatId(message.getChatId());
                user.setStatus("request-password");
                userService.setUser(user);
                sendMessage.setText("Введите вам выданный пароль");
                return sendMessage;
            case "/logout":
                if (userService.existsById(message.getChatId())){
                    userService.deleteUserById(message.getChatId());
                    sendMessage.setText("Успешно!");
                    return sendMessage;
                } else {
                    sendMessage.setText("Вы еще не авторизированы");
                    return sendMessage;
                }
            default:
                sendMessage.setText("Не знаю такой команды!");
                return sendMessage;
        }
    }
}

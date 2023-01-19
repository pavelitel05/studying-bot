package com.example.tgbot.security;

import com.example.tgbot.entity.Role;
import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.User;
import com.example.tgbot.handler.InlineDialog;
import com.example.tgbot.handler.SendMessageFactory;
import com.example.tgbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorizationManager {
    private final UserService userService;
    private final InlineDialog inlineDialog = InlineDialog.getInlineDialog();
    private final SendMessageFactory sendMessageFactory;
    @Autowired
    public AuthorizationManager(UserService userService,
                                SendMessageFactory sendMessageFactory) {
        this.userService = userService;
        this.sendMessageFactory = sendMessageFactory;
    }

    private final String adminPassword = System.getenv("ADMIN_ID");

    private final String studentPassword = System.getenv("STUDENT_PASSWORD");

    public BotApiMethod<?> checkPassword(Message message){
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        String messageText = message.getText();
        if (messageText.matches("[0-9]{3,}")){
            if (messageText.equals(adminPassword)){
                user.setRole(Role.TEACHER);
            } else if (messageText.equals(studentPassword)){
                user.setRole(Role.STUDENT);
            }
            user.setStatus(Status.REQUEST_NAME);
            userService.setUser(user);
            sendMessage.setText("Введите ФИО через пробел");
        } else {
            sendMessage.setText("Не знаю такого пароля!\n Нарекаю вас наблюдателем");
            user.setStatus(Status.REQUEST_NAME);
            user.setRole(Role.VIEWER);
            userService.setUser(user);
        }
        return sendMessage;
    }

    public BotApiMethod<?> setName(Message message){
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        String messageText = message.getText();
        if (messageText.matches("[А-я]{2,} [А-я]{2,} [А-я]{2,}")){
            user.setName(messageText);
            user.setStatus(Status.REQUEST_MODULE);
            userService.setUser(user);
            sendMessage.setText("Выберите предметную область");
            List<String> buttons = new ArrayList<>();
            buttons.add("ЕГЭ");
            buttons.add("ОГЭ");
            buttons.add("Python");
            buttons.add("Java");
            buttons.add("Школьная программа");
            buttons.add("Все сразу");
            buttons.add("Просто смотрю");
            inlineDialog.setButtonsText(buttons);
            sendMessage.setReplyMarkup(inlineDialog.getMarkup());
        } else{
            sendMessage.setText("Введите ФИО по примеру:\n" +
                                "Фамилия Имя Отчество");
        }
        return sendMessage;
    }

    public BotApiMethod<?> setModule(Update update){
        CallbackQuery callbackQuery = update.getCallbackQuery();
        User user = userService.getUserById(callbackQuery.getMessage().getChatId());
        SendMessage sendMessage = sendMessageFactory.getSendMessage(update.getCallbackQuery());
        sendMessage.setText("Успешно!");
        user.setModule(callbackQuery.getData());
        user.setStatus(Status.AUTHORIZED);
        userService.setUser(user);
        return sendMessage;
    }
}

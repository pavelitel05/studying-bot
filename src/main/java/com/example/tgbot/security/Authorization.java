package com.example.tgbot.security;

import com.example.tgbot.domain.User;
import com.example.tgbot.handlers.InlineDialog;
import com.example.tgbot.services.UserService;
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
public class Authorization {
    private final UserService userService;
    private final InlineDialog inlineDialog;
    @Autowired
    public Authorization(UserService userService, InlineDialog inlineDialog) {
        this.userService = userService;
        this.inlineDialog = inlineDialog;
    }

    private final String adminPassword = System.getenv("ADMIN_ID");

    private final String studentPassword = System.getenv("STUDENT_PASSWORD");

    public BotApiMethod<?> checkPassword(Message message){
        String messageText = message.getText();
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        if (messageText.equals(adminPassword)){
            user.setRole("Teacher");
        } else if (messageText.equals(studentPassword)){
            user.setRole("Student");
        } else {
            user.setRole("Viewer");
        }
        user.setStatus("request-name");
        userService.setUser(user);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Введите ФИО через пробел");
        return sendMessage;
    }

    public BotApiMethod<?> setName(Message message){
        String messageText = message.getText();
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        user.setName(messageText);
        user.setStatus("request-module");
        userService.setUser(user);
        sendMessage.setChatId(message.getChatId());
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
        return sendMessage;
    }

    public BotApiMethod<?> setModule(Update update){
        CallbackQuery callbackQuery = update.getCallbackQuery();
        User user = userService.getUserById(callbackQuery.getMessage().getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId());
        sendMessage.setText("Успешно!");
        user.setModule(callbackQuery.getData());
        user.setStatus("authorized");
        userService.setUser(user);
        return sendMessage;
    }
}

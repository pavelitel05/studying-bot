package com.example.tgbot.security;

import com.example.tgbot.domain.User;
import com.example.tgbot.handlers.Dialog;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class Authorization {
    private final UserService userService;
    private final Dialog dialog;
    @Autowired
    public Authorization(UserService userService, Dialog dialog) {
        this.userService = userService;
        this.dialog = dialog;
    }

    private final String adminPassword =System.getenv("ADMIN_ID");

    private final String studentPassword = System.getenv("STUDENT_PASSWORD");

    public BotApiMethod<?> checkPassword(Message message){
        String messageText = message.getText();
        User user = userService.getUserByChatId(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        if (messageText.equals(adminPassword)){
            user.setRole("Teacher");
        } else if (messageText.equals(studentPassword)){
            user.setRole("Student");
        } else {
            user.setRole("Viewer");
        }
        userService.setUser(user);
        sendMessage.setReplyToMessageId(2);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Введите ФИО через пробел");
        return sendMessage;
    }

    public BotApiMethod<?> setName(Message message){
        String messageText = message.getText();
        User user = userService.getUserByChatId(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        user.setName(messageText);
        userService.setUser(user);
        sendMessage.setReplyToMessageId(3);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Выберите предметную область");
        List<String> buttons = new ArrayList<>();
        buttons.add("ЕГЭ");
        buttons.add("ОГЭ");
        buttons.add("Python");
        buttons.add("Java");
        buttons.add("Школьная программа");
        dialog.setButtonsText(buttons);
        sendMessage.setReplyMarkup(dialog.getMarkup());
        return sendMessage;
    }

    public BotApiMethod<?> setModule(Update update){
        String messageText = update.getCallbackQuery().getData();
        User user = userService.getUserByChatId(update.getMessage().getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.setText("Успешно!");
        user.setModule(messageText);
        userService.setUser(user);
        return sendMessage;
    }
}

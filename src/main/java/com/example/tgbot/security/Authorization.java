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

//todo Если это сервис или компонент, то явно должно быть понятно что это
//todo Например, AuthorizationService / AuthorizationProvider / AuthorizationManager
//todo А нужна ли авторизация для телеги?
@Component
public class Authorization {
    private final UserService userService;
    private final InlineDialog inlineDialog;
    @Autowired
    public Authorization(UserService userService, InlineDialog inlineDialog) {
        this.userService = userService;
        this.inlineDialog = inlineDialog;
    }

    //todo Мб вынес бы в проперти, а туда уже из ENV
    private final String adminPassword = System.getenv("ADMIN_ID");

    private final String studentPassword = System.getenv("STUDENT_PASSWORD");

    public BotApiMethod<?> checkPassword(Message message){
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        String messageText = message.getText();
        sendMessage.setChatId(message.getChatId());
        //todo Такие моменты лучше комментить, потом забудешь
        if (messageText.matches("[0-9]{3,}")){
            if (messageText.equals(adminPassword)){
                user.setRole("Teacher");
            } else if (messageText.equals(studentPassword)){
                user.setRole("Student");
            }
            user.setStatus("request-name");
            userService.setUser(user);
            sendMessage.setText("Введите ФИО через пробел");
        } else {
            sendMessage.setText("Не знаю такого пароля!\n Нарекаю вас наблюдателем");
            user.setStatus("request-name");
            user.setRole("Viewer");
            userService.setUser(user);
        }
        return sendMessage;
    }

    public BotApiMethod<?> setName(Message message){
        User user = userService.getUserById(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        String messageText = message.getText();
        if (messageText.matches("[А-я]{2,} [А-я]{2,} [А-я]{2,}")){
            user.setName(messageText);
            user.setStatus("request-module");
            userService.setUser(user);
            sendMessage.setText("Выберите предметную область");
            List<String> buttons = new ArrayList<>();
            //todo ИМХО - предметы в отдельную табличку/массив
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
        //todo создание SendMessage вынес бы в отдельную фабрику или используй билдер (SendMessage.builder()...build())
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId());
        //todo ИМХО - заменил бы на "Привет, <Имя Фамилия>"
        sendMessage.setText("Успешно!");
        user.setModule(callbackQuery.getData());
        //todo Вообще не понял зачем это
        user.setStatus("authorized");
        userService.setUser(user);
        return sendMessage;
    }
}

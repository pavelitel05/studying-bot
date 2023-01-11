package com.example.tgbot.handlers;

import com.example.tgbot.security.Authorization;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.transaction.Transactional;

//todo Аналогично интерфейс + @Service
@Component
public class MessageHandler {
    @Autowired
    private Authorization authorization;

    @Autowired
    private UserService userService;

    @Autowired
    private CommandHandler commandHandler;

    //todo Заменить стринги на ENUM
    public BotApiMethod<?> answerMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        Message receivedMessage = update.getMessage();
        if (userService.existsById(receivedMessage.getChatId())){
            String status = userService.getUserById(receivedMessage.getChatId()).getStatus();
            if ("request-password".equals(status)){
                return authorization.checkPassword(receivedMessage);
            } else if ("request-name".equals(status)){
                return authorization.setName(receivedMessage);
            } else if ("request-module".equals(status)) {
                return authorization.setModule(update);
            } else if ("request-timetable".equals(status)) {
                if ("Teacher".equals(userService.getUserById(receivedMessage.getChatId()).getRole())){
                    return commandHandler.setLessonForStudent(receivedMessage);
                }else{
                    return commandHandler.setMyLessons(receivedMessage);
                }
            } else if ("request-timetable-to-delete".equals(status)){
                return commandHandler.deleteLesson(receivedMessage);
            } else if ("request-timetable-to-mark".equals(status)){
                return commandHandler.addMark(receivedMessage);
            } else if ("request-mark".equals(status)) {
                return commandHandler.addMark(receivedMessage);
            }
        }
        sendMessage.setChatId(receivedMessage.getChatId());
        sendMessage.setText(receivedMessage.getText());
        return sendMessage;
    }
}

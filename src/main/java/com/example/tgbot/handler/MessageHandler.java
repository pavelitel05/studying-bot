package com.example.tgbot.handler;

import com.example.tgbot.entity.Role;
import com.example.tgbot.entity.Status;
import com.example.tgbot.security.AuthorizationManager;
import com.example.tgbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.transaction.Transactional;

@Service
public class MessageHandler {
    @Autowired
    public MessageHandler(AuthorizationManager authorizationManager,
                          UserService userService,
                          CommandHandler commandHandler,
                          SendMessageFactory sendMessageFactory) {
        this.authorizationManager = authorizationManager;
        this.userService = userService;
        this.commandHandler = commandHandler;
        this.sendMessageFactory = sendMessageFactory;
    }
    private final AuthorizationManager authorizationManager;
    private final UserService userService;
    private final CommandHandler commandHandler;
    private final SendMessageFactory sendMessageFactory;


    @Transactional
    public BotApiMethod<?> answerMessage(Update update) {
        Message receivedMessage = update.getMessage();
        SendMessage sendMessage = sendMessageFactory.getSendMessage(receivedMessage);
        if (userService.existsById(receivedMessage.getChatId())){
            Status status = userService.getUserById(receivedMessage.getChatId()).getStatus();
            switch (status){
                case REQUEST_PASSWORD:
                    return authorizationManager.checkPassword(receivedMessage);
                case REQUEST_NAME:
                    return authorizationManager.setName(receivedMessage);
                case REQUEST_MODULE:
                    return authorizationManager.setModule(update);
                case REQUEST_TIMETABLE:
                    if (Role.TEACHER.equals(userService.getUserById(receivedMessage.getChatId()).getRole())){
                        return commandHandler.setLessonForStudent(receivedMessage);
                    }else{
                        return commandHandler.addLesson(update);
                    }
                case REQUEST_TIMETABLE_TO_DELETE:
                    return commandHandler.deleteLesson(receivedMessage);
                case REQUEST_BOOKMARK:
                case REQUEST_TIMETABLE_TO_BOOKMARK:
                    return commandHandler.addMark(receivedMessage);
                case REQUEST_TIME:
                    return commandHandler.addLesson(update);
            }
        }
        sendMessage.setText(receivedMessage.getText());
        return sendMessage;
    }
}

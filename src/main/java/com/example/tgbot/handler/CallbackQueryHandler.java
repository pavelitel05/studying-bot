package com.example.tgbot.handler;

import com.example.tgbot.entity.Status;
import com.example.tgbot.security.AuthorizationManager;
import com.example.tgbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CallbackQueryHandler {
    @Autowired
    public CallbackQueryHandler(AuthorizationManager authorizationManager,
                                UserService userService,
                                CommandHandler commandHandler) {
        this.authorizationManager = authorizationManager;
        this.userService = userService;
        this.commandHandler = commandHandler;
    }

    private final AuthorizationManager authorizationManager;
    private final UserService userService;
    private final CommandHandler commandHandler;
    public BotApiMethod<?> answerCallbackQuery(Update update) {
        Status status = userService.getUserById(update.getCallbackQuery().getMessage().getChatId()).getStatus();
        switch (status){
            case REQUEST_MODULE:
                return authorizationManager.setModule(update);
            case REQUEST_WEEKDAY:
                return commandHandler.addLesson(update);
        }
        return null;
    }
}

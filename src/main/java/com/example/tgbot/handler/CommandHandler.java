package com.example.tgbot.handler;

import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.User;
import com.example.tgbot.entity.WeekDay;
import com.example.tgbot.security.AuthenticationManager;
import com.example.tgbot.service.TimetableFactory;
import com.example.tgbot.service.TimetableService;
import com.example.tgbot.service.UserFactory;
import com.example.tgbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.tgbot.handler.TelegramCommand.*;


@Service
public class CommandHandler {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TimetableService timetableService;
    private final ReplyDialog replyDialog = ReplyDialog.getReplyDialog();
    private final InlineDialog inlineDialog = InlineDialog.getInlineDialog();
    private final UserFactory userFactory;
    private final SendMessageFactory sendMessageFactory;
    private final TimetableFactory timetableFactory;

    @Autowired
    public CommandHandler(UserService userService,
                          AuthenticationManager authenticationManager,
                          TimetableService timetableService,
                          UserFactory userFactory,
                          SendMessageFactory sendMessageFactory,
                          TimetableFactory timetableFactory) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.timetableService = timetableService;
        this.userFactory = userFactory;
        this.sendMessageFactory = sendMessageFactory;
        this.timetableFactory = timetableFactory;
    }
    public BotApiMethod<?> answerCommand(Update update) {
        Message message = update.getMessage();
        switch (message.getText()){
            case start:
                return start(message);
            case help:
                return help(message);
            case authorization:
               return authorization(message);
            case logout:
                return logout(message);
            case addLesson:
                return addLesson(update);
            case setLessonForStudent:
                return setLessonForStudent(message);
            case cancel:
                return cancel(message);
            case deleteLesson:
                return deleteLesson(message);
            case addMark:
                return addMark(message);
            default:
                return def(message);
        }
    }

    public BotApiMethod<?> addMark(Message message) {
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        var user = userService.getUserById(message.getChatId());
        Status status = user.getStatus();
        List<String> buttons = timetableService.getAllEntriesForStudent(message.getChatId())
                .stream()
                .map(el -> el.toString())
                .collect(Collectors.toList());
        if (buttons != null){
            switch (status) {
                case AUTHORIZED:
                    replyDialog.setButtonsText(buttons);
                    sendMessage.setReplyMarkup(replyDialog.getMarkup());
                    sendMessage.setText("Выберете время, которое хотите пометить");
                    user.setStatus(Status.REQUEST_TIMETABLE_TO_BOOKMARK);
                    userService.setUser(user);
                    break;
                case REQUEST_TIMETABLE_TO_BOOKMARK:
                    if (buttons.contains(message.getText())){
                        String[] weekdayAndTime = message.getText().split(" - ");
                        var timetable = timetableService.getTimetableByWeekdayAndTime(weekdayAndTime[0], weekdayAndTime[1]);
                        timetable.setStatus(Status.REQUEST_BOOKMARK);
                        timetableService.setEntryInTimetable(timetable);
                        user.setStatus(Status.REQUEST_BOOKMARK);
                        userService.setUser(user);
                        sendMessage.setText("Введите заметку :)");
                    }else {
                        sendMessage.setText("Выберете время, которое хотите пометить");
                    }
                    break;
                case REQUEST_BOOKMARK:
                    var timetableTwo = timetableService.getTimetableByChatIdAndStatus(message.getChatId(), status);
                    timetableTwo.setBookmark(message.getText());
                    timetableTwo.setStatus(Status.FREE);
                    timetableService.setEntryInTimetable(timetableTwo);
                    user.setStatus(Status.AUTHORIZED);
                    userService.setUser(user);
                    sendMessage.setText("Заметка успешно добавлена");
                    break;
            }
        } else{
            sendMessage.setText("У вас еще нет расписания!");
        }
        return sendMessage;
    }

    public BotApiMethod<?> deleteLesson(Message message) {
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        var user = userService.getUserById(message.getChatId());
        Status status = user.getStatus();
        List<String> buttons = timetableService.getAllEntriesForStudent(message.getChatId())
                .stream()
                .map(el -> el.toString())
                .collect(Collectors.toList());
        switch (status){
            case AUTHORIZED:
                replyDialog.setButtonsText(buttons);
                sendMessage.setReplyMarkup(replyDialog.getMarkup());
                sendMessage.setText("Выберете время, которое хотите удалить");
                user.setStatus(Status.REQUEST_TIMETABLE_TO_DELETE);
                userService.setUser(user);
                break;
            case REQUEST_TIMETABLE_TO_DELETE:
                if (buttons.contains(message.getText())){
                    String[] weekdayAndTime = message.getText().split(" - ");
                    timetableService.deleteEntryInTimetableWeekdayAndTime(weekdayAndTime[0], weekdayAndTime[1]);
                    user.setStatus(Status.AUTHORIZED);
                    userService.setUser(user);
                    sendMessage.setText("Успешно!");
                }else {
                    sendMessage.setText("Выберете время, которое хотите удалить");
                }
                break;
        }
        return sendMessage;
    }

    public BotApiMethod<?> addLesson(Update update) {
        Message message = update.getMessage();
        CallbackQuery callbackQuery = update.getCallbackQuery();
        SendMessage sendMessage = null;
        if (message != null){
            sendMessage = sendMessageFactory.getSendMessage(message);
        } else {
            sendMessage = sendMessageFactory.getSendMessage(callbackQuery);
        }
        Long chatId = null;
        if (message != null){
            chatId = message.getChatId();
        } else {
            chatId = callbackQuery.getMessage().getChatId();
        }
        if ( "low".equals(authenticationManager.getPermission(chatId))){
            sendMessage.setText("У вас нет доступа для использования этой команды!");
        } else {
            var user = userService.getUserById(chatId);
            Status status = user.getStatus();
            switch (status) {
                case AUTHORIZED:
                    for (WeekDay weekDay : WeekDay.values()) {
                        inlineDialog.addButtonText(weekDay.getValue());
                    }
                    sendMessage.setReplyMarkup(inlineDialog.getMarkup());
                    sendMessage.setText("Выберите день недели");
                    var timetable = timetableFactory.newTimetable(chatId);
                    timetableService.setEntryInTimetable(timetable);
                    user.setStatus(Status.REQUEST_WEEKDAY);
                    userService.setUser(user);
                    break;
                case REQUEST_WEEKDAY:
                    var timetableTwo = timetableService
                            .getTimetableByChatIdAndStatus(chatId, Status.REQUEST_WEEKDAY);
                    timetableTwo.setStatus(Status.REQUEST_TIME);
                    timetableTwo.setWeekDay(callbackQuery.getData());
                    timetableService.setEntryInTimetable(timetableTwo);
                    user.setStatus(Status.REQUEST_TIME);
                    sendMessage.setText("Введите время урока в формате (ЧЧ:ММ)\n" +
                            "Например:\n" +
                            "20:45");
                    userService.setUser(user);
                    break;
                case REQUEST_TIME:
                    String messageText = message.getText();
//                    Проверка корректности ввода времени в формате ЧЧ:ММ
                    if (messageText.matches("(00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23):[0-5][0-9]")) {
                        Integer hour = Integer.getInteger(messageText.split(":")[0]);
                        Integer minute = Integer.getInteger(messageText.split(":")[1]);
                        var timetableThree = timetableService
                                .getTimetableByChatIdAndStatus(chatId, Status.REQUEST_TIME);
                        timetableThree.setTime(messageText);
                        timetableThree.setStatus(Status.FREE);
                        timetableService.setEntryInTimetable(timetableThree);
                        sendMessage.setText("Успешно!");
                        user.setStatus(Status.AUTHORIZED);
                        userService.setUser(user);
                    } else {
                        sendMessage.setText("Некорреткный формат ввода, повторите попытку");
                    }
                    break;
            }
        }
        return sendMessage;
    }
    public BotApiMethod<?> authorization(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        if (userService.existsById(message.getChatId())){
            User user = userService.getUserById(message.getChatId());
            Status status = user.getStatus();
            switch (status){
                case AUTHORIZED:
                    sendMessage.setText("❗Вы уже авторизированы❗\n" +
                            "Если хотите выйти из учетной записи воспользуйтесь:\n" +
                            logout);
                    return sendMessage;
                case REQUEST_PASSWORD:

            }
        }else {
            userFactory.newUser(message);
            sendMessage.setText("Введите вам выданный пароль");
        }
        return sendMessage;
    }
    private BotApiMethod<?> cancel(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        if (userService.existsById(message.getChatId())){
            Status status = userService.getUserById(message.getChatId()).getStatus();
            if (Status.REQUEST_PASSWORD.equals(status) |
                    Status.REQUEST_NAME.equals(status) |
                    Status.REQUEST_MODULE.equals(status)){
                userService.deleteUserById(message.getChatId());
            } else if (Status.REQUEST_WEEKDAY.equals(status)){
                var timetable = timetableService
                        .getTimetableByChatIdAndStatus(message.getChatId(), Status.REQUEST_WEEKDAY);
                timetableService.deleteEntryInTimetableWeekdayAndTime(timetable.getWeekDay(), timetable.getTime());
            } else if (Status.REQUEST_TIME.equals(status)){
                var timetable = timetableService
                        .getTimetableByChatIdAndStatus(message.getChatId(), Status.REQUEST_TIME);
                timetableService.deleteEntryInTimetableWeekdayAndTime(timetable.getWeekDay(), timetable.getTime());
            }else{
                var user = userService.getUserById(message.getChatId());
                user.setStatus(Status.AUTHORIZED);
                userService.setUser(user);
            }
        }
        sendMessage.setText("Команда отменена, список команд: /help");
        return sendMessage;
    }
    private BotApiMethod<?> help(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
//        строковые переменные лежат в классе TelegramCommands
        sendMessage.setText("Список команд:\n" +
                "1. " + help + " - Список доступных команд (они так же есть в меню\uD83E\uDD2B)\n" +
                "2. " + start + " - Приветствие\uD83D\uDD96\n" +
                "3. " + authorization + "- Автоизируйтесь, чтобы получить больше возможностей (если вы ученик, конечно)\n" +
                "Доступные после авторизации:\n" +
                "4. " + logout + " - Выход из учетной записи, может пригодится\n" +
                "5. " + cancel + " - Отмена последней примененной команды\n" +
                "6. " + addLesson + " - Добавь себе уроков в расписание!\n" +
                "7. " + deleteLesson + " - Удалить их тоже можно\n" +
                "8. " + addMark + " - Добавь заметку ко времени\n" +
                "Доступные учителям:\n" +
                "9. " + deleteLessonForStudent + "Удалить урок ученику\n" +
                "10. " + setLessonForStudent + "Установить урок ученику");
        return sendMessage;
    }
    private BotApiMethod<?> logout(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        if (userService.existsById(message.getChatId())){
            userService.deleteUserById(message.getChatId());
            sendMessage.setText("Успешно!");
            return sendMessage;
        } else {
            sendMessage.setText("Вы еще не авторизированы");
            return sendMessage;
        }
    }
    private BotApiMethod<?> start(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Приветствую в NerzonStudyingBot!\n" +
                "*\n" +
                "Он создан для коммуникации учеников с их преподавателем.\n" +
                "*\n" +
                "Вы можете ознакомиться со своим расписанием и изменить его статус\n");
        return sendMessage;
    }
    private BotApiMethod<?> def(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        sendMessage.setText("Не знаю такой команды!");
        return sendMessage;
    }
/*
Ниже приведены команды, если быть точнее их методы, нужные для работы импровизированной админки
 */
    public BotApiMethod<?> setLessonForStudent(Message message) {
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        return sendMessage;
    }

    public BotApiMethod<?> deleteLessonForStudent(Message message){
        SendMessage sendMessage = sendMessageFactory.getSendMessage(message);
        return sendMessage;
    }
}
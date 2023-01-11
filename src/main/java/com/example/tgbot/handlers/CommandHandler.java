package com.example.tgbot.handlers;

import com.example.tgbot.domain.Timetable;
import com.example.tgbot.domain.User;
import com.example.tgbot.security.Authentication;
import com.example.tgbot.services.TimetableService;
import com.example.tgbot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


//TODO Аналогично - @Component на @Service
@Component
@Slf4j
public class CommandHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private Authentication authentication;
    @Autowired
    private TimetableService timetableService;
    @Autowired
    private ReplyDialog replyDialog;

    //todo Заменить все на Enum'ы
    public BotApiMethod<?> answerCommand(Update update) {
        Message message = update.getMessage();
        switch (message.getText()){
            case "/start":
                return start(message);
            case "/help":
                return help(message);
            case "/authorization":
               return authorization(message);
            case "/logout":
                return logout(message);
            case "/set_my_lessons":
                return setMyLessons(message);
            case "/set_lesson_for_student":
                return setLessonForStudent(message);
            case "/cancel":
                return cancel(message);
            case "/delete_lesson":
                return deleteLesson(message);
            case "/add_mark":
                return addMark(message);
            default:
                return def(message);
        }
    }

    //todo Тут тоже на Enum'ы заменить
    public BotApiMethod<?> setMyLessons(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        AtomicBoolean validateFactor = new AtomicBoolean(true);
        var user = userService.getUserById(message.getChatId());
        if ("medium".equals(authentication.getPermission(message.getChatId())) |
            "high".equals(authentication.getPermission(message.getChatId()))) {
            if ("request-timetable".equals(userService
                    .getUserById(message.getChatId())
                    .getStatus())) {
                String[] entries = message.getText().split("\n");
                Arrays.stream(entries).forEach(el -> {
                    if (el.toLowerCase().matches("(понедельник|вторник|среда|четверг|пятница|суббота|воскресенье)\\s-\\s(00|01|02|03|04|05|06|07|08|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23):[0-5][0-9]\n")){
                        validateFactor.set(false);
                    }
                });
                if (validateFactor.get()){
                    //todo ИМХО - выбор даты лучше через клавиатуру
                    sendMessage.setText("Некорректный формат ввода, повторите попытку!\n" +
                                        "Возможные проблемы:\n" +
                                        "Лишние пробелы\n" +
                                        "Не соблюдение шаблона (неверно или с ошибкой указаны день и время.");
                    return sendMessage;
                }
                for (String entry : entries) {
                    Timetable timetable = new Timetable();
                    timetable.setStudentName(userService.getUserById(message.getChatId()).getName());
                    timetable.setDateTime(entry);
                    timetableService.setEntryInTimetable(timetable);
                }
                sendMessage.setText("Успешно!");
                user.setStatus("authorized");
                userService.setUser(user);
                sendMessage.setReplyMarkup(new ReplyKeyboardMarkup());
            } else {
                sendMessage.setText("Напишите ваше расписание в формате:\n" +
                        //todo Формат указать вместо ч:м - чч:мм
                                    "День недели (Полностью) - время в формате (ч:м)\n" +
                                    "Например:\n" +
                                    "Понедельник - 12:00\n" +
                                    "Суббота - 16:15");
                user.setStatus("request-timetable");
                userService.setUser(user);
            }
        } else {
            sendMessage.setText("Извините, у вас нет доступа к использованию этой команды!");
        }
        return sendMessage;
    }

    public BotApiMethod<?> setLessonForStudent(Message message){
        //todo Builder / Factory
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        //todo Заменить на Enum'ы
        if ("high".equals(authentication.getPermission(message.getChatId()))){
            User user = userService.getUserById(message.getChatId());
            if ("request-timetable".equals(user.getStatus())){
                String[] entry = message.getText().split("\n");
                //todo Нечитаемо без контекста, оставляй комменты
                if (entry.length == 2) {
                    Timetable timetable = new Timetable();
                    timetable.setStudentName(entry[0]);
                    timetable.setDateTime(entry[1]);
                    timetableService.setEntryInTimetable(timetable);
                    sendMessage.setText("Успешно");
                    user.setStatus("authorized");
                    userService.setUser(user);
                }else {
                    //todo ИМХО опять же - сообщение вынести в private static final поля в начало класса или в отдельный класс (тогда public)
                    sendMessage.setText("Некорректный формат ввода, повторите попытку!");
                }
            } else {
                user.setStatus("request-timetable");
                userService.setUser(user);
                sendMessage.setText("Введите данные по шаблону:\n" +
                                    "ФИО ученика\n" +
                                    "День недели - время");
            }
        }else {
            sendMessage.setText("Извините, у вас нет доступа к использованию этой команды!");
        }
        return sendMessage;
    }

    private BotApiMethod<?> cancel(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (userService.existsById(message.getChatId())){
            String status = userService.getUserById(message.getChatId()).getStatus();
            if ("request-password".equals(status) |
                    "request-name".equals(status) |
                    "request-module".equals(status)){
                userService.deleteUserById(message.getChatId());
            } else{
                var userTwo = userService.getUserById(message.getChatId());
                userTwo.setStatus("authorized");
                userService.setUser(userTwo);
            }
        }
        sendMessage.setText("Команда отменена, список команд: /help");
        return sendMessage;
    }

    public BotApiMethod<?> authorization(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        // validation
        if (userService.existsById(message.getChatId())){
            if ("authorized".equals(userService.getUserById(message.getChatId()).getStatus())){
                sendMessage.setText("❗Вы уже авторизированы❗\n" +
                        "Если хотите выйти из учетной записи воспользуйтесь:\n" +
                        "/logout\n");
                return sendMessage;
            }
        }
        //Creating not authorized user
        User user = new User();
        user.setName("Not authorized");
        user.setChatId(message.getChatId());
        user.setStatus("request-password");
        userService.setUser(user);
        sendMessage.setText("Введите вам выданный пароль");
        return sendMessage;
    }

    private BotApiMethod<?> help(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Список команд:\n" +
                "1. /help - Список доступных команд (они так же есть в меню\uD83E\uDD2B)\n" +
                "2. /start - Приветствие\uD83D\uDD96\n " +
                "3. /authorization- Автоизируйтесь, чтобы получить больше возможностей (если вы ученик, конечно)\n" +
                "Доступные после авторизации:\n" +
                "4. /logout - Выход из учетной записи, может пригодится\n" +
                "5. /cancel - Отмена последней примененной команды\n" +
                "6. /set_my_lessons - Добавь себе уроков в расписание!\n" +
                "7. /delete_lesson - Удалить их тоже можно\n" +
                "8. /add_mark - Добавь заметку ко времени\n");
        return sendMessage;
    }

    public BotApiMethod<?> logout(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
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
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Не знаю такой команды!");
        return sendMessage;
    }

    /*
    deleteLesson() method removes entry which user chose
     */
    public BotApiMethod<?> deleteLesson(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        var user = userService.getUserById(message.getChatId());
        if ("request-timetable-to-delete".equals(user.getStatus())){
            timetableService.deleteEntryInTimetableOnTime(message.getText());
            user.setStatus("authorized");
            userService.setUser(user);
            sendMessage.setText("Удалено из расписания!");
            sendMessage.setReplyMarkup(new ReplyKeyboardMarkup());
        } else {
            List<String> buttons = new ArrayList<>();
            for (Timetable timetable : timetableService
                    .getTimeTableForStudentWithName(user.getName())) {
                buttons.add(timetable.getDateTime());
            }
            replyDialog.setButtonsText(buttons);
            sendMessage.setReplyMarkup(replyDialog.getMarkup());
            sendMessage.setText("Выберите время, которое хотите удалить");
            user.setStatus("request-timetable-to-delete");
            userService.setUser(user);
        }
        return sendMessage;
    }

    /*
    addMark method() adds mark gave by user in entry chose by user
     */
    public BotApiMethod<?> addMark(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        var user = userService.getUserById(message.getChatId());
        if ("request-timetable-to-mark".equals(user.getStatus())){
            Timetable timetable = timetableService.findTimetableByDateTime(message.getText());
            timetable.setMark("changing");
            timetableService.setEntryInTimetable(timetable);
            sendMessage.setText("Введите маркер, который вы хотите добавить");
            user.setStatus("request-mark");
            userService.setUser(user);
        } else if ("request-mark".equals(user.getStatus())) {
            Timetable timetable = timetableService.findTimetableByMark("changing");
            timetable.setMark(message.getText());
            sendMessage.setText("Заметка добавлена");
            timetableService.setEntryInTimetable(timetable);
            user.setStatus("authorized");
            userService.setUser(user);
        } else {
            user.setStatus("request-timetable-to-mark");
            userService.setUser(user);
            List<String> buttons = new ArrayList<>();
            for (Timetable timetable : timetableService
                    .getTimeTableForStudentWithName(user.getName())) {
                buttons.add(timetable.getDateTime());
            }
            replyDialog.setButtonsText(buttons);
            sendMessage.setReplyMarkup(replyDialog.getMarkup());
            sendMessage.setText("Выберите время, к которому хотите поставить маркер");
        }
        return sendMessage;
    }
}
package com.example.tgbot.service;

import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableFactory {

    private final TimetableService timetableService;
    private final UserService userService;

    @Autowired
    public TimetableFactory(TimetableService timetableService,
                            UserService userService) {
        this.timetableService = timetableService;
        this.userService = userService;
    }

    public Timetable newTimetable(Long chatId){
        Timetable timetable = new Timetable();
        var user = userService.getUserById(chatId);
        timetable.setUser(user);
        timetable.setStudentName(user.getName());
        timetable.setChatId(chatId);
        timetable.setStatus(Status.REQUEST_WEEKDAY);
        return timetable;
    }
}

package com.example.tgbot.repository;

import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByStudentName(String studentName);
    void deleteTimetableByWeekDayAndTime(String time, String weekDay);
    void deleteAllByStudentName(String name);
    Timetable findTimetableByWeekDayAndTime(String time, String weekDay);
    Timetable findTimetableByBookmark(String mark);
    Timetable findTimetableByChatIdAndStatus(Long chatId, Status status);
    List<Timetable> findAllByChatId(Long chatId);
}

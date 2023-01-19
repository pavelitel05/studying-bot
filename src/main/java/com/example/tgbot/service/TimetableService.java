package com.example.tgbot.service;

import com.example.tgbot.entity.Status;
import com.example.tgbot.entity.Timetable;
import com.example.tgbot.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> getTimetable(){
        return timetableRepository.findAll();
    }

    public List<Timetable> getTimeTableForStudentWithName(String studentName){
        return timetableRepository.findByStudentName(studentName);
    }
    public void setEntryInTimetable(Timetable timetable){
        timetableRepository.save(timetable);
    }
    public void deleteEntryInTimetableWeekdayAndTime(String weekDay, String time){
        timetableRepository.deleteTimetableByWeekDayAndTime(weekDay, time);
    }
    public void deleteEntriesFotStudent(String studentName){
        timetableRepository.deleteAllByStudentName(studentName);
    }
    public Timetable getTimetableByWeekdayAndTime(String weekDay, String time){
        return timetableRepository.findTimetableByWeekDayAndTime(weekDay, time);
    }
    public Timetable getTimetableByMark(String mark){
        return timetableRepository.findTimetableByBookmark(mark);
    }
    public Timetable getTimetableByChatIdAndStatus(Long chatId, Status status){
        return timetableRepository.findTimetableByChatIdAndStatus(chatId, status);
    }

    public List<Timetable> getAllEntriesForStudent(Long chatId){
        return timetableRepository.findAllByChatId(chatId);
    }
}

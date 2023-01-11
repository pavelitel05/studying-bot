package com.example.tgbot.services;

import com.example.tgbot.domain.Timetable;
import com.example.tgbot.repositories.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> getTimeTable(){
        return timetableRepository.findAll();
    }

    public List<Timetable> getTimeTableForStudentWithName(String studentName){
        return timetableRepository.findByStudentName(studentName);
    }

    public void setEntryInTimetable(Timetable timetable){
        timetableRepository.save(timetable);
    }

    public void deleteEntryInTimetableOnTime(String dateTime){
        timetableRepository.deleteTimetableByDateTime(dateTime);
    }

    public void deleteEntriesFotStudent(String studentName){
        timetableRepository.deleteAllByStudentName(studentName);
    }

    public Timetable findTimetableByDateTime(String dateTime){
        return timetableRepository.findTimetableByDateTime(dateTime);
    }

    public Timetable findTimetableByMark(String mark){
        return timetableRepository.findTimetableByMark(mark);
    }
}

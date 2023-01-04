package com.example.tgbot.repositories;

import com.example.tgbot.domain.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, String> {
    List<Timetable> findByStudentName(String studentName);

    void deleteAllByStudentName(String name);
}

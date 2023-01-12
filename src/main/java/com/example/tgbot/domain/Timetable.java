package com.example.tgbot.domain;

import com.example.tgbot.services.TimetableService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "timetable")
@Getter
@Setter
@ToString
public class Timetable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long chatId;

    @Column(name = "student", nullable = false)
    private String studentName;

    @Column(name = "week_day")
    private WeekDay weekDay;

    @Column(name = "time")
    private Time time;

    @Column(name = "topic")
    private String topic;

    @Column(name = "mark")
    private String mark;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private User user;
}

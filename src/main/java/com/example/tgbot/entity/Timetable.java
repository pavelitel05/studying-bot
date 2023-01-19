package com.example.tgbot.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "timetable")
@Getter
@Setter
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
    private String weekDay;

    @Column(name = "time")
    private String time;

    @Column(name = "topic")
    private String topic;

    @Column(name = "bookmark")
    private String bookmark;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private User user;

    @Override
    public String toString(){
        return weekDay + " - " + time;
    }
}

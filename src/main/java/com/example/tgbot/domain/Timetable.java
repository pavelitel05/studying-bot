package com.example.tgbot.domain;

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

    @Column(name = "dateTime")
    private String dateTime;

    @Column(name = "student", nullable = false)
    private String studentName;

    @Column(name = "topic")
    private String topic;

    @Column(name = "mark")
    private String mark;
}

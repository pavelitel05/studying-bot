package com.example.tgbot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "timetable")
@Getter
@Setter
public class Timetable {
    @Id
    @Column(name = "dateTime", nullable = false)
    private String dateTime;

    @Column(name = "student", nullable = false)
    private String name;

    @Column(name = "topic")
    private String topic;

    @Column(name = "status")
    private String status;
}

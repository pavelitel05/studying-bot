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

    //todo Заменить на LocalDate / Date
    @Column(name = "dateTime")
    private String dateTime;

    //todo Связь с user
    @Column(name = "student", nullable = false)
    private String studentName;
    @Column(name = "topic")
    private String topic;

    //todo А тип точно String?
    @Column(name = "mark")
    private String mark;

    //todo Сюда lombok'овский toString можно
    @Override
    public String toString() {
        return "Timetable{" +
                "id=" + id +
                ", dateTime='" + dateTime + '\'' +
                ", studentName='" + studentName + '\'' +
                ", topic='" + topic + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}

package com.example.tgbot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {

    @Id
    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "module")
    private String module;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "chatId")
    private Set<Timetable> entries;

}

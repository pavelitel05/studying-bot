package com.example.tgbot.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @Column(name = "role")
    private String role;

    @Column(name = "module")
    private String module;
}

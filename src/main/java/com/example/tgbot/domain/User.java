package com.example.tgbot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "chatId", nullable = false)
    private Long chatId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "module")
    private String module;

    @Column(name = "status")
    private String status;


    @Override
    public String toString() {
        return "User{" +
                "id=" + chatId +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}

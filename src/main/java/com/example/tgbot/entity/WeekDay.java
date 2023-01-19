package com.example.tgbot.entity;

public enum WeekDay {
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятнциа"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье");

    private final String value;
    WeekDay(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package com.example.tgbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "telegram-bot")
public class TelegramBotConfigurationProperties {
    private String token;

    private String name;

    private String webhookPath;
}

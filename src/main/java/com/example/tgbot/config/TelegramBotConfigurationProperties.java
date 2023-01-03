package com.example.tgbot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegram-bot")
public class TelegramBotConfigurationProperties {
    private String token;
    private String apiUrl;
    private String name;
    private String webhookPath;
}

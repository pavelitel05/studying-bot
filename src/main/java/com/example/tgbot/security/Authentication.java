package com.example.tgbot.security;

import com.example.tgbot.domain.User;
import com.example.tgbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//todo Если это сервис или компонент, то явно должно быть понятно что это
//todo Например, AuthenticationService / AuthenticationProvider / AuthenticatorManager
@Component
public class Authentication {
    private final UserService userService;

    @Autowired
    public Authentication(UserService userService){
        this.userService = userService;
    }

    public String getPermission(Long id){
        //todo Делаешь лишний запрос, я бы сделал так:
        //todo Первый запрос проверяет есть ли пользователь, а второй уже ищет.
        //todo Возможно под капотом PostgreSQL закеширует это и времени не потеряем, но не уверен
        /*

        var user = userService.findUserById(id);
        if (user != null) {
            ...
        }

         */
        if (userService.existsById(id)){
            User user = userService.getUserById(id);
            String role = user.getRole();
            //todo Заменить на Enum
            switch (role) {
                case "Viewer":
                    return "low";
                case "Student":
                    return "medium";
                case "Teacher":
                    return "high";
            }
        } else {
            return "low";
        }
        //todo Вот с этого вообще ахуел
        throw new NullPointerException();
    }
}

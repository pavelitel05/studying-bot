package com.example.tgbot.handlers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@NoArgsConstructor
public class ReplyDialog {
    private List<String> buttonsText;

    public ReplyKeyboardMarkup getMarkup(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < this.buttonsText.size(); i += 2){
            KeyboardRow row = new KeyboardRow();
            KeyboardButton button = new KeyboardButton();
            button.setText(this.buttonsText.get(i));
            row.add(button);
            if (this.buttonsText.size() > i + 1){
                KeyboardButton buttonTwo = new KeyboardButton();
                buttonTwo.setText(this.buttonsText.get(i + 1));
                row.add(buttonTwo);
            }
            rows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(rows);
        this.buttonsText.clear();
        return replyKeyboardMarkup;
    }
}

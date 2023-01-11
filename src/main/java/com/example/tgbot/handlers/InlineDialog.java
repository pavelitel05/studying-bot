package com.example.tgbot.handlers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
//todo А это здесь зачем?
@NoArgsConstructor
public class InlineDialog {

    private List<String> buttonsText;

    public InlineKeyboardMarkup getMarkup(){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        //todo здесь по идеи поймаешь NPE, так как buttonsText не инициализирован
        for (int i = 0; i < this.buttonsText.size(); i += 2){
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(this.buttonsText.get(i));
            button.setCallbackData(this.buttonsText.get(i));
            row.add(button);
            if (this.buttonsText.size() > i + 1){
                InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
                buttonTwo.setText(this.buttonsText.get(i + 1));
                buttonTwo.setCallbackData(this.buttonsText.get(i + 1));
                row.add(buttonTwo);
            }
            rows.add(row);
        }
        keyboardMarkup.setKeyboard(rows);
        //todo А this зачем?
        this.buttonsText.clear();
        return keyboardMarkup;
    }
}

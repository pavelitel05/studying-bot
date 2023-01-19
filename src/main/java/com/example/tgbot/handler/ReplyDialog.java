package com.example.tgbot.handler;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReplyDialog {
    private static ReplyDialog replyDialog;

    private ReplyDialog(){}

    public static ReplyDialog getReplyDialog() {
        if (replyDialog == null){
            replyDialog = new ReplyDialog();
        }
        return replyDialog;
    }
    private List<String> buttonsText;

    public void addButtonText(String buttonText){
        buttonsText.add(buttonText);
    }

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

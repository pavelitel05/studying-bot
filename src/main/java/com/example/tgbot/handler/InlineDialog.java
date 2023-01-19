package com.example.tgbot.handler;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Data
public class InlineDialog {
    private static InlineDialog inlineDialog;
    private InlineDialog(){}
    public static InlineDialog getInlineDialog() {
        if (inlineDialog == null){
            inlineDialog = new InlineDialog();
        }
        return inlineDialog;
    }
    private List<String> buttonsText = new ArrayList<>();
    public void addButtonText(String buttonText){
        buttonsText.add(buttonText);
    }
    public InlineKeyboardMarkup getMarkup(){
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < buttonsText.size(); i += 2){
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonsText.get(i));
            button.setCallbackData(buttonsText.get(i));
            row.add(button);
            if (buttonsText.size() > i + 1){
                InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
                buttonTwo.setText(buttonsText.get(i + 1));
                buttonTwo.setCallbackData(buttonsText.get(i + 1));
                row.add(buttonTwo);
            }
            rows.add(row);
        }
        keyboardMarkup.setKeyboard(rows);
        buttonsText.clear();
        return keyboardMarkup;
    }
}

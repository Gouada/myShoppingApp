package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gouadadopavogui on 15.02.2017.
 */

public class Validator {

    public Validator() {
    }

    public List<EditText> validateEditTextFieldsNotEmpty(List<EditText> editTextFields, View view) {
        List<EditText> failedValidation = null;
        if (editTextFields != null)
        {
            failedValidation = new ArrayList<EditText>();
            for (EditText editText:editTextFields)
            {
                String textInField = editText.getText().toString();
                if (textInField.equals("") || textInField.isEmpty())
                    failedValidation.add(editText);
            }
        }
        return failedValidation;
    }
    public List<EditText> validateEditTextFieldsLength(List<EditText> editTextFields, View view) {
        List<EditText> failedValidation = null;
        if (editTextFields != null)
        {
            failedValidation = new ArrayList<EditText>();
            Pattern pattern = Pattern.compile("\\d{8,20}+");
            Matcher matcher;
            for (EditText editText:editTextFields)
            {
                String textInField = editText.getText().toString();
                matcher = pattern.matcher(textInField);
                if (!matcher.matches())
                    failedValidation.add(editText);
            }
        }
        return failedValidation;
    }
}

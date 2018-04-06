package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by gouadadopavogui on 02.03.2017.
 */

public abstract class TextChangedListener<T> implements TextWatcher {
    private T targetField;
    private String valueBefore ="";
    private String valueAfter="";
    public TextChangedListener(T targetField) {
        this.targetField = targetField;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        valueBefore = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.onTextChanged(targetField, s);
    }

    public abstract void onTextChanged(T targetField, Editable s);
}

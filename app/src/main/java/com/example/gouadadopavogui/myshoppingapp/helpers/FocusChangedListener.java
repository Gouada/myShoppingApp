package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.content.Context;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by gouadadopavogui on 02.03.2017.
 */

public abstract class FocusChangedListener<T> implements View.OnFocusChangeListener {
    private T targetField;
    private  Context context;
    private IBinder windowToken;

    public FocusChangedListener(T targetField) {
        this.targetField = targetField;
    }

    public FocusChangedListener(T targetField, Context context, IBinder windowToken) {
        this.targetField = targetField;
        this.context = context;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
     //onFocusChange(targetField, v, hasFocus, windowToken);
        //if (targetField.getClass() == EditText.class) {
            //IBinder windowToken = ((EditText) targetField).getWindowToken();
        hideSoftKeyBoard(windowToken);
        //}
    }

    //public abstract void onFocusChange(T targetField, View view, boolean hasFocus, IBinder windowToken);


    public void hideSoftKeyBoard(IBinder windowToken)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText() )
            inputMethodManager.hideSoftInputFromWindow(windowToken,0);
    }
}

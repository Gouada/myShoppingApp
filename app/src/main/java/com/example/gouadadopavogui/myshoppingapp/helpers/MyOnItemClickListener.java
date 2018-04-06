package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by GouadaDopavogui on 01.07.2017.
 */

public abstract class MyOnItemClickListener<T> implements AdapterView.OnItemClickListener {

    public MyOnItemClickListener() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.myOnItemClick();
    }

    public abstract void myOnItemClick();
}

package com.example.gouadadopavogui.myshoppingapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.gouadadopavogui.myshoppingapp.R;

/**
 * Created by gouadadopavogui on 21.03.2017.
 */

public class MyAlertDialog extends DialogFragment {

    private int layoutId;
    private int positiveButtonId;
    private int negativeButtonId;

    private static final String ARG_PARAM1 = "layoutId";
    private static final String ARG_PARAM2 = "positiveButtonId";
    private static final String ARG_PARAM3 = "negativeButtonId";

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getPositiveButtonId() {
        return positiveButtonId;
    }

    public void setPositiveButtonId(int positiveButtonId) {
        this.positiveButtonId = positiveButtonId;
    }

    public int getNegativeButtonId() {
        return negativeButtonId;
    }

    public void setNegativeButtonId(int negativeButtonId) {
        this.negativeButtonId = negativeButtonId;
    }

    public interface MyDialogFragmentListener
    {
        public void onPositiveButtonClick(MyAlertDialog dialogFragment );
        public void onNegativeButtonClick(DialogFragment dialogFragment);
    }

    MyDialogFragmentListener myDialogFragmentListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            myDialogFragmentListener = (MyDialogFragmentListener) context;
        }
        catch(ClassCastException e)
        {
            Log.e("ERROR", e.getStackTrace().toString());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() !=null)
        {
            setLayoutId(getArguments().getInt(ARG_PARAM1));
            setPositiveButtonId(getArguments().getInt(ARG_PARAM2));
            if (getArguments().getInt(ARG_PARAM3) > 0)
                setNegativeButtonId(getArguments().getInt(ARG_PARAM3));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //builder.setView(inflater.inflate(R.layout.shopping_done_alert, null));
        builder.setView(inflater.inflate(getLayoutId(), null));
        builder.setPositiveButton(getPositiveButtonId(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDialogFragmentListener.onPositiveButtonClick(MyAlertDialog.this);
            }
        });

        if (getNegativeButtonId() > 0) {
            builder.setNegativeButton(getNegativeButtonId(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDialogFragmentListener.onNegativeButtonClick(MyAlertDialog.this);
                }
            });
        }
        return builder.create();
    }

    public static MyAlertDialog newInstance(int layoutId, int positiveButtonId, int negativeButtonId)
    {
        MyAlertDialog myAlertDialog = new MyAlertDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layoutId);
        args.putInt(ARG_PARAM2, positiveButtonId);
        args.putInt(ARG_PARAM3, negativeButtonId);

        myAlertDialog.setArguments(args);
        return myAlertDialog;
    }
}

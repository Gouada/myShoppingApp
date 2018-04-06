package com.example.gouadadopavogui.myshoppingapp.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;

import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends Fragment {


    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        TextView errorMessageView = (TextView) rootView.findViewById(R.id.messageViewId);
        Intent initializationItent = getActivity().getIntent();
        if (initializationItent != null && initializationItent.hasExtra("ERROR_MESSAGE"))
        {
            String errorMessage = initializationItent.getExtras().getString("ERROR_MESSAGE");
            errorMessageView.setTextColor(Color.RED);
            errorMessageView.setText(errorMessage);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.initialization_Activity_FmL_id, new AdhereExistingGroup()).commit();
        }

        Button createNewGroupBtn    =   (Button) rootView.findViewById(R.id.create_new_group_id);
        Button adhereToGroupBtn     =   (Button) rootView.findViewById(R.id.adhere_existing_group);

        createNewGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.initialization_Activity_FmL_id, new CreateNewGroup()).commit();
            }
        });

        adhereToGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.initialization_Activity_FmL_id, new AdhereExistingGroup()).commit();

            }
        });
        return rootView;
    }

}

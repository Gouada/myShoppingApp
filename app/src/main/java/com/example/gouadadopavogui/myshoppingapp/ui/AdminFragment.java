package com.example.gouadadopavogui.myshoppingapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManager;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;
import com.example.gouadadopavogui.myshoppingapp.services.GroupBackendHandlerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MySharedPreferencesManager mySharedPreferences;

    private static  long shoppingGroupId = 0;
    private static  long currentCartId = 0;
    private static long memberId = 0;
    private static String appInstanceId="";

    private EditText usernameEditText;
    private EditText userPhoneEditText;
    private EditText userMailEditText;
    private Button addUserToGroupButton;

    private View rootView;
    private String newUserName;
    private String newUserPhoneNumer;
    private String newUserEmail;


    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
          //  EventBus.getDefault().register(this);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin, container, false);
        usernameEditText        =   (EditText) rootView.findViewById(R.id.user_name_edit_text);
        userPhoneEditText       =   (EditText) rootView.findViewById(R.id.user_phone_number_edit_text);
        userMailEditText        =   (EditText) rootView.findViewById(R.id.user_email_adress_edit_text);
        addUserToGroupButton    =   (Button) rootView.findViewById(R.id.add_new_user_to_group_button_id);


        addUserToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFields().isEmpty()) {
                    addUserToGroup();
                }
                else {
                    Snackbar.make(rootView, "Bitte füllen Sie das Feld"+checkFields(), Snackbar.LENGTH_LONG).show();
                }

            }
        });
        return rootView;
    }

    private String checkFields()
    {
        String emptyFiledName="";

        newUserName             =   usernameEditText.getText().toString();
        newUserPhoneNumer       =   userPhoneEditText.getText().toString();
        newUserEmail            =   userMailEditText.getText().toString();


        if (newUserName.isEmpty())
        {
            emptyFiledName = "Username";
            usernameEditText.setBackgroundColor(Color.RED);
        }
        else if (newUserPhoneNumer.isEmpty())
        {
            emptyFiledName = "Tel. Nr";
            userPhoneEditText.setBackgroundColor(Color.RED);
        }
        else if (newUserEmail.isEmpty())
        {
            emptyFiledName = "Email-adresse";
            userMailEditText.setBackgroundColor(Color.RED);
        }
        return  emptyFiledName;
    }

    private void addUserToGroup() {

        ShoppingGroupMember newMember = new ShoppingGroupMember();
        newMember.setUserName(newUserName);
        newMember.setPhoneNumber(newUserPhoneNumer);

        mySharedPreferences = new MySharedPreferencesManager(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        shoppingGroupId = mySharedPreferences.getShoppingGroupId();
        currentCartId = mySharedPreferences.getCurrentCartId();
        memberId = mySharedPreferences.getMemberId();

        if (shoppingGroupId != 0 && memberId != 0)
        {
            GroupBackendHandlerService.startActionAddNewUserToGroup(getActivity(), shoppingGroupId, memberId, newMember);
        }
    }

    /*
    @Subscribe
    public void adminTask(ShoppingGroupEvent sgpEvent)
    {

    }
    */
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        //EventBus.getDefault().unregister(this);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

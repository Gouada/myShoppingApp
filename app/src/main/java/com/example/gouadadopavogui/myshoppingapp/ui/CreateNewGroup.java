package com.example.gouadadopavogui.myshoppingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.dao.MyBacckendStorageManager;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;
import com.example.gouadadopavogui.myshoppingapp.helpers.Validator;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;
import com.example.gouadadopavogui.myshoppingapp.services.ProductBackendHandlerService;

import java.util.ArrayList;
import java.util.List;

import static com.example.gouadadopavogui.myshoppingapp.R.string.SHAHRED_PREFERENCES_FILENAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateNewGroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNewGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewGroup extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String RESULT_CODE = "RESULT_CODE";
    private static String ACTION_RESULT_VALUE = "NEW GROUP SUCCEFULLY CREATED";
    private Validator validator;

    private MySharedPreferencesManagerSingleton mySharedPreferences;


    private View rootView;
    private OnFragmentInteractionListener mListener;

    public CreateNewGroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNewGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewGroup newInstance(String param1, String param2) {
        CreateNewGroup fragment = new CreateNewGroup();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_new_group, container, false);
        mySharedPreferences = MySharedPreferencesManagerSingleton.getInstance(getContext(), getResources().getString(SHAHRED_PREFERENCES_FILENAME));

        //TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        final String deviceId = mySharedPreferences.getDeviceID();  //telephonyManager.getDeviceId(); //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ REQUEST PERMISSION
        //LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View popUpView = inflater.inflate(R.layout.new_user_popup_window, null, false);
        final EditText group_name_txt = (EditText) rootView.findViewById(R.id.shopping_group_name);
        final EditText user_name_txt = (EditText) rootView.findViewById(R.id.user_name);
        final EditText user_phone_number_txt = (EditText) rootView.findViewById(R.id.user_phone_number);
        final EditText confirm_user_phone_number_txt = (EditText) rootView.findViewById(R.id.confirm_user_phone_number);


        //final PopupWindow myWindow = new PopupWindow(this);
        validator = new Validator();

        Button save_group_name_button = (Button) rootView.findViewById(R.id.save_shopping_group_name);
        save_group_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<EditText> failedValidation;
                List<EditText> failedLengthValidation;

                List<EditText> toValidate = new ArrayList<EditText>();
                List<EditText> toValidateLength = new ArrayList<EditText>();

                toValidate.add(group_name_txt);
                toValidate.add(user_name_txt);
                toValidate.add(user_phone_number_txt);
                toValidate.add(confirm_user_phone_number_txt);

                toValidateLength.add(user_phone_number_txt);
                toValidateLength.add(confirm_user_phone_number_txt);

                failedValidation = validator.validateEditTextFieldsNotEmpty(toValidate, rootView);
                failedLengthValidation = validator.validateEditTextFieldsLength(toValidateLength, rootView);
                if (!isNumberCorrectlyConfirmed(user_phone_number_txt, confirm_user_phone_number_txt)) {
                    Snackbar.make(rootView, R.string.NUMBER_NOT_CORRECTLY_CONFIRMED_ERROR, Snackbar.LENGTH_LONG).show();
                    confirm_user_phone_number_txt.setBackgroundColor(Color.RED);
                } else if (failedValidation.size() > 0 || failedLengthValidation.size() >0) {
                    Snackbar.make(rootView, R.string.FILL_IN_ALL_FIELD, Snackbar.LENGTH_LONG).show();
                    for (EditText editText : failedValidation) {
                        editText.setBackgroundColor(Color.RED);
                    }

                } else if (failedValidation == null || failedValidation.size() == 0) {

                    MyShoppingCartUI myShoppingCartUI = new MyShoppingCartUI();
                    //backendStorage = new MyBacckendStorageManager(getActivity());
                    Intent initialazationIntent = getActivity().getIntent();
                    String app_instance_id = initialazationIntent.getExtras().getString("APP-INSTANCE-ID"); //myShoppingCartUI.getInstanceID();
                    String firebaseDeviceToken = initialazationIntent.getExtras().getString("APP-FIREBASE-INSTANCE-ID");

                    String group_name = group_name_txt.getText().toString();
                    String user_name = user_name_txt.getText().toString();
                    String user_phone_number = user_phone_number_txt.getText().toString();

                    ShoppingGroupMember newShopping_groupMember = new ShoppingGroupMember();
                    newShopping_groupMember.setInstanceId(app_instance_id);
                    newShopping_groupMember.setDeviceId(deviceId);
                    newShopping_groupMember.setUserName(user_name);
                    newShopping_groupMember.setPhoneNumber(user_phone_number);



                    newShopping_groupMember.setFirebaseDeviceToken(mySharedPreferences.getFirebaseDeviceToken());

                    //newShopping_groupMember.setFirebaseDeviceToken(firebaseDeviceToken);
                    ShoppingGroup newshoppingGroup = new ShoppingGroup();
                    newshoppingGroup.setGroupName(group_name);
                    //newShopping_groupMember.setShoppingGroup(newshoppingGroup);
                    newshoppingGroup.addMemmber(newShopping_groupMember);
                    ProductBackendHandlerService.startActionCreateNewShoppingGroup(getActivity(), newshoppingGroup);
                    //backendStorage.createNewShoppingGroup(newshoppingGroup);

                    Intent resutIntent = new Intent();
                    resutIntent.putExtra("ACTION_RESULT", ACTION_RESULT_VALUE);
                    getActivity().setResult(getActivity().RESULT_OK, resutIntent);
                    Intent intent = new Intent(getActivity(), MyShoppingCartUI.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return rootView;
    }

    private boolean isNumberCorrectlyConfirmed(EditText user_phone_number_txt, EditText confirm_user_phone_number_txt) {
        String user_phone_number = user_phone_number_txt.getText().toString();
        String confirm_user_phone_number = confirm_user_phone_number_txt.getText().toString();
        return user_phone_number.equals(confirm_user_phone_number)?true:false;
    }

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

    public void createGroup()
    {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        final String deviceId = telephonyManager.getDeviceId();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View popUpView = inflater.inflate(R.layout.new_user_popup_window, null, false);
        final EditText group_name_txt = (EditText) rootView.findViewById(R.id.shopping_group_name);
        final EditText user_name_txt = (EditText) rootView.findViewById(R.id.user_name);
        final EditText user_phone_number_txt = (EditText) rootView.findViewById(R.id.user_phone_number);

        //final PopupWindow myWindow = new PopupWindow(this);

        Button save_group_name_button = (Button) rootView.findViewById(R.id.save_shopping_group_name);
        save_group_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyShoppingCartUI myShoppingCartUI = new MyShoppingCartUI();
                //backendStorage = new MyBacckendStorageManager(getActivity());
                String app_instance_id = myShoppingCartUI.getInstanceID();

                String group_name = group_name_txt.getText().toString();
                String user_name = user_name_txt.getText().toString();
                String user_phone_number = user_phone_number_txt.getText().toString();

                ShoppingGroupMember newShopping_groupMember = new ShoppingGroupMember();
                newShopping_groupMember.setInstanceId(app_instance_id);
                newShopping_groupMember.setDeviceId(deviceId);
                newShopping_groupMember.setUserName(user_name);
                newShopping_groupMember.setPhoneNumber(user_phone_number);
                newShopping_groupMember.setFirebaseDeviceToken(myShoppingCartUI.getFirebaseDeviceToken());
                ShoppingGroup newshoppingGroup = new ShoppingGroup();
                newshoppingGroup.setGroupName(group_name);
                //newShopping_groupMember.setShoppingGroup(newshoppingGroup);
                newshoppingGroup.addMemmber(newShopping_groupMember);
                ProductBackendHandlerService.startActionCreateNewShoppingGroup(getActivity(), newshoppingGroup);
                //backendStorage.createNewShoppingGroup(newshoppingGroup);

                Intent resultIntent = new Intent(); //getActivity(), MyShoppingCartUI.class
                resultIntent.putExtra("ACTION_RESULT", ACTION_RESULT_VALUE);
                getActivity().setResult(getActivity().RESULT_OK, resultIntent);
                //resultIntent.putExtra("request_code", 1);
                getActivity().finish();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

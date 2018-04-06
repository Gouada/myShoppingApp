package com.example.gouadadopavogui.myshoppingapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.helpers.FocusChangedListener;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManager;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;
import com.example.gouadadopavogui.myshoppingapp.helpers.TextChangedListener;
import com.example.gouadadopavogui.myshoppingapp.helpers.Validator;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;
import com.example.gouadadopavogui.myshoppingapp.services.GroupBackendHandlerService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.gouadadopavogui.myshoppingapp.R.string.SHAHRED_PREFERENCES_FILENAME;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdhereExistingGroup.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdhereExistingGroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdhereExistingGroup extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //private MySharedPreferencesManager mySharedPreferences;
    private MySharedPreferencesManagerSingleton mySharedPreferences;


    private static  long shoppingGroupId = 0;
    private static  long currentCartId = 0;
    private static long memberId = 0;
    private static String appInstanceId="";

    private EditText adherent_user_name_edit_text;
    private EditText group_owner_phone_number_edit_text;
    //private EditText confirm_group_owner_number_edit_text;
    private EditText adherent_user_phone_number_edit_text;
    private Button adhereToGroupButton;

    //private ImageButton addContactImageButton;
    private View rootView;
    private String newUserName;
    private String newUserPhoneNumber;
    private String groupOwnerPhoneNumber;
    private String adherentPhoneNumber;
    private Validator validator;
    private static String RESULT_CODE = "RESULT_CODE";
    private static String ACTION_RESULT_VALUE = "SUCCESSFULLY ADHERED GROUP";

    private String mContactName;
    private String madherentSearchWord;
    private final static String[] FROM_COLUMNS ={
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            ,ContactsContract.CommonDataKinds.Phone.NUMBER
    };

    private final static int[] TO_IDs =
            {
                    R.id.contact_name,
                    R.id.contact_phone_number
                    //android.R.id.text1
            };
    ListView contactListGruopOwner;
    ListView contactListadherent;
    long contact_Id;
    String contact_lookup_key;
    URI contactUri;
    SimpleCursorAdapter contactCursorAdapterGroupOwner;
    SimpleCursorAdapter contactCursorAdapterAdherent;

    @SuppressLint("InlinedApi")
    private final String[] PROJECTION={
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
            Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.HONEYCOMB ?
                    //ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY:
                    //ContactsContract.Contacts.DISPLAY_NAME ,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
             ContactsContract.CommonDataKinds.Phone.NUMBER
    };
    @SuppressLint("InlinedApi")
    private final String SELECTION =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " LIKE ?" :
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ?";
    private String [] mSelectionArgs = {mContactName};

    private static final int CONTACT_ID_INDEX = 0;
    private static final int LOOKUP_KEY_INDEX = 1;

    public AdhereExistingGroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdhereExistingGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static AdhereExistingGroup newInstance(String param1, String param2) {
        AdhereExistingGroup fragment = new AdhereExistingGroup();
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
        getLoaderManager().initLoader(2,null,this);
        getLoaderManager().initLoader(3,null,this);
    }

    public void hideSoftKeyBoard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText() )
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_adhere_existing_group, container, false);
        adherent_user_name_edit_text                =   (EditText) rootView.findViewById(R.id.adherent_user_name_id);
        adherent_user_phone_number_edit_text        =   (EditText) rootView.findViewById(R.id.adherent_user_phone_number_id);
        group_owner_phone_number_edit_text          =   (EditText) rootView.findViewById(R.id.group_owner_number_id);
        adhereToGroupButton                         =   (Button) rootView.findViewById(R.id.adhere_to_group_button);

        adherent_user_name_edit_text.setOnFocusChangeListener(new FocusChangedListener<EditText>(adherent_user_name_edit_text, getActivity().getApplicationContext(), adherent_user_name_edit_text.getWindowToken()) {
        });

        adherent_user_phone_number_edit_text.setOnFocusChangeListener(new FocusChangedListener<EditText>(adherent_user_phone_number_edit_text, getActivity().getApplicationContext(), adherent_user_phone_number_edit_text.getWindowToken()) {
        });

        group_owner_phone_number_edit_text.setOnFocusChangeListener(new FocusChangedListener<EditText>(group_owner_phone_number_edit_text, getActivity().getApplicationContext(), group_owner_phone_number_edit_text.getWindowToken()) {
        });

        //addContactImageButton                       =   (ImageButton) rootView.findViewById(R.id.imageButton_Add_contact);
        mContactName = group_owner_phone_number_edit_text.getText().toString();
        adherentPhoneNumber = adherent_user_phone_number_edit_text.getText().toString();
        /*
        addContactImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), User_managementActivity.class);
                intent.putExtra("CONTACT_NAME", group_owner_phone_number_edit_text.getText().toString());
                getActivity().startActivityForResult(intent,1);
            }
        });
        */
        Intent initializationItent = getActivity().getIntent();
        if (initializationItent != null && initializationItent.hasExtra("ERROR_MESSAGE")) {
            TextView textView = (TextView) rootView.findViewById(R.id.error_messages_tv);
            textView.setText(R.string.GIVEN_OWNER_NOT_EXISTS);
            group_owner_phone_number_edit_text.setText(initializationItent.getStringExtra("GIVEN_OWNER_NUMBER"));
            group_owner_phone_number_edit_text.setTextColor(Color.RED);
            adherent_user_phone_number_edit_text.setText(((ShoppingGroupMember) initializationItent.getExtras().getSerializable("NEW_GROUP_MEMBER")).getPhoneNumber());
            adherent_user_name_edit_text.setText(((ShoppingGroupMember) initializationItent.getExtras().getSerializable("NEW_GROUP_MEMBER")).getUserName());
            //Toast.makeText(getActivity(), getResources().getString(R.string.GIVEN_OWNER_NOT_EXISTS), Toast.LENGTH_LONG).show();
        }
        validator = new Validator();

        adhereToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EditText> failedValidation;
                List<EditText> failedLengthValidation;
                List<EditText>  toValidate = new ArrayList<EditText>();
                List<EditText>  toValidateLength = new ArrayList<EditText>();

                toValidate.add(adherent_user_name_edit_text);
                toValidate.add(adherent_user_phone_number_edit_text);
                toValidate.add(group_owner_phone_number_edit_text);
                //toValidate.add(confirm_group_owner_number_edit_text);

                toValidateLength.add(adherent_user_phone_number_edit_text);
                //toValidateLength.add(group_owner_phone_number_edit_text);

                failedValidation = validator.validateEditTextFieldsNotEmpty(toValidate, rootView);
                failedLengthValidation = validator.validateEditTextFieldsLength(toValidateLength, rootView);
                /*if(!isNumberCorrectlyConfirmed(group_owner_phone_number_edit_text, confirm_group_owner_number_edit_text))
                {
                    Snackbar.make(rootView, R.string.CHECK_GROUP_OWNER_NUMBER, Snackbar.LENGTH_LONG).show();
                    confirm_group_owner_number_edit_text.setBackgroundColor(Color.RED);
                }
                else */
                if (failedLengthValidation.size() > 0)
                {
                    if (failedLengthValidation.get(0).getText().length()<0)
                        Snackbar.make(rootView, R.string.TO_LONG_NUMBER, Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(rootView, R.string.TO_SHORT_NUMBER,  Snackbar.LENGTH_LONG).show();
                }
                else if (failedValidation.size() > 0)
                {
                    Snackbar.make(rootView, R.string.FILL_IN_ALL_FIELD, Snackbar.LENGTH_LONG).show();
                    for (EditText editText:failedValidation)
                    {
                        editText.setBackgroundColor(Color.RED);
                    }
                }
                else if(failedValidation == null || failedValidation.size() == 0)
                {
                    //TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
                    mySharedPreferences = MySharedPreferencesManagerSingleton.getInstance(getContext(), getResources().getString(SHAHRED_PREFERENCES_FILENAME));
                    ShoppingGroupMember newAdherent = new ShoppingGroupMember();
                    newUserPhoneNumber = adherent_user_phone_number_edit_text.getText().toString();
                    newAdherent.setPhoneNumber(newUserPhoneNumber);
                    newAdherent.setFirebaseDeviceToken(mySharedPreferences.getFirebaseDeviceToken());
                    newAdherent.setInstanceId(mySharedPreferences.getAppInstanceId());
                    String deviceId = mySharedPreferences.getDeviceID(); //telephonyManager.getDeviceId();
                    newAdherent.setDeviceId(deviceId);  // To be changed

                    newUserName = adherent_user_name_edit_text.getText().toString();
                    newAdherent.setUserName(newUserName);

                    if (groupOwnerPhoneNumber == null || groupOwnerPhoneNumber.equals(""))  // this user did not click any item in the listview
                    {
                        Pattern pattern = Pattern.compile("\\d{8,20}+");
                        Matcher matcher = pattern.matcher(group_owner_phone_number_edit_text.getText().toString());
                        if (matcher.matches() ) {
                            groupOwnerPhoneNumber = group_owner_phone_number_edit_text.getText().toString();
                            GroupBackendHandlerService.startAdhereExistingGroup(getActivity(), groupOwnerPhoneNumber, newAdherent);
                            returnIntent();
                        }
                        else {
                            Snackbar.make(rootView, R.string.CHECK_GROUP_OWNER_NUMBER, Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else if (!groupOwnerPhoneNumber.equals("")) {
                        GroupBackendHandlerService.startAdhereExistingGroup(getActivity(), groupOwnerPhoneNumber, newAdherent);
                        returnIntent();
                    }
                   /* else {
                        Snackbar.make(rootView, R.string.CHECK_GROUP_OWNER_NUMBER, Snackbar.LENGTH_LONG).show();
                    } */
                }
            }
        });

        final AdhereExistingGroup thisClass = this;

        group_owner_phone_number_edit_text.addTextChangedListener(new TextChangedListener<EditText>(group_owner_phone_number_edit_text) {
            @Override
            public void onTextChanged(EditText targetField, Editable s) {
                getLoaderManager().restartLoader(2,null, thisClass);
                contactListGruopOwner.setAdapter(contactCursorAdapterGroupOwner);
            }
        });

        adherent_user_phone_number_edit_text.addTextChangedListener(new TextChangedListener<EditText>(adherent_user_name_edit_text) {
            @Override
            public void onTextChanged(EditText targetField, Editable s) {
                getLoaderManager().restartLoader(3,null, thisClass);
                contactListadherent.setAdapter(contactCursorAdapterAdherent);
            }
        });
        contactListGruopOwner = (ListView) rootView.findViewById(R.id.list_view);
        contactListadherent = (ListView) rootView.findViewById(R.id.list_viewadherent);

        contactCursorAdapterGroupOwner = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contact_element,
                null,
                FROM_COLUMNS,
                TO_IDs,
                0);

        contactCursorAdapterAdherent = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contact_element,
                null,
                FROM_COLUMNS,
                TO_IDs,
                0);

        contactListGruopOwner.setAdapter(contactCursorAdapterGroupOwner);
        contactListadherent.setAdapter(contactCursorAdapterAdherent);

        contactListGruopOwner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupOwnerPhoneNumber = ((TextView)view.findViewById(R.id.contact_phone_number)).getText().toString();
                groupOwnerPhoneNumber = deleteSpecialCharactersFromPhoneNumber(groupOwnerPhoneNumber);
                String group_owner_name = ((TextView) view.findViewById(R.id.contact_name)).getText().toString();
                group_owner_phone_number_edit_text.setText(group_owner_name);
                contactListGruopOwner.setAdapter(null);
                //contactCursorAdapter.swapCursor(null);
                //contactListGruopOwner.setAdapter(contactCursorAdapter);
            }
        });

        contactListadherent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adherentPhoneNumber = ((TextView)view.findViewById(R.id.contact_phone_number)).getText().toString();
                String adherentName = ((TextView) view.findViewById(R.id.contact_name)).getText().toString();
                adherent_user_name_edit_text.setText(adherentName);
                adherent_user_phone_number_edit_text.setText(adherentPhoneNumber);

                contactListadherent.setAdapter(null);
                //contactCursorAdapter.swapCursor(null);
                //contactListAdherent.setAdapter(contactCursorAdapter);
            }
        });
        getLoaderManager().restartLoader(2,null, this);
        return rootView;

    }
    public String deleteSpecialCharactersFromPhoneNumber(String phoneNumber)
    {
        /*
        Pattern pattern= Pattern.compile("\\D");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches())
        {
            matcher.replaceAll("").trim();
        }
        */
        String tempNumb = phoneNumber.replaceAll("[-_\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+","");
        tempNumb.replace(" ", "");
        return tempNumb.trim();
    }

    public void returnIntent()
    {
        Intent resultIntent = new Intent(); //getActivity(), MyShoppingCartUI.class
        resultIntent.putExtra("ACTION_RESULT", ACTION_RESULT_VALUE);
        getActivity().setResult(getActivity().RESULT_OK, resultIntent);
        getActivity().finish();
    }
    private boolean isNumberCorrectlyConfirmed(EditText group_owner_phone_number_edit_text, EditText confirm_group_owner_number_edit_text) {

        String owner_number = group_owner_phone_number_edit_text.getText().toString();
        String confirm_owner_number = confirm_group_owner_number_edit_text.getText().toString();
        return owner_number.equals(confirm_owner_number)?true:false;
    }

    @Override
    public void onStart() {
        super.onStart();
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

      /*  mSelectionArgs[0] = "%"+ mContactName +"%";
        //mSelectionArgs[0] = "%" + mContactName + "%";
        // Starts the query
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                mSelectionArgs,
                null
        );
*/
    if (id==2) {
        if (group_owner_phone_number_edit_text != null)
            mContactName = group_owner_phone_number_edit_text.getText().toString();
        Uri contentUri = Uri.withAppendedPath(
                ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                Uri.encode(mContactName));
        // Starts the query
        return new CursorLoader(
                getActivity(),
                contentUri,
                PROJECTION,
                null,
                null,
                null);
    }
    else if (id==3)
    {
        if (adherent_user_phone_number_edit_text != null)
           madherentSearchWord = adherent_user_phone_number_edit_text.getText().toString();
        Uri contentUri = Uri.withAppendedPath(
                ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                Uri.encode(madherentSearchWord));
        // Starts the query
        return new CursorLoader(
                getActivity(),
                contentUri,
                PROJECTION,
                null,
                null,
                null);
    }
        else return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == 2)
            contactCursorAdapterGroupOwner.swapCursor(data);
        else if (loader.getId() == 3)
            contactCursorAdapterAdherent.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == 2)
            contactCursorAdapterGroupOwner.swapCursor(null);
        else if (loader.getId() == 3)
            contactCursorAdapterAdherent.swapCursor(null);
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

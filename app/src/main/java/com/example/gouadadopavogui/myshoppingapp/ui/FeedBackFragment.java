package com.example.gouadadopavogui.myshoppingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedBackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedBackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button feedBackSendButton;
    private EditText feedBackTitle;
    private EditText feedBackContent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedBackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedBackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedBackFragment newInstance(String param1, String param2) {
        FeedBackFragment fragment = new FeedBackFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_feed_back, container, false);
        feedBackSendButton = (Button) rootView.findViewById(R.id.feedBack_send_button);
        feedBackTitle = (EditText) rootView.findViewById(R.id.feedBackTitle);
        feedBackContent = (EditText) rootView.findViewById(R.id.feedBackContent);

        feedBackSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedBack();
            }
        });
        return rootView;
    }

    public void sendFeedBack()
    {
        if (inputFieldSuccessfullyValidated()) {
            try {

                Intent feedBackIntent = new Intent(Intent.ACTION_SEND);//getActivity(), MyShoppingCartUI.class
                feedBackIntent.setType("text/html");
                // feedBackIntent.setAction(Intent.ACTION_SEND);
                feedBackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.FEEDBACK_RECIPIENT_MAIL_ADRESS)});
                //feedBackIntent.putExtra(Intent.EXTRA_CC, new String [] {""});
                //feedBackIntent.setData(Uri.parse("mailto:gouada1@yahoo.fr"));
                feedBackIntent.putExtra(Intent.EXTRA_SUBJECT, feedBackTitle.getText().toString());
                feedBackIntent.putExtra(Intent.EXTRA_TEXT, feedBackContent.getText().toString());
                startActivity(Intent.createChooser(feedBackIntent, feedBackTitle.getText().toString()));
                //startActivity(feedBackIntent);
            }catch(Exception e)
            {
                Snackbar.make(getView(), R.string.NO_MAIL_CLIENT_FOUND_ON_DEVICE, Snackbar.LENGTH_LONG).show();
            }
        }
        else
            Snackbar.make(getView(), R.string.FEED_BACK_FIELD_EMPTY_ERROR, Snackbar.LENGTH_LONG).show();
    }
    public boolean inputFieldSuccessfullyValidated()
    {
        if (feedBackTitle.getText().toString().equals(""))
            return false;
        else if (feedBackContent.getText().toString().equals(""))
            return false;
        else
            return true;
        //Pattern pattern =  Pattern.compile("\\d,[{1,1000}");
        //Matcher matcher = pattern.matcher()
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFeedBackFragmentInteraction(uri);
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
        void onFeedBackFragmentInteraction(Uri uri);
    }
}

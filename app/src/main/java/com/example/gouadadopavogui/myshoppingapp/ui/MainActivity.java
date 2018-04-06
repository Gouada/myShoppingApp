package com.example.gouadadopavogui.myshoppingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

//import com.example.gouadadopavogui.myshoppingapp.Manifest;
import android.Manifest;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManager;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements AdhereExistingGroup.OnFragmentInteractionListener, CreateNewGroup.OnFragmentInteractionListener, FeedBackFragment.OnFragmentInteractionListener{

    //private MySharedPreferencesManager mySharedPreferencesManager;
    private MySharedPreferencesManagerSingleton mySharedPreferencesManager;



    private static long shoppingGroupId = 0;
    //private static  String currentCartId ="";
    private static long memberId = 0;
    private static String appInstanceId="";
    private int permissionCheck_phone_state;
    private int permissionCheck_read_contact;
    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE=1;
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private MySharedPreferencesManagerSingleton mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent() !=null && getIntent().hasExtra("FEEDBACK_ACTION")) {

            getSupportFragmentManager().beginTransaction().replace(R.id.initialization_Activity_FmL_id, new FeedBackFragment()).commit();
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck_phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
                permissionCheck_read_contact = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
                if (permissionCheck_phone_state != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(this, Manifest.permission.READ_PHONE_STATE, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                }
                if (permissionCheck_read_contact != PackageManager.PERMISSION_GRANTED)
                    requestPermission(this, Manifest.permission.READ_CONTACTS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            else
            {
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                final String deviceId = telephonyManager.getDeviceId();
                mySharedPreferences = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
                mySharedPreferences.setDeviceID(deviceId);
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.initialization_Activity_FmL_id, new WelcomeFragment()).commit();
        }
        }

    public void requestPermission (Activity thisActivity, String permissionType, int myRequestCode)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,permissionType))
        {
            Toast.makeText(getApplicationContext(), "Please give me the permission", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(thisActivity,new String[] {permissionType},myRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
                    final String deviceId = telephonyManager.getDeviceId();
                    mySharedPreferences = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
                    mySharedPreferences.setDeviceID(deviceId);
                }
                else
                {
                    //implement alert
                }
                break;
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                break;
            default: break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFeedBackFragmentInteraction(Uri uri) {

    }
}

package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.util.Log;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.services.CartBackendHandlerService;
import com.example.gouadadopavogui.myshoppingapp.services.ProductBackendHandlerService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by gouadadopavogui on 06.02.2017.
 */
public class MyFirebaseInstanceIdServiceHandler extends FirebaseInstanceIdService {

    private String deviceFirebaseToken;

    public String getDeviceFirebaseToken() {
        return deviceFirebaseToken;
    }

    public void setDeviceFirebaseToken(String deviceFirebaseToken) {
        this.deviceFirebaseToken = deviceFirebaseToken;
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        setDeviceFirebaseToken(FirebaseInstanceId.getInstance().getToken());
        Log.e("TOKEN", getDeviceFirebaseToken());
        MySharedPreferencesManager mySharedPreferencesManager = new MySharedPreferencesManager(getApplicationContext(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager.setFirebaseDeviceToken(getDeviceFirebaseToken());
        //ProductBackendHandlerService.startActionSaveFirebaseDeviceToken(getDeviceFirebaseToken(), getApplicationContext(), mySharedPreferencesManager.getMemberId());
    }
}

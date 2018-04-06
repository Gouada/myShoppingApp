package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by GouadaDopavogui on 19.11.2016.
 */

public class MySharedPreferencesManager
{

    public void setSharedPreferencesFileName(String sharedPreferencesFileName) {
        this.sharedPreferencesFileName = sharedPreferencesFileName;
    }

    public void saveInsharedPreferences(String keyName, String keyValue)
    {
        SharedPreferences sharedPreferences = givenContext.getSharedPreferences(getSharedPreferencesFileName(), getGivenContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, keyValue);
        editor.commit();
    }

    private Context givenContext;
    private String sharedPreferencesFileName;

    private final static String APP_INSTANCE_ID_Key = "com.example.gouadadopavogui.myshoppingapp.APP_INSTANCE_ID_KEY";
    private final static String SHOPPING_GROUP_ID_Key = "com.example.gouadadopavogui.myshoppingapp.SHOPPING_GROUP_ID_Key";
    private final static String SHOPPING_GROUP_MEMBER_ID_Key = "com.example.gouadadopavogui.myshoppingapp.SHOPPING_GROUP_MEMBER_ID_Key";
    private final static String CURRENT_SHOPPING_CART_ID_Key = "com.example.gouadadopavogui.myshoppingapp.CURRENT_SHOPPING_CART_ID_Key";
    private final static String FIREBASE_DEVICE_TOKEN_KEY = "com.example.gouadadopavogui.myshoppingapp.FIREBASE_DEVICE_TOKEN_KEY";

    public MySharedPreferencesManager(Context context, String sharedPreferencesFileName)
    {
        this.setGivenContext(context);
        this.setSharedPreferencesFileName(sharedPreferencesFileName);
    }



    public Context getGivenContext() {
        return givenContext;
    }

    public String getSharedPreferencesFileName() {
        return sharedPreferencesFileName;
    }

    public void setGivenContext(Context context) {
        this.givenContext = context;
    }


    public String readStringFromSharedPreferences(String keyName)
    {
        SharedPreferences sharedPreferences = givenContext.getSharedPreferences(getSharedPreferencesFileName(), getGivenContext().MODE_PRIVATE);
        return sharedPreferences.getString(keyName, "0");
    }

    public long getMemberId() {
        return Long.parseLong(readStringFromSharedPreferences(SHOPPING_GROUP_MEMBER_ID_Key));
    }

    public  long getShoppingGroupId() {
        return Long.parseLong(readStringFromSharedPreferences(SHOPPING_GROUP_ID_Key));
    }

    public  long getCurrentCartId() {
        return Long.parseLong(readStringFromSharedPreferences(CURRENT_SHOPPING_CART_ID_Key));
    }

    public  String getAppInstanceId() {
        return readStringFromSharedPreferences(APP_INSTANCE_ID_Key);
    }

    public  void setCurrentCartId(long cartId) {
        saveInsharedPreferences(CURRENT_SHOPPING_CART_ID_Key, Long.toString(cartId));
    }

    public  void setMemberId(long memberId) {
        saveInsharedPreferences(SHOPPING_GROUP_MEMBER_ID_Key, Long.toString(memberId));
    }

    public void setShoppingGroupId(long groupId) {
        saveInsharedPreferences(SHOPPING_GROUP_ID_Key, Long.toString(groupId));
    }

    public void setAppInstanceId(String appInstanceIDValue) {
        saveInsharedPreferences(APP_INSTANCE_ID_Key, appInstanceIDValue);
    }

    public void setFirebaseDeviceToken(String firebaseDeviceToken) {
        saveInsharedPreferences(FIREBASE_DEVICE_TOKEN_KEY, firebaseDeviceToken);
    }
    public String getFirebaseDeviceToken()
    {
        return readStringFromSharedPreferences(FIREBASE_DEVICE_TOKEN_KEY);
    }
}

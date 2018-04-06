package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gouadadopavogui on 12.03.2017.
 */
public class MySharedPreferencesManagerSingleton {

    private static Context givenContext;

    private static MySharedPreferencesManagerSingleton ourInstance = new MySharedPreferencesManagerSingleton();

    public MySharedPreferencesManagerSingleton() {
    }

    public synchronized static MySharedPreferencesManagerSingleton getInstance(Context context, String sharedPreferencesFileName) {

        MySharedPreferencesManagerSingleton.givenContext = context;
        MySharedPreferencesManagerSingleton.sharedPreferencesFileName = sharedPreferencesFileName;

        return ourInstance;
    }
/*
    private MySharedPreferencesManagerSingleton(Context context, String sharedPreferencesFileName) {
        this.setGivenContext(context);
        this.setSharedPreferencesFileName(sharedPreferencesFileName);
    }
*/
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

    private static String sharedPreferencesFileName;

    private final static String APP_INSTANCE_ID_Key = "com.example.gouadadopavogui.myshoppingapp.APP_INSTANCE_ID_KEY";
    private final static String SHOPPING_GROUP_ID_Key = "com.example.gouadadopavogui.myshoppingapp.SHOPPING_GROUP_ID_Key";
    private final static String SHOPPING_GROUP_MEMBER_ID_Key = "com.example.gouadadopavogui.myshoppingapp.SHOPPING_GROUP_MEMBER_ID_Key";
    private final static String CURRENT_SHOPPING_CART_ID_Key = "com.example.gouadadopavogui.myshoppingapp.CURRENT_SHOPPING_CART_ID_Key";
    private final static String FIREBASE_DEVICE_TOKEN_KEY = "com.example.gouadadopavogui.myshoppingapp.FIREBASE_DEVICE_TOKEN_KEY";
    private final static String DEVICE_LANGUAGE_KEY = "com.example.gouadadopavogui.myshoppingapp.DEFAULT_LANGUAGE";

    private final static String DEVICE_ID = "com.example.gouadadopavogui.myshoppingapp.DEVICE_ID";


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

    public void setDeviceID(String deviceID) {
        saveInsharedPreferences(DEVICE_ID, deviceID);
    }
    public String getDeviceID()
    {
        return readStringFromSharedPreferences(DEVICE_ID);
    }


    public String getDeviceLanguage()
    {
        return readStringFromSharedPreferences(DEVICE_LANGUAGE_KEY);
    }

    public void setDeviceLanguage(String defaultLanguage)
    {
        saveInsharedPreferences(DEVICE_LANGUAGE_KEY, defaultLanguage);
    }
}

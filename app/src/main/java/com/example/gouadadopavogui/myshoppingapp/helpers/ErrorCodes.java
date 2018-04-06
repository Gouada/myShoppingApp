package com.example.gouadadopavogui.myshoppingapp.helpers;

/**
 * Created by gouadadopavogui on 03.07.2017.
 */

public  abstract class ErrorCodes {

    public static final int BACKEND_ERROR = 600;

    public static final int CONNECTION_ERROR = 700;

    public static final int BACKEND_ERROR_GROUP_NOT_CREATED = 800;
    public static final int BACKEND_ERROR_ADHESION_FAILED = 801;
    public static final int BACKEND_ERROR_GROUP_NOT_LOADED = 802;
    public static final int BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS = 804;

    public static final int BACKEND_ERROR_CART_NOT_CREATED = 901;
    public static final int BACKEND_ERROR_CURRENT_CART_NOT_LOADED = 902;
    public static final int BACKEND_ERROR_CART_NOT_SAVED = 903;
    public static final int BACKEND_ERROR_CART_NOT_ARCHIVED = 904;


    public static final int BACKEND_ERROR_PRODUCT_LIST_NOT_LOADED = 1000;
    public static final int BACKEND_ERROR_PRODUCT_NOT_SAVED = 1001;
    public static final int BACKEND_ERROR_PRODUCT_ALREADY_EXISTS = 1002;
    //public static final String CHANGES_SAVED_IN_BACKEND = "CHANGES SAVED IN BACKEND";
}


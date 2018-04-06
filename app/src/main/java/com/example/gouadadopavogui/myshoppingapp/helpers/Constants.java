package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.content.Context;
import com.example.gouadadopavogui.myshoppingapp.R;
/**
 * Created by GouadaDopavogui on 01.11.2016.
 */
public class Constants
{
    private static Context giveContext;
    private static Constants constantsInstance = new Constants();

    private static final String[] spinnerText = new String[12];


    public  static  final String BASEURL = "http://10.0.2.2:8080/myShoppingCart/webapi/";

    //public  static  final String BASEURL = "http://172.24.104.41:8080/myShoppingCart/webapi/";

    //public static final String BASEURL = "http://custom-env-1.6nabmmmzge.us-west-2.elasticbeanstalk.com/webapi/";

    public Constants(){}

    public synchronized static Constants getInstance(Context context)
    {
        giveContext = context;
        spinnerText[0] =  context.getResources().getString(R.string.DEFAULT_TYPE);
        spinnerText[1] = context.getResources().getString(R.string.MILK_PRODUCTS);
        spinnerText[2] = context.getResources().getString(R.string.BREAKFAST_PRODUCTS);
        spinnerText[3] = context.getResources().getString(R.string.REFRIGERATOR_PRODUCTS);
        spinnerText[4] = context.getResources().getString(R.string.MEAT_PRODUCTS);

        spinnerText[5] = context.getResources().getString(R.string.OBST_PRODUCTS);
        spinnerText[6] = context.getResources().getString(R.string.FRUIT_PRODUCTS);

        spinnerText[7] = context.getResources().getString(R.string.DRINKS_PRODUCTS);
        spinnerText[8] = context.getResources().getString(R.string.SWEETY_PRODUCTS);
        spinnerText[9] = context.getResources().getString(R.string.TOILET_PRODUCTS);
        spinnerText[10] = context.getResources().getString(R.string.BABY_PRODUCTS);
        spinnerText[11] = context.getResources().getString(R.string.OTHERS_PRODUCTS);
        return constantsInstance;
    }
    public static String[] getProductTyPeSpinnerText(Context context)
    {
        return spinnerText;
    }
    public String getProductTypeById(int i)
    {
        return spinnerText[i];
    }
}

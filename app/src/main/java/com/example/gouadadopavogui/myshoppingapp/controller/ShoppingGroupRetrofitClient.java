package com.example.gouadadopavogui.myshoppingapp.controller;

import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.interfaces.ShoppingGroupRestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GouadaDopavogui on 19.11.2016.
 */

public class ShoppingGroupRetrofitClient {

    public ShoppingGroupRestService getIGrouP()
    {
        Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.BASEURL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
        ShoppingGroupRestService shoppingGroupRestService = retrofit.create(ShoppingGroupRestService.class);
        return shoppingGroupRestService;
    }
}

package com.example.gouadadopavogui.myshoppingapp.controller;

import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.interfaces.CartRestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gouadadopavogui on 01.12.2016.
 */


public class CartRetrofitClient {

    public CartRestService getCartsService()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CartRestService cartRestService = retrofit.create(CartRestService.class);
        return cartRestService;
    }
}

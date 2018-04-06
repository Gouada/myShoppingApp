package com.example.gouadadopavogui.myshoppingapp.controller;

import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.interfaces.ProductRestServivce;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GouadaDopavogui on 01.11.2016.
 */
public class ProductRetrofitClient
{
    //GsonBuilder gsonBuilder = new GsonBuilder();

    public ProductRestServivce getRectrofitClient() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ProductRestServivce productRestServivce = retrofit.create(ProductRestServivce.class);
        return productRestServivce;
    }



}

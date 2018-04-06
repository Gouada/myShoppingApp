package com.example.gouadadopavogui.myshoppingapp.interfaces;

import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ProductNameInLang;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by GouadaDopavogui on 31.10.2016.
 */
public interface ProductRestServivce
{
    @GET("products/all/language/{language}")
    Call<List<Product>> loadProducts(@Path("language") String language);

    @GET("products?productId={productId}")
    Call<Product> getProductById(@Query("productId") long productId);

    @GET("products/{productName}")
    Call<Product> getProductByName(@Path("productName") String productName);

    @POST("products")
    Call<Product> addProductToList(@Body Product product);

}


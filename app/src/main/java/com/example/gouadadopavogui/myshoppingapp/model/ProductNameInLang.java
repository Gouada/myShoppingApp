package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gouadadopavogui on 08.01.2018.
 */

public class ProductNameInLang  implements Serializable
{

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("product")
    @Expose
    private Product product;

    //private long id;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
/*
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    */
}

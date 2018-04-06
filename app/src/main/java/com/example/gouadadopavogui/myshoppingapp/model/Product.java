package com.example.gouadadopavogui.myshoppingapp.model;

import android.support.annotation.TransitionRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GouadaDopavogui on 12.09.2016.
 */
public class Product implements Serializable{

    /*
    @SerializedName("cartProducts")
    @Expose
    private List<Object> cartProducts = new ArrayList<Object>();
*/
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("productAmount")
    @Expose
    private int productAmount;

    @SerializedName("productId")
    @Expose
    private long productId;

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("productType")
    @Expose
    private int productType;

    @Expose(serialize = false, deserialize = false)
     private transient boolean isSelected;

    @SerializedName("isInCart")
    @Expose //(serialize = true, deserialize = true)
    private transient boolean isInCart;

    @SerializedName("is_standard_product")
    @Expose
    private int is_standard_product;

    @SerializedName("lang")
    @Expose
    private String lang;


    public boolean isInCart() {
        return isInCart;
    }

    public void setInCart(boolean inCart) {
        isInCart = inCart;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Product(String productName) {
        this.setProductName(productName);
    }

    public String getProductName() {
        return productName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductName(String productName) {

        this.productName = productName;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public Product() {
    }

    public Product(String productName, int amount) {
        this.productName = productName;
        this.productAmount = amount;
    }

    public Product(String productName, int amount, int productType) {
        this.productName = productName;
        this.productAmount = amount;
        this.productType=productType;
    }

    public Product(long productId, int amount) {
        this.productName = productName;
        this.productId = productId;
    }

    public boolean isProductSelected()
    {
        return isSelected;
    }
    public void selectProduct()
    {
        this.isSelected =true;
    }
    public void deSelectProduct()
    {
        this.isSelected = false;
    }

    public int getIs_standard_product() {
        return is_standard_product;
    }

    public void setIs_standard_product(int is_standard_product) {
        this.is_standard_product = is_standard_product;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}

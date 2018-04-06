package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by GouadaDopavogui on 27.05.2017.
 */

public class CartProductsId implements Serializable {

    @SerializedName("product")
    private Product product;

    @SerializedName("cart")
    private Cart cart;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}



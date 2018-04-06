package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by GouadaDopavogui on 31.10.2016.
 */
public class Cart implements Serializable
{
    @Expose
    @SerializedName("cartId")
    private long cartId;

    @Expose
    @SerializedName("created")
    private Date created;

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("shoppingGroup")
    private ShoppingGroup shoppingGroup;

    @Expose
    @SerializedName("productsInCart")
    private List<CartProducts> productsInCart;



/*
    public Cart(long cartId, Date created, boolean status) {
        this.cartId = cartId;
        this.created = created;
        this.status = status;
    }
*/
    public Cart() {
    }

    public long getCartId() {
        return cartId;
    }

    public Date getCreated() {
        return created;
    }

    public boolean isStatus() {
        return status;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ShoppingGroup getShoppingGroup() {
        return shoppingGroup;
    }

    public void setShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroup = shoppingGroup;
    }

    public List<CartProducts> getProductsInCart() {
        return productsInCart;
    }

    public void setProductsInCart(List<CartProducts> productsInCart) {
        this.productsInCart = productsInCart;
    }
}

package com.example.gouadadopavogui.myshoppingapp.events;

import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.CartProducts;
import com.example.gouadadopavogui.myshoppingapp.model.Product;

import java.util.List;

/**
 * Created by gouadadopavogui on 03.12.2016.
 */

public class CartEvent {
    private List<CartProducts> cartProducts;
    private Cart currentCart;
    public  int errorCode=0;

    public CartEvent() {
    }

    public CartEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public CartEvent(List<CartProducts> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public CartEvent(Cart currentCart) {
        this.currentCart = currentCart;
       /*
        if(this.currentCart != null)
        {
            if (currentCart.getProductsInCart() != null && currentCart.getProductsInCart().size() > 0)
            {

            }
        }
        */
    }

    public List<CartProducts> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProducts> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }

    public  int getErrorCode() {
        return errorCode;
    }

    public  void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

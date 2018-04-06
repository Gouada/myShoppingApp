package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by GouadaDopavogui on 05.11.2016.
 */
public class CartProducts implements Serializable{

    @SerializedName("primaryKey")
    @Expose
    CartProductsId primaryKey;

    @SerializedName("productAmount")
    @Expose
    private int productAmount;

    @SerializedName("updateDate")
    @Expose
    private Date updatedDate;

    @SerializedName("addedDate")
    @Expose
    private Date addedDate;

    //@SerializedName("product")
    //private Product product;

    //@SerializedName("cart")
    //private Cart cart;

    @SerializedName("productInCart")
    private boolean productInCart;

    public boolean isProductInCart() {
        return productInCart;
    }

    public void setProductInCart(boolean productInCart) {
        this.productInCart = productInCart;
    }


  /*  public Product getProduct() {
        return product;
    }
*/
    /*
    public void setProduct(Product product) {
        this.product = product;
        this.setProductInCart(product.isInCart());
        this.setProductAmount(product.getProductAmount());
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
*/
    public CartProducts() {
    }

    public int getProductAmount() {
        return productAmount;
    }
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public CartProductsId getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(CartProductsId primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Product getProduct()
    {
        return  getPrimaryKey().getProduct();
    }

    void setProduct(Product product)
    {
        getPrimaryKey().setProduct(product);
        this.setProductInCart(product.isInCart());
        this.setProductAmount(product.getProductAmount());
    }

    public Cart getCart()
    {
        return getPrimaryKey().getCart();
    }
    void setCart(Cart cart)
    {
        getPrimaryKey().setCart(cart);
    }
}

package com.example.gouadadopavogui.myshoppingapp.events;

import android.view.View;

import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.Product;

/**
 * Created by gouadadopavogui on 06.04.2017.
 */

public class LocalDBEvent {

    public static final String ADD_NEW_PRODUCT="add_new_product";
    public static final String DELETE_PRODUCT="delete_product";
    public static final String UPDATE_PRODUCT="update_product";
    public static final String UPDATE_PRODUCT_LIST = "update_product_list";
    public static final String ARCHIVE_CURRENT_CART = "archive_current_cart";
    public static final String REMOVE_PRODUCT_FROM_CART = "remove_product_from_cart";

    private Product product;
    private Cart currentCart;
    private  boolean isProduct_inserted = false;
    private  boolean isProduct_deleted = false;
    private boolean  isProduct_updated=false;
    private boolean isProduct_list_updated;
    private boolean isArchived_current_cart = false;
    private boolean isBackendAvailable=true;
    private boolean isProduct_removed_from_cart;
    private int chPos=0;
    private int gPos=0;
    private String operation="";
    private  View childView;
    private int errorCode = 0;

    public LocalDBEvent(Product product, int errorCode) {
        this.setProduct(product);
        this.setErrorCode(errorCode);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }


    public LocalDBEvent(String operation, Product product, boolean operation_result) {
        this.setOperation(operation);
        this.product = product;
        if (ADD_NEW_PRODUCT.equals(operation) && operation_result == true)
            this.isProduct_inserted = true;
        else if(DELETE_PRODUCT.equals(operation) && operation_result == true)
            this.isProduct_deleted = true;
        else if (UPDATE_PRODUCT.equals(operation) && operation_result == true)
            this.isProduct_updated=true;
        else if (UPDATE_PRODUCT_LIST.equals(operation) && operation_result == true)
            this.isProduct_list_updated =true;
        else if (ARCHIVE_CURRENT_CART.equals(operation) && operation_result == true)
            this.isArchived_current_cart = true;
    }

    public LocalDBEvent()
    {}

    public LocalDBEvent(String operation, Product product, int gPos) { // , int chPos, View childView
        this.setOperation(operation);
        this.setgPos(gPos);
        //this.setChPos(chPos);
        if (operation.equals(this.UPDATE_PRODUCT))
            setProduct_updated(true);
        if (operation.equals(this.REMOVE_PRODUCT_FROM_CART))
            setProduct_removed_from_cart(true);
        this.product=product;

    }

    public LocalDBEvent(String operation, Product product, boolean operation_result, int gPos) { // , int chPos, View childView
        this.setOperation(operation);
        this.setgPos(gPos);
        //this.setChPos(chPos);
        this.product=product;
        if(DELETE_PRODUCT.equals(operation) && operation_result == true)
            this.isProduct_deleted = true;
        else if (ADD_NEW_PRODUCT.equals(operation) && operation_result == true)
            this.isProduct_inserted = true;
    }


    public LocalDBEvent(Product product)
    {
        this.product = product;
    }

    public  LocalDBEvent(Cart currentCart)
    {
        this.currentCart = currentCart;
    }

    public LocalDBEvent(Product product, Cart currentCart)
    {
        this.product = product;
        this.currentCart = currentCart;
    }
    public LocalDBEvent(String message)
    {
        this.isBackendAvailable = false;
    }

    public LocalDBEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public View getChildView() {
        return childView;
    }

    public void setChildView(View childView) {
        this.childView = childView;
    }

    public int getChPos() {
        return chPos;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setChPos(int chPos) {
        this.chPos = chPos;
    }

    public int getgPos() {
        return gPos;
    }

    public void setgPos(int gPos) {
        this.gPos = gPos;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean productInserted()
    {
        return this.isProduct_inserted;
    }

    public boolean productDeleted()
    {
        return this.isProduct_deleted;
    }

    public boolean isProduct_updated() {
        return isProduct_updated;
    }

    public void setProduct_updated(boolean product_updated) {
        isProduct_updated = product_updated;
    }

    public boolean isProduct_list_updated() {
        return isProduct_list_updated;
    }

    public void setProduct_list_updated(boolean product_list_updated) {
        isProduct_list_updated = product_list_updated;
    }

    public boolean isArchived_current_cart() {
        return isArchived_current_cart;
    }

    public void setArchived_current_cart(boolean archived_current_cart) {
        isArchived_current_cart = archived_current_cart;
    }

    public boolean isBackendAvailable() {
        return isBackendAvailable;
    }

    public void setProduct_removed_from_cart(boolean product_removed_from_cart) {
        this.isProduct_removed_from_cart = product_removed_from_cart;
    }

    public boolean isProduct_removed_from_cart() {
        return isProduct_removed_from_cart;
    }
}

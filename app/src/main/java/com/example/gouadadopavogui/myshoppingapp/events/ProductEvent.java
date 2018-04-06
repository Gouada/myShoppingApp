package com.example.gouadadopavogui.myshoppingapp.events;

import com.example.gouadadopavogui.myshoppingapp.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gouadadopavogui on 30.11.2016.
 */

public class ProductEvent
{
    public  int errorCode = 0;
    private List<Product> productList = new ArrayList<Product>();

    public ProductEvent(int i) {
        errorCode = i;
    }

    public ProductEvent(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public  int getErrorCode() {
        return errorCode;
    }

    public  void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

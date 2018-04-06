package com.example.gouadadopavogui.myshoppingapp.dao;

import android.content.Context;
import android.util.Log;

import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.model.Product;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by GouadaDopavogui on 15.09.2016.
 */
public class MyStorage
{
    Context context;
    private  List<Product> allProducts;
    MyDBHandler myDBHandler;
    public MyStorage(Context context) {
        this.context = context;
        myDBHandler = new MyDBHandler(context);

    }

    public List<Product> getAllProducts()
    {
        return myDBHandler.retrieve_all_products();
    }

    public boolean addProduct(Product product)
    {
        boolean inserted=false;
        //MyDBHandler myDBHandler = new MyDBHandler(context);
        if(!myDBHandler.productExists(product))
        {
            myDBHandler.insert_product(product);
            inserted = true;
            //EventBus.getDefault().post(new LocalDBEvent("add_new_product", product, inserted));
        }
        return inserted;
    }

    public boolean deleteProductFromList(String productName)
    {
       // MyDBHandler myDBHandler = new MyDBHandler(context);
        return myDBHandler.deleteFromProductList(productName);
    }

    public boolean addProductToCart(Product product)
    {
        boolean inserted=false;
       // MyDBHandler myDBHandler = new MyDBHandler(context);
        if(!myDBHandler.isProductInCart(product))
        {
            myDBHandler.insert_product_in_cart(product);
            inserted=true;
        }
        return inserted;
    }

    public boolean isProductInCart(Product product)
    {
        return myDBHandler.isProductInCart(product);
    }
    public void put_products_into_cart(List<Product> pdct_List)
    {
        //MyDBHandler myDBHandler = new MyDBHandler(context);
        myDBHandler.add_Products_to_cart(pdct_List);
    }

    public List<Product> load_cart()
    {
        //MyDBHandler myDBHandler = new MyDBHandler(context);
        return myDBHandler.select_All_from_cart();
    }

    public void deleteProduktFromCart(String productName) {
        //MyDBHandler myDBHandler = new MyDBHandler(context);
        myDBHandler.deleteFromFromCart(productName);
    }
    public void cleanUpCart()
    {
        //MyDBHandler myDBHandler = new MyDBHandler(context);
        myDBHandler.cleanUpCart();
    }

    public int[] getExistingProductTypes()
    {
        return myDBHandler.select_product_types();
    }

    public void updateProductInCart(Product product) {
        myDBHandler.updateProductInCart(product);
    }

    public boolean archiveCurrentCar() {
        return myDBHandler.archiveCurrentCart();
    }

    public void removeProductOutOfCart(Product product) {
        myDBHandler.removeProductOutOfCart(product);
    }

    public void save_device_lang(String device_lang) {
        myDBHandler.insert_current_LANG(device_lang);
    }
}

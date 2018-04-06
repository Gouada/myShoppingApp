package com.example.gouadadopavogui.myshoppingapp.dao;

import android.content.Context;
import android.util.Log;

import com.example.gouadadopavogui.myshoppingapp.controller.ShoppingGroupRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.controller.ProductRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.interfaces.ProductRestServivce;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ProductNameInLang;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gouadadopavogui on 12.11.2016.
 */

public class MyBacckendStorageManager {

    private ProductRetrofitClient mProductManager;
    private ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
    private ProductRestServivce productRestServivce;

    private ShoppingGroupMember shoppingGroupMember;

    public static List<Product> allProducts = new ArrayList<Product>();
    private Context context;


    boolean existUser;

    public MyBacckendStorageManager(Context context ) {
        this.context = context;
        mProductManager= new ProductRetrofitClient();
        productRestServivce = mProductManager.getRectrofitClient();

    }

    public ShoppingGroup getShoppingGroup() {
        return shoppingGroup;
    }

    public void setShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroup = shoppingGroup;
    }

    private ShoppingGroup shoppingGroup;

    public boolean isExistUser() {
        return existUser;
    }

    public void setExistUser(boolean existUser) {
        this.existUser = existUser;
    }

    public ShoppingGroupMember getShoppingGroupMember() {
        return shoppingGroupMember;
    }

    public void setShoppingGroupMember(ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroupMember = shoppingGroupMember;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }
    // Save new product in backend
    // This method send a request to the backend part to save a newly created product
    public boolean addProductToDB(Product product) {
        final boolean[] saved = {false};
        Call<Product> iProduct = mProductManager.getRectrofitClient().addProductToList(product);
        iProduct.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful())
                    saved[0] =true;
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });
        return saved[0];
    }

    public void createNewShoppingGroup(ShoppingGroup shoppingGroup)
    {
        Call<ShoppingGroup> igroup = mShoppingGroupRetrofitClient.getIGrouP().createNewShoppingGroup(shoppingGroup);
        igroup.enqueue(new Callback<ShoppingGroup>() {
            @Override
            public void onResponse(Call<ShoppingGroup> call, Response<ShoppingGroup> response) {
                if(response.isSuccessful())
                {
                    EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                    //setShoppingGroup(response.body());
                    Log.e("Message: ", "  Group created ");
                }
                else
                {
                    Log.e("ERROR", " Group could not be created ");
                    setExistUser(false);
                }

            }

            @Override
            public void onFailure(Call<ShoppingGroup> call, Throwable t) {
                Log.e("ERROR", " "+ t.getMessage() + "       " + t.getStackTrace());
            }
        });
    }

    //Check if the current app_instance id is saved in backend
    // This method send a request to the backend part to check if user exists
    public void getShoppingGroupMemberFromBackend(String appInsstanceID)
    {
        Call<ShoppingGroupMember> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().getShoppingGroupMember(appInsstanceID);
        igroupMember.enqueue(new Callback<ShoppingGroupMember>() {
            @Override
            public void onResponse(Call<ShoppingGroupMember> call, Response<ShoppingGroupMember> response) {
              if (response.isSuccessful())
              {
                  EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                  Log.e("Message: ", "  Group created ");
              }
                else
              {
                  Log.e("Eror", " Group could not be created ");
                  setExistUser(false);
              }
            }

            @Override
            public void onFailure(Call<ShoppingGroupMember> call, Throwable t) {
                Log.e("ERROR", " " + t.getStackTrace());

            }
        });
    }
    public void loadProducts()
    {
        Call<List<Product>> iProductList = productRestServivce.loadProducts(Locale.getDefault().getLanguage());
        iProductList.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {

                    List<Product> productList = new ArrayList<Product>();
                    List<ProductNameInLang> productNameList = new ArrayList<ProductNameInLang>();
                    //productNameList = response.body();
                    productList = response.body();
                    /*
                    for(ProductNameInLang pNLg: productNameList)
                    {
                        productList.add(pNLg.getProduct());
                    }
                    */
                    setAllProducts(productList);
                    Log.e("SUCCESS:", "........................." + "\n\n");

                    MyDBHandler myDBHandler = new MyDBHandler(context);
                    if(myDBHandler.cleanUpTable())
                        myDBHandler.load_Products_into_device(getAllProducts());


                } else {
                    Log.e("GOUADA LOG", " Error Body" + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("GOUADA CAUSE:", t.getMessage() + "  Stacktrace" + t.getStackTrace() + "\n\n");
            }
        });
    }




    /*
        Call <Product> iProduct = mProductManager.getRectrofitClient().getProductByName("Kaffee");
        iProduct.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()) {
                    allProducts = new ArrayList<Product>();
                    allProducts.add(response.body());
                }
                else {
                    response.errorBody().toString();
                    System.out.print("RESPONSE CODE IS .        ***********" + response.code());
                    System.out.print(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                System.out.print("RESPONSE CODE IS .        ***********");
                System.out.print(t.getMessage()+t.getStackTrace());
            }
        });
        */

}

package com.example.gouadadopavogui.myshoppingapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.gouadadopavogui.myshoppingapp.controller.ProductRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.controller.ShoppingGroupRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.dao.MyDBHandler;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ProductEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.interfaces.ProductRestServivce;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ProductNameInLang;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ProductBackendHandlerService extends IntentService {
    // action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LOAD_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.action.LOAD_PRODUCT";
    private static final String ACTION_SAVE_NEW_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.action.SAVE_NEW_PRODUCT";
    private static final String ACTION_GET_MEMBER_SHOPPINGROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.GET_MEMBER_SHOPPINGROUP";
    private static final String ACTION_CREATE_NEW_SHOPPINGGROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.CREATE_NEW_SHOPPINGGROUP";
    private static final String ACTION_SAVE_FIREBASE_DEVICE_TOKEN = "com.example.gouadadopavogui.myshoppingapp.services.action.SAVE_FIREBASE_DEVICE_TOKEN";


    // parameters
    public static final String BROCAST_SENDER_BACKEND_HANDLER = "com.example.gouadadopavogui.myshoppingapp";
    private static final String EXTRA_PARAM_APP_INSTANCE_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_APP_INSTANCE_ID";
    private static final String EXTRA_PARAM_NEW_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_NEW_PRODUCT";
    private static final String EXTRA_PARAM_SHOPPING_GROUP = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP";
    private static final String EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP_MEMEBER_ID";
    private static final String EXTRA_PARAM_FIREBASE_DEVICE_TOKEN = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_FIREBASE_DEVICE_TOKEN";

    private static Context activityContext;
    private ProductRestServivce productRestServivce;
    private ProductRetrofitClient mProductManager;

    public static final String BROCAST_SENDER = "com.example.gouadadopavogui.myshoppingapp";


    public ProductBackendHandlerService() {
        super("ProductBackendHandlerService");
    }

    private ProductRetrofitClient mManager= new ProductRetrofitClient();
    public static List<Product> allProducts;
    private ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();


    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startloadingProduct(Context context) {
        Intent intent = new Intent(context, ProductBackendHandlerService.class);
        intent.setAction(ACTION_LOAD_PRODUCT);
        activityContext = context;
        context.startService(intent);
    }

    public static void startActionSaveNewProduct(Context context, Product product)
    {
        Intent intent = new Intent(context, ProductBackendHandlerService.class);
        intent.setAction(ACTION_SAVE_NEW_PRODUCT);

        intent.putExtra(EXTRA_PARAM_NEW_PRODUCT, product);
        activityContext = context;
        context.startService(intent);
    }

    public static  void startActionGetMemberShoppingGroup(Context context, String appInsstanceID)
    {
        Intent intent = new Intent(context,  ProductBackendHandlerService.class);
        intent.setAction(ACTION_GET_MEMBER_SHOPPINGROUP);
        intent.putExtra(EXTRA_PARAM_APP_INSTANCE_ID, appInsstanceID);
        context.startService(intent);
    }

    public static void startActionCreateNewShoppingGroup(Context context, ShoppingGroup group)
    {
        Intent intent = new Intent(context, ProductBackendHandlerService.class);
        intent.setAction(ACTION_CREATE_NEW_SHOPPINGGROUP);

        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP, (Serializable) group);
        activityContext = context;
        context.startService(intent);
    }

    public static void startActionSaveFirebaseDeviceToken(String deviceFirebaseToken, Context context, long memberID)
    {
        Intent intent = new Intent(context,  ProductBackendHandlerService.class);
        intent.setAction(ACTION_SAVE_FIREBASE_DEVICE_TOKEN);
        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, memberID);
        intent.putExtra(EXTRA_PARAM_FIREBASE_DEVICE_TOKEN, deviceFirebaseToken);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_PRODUCT.equals(action)) {
                handleActionLoadProducts(activityContext);
            }
            else if (ACTION_SAVE_NEW_PRODUCT.equals(action))
            {
                final Product product = (Product) intent.getExtras().getSerializable(EXTRA_PARAM_NEW_PRODUCT);
                handleActionSaveNewProduct(activityContext, product);
            }
            else if (ACTION_GET_MEMBER_SHOPPINGROUP.equals(action))
            {
                final String appInstanceId = intent.getStringExtra(EXTRA_PARAM_APP_INSTANCE_ID);
                final Context context = activityContext; //getApplicationContext();
                handleActionGetMemberShoppingGroup(context, appInstanceId);
            }
            else if (ACTION_CREATE_NEW_SHOPPINGGROUP.equals(action))
            {
                final ShoppingGroup group = (ShoppingGroup) intent.getExtras().getSerializable(EXTRA_PARAM_SHOPPING_GROUP);
                handleActionCreateNewShoppingGroup(getApplicationContext(), group);
            }
            else if (ACTION_SAVE_FIREBASE_DEVICE_TOKEN.equals(action))
            {
                final long memberID = intent.getLongExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, 0);
                final String firebaseDeviceToken = intent.getStringExtra(EXTRA_PARAM_FIREBASE_DEVICE_TOKEN);
                if (memberID !=0 && !firebaseDeviceToken.isEmpty())
                {
                    handleActionSaveFirebaseDeviceToken(firebaseDeviceToken, memberID);
                }
            }
        }
    }

    public void handleActionLoadProducts(final Context context)
    {
        final Context ctxt = context;
        mProductManager= new ProductRetrofitClient();
        productRestServivce = mProductManager.getRectrofitClient();
        //Call<List<Product>> iProductList = productRestServivce.loadProducts(Locale.getDefault().getLanguage());
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
                    MyDBHandler myDBHandler = new MyDBHandler(context);
                    if(myDBHandler.cleanUpTable()){
                        myDBHandler.load_Products_into_device(productList);
                    }
                    EventBus.getDefault().post(new ProductEvent(productList));
                } else {
                    Log.e("MESSAGE: ", "PRODUCT LIST COULD NOT BE LOADED");
                    EventBus.getDefault().post(new ProductEvent(ErrorCodes.BACKEND_ERROR_PRODUCT_LIST_NOT_LOADED));
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage() + "  Stacktrace" + t.getStackTrace() + "\n\n");
                EventBus.getDefault().post(new ProductEvent(ErrorCodes.BACKEND_ERROR_PRODUCT_LIST_NOT_LOADED));
            }
        });
    }


    public void handleActionSaveNewProduct(Context context, final Product product)
    {
        final boolean[] saved = {false};
        final Context ctxt = context;
        product.setLang(Locale.getDefault().getLanguage());
        mProductManager= new ProductRetrofitClient();
        Call<Product> iProduct = mProductManager.getRectrofitClient().addProductToList(product);
        iProduct.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200 || response.code() == 201) {
                        saved[0] = true;
                        Product newProduct = response.body();
                        MyDBHandler myDBHandler = new MyDBHandler(ctxt);
                        //if(myDBHandler.cleanUpTable())
                        //myDBHandler.insert_product(response.body());
                        boolean insertProduct = false;
                        MyStorage storage = new MyStorage(getApplicationContext());

                        insertProduct = storage.addProduct(newProduct);
                        if (insertProduct) {
                            Product cartProduct = newProduct;
                            cartProduct.setInCart(false);
                            cartProduct.setProductAmount(1);
                            cartProduct.setIs_standard_product(0);
                            cartProduct.setLang(Locale.getDefault().getLanguage());

                            if (storage.addProductToCart(cartProduct)) {
                                Intent intent = new Intent(BROCAST_SENDER);
                                intent.putExtra("Action", "ADD_TO_CART");
                                intent.putExtra("productName", product.getProductName());
                                intent.putExtra("productId", product.getProductId());
                                intent.putExtra("position", product.getProductType());
                                intent.putExtra("productAmount", product.getProductAmount());
                                sendBroadcast(intent);
                                EventBus.getDefault().post(new LocalDBEvent("add_new_product", product, insertProduct, product.getProductType()));
                            }
                        }
                        else {
                            EventBus.getDefault().post(new LocalDBEvent(newProduct, ErrorCodes.BACKEND_ERROR_PRODUCT_ALREADY_EXISTS));
                            Log.e("STATE", "201");
                        }
                    } else {
                        if (response.code() == 204) {
                            EventBus.getDefault().post(new LocalDBEvent(product, ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED));
                            Log.e("STATE", "204");
                        }
                    }
                }
                else
                    EventBus.getDefault().post(new ProductEvent(ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED));
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                EventBus.getDefault().post(new ProductEvent(ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED));
            }
        });
    }

    private void handleActionGetMemberShoppingGroup(Context context, String appInstanceId)
    {
        Log.e("APPID", " " + appInstanceId);
        ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
        Call<ShoppingGroup> iShoppingGroup = mShoppingGroupRetrofitClient.getIGrouP().getMemberShoppingGroup(appInstanceId);
        iShoppingGroup.enqueue(new Callback<ShoppingGroup>() {
            @Override
            public void onResponse(Call<ShoppingGroup> call, Response<ShoppingGroup> response) {
                if(response.isSuccessful()) {
                    if (response.code()== 200 ) {
                        EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                    }
                    else if(response.code()==204) {
                        EventBus.getDefault().post(new ShoppingGroupEvent());
                        Log.e("MESSAGE: ", "NO CONTENT FOR SHOPPINGGROUP");
                    }
                }
                else {
                    Log.e("MESSAGE: ", "NO SHOPPINGGROUP AVAILABLE");
                    EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                }
            }

            @Override
            public void onFailure(Call<ShoppingGroup> call, Throwable t) {
                Log.e("ERROR: ", "NO SHOPPINGGROUP AVAILABLE " + t.getStackTrace().toString());
                EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
            }
        });
    }

    public void handleActionCreateNewShoppingGroup(Context context, ShoppingGroup shoppingGroup)
    {
        Call<ShoppingGroup> igroup = mShoppingGroupRetrofitClient.getIGrouP().createNewShoppingGroup(shoppingGroup);
        igroup.enqueue(new Callback<ShoppingGroup>() {
            @Override
            public void onResponse(Call<ShoppingGroup> call, Response<ShoppingGroup> response) {
                if(response.isSuccessful())
                {
                    EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                    //setShoppingGroup(response.body());
                    Log.e("MESSAGE: ", "  GROUP CREATED ");
                }
                else
                {
                    Log.e("MESSAGE: ", " GROUP COULD NOT BE CREATED "+response.errorBody());
                    EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_CREATED));
                }
            }

            @Override
            public void onFailure(Call<ShoppingGroup> call, Throwable t) {
                Log.e("ERROR: ", " "+ t.getMessage() + "       " + t.getStackTrace());
                EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_CREATED));
            }
        });
    }

    public void handleActionSaveFirebaseDeviceToken(String firebaseDeviceToken, long memberId) {
        //final Context ctxt = context;

        Call<Boolean> iDeviceToken = mShoppingGroupRetrofitClient.getIGrouP().FirebaseDeviceToken(firebaseDeviceToken, memberId);
        iDeviceToken.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.e("MESSAGE: ", "DEVICE TOKEN SUCCEFULLY SAVED");
                }
                else {
                    Log.e("MESSAGE: ", "DEVICE TOKEN COULD NOT BE SAVED");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ERROR: ", "DEVICE TOKEN NOT SAVED " + t.getMessage() +" " + t.getStackTrace());
            }
        });
    }
}

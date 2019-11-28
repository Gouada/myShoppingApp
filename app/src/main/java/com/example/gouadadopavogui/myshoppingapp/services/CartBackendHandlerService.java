package com.example.gouadadopavogui.myshoppingapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;


import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.controller.CartRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.dao.MyDBHandler;
import com.example.gouadadopavogui.myshoppingapp.events.CartEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.helpers.MessageProvider;
import com.example.gouadadopavogui.myshoppingapp.interfaces.CartRestService;
import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.CartProducts;
import com.example.gouadadopavogui.myshoppingapp.model.CartProductsId;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.MyShoppingCartUI;

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
public class CartBackendHandlerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LOAD_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.LOAD_CURRENT_CART";
    private static final String ACTION_SAVE_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.SAVE_CURRENT_CART";
    private static final String ACTION_CREATE_NEW_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.CREATE_NEW_CART";
    private static final String ACTION_ARCHIVE_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.ARCHIVE_CURRENT_CART";
    private static final String ACTION_REINITIALYZE_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.REINIT_CURRENT_CART";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_GROUP_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_GROUP_ID";
    private static final String EXTRA_PARAM_CURRENT_CART_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_CURRENT_CART_ID";

    private static final String EXTRA_PARAM_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_CURRENT_CART";
    private static final String EXTRA_PARAM_CURRENT_CART_PRODUCTS = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_CURRENT_CART_PRODUCTS";

    private static final String EXTRA_PARAM_SHOPPING_GROUP = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP";
    private static final String ACTION_GET_MEMBER_SHOPPINGROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.GET_MEMBER_SHOPPINGROUP";
    private static final String EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP_MEMEBER_ID";

    private MessageProvider messageProvider = new MessageProvider();


    private CartRetrofitClient cartRetrofitClient = new CartRetrofitClient();
    private CartRestService cartRestService;

    //private Context activityContext;
    public CartBackendHandlerService() {
        super("CartBackendHandlerService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionLoadCurrentCart(Context context, long groupId) {
        Intent intent = new Intent(context, CartBackendHandlerService.class);
        intent.setAction(ACTION_LOAD_CURRENT_CART);
        intent.putExtra(EXTRA_PARAM_GROUP_ID, groupId);
        context.startService(intent);
    }

    public static void startActionSaveCurrentCart(Context context, long groupId, long cartID, long memberId, List<Product> cartProducts)
    {
        Intent intent = new Intent(context, CartBackendHandlerService.class);
        intent.setAction(ACTION_SAVE_CURRENT_CART);
        intent.putExtra(EXTRA_PARAM_GROUP_ID, groupId);
        intent.putExtra(EXTRA_PARAM_CURRENT_CART_ID, cartID);
        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, memberId);

        List<CartProducts> cartProductsList = new ArrayList<CartProducts>();
        for (Product pdt:cartProducts)
        {
            CartProducts cpdt = new CartProducts();
            CartProductsId cartProductsId = new CartProductsId();
            cartProductsId.setProduct(pdt);
            cpdt.setPrimaryKey(cartProductsId);
            cpdt.setProductAmount(pdt.getProductAmount());
            cpdt.setProductInCart(pdt.isInCart());
            cartProductsList.add(cpdt);
        }


        intent.putExtra(EXTRA_PARAM_CURRENT_CART_PRODUCTS, (Serializable) cartProductsList);
        context.startService(intent);
    }

    public static void startActionCreateNew(Context context, long shoppingGroupId, Cart currentShoppingCart)
    {
        Intent intent = new Intent(context, CartBackendHandlerService.class);
        intent.setAction(ACTION_CREATE_NEW_CART);
        intent.putExtra(EXTRA_PARAM_GROUP_ID, shoppingGroupId);
        intent.putExtra(EXTRA_PARAM_CURRENT_CART, (Serializable) currentShoppingCart);
        context.startService(intent);
    }

    public static void startActionArchiveCurrentCart(Context context, long shoppingGroupId, long currentCartId, long memberId)
    {
        Intent intent = new Intent(context, CartBackendHandlerService.class);
        intent.setAction(ACTION_ARCHIVE_CURRENT_CART);
        intent.putExtra(EXTRA_PARAM_GROUP_ID, shoppingGroupId);
        intent.putExtra(EXTRA_PARAM_CURRENT_CART_ID, currentCartId);
        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, memberId);
        context.startService(intent);
    }

    public static void startActionReinitializeCart(Context context) {
        Intent intent = new Intent(context, CartBackendHandlerService.class);
        intent.setAction(ACTION_REINITIALYZE_CURRENT_CART);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_CURRENT_CART.equals(action) ) {
                final long groupId = intent.getLongExtra(EXTRA_PARAM_GROUP_ID, 0);
                final Context context = getApplicationContext(); //intent.getStringExtra(EXTRA_PARAM2);
                if(groupId != 0) {
                    handleActionLoadCurrentCart(context, groupId);
                }
            }else if (ACTION_SAVE_CURRENT_CART.equals(action)){
                final long groupId = intent.getLongExtra(EXTRA_PARAM_GROUP_ID, 0L);
                final long cart_ID = intent.getLongExtra(EXTRA_PARAM_CURRENT_CART_ID, 0L);
                final List<CartProducts> productList = (List<CartProducts>) intent.getSerializableExtra(EXTRA_PARAM_CURRENT_CART_PRODUCTS);
                final  long memberId = intent.getLongExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, 0L);
                if (groupId !=0 && cart_ID !=0 && !productList.isEmpty())
                    handleActionSaveCurrentCart(groupId, cart_ID, memberId, productList);
                /*else
                {
                    if (groupId == 0) {
                        errorMessageProvider.setErrorCode("ERROR");
                        errorMessageProvider.setErrorMessage("GROUP_ID MISSING");
                    }
                    else if (cart_ID == 1) {
                        errorMessageProvider.setErrorCode("ERROR");
                        errorMessageProvider.setErrorMessage("CART_ID MISSING");
                    }
                    else if (productList.isEmpty()) {
                        errorMessageProvider.setErrorCode("ERROR");
                        errorMessageProvider.setErrorMessage("PRODUCT_LIST EMPTY");
                    }
                } */
            }
            else if (ACTION_CREATE_NEW_CART.equals(action)) {
                final long groupId = intent.getLongExtra(EXTRA_PARAM_GROUP_ID, 0);
                final Cart currentCart = (Cart) intent.getSerializableExtra(EXTRA_PARAM_CURRENT_CART);
                if (groupId != 0 && currentCart != null)
                    handleActionCreateNewCart(groupId, currentCart);
            }
            else if (ACTION_ARCHIVE_CURRENT_CART.equals(action))
            {
                final long groupId = intent.getLongExtra(EXTRA_PARAM_GROUP_ID, 0L);
                final long cart_ID = intent.getLongExtra(EXTRA_PARAM_CURRENT_CART_ID, 0L);
                final long memeberId = intent.getLongExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMEBER_ID, 0L);
                if (groupId != 0 && cart_ID!=0)
                handleActionArchiveCurrentCart(groupId, cart_ID,memeberId);
            }
            else if (ACTION_REINITIALYZE_CURRENT_CART.equals(action))
                handleActionReInitialyzeCurrentCart(getApplicationContext());
        }
    }

    private void handleActionCreateNewCart(long groupId, Cart currentCart)
    {
        cartRestService = cartRetrofitClient.getCartsService();
        Call<Cart> cartCall = cartRestService.createNewCart(groupId, currentCart);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(new CartEvent(response.body()));
                    messageProvider.setMessage("NEW_CART_CREATED");
                    messageProvider.setMessageCode("ERROR");
                    Log.e("MESSAGE: ", "NEW CART CREATED");
                } else {
                    messageProvider.setMessageCode("ERROR");
                    messageProvider.setMessage("COULD_NOT_CREATE_A_NEW_CART");
                    EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_CREATED));
                    Log.e("ERROR: ", "COULD NOT CREATE A NEW CART");
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("ERROR: ", t.getStackTrace().toString());
                EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_CREATED));
                Log.e("ERROR", t.getStackTrace().toString());
            }
        });
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */

    private void handleActionLoadCurrentCart(final Context context, final long groupId) {
        cartRestService = cartRetrofitClient.getCartsService();
        Call<Cart> cartCall = cartRestService.getCurrentShoppingCart(groupId, Locale.getDefault().getLanguage());
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.isSuccessful())
                {
                    Cart currentCart = response.body();
                    messageProvider.setMessageCode("MESSAGE");
                    messageProvider.setMessage("CURRENT_CART_LOADED");
                    Log.e("MESSAGE: ", " CURRENT CART LOADED");

                    //MyDBHandler handler = new MyDBHandler(context);
                    //handler.cleanUpCart();

                    /*handleActionLoadCurrentCartList(context, groupId, currentCart.getCartId());*/
                    EventBus.getDefault().post(new CartEvent(currentCart));
                }
                else
                {
                    messageProvider.setMessageCode("ERROR");
                    messageProvider.setMessage("COULD_NOT_GET_THE_CURRENT_CART");
                    Log.e("ERROR: ", " COULD NOT GET THE CURRENT CART "+ response.errorBody());
                    EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CURRENT_CART_NOT_LOADED));
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CURRENT_CART_NOT_LOADED));
                Log.e("FAILED: ", " COULD NOT GET THE CURRENT CART "+ t.getMessage() + t.getStackTrace() );
            }
        });
    }

    private void handleActionLoadCurrentCartList(final Context context, long groupId, long cartId) {
        cartRestService = cartRetrofitClient.getCartsService();
        Call<List<Product>> cartListCall = cartRestService.getCurrentShoppingCartList(groupId, cartId, Locale.getDefault().getLanguage());
        cartListCall.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful())
                {
                    response.headers().get("Location");
                    List<Product> currentCartProductList = response.body();
                    //EventBus.getDefault().post(new CartEvent(currentCartProductList));
                    MyDBHandler handler = new MyDBHandler(context);
                    if (currentCartProductList != null)
                    {
                        if (currentCartProductList.size()>0)
                            //handler.cleanUpCart();
                            handler.add_Products_to_cart(currentCartProductList);
                            Log.e("MESSAGE: ", " CURRENT LIST RE-LOADED");
                        /*EventBus.getDefault().post(new CartEvent(currentCartProductList));*/
                    }
                     //   handler.add_Products_to_cart(currentCart.getProductsInCart());
                }
                else
                    Log.d("MESSAGE: ", "COULD NOT LOAD CURRENT SHOPPING LIST.......");
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ERROR: ", "COULD NOT LOAD CURRENT SHOPPING LIST" + t.getMessage());
                EventBus.getDefault().post(new CartEvent(500));
            }
        });
    }


    public void handleActionSaveCurrentCart(long groupId, long cartId, long memberId, List<CartProducts> cartProductsList)
    {
       /*
        List<CartProducts> cartProductsList = new ArrayList<CartProducts>();
        for (Product pdt:cartProducts)
        {
            CartProducts cpdt = new CartProducts();
            CartProductsId cartProductsId = new CartProductsId();
            cartProductsId.setProduct(pdt);
            cpdt.setPrimaryKey(cartProductsId);
            cpdt.setProductAmount(pdt.getProductAmount());
            cpdt.setProductInCart(pdt.isInCart());
            cartProductsList.add(cpdt);
        }
        */
        cartRestService = cartRetrofitClient.getCartsService();
        //Call<Cart> cartCall = cartRestService.saveCurrentCart(groupId, cartId, memberId, cartProducts);
        Call<Cart> cartCall = cartRestService.saveCurrentCart(groupId, cartId, memberId, Locale.getDefault().getLanguage(), cartProductsList);
        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                //messageProvider.setMessageCode("ERROR");
                if (response.isSuccessful()) {
                    Cart currentCart = response.body();
                    //EventBus.getDefault().post(new CartEvent(currentCart));
                    messageProvider.setMessageCode("MESSAGE");
                    messageProvider.setMessage(getResources().getString(R.string.CHANGES_SAVED_IN_BACKEND));
                    Log.d("MESSAGE: ", "CHANGES SAVED IN BACKEND");
                    EventBus.getDefault().post(new CartEvent(currentCart));
                } else {
                    Log.d("MESSAGE: ", "CHANGES COULD NOT BE SAVED IN BACKEND ...." + response.code() +" ... " + response.errorBody());
                    EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_SAVED));
                    messageProvider.setMessageCode("ERROR");
                    messageProvider.setMessage("CHANGES_COULD_NOT_BE_SAVED_IN_BACKEND");
                }
            }
                @Override
                public void onFailure (Call < Cart > call, Throwable t){
                    Log.e("ERROR: ", "CHANGES COULD NOT BE SAVED IN BACKEND" + t.getMessage()+ t.getStackTrace()[0].toString() );
                    EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_SAVED));
                }

    });
}
    private void handleActionArchiveCurrentCart(long groupId, long cartId, long memberId)
    {
        cartRestService = cartRetrofitClient.getCartsService();
        Call<Boolean> cartCall = cartRestService.archiveCurrentCart(groupId, cartId, memberId, Locale.getDefault().getLanguage());
        cartCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful())
                {
                    messageProvider.setMessageCode("MESSAGE");
                    messageProvider.setMessage("CART_ARCHIEVED");
                    MyDBHandler handler = new MyDBHandler(getApplicationContext());
                    handler.cleanUpCart();
                }
                else {
                    messageProvider.setMessageCode("MESSAGE");
                    messageProvider.setMessage("CART_COULD_NOT_BE_ARCHIEVED");
                    EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_ARCHIVED));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                messageProvider.setMessageCode("MESSAGE");
                messageProvider.setMessage("CART_COULD_NOT_BE_ARCHIEVED");
                EventBus.getDefault().post(new CartEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_ARCHIVED));
            }
        });
    }

    private void handleActionReInitialyzeCurrentCart(Context applicationContext)
    {
        MyDBHandler handler = new MyDBHandler(applicationContext);
        handler.reInitialyzeCurrentCart();
    }
}

package com.example.gouadadopavogui.myshoppingapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.MyShoppingCartUI;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyStorageHandlerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_DELETE_PRODUCT_FROM_PRODUCT_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.DELETE_PRODUCT_FROM_PRODUCT_TBL";
    private static final String ACTION_DELETE_PRODUCT_FROM_CART_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.DELETE_PRODUCT_FROM_CART_TBL";
    private static final String ACTION_ADD_PRODUCT_TO_PRODUCT_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.ADD_PRODUCT_TO_PRODUCT_TBL";
    private static final String ACTION_ADD_PRODUCT_TO_CART_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.ADD_PRODUCT_TO_CART_TBL";
    private static final String ACTION_SEARCH_PRODUCT_IN_PRODUCT_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.SEARCH_PRODUCT_IN_PRODUCT_TBL";
    private static final String ACTION_SEARCH_PRODUCT_IN_CART_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.SEARCH_PRODUCT_IN_CART_TBL";
    private static final String ACTION_UPDATE_PRODUCT_IN_CART_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.UPDATE_PRODUCT_IN_CART_TBL";
    private static final String ACTION_GET_ALL_FROM_PRODUCT_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.GET_ALL_FROM_PRODUCT_TBL";
    private static final String ACTION_ADD_PRODUCT_IN_PRODUCT_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.ADD_PRODUCT_TO_PRODUCT_TBL";
    private static final String ACTION_UPDATE_CURRENT_CART = "com.example.gouadadopavogui.myshoppingapp.services.action.UPDATE_CURRENT_CART";
    private static final String ACTION_REMOVE_PRODUCT_OUT_OF_CART_TBL = "com.example.gouadadopavogui.myshoppingapp.services.action.REMOVE_PRODUCT_OUT_OF_CART_TBL";

    // TODO: Rename parameters
    private static final String EXTRA_PRODUCT_NAME = "com.example.gouadadopavogui.myshoppingapp.services.extra.PRODUCT_NAME";
    private static final String EXTRA_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.extra.PRODUCT";
    private static final String EXTRA_IS_PRODUCT_IN_CART = "com.example.gouadadopavogui.myshoppingapp.services.extra.IS_PRODUCT_IN_CART";

    private static final String EXTRA_GPOS = "com.example.gouadadopavogui.myshoppingapp.services.extra.GPOS";
    private static final String EXTRA_CHPOS = "com.example.gouadadopavogui.myshoppingapp.services.extra.CHPOS";
    private static final String EXTRA_CHILD_VIEW = "com.example.gouadadopavogui.myshoppingapp.services.extra.CHILD_VIEW";

    private static MyStorage storage; // = new MyStorage(context);
    public MyStorageHandlerService() {
        super("MyStorageHandlerService");
    }
    public static View childView_stat;
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionDeleteProductFromProduct_TBL(Context context, String product_name, int groupPosition) {
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        storage = new MyStorage(context);
        intent.setAction(ACTION_DELETE_PRODUCT_FROM_PRODUCT_TBL);
        intent.putExtra(EXTRA_PRODUCT_NAME, product_name);
        intent.putExtra(EXTRA_GPOS, groupPosition);
        context.startService(intent);
    }
    public static void startActionDeleteProductFromCart_TBL(Context context, String product_name) {
        storage = new MyStorage(context);
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.setAction(ACTION_DELETE_PRODUCT_FROM_CART_TBL);
        intent.putExtra(EXTRA_PRODUCT_NAME, product_name);
        context.startService(intent);
    }

    public static void startActionAddProductToCart_TBL(Context context, Product product) {
        storage = new MyStorage(context);
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.setAction(ACTION_ADD_PRODUCT_TO_CART_TBL);
        intent.putExtra(EXTRA_PRODUCT, product);
        context.startService(intent);
    }

    public static void startActionGetAllProduct(Context context) {
        storage = new MyStorage(context);
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.setAction(ACTION_GET_ALL_FROM_PRODUCT_TBL);
        context.startService(intent);
    }

    public static void startActionUpdateProductInCart_TBL(Context context, Product productToUpdate, int gPos, int chPos, View childView) {
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.putExtra(EXTRA_GPOS, gPos);
        intent.putExtra(EXTRA_CHPOS, chPos);
        //intent.putExtra(EXTRA_CHILD_VIEW, (CharSequence) childView);
        childView_stat = childView;
        intent.setAction(ACTION_UPDATE_PRODUCT_IN_CART_TBL);

        // we need to save this property because it is not being serialized with the Product-Model @Expose(serialize = false, deserialize = false)  private transient boolean isInCart;

        intent.putExtra(EXTRA_IS_PRODUCT_IN_CART, productToUpdate.isInCart());
        intent.putExtra(EXTRA_PRODUCT, (Serializable) productToUpdate);
        context.startService(intent);
    }

    public static void startActionRemoveProductOutOfCart_TBL(Context context, Product productToUpdate, int gPos, int chPos, View childView) {
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.putExtra(EXTRA_GPOS, gPos);
        intent.putExtra(EXTRA_CHPOS, chPos);
        //intent.putExtra(EXTRA_CHILD_VIEW, (CharSequence) childView);
        childView_stat = childView;
        intent.setAction(ACTION_REMOVE_PRODUCT_OUT_OF_CART_TBL);

        // we need to save this property because it is not being serialized with the Product-Model @Expose(serialize = false, deserialize = false)  private transient boolean isInCart;

        intent.putExtra(EXTRA_IS_PRODUCT_IN_CART, productToUpdate.isInCart());
        intent.putExtra(EXTRA_PRODUCT, (Serializable) productToUpdate);
        context.startService(intent);
    }

    public static void startActionAddProductToProduct_TBL(Context context, Product newProduct) {
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.setAction(ACTION_ADD_PRODUCT_IN_PRODUCT_TBL);
        intent.putExtra(EXTRA_PRODUCT, newProduct);
        context.startService(intent);
    }

    public static void startActionArchiveCurrentCart(Context context)
    {
        Intent intent = new Intent(context, MyStorageHandlerService.class);
        intent.setAction(ACTION_UPDATE_CURRENT_CART);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            final String action = intent.getAction();
            if (ACTION_DELETE_PRODUCT_FROM_PRODUCT_TBL.equals(action)) {
                final String product_name = intent.getStringExtra(EXTRA_PRODUCT_NAME);
                final int groupPosition = intent.getExtras().getInt(EXTRA_GPOS);
                handleActionDeleteProductFromProduct_TBL(product_name, groupPosition);
            } else if (ACTION_DELETE_PRODUCT_FROM_CART_TBL.equals(action)) {
                final String product_name = intent.getStringExtra(EXTRA_PRODUCT_NAME);
                handleActionDeleteProductFromCart_TBL(product_name);
            }
            else if (ACTION_ADD_PRODUCT_TO_CART_TBL.equals(action))
            {
                final Product product = (Product) intent.getSerializableExtra(EXTRA_PRODUCT);
                handleActionAddProductToCart_TBL(product);
            }
            else if (ACTION_GET_ALL_FROM_PRODUCT_TBL.equals(action))
            {
                handleActionGetAllProductFromProduct_TBL();
            }
            else if(ACTION_UPDATE_PRODUCT_IN_CART_TBL.equals(action))
            {
                final Product product = (Product) intent.getSerializableExtra(EXTRA_PRODUCT);
                final boolean is_Product_In_Cart = intent.getExtras().getBoolean(EXTRA_IS_PRODUCT_IN_CART);
                final int gPos = intent.getExtras().getInt(EXTRA_GPOS);
                final int chPos = intent.getExtras().getInt(EXTRA_CHPOS);
                //we need this step otherwise the boolean value will get lost through the serialization due to this definition in
                // Product-class @Expose(serialize = false, deserialize = false) private transient boolean isInCart;
                //final View childView = (View) intent.getParcelableExtra(EXTRA_CHILD_VIEW);
                product.setInCart(is_Product_In_Cart);
                handleActionUpdateProductInCart_TBL(product, gPos, chPos, childView_stat);
            }
            else if (ACTION_REMOVE_PRODUCT_OUT_OF_CART_TBL.equals(action))
            {
                final Product product = (Product) intent.getSerializableExtra(EXTRA_PRODUCT);
                final boolean is_Product_In_Cart = intent.getExtras().getBoolean(EXTRA_IS_PRODUCT_IN_CART);
                final int gPos = intent.getExtras().getInt(EXTRA_GPOS);
                final int chPos = intent.getExtras().getInt(EXTRA_CHPOS);
                //we need this step otherwise the boolean value will get lost through the serialization due to this definition in
                // Product-class @Expose(serialize = false, deserialize = false) private transient boolean isInCart;
                //final View childView = (View) intent.getParcelableExtra(EXTRA_CHILD_VIEW);
                product.setInCart(is_Product_In_Cart);
                handleActionRemoveProductOutOfCart_TBL(product, gPos, chPos, childView_stat);
            }
            else if (ACTION_ADD_PRODUCT_IN_PRODUCT_TBL.equals(action))
            {
                final Product product = (Product) intent.getSerializableExtra(EXTRA_PRODUCT);
                handleActionAddProductInProduct_TBL(product);
            }
            else if(ACTION_UPDATE_CURRENT_CART.equals(action))
            {
                handleActionArchiveCurrentCart();
            }
        }
    }

    private void handleActionAddProductInProduct_TBL(Product product) {
        boolean insertProduct = false;
        storage = new MyStorage(getApplicationContext());
        insertProduct = storage.addProduct(product);
        EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.ADD_NEW_PRODUCT, product, insertProduct));
    }

    private void handleActionUpdateProductInCart_TBL(Product product, int gPos, int chPos, View childView) {
        storage = new MyStorage(getApplicationContext());
        storage.updateProductInCart(product);
        EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.UPDATE_PRODUCT, product, gPos+1));
    }

    private void handleActionRemoveProductOutOfCart_TBL(Product product, int gPos, int chPos, View childView) {
        storage = new MyStorage(getApplicationContext());
        storage.removeProductOutOfCart(product);
        EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.REMOVE_PRODUCT_FROM_CART, product, gPos+1));
    }


    private void handleActionGetAllProductFromProduct_TBL() {
        storage.getAllProducts();
    }

    private void handleActionAddProductToCart_TBL(Product product) {
        storage.addProductToCart(product);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDeleteProductFromProduct_TBL(String product_name, int groupPosition) {
        boolean deleteProduct = false;
        storage = new MyStorage(getApplicationContext());
        //storage.deleteProductFromList(product_name);
        Product productToDelete = new Product(product_name);
        deleteProduct = storage.deleteProductFromList(product_name);
        EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.DELETE_PRODUCT, productToDelete, deleteProduct, groupPosition));
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDeleteProductFromCart_TBL(String product_name) {
        storage = new MyStorage(getApplicationContext());
        storage.deleteProduktFromCart(product_name);
    }

    private void handleActionArchiveCurrentCart()
    {
        storage = new MyStorage(getApplicationContext());
        boolean archived = storage.archiveCurrentCar();
        EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.ARCHIVE_CURRENT_CART, null, archived));
    }
}

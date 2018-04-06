package com.example.gouadadopavogui.myshoppingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.controller.ProductRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.dao.MyBacckendStorageManager;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.helpers.FocusChangedListener;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;
import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;
import com.example.gouadadopavogui.myshoppingapp.services.CartBackendHandlerService;
import com.example.gouadadopavogui.myshoppingapp.services.MyStorageHandlerService;
import com.example.gouadadopavogui.myshoppingapp.services.ProductBackendHandlerService;
//import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Locale;

public class MyShoppingCartUI extends AppCompatActivity implements ProductListFragment.OnFragmentInteractionListener,
        CartFragment.OnCartFragmentInteractionListener, AdminFragment.OnFragmentInteractionListener, MyAlertDialog.MyDialogFragmentListener, FeedBackFragment.OnFragmentInteractionListener{

    private static final String FEEDBACK_ACTION = "feedback";
    private static int FEEDBACK_REQUEST_CODE= 5;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static final String BROADCAST_SENDER = "com.example.gouadadopavogui.myshoppingapp";
    //public static final String ADD_TO_CART = "com.example.gouadadopavogui.myshoppingapp";
    //private static final String SHAHRED_PREFERENCES_FILENAME = "com.example.gouadadopavogui.myshoppingapp.INSTANCE_ID";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout sliding_tabs;

    private MySharedPreferencesManagerSingleton mySharedPreferencesManager;

    public static List<Product> allProducts;
    private ProductRetrofitClient mManager;

    private ShoppingGroupMember shoppingGroupMember; // = new ShoppingGroupMember();
    private Cart currentShoppingCart;
    private ShoppingGroup shoppingGroup; // = new ShoppingGroupMember();

    private MyStorage storage; // = new MyStorage(getActivity());
    private MyBacckendStorageManager backendStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = new MyStorage(this);

        saveInstanceID();
        saveDeviceToken();
        setContentView(R.layout.activity_my_shopping_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        //sliding_tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        sliding_tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        //sliding_tabs.setDistributeEvenly(true);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        sliding_tabs.setupWithViewPager(mViewPager);

        //sliding_tabs.setViewPager(mViewPager);

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void initContent()
    {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        sliding_tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_shopping_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_add_product)
        {
            this.showNewProductPopUpwindow();
            return true;
        }
        else if (id == R.id.action_feedback)
        {
            Intent feedBackIntent = new Intent(this, MainActivity.class);
            feedBackIntent.putExtra("FEEDBACK_ACTION", FEEDBACK_ACTION);
            startActivity(feedBackIntent);//, FEEDBACK_REQUEST_CODE);

            //getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new FeedBackFragment()).commit();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void addProductToList(Product newProduct)
    {
        MyStorageHandlerService.startActionAddProductToProduct_TBL(getApplicationContext(), newProduct);
        /*Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "ADD_TO_LIST");
        intent.putExtra("product", newProduct);
        sendBroadcast(intent);
        */
    }

    @Override
    public void deleteProduct(String productName, int groupPosition)
    {
        //MyStorage storage = new MyStorage(this);
        //storage.deleteProductFromList(productName);
        MyStorageHandlerService.startActionDeleteProductFromProduct_TBL(this, productName, groupPosition);
       // MyStorageHandlerService.startActionDeleteProductFromCart_TBL(this, productName);
 /*       Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "DELETE");
        intent.putExtra("productName", productName);
        intent.putExtra("position", position);
        sendBroadcast(intent);
*/
        //Snackbar.make(findViewById(R.id.producListFgmt), productName + getResources().getText(R.string.DELETED), Snackbar.LENGTH_SHORT).show();;
    }

    @Override
    public void addProductToCart(Product product, int groupPosition )
    {
     /*   List<Product> pdctList = new ArrayList<Product>();
        pdctList.add(product);*/
        if (product.getProductAmount() == 0)
            Snackbar.make(findViewById(R.id.producListFgmt),  getResources().getText(R.string.ENTER_PRODUCT_AMOUNT), Snackbar.LENGTH_SHORT).show();
        else
        {
            MyStorage storage = new MyStorage(this);
            //boolean added = MyStorageHandlerService.startActionAddProductToCart_TBL(this, product);
            if (storage.addProductToCart(product)) {
                Snackbar.make(findViewById(R.id.producListFgmt),  getResources().getText(R.string.PRODUCT_ADDED_TO_CART), Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(BROADCAST_SENDER);
                intent.putExtra("Action", "ADD_TO_CART");
                intent.putExtra("productName", product.getProductName());
                intent.putExtra("productId", product.getProductId());
                intent.putExtra("position", groupPosition);
                intent.putExtra("productAmount", product.getProductAmount());
                sendBroadcast(intent);
            }
            else
                Snackbar.make(findViewById(R.id.producListFgmt),  getResources().getText(R.string.PRODUCT_ALREADY_EXISTS_IN_CART), Snackbar.LENGTH_SHORT).show();
        }
        product = null;
    }

    public void setProductIsChecked(Product product, boolean isChecked, int position)
    {
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "SELECTION");
        intent.putExtra("productName", product.getProductName());
        intent.putExtra("status", isChecked);
        intent.putExtra("position", position);
        sendBroadcast(intent);
    }

    @Override
    public void deleteProductFromCart(String productName, int groupPosition, int childPosition)
    {
        //MyStorage storage = new MyStorage(this);
        //storage.deleteProduktFromCart(productName);
        MyStorageHandlerService.startActionDeleteProductFromCart_TBL(this, productName);
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "DELETE_FROM_CART");
        intent.putExtra("productName", productName);
        intent.putExtra("groupPosition", groupPosition);
        intent.putExtra("childPosition", childPosition);

        sendBroadcast(intent);
        Snackbar.make(findViewById(R.id.producListFgmt), productName + getResources().getText(R.string.DELETED), Snackbar.LENGTH_SHORT).show();;
    }


    @Override
    public void deleteProductFromCart(Product product, int groupPosition)
    {
        //MyStorage storage = new MyStorage(this);
       // storage.deleteProduktFromCart(product.getProductName());
        MyStorageHandlerService.startActionDeleteProductFromCart_TBL(this, product.getProductName());
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "DELETE_FROM_CART");
        intent.putExtra("product", product);
        intent.putExtra("position", groupPosition);

        sendBroadcast(intent);
        Snackbar.make(findViewById(R.id.producListFgmt), product.getProductName() + getResources().getText(R.string.DELETED), Snackbar.LENGTH_SHORT).show();;
    }

    @Override
    public void archivesCurrentCart() {
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "SHOPPING_DONE");
        sendBroadcast(intent);
        //Snackbar.make(findViewById(R.id.producListFgmt), "you are done congratulation!!!! would you like to archive the current cart", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAlertDialog(int layoutId, int negativeButton)
    {
        //int layoutId = R.layout.shopping_done_alert;
        int positiveButton = R.string.POSITIVE_BUTTON;
        //int negativeButton = R.string.NEGATIVE_BUTTON;

        DialogFragment dialogFragment = MyAlertDialog.newInstance(layoutId, positiveButton, negativeButton);
        dialogFragment.show(getSupportFragmentManager(), "MyAlertDialog");
    }

    @Override
    public void updateProductInCart(Product productToUpdate, int gpPos, int chldPos) {
        MyStorageHandlerService.startActionUpdateProductInCart_TBL(this.getApplicationContext(), productToUpdate, gpPos, chldPos, null);
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "UPDATE_IN_CART");
        intent.putExtra("product", productToUpdate);
        //sendBroadcast(intent);
    }

    @Override
    public void removeProductFromCart(Product productToUpdate, int gpPos, int chldPos)
    {
        MyStorageHandlerService.startActionRemoveProductOutOfCart_TBL(this.getApplicationContext(), productToUpdate, gpPos, chldPos, null);
        Intent intent = new Intent(BROADCAST_SENDER);
        intent.putExtra("Action", "ACTION_REMOVE_PRODUCT_OUT_OF_CART_TBL");
        intent.putExtra("product", productToUpdate);

    }



    @Override
    public void onPositiveButtonClick(MyAlertDialog dialogFragment) {
        if (dialogFragment.getLayoutId() == R.layout.shopping_done_alert) {
            //MySharedPreferencesManager mySharedPreferencesManager  = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
            mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
            CartBackendHandlerService.startActionArchiveCurrentCart(this, mySharedPreferencesManager.getShoppingGroupId(), mySharedPreferencesManager.getCurrentCartId(), mySharedPreferencesManager.getMemberId());
            MyStorageHandlerService.startActionArchiveCurrentCart(getApplicationContext());
            mySharedPreferencesManager.setCurrentCartId(0);
        }
        else
            dialogFragment.dismiss();
    }

    @Override
    public void onNegativeButtonClick(DialogFragment dialogFragment) {
        //MySharedPreferencesManager mySharedPreferencesManager  = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        CartBackendHandlerService.startActionReinitializeCart(this);
    }

    /*
        @Override
        public void updateProductAmount(int newProductAmount, int position, String productName)
        {
            Intent intent = new Intent(BROADCAST_SENDER);
            intent.putExtra("Action", "UPDATE_PDCT_AMOUNT_IN_CART");
            intent.putExtra("productAmount", newProductAmount);
            intent.putExtra("productName", productName);
            intent.putExtra("position", position);
            sendBroadcast(intent);
        }

        @Override
        public void saveNewProductAmountInList(int position, int productAmount) {
            Intent intent = new Intent(BROADCAST_SENDER);
            intent.putExtra("Action", "SAVE_NEW_PDCT_AMOUNT_IN_LIST");
            intent.putExtra("position", position);
            intent.putExtra("productAmount", productAmount);
            sendBroadcast(intent);
        }
    */
    @Override
    public void onCartFragmentInteraction(Uri uri) {
    }

    public void saveInstanceID() {
        //String appInstanceIDValue = InstanceID.getInstance(this).getId();
        String appInstanceIDValue = FirebaseInstanceId.getInstance().getId();

        String appInstanceIDKey = "appUniqueKey";
        //mySharedPreferencesManager = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager.setAppInstanceId(appInstanceIDValue);
        //sharedPreferences.saveInsharedPreferences(appInstanceIDKey, appInstanceIDValue );
    }

    public void saveDeviceToken()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("MyShoppingCartApp");
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        //mySharedPreferencesManager = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));

        mySharedPreferencesManager.setFirebaseDeviceToken(deviceToken);
    }
    public boolean existsUser()
    {
        if (getShoppingGroupMember() != null) {
            Log.d("LOGGER","EXISTS USER");
            return true;
        }
        else
        {
            Log.d("LOGGER","NOT EXISTS USER");
            return false;
        }
    }

    @Override
    public boolean isNetworkConnectionAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        Log.e("STATE", networkInfo.getState().toString() +".."+ networkInfo.getExtraInfo().toString() +".."+ networkInfo.getReason()+
 //               ".."+networkInfo.isAvailable());
        if (networkInfo != null && networkInfo.isConnected())
        //if (networkInfo.isConnectedOrConnecting())
                return true;
        else return false;
    }



    @Override
    public void showPopUpwindow()
    {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        final String deviceId = telephonyManager.getDeviceId();
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.new_user_popup_window,null,false);
        final EditText group_name_txt = (EditText) popUpView.findViewById(R.id.shopping_group_name);
        final EditText user_name_txt = (EditText)  popUpView.findViewById(R.id.user_name);
        final EditText user_phone_number_txt = (EditText) popUpView.findViewById(R.id.user_phone_number);

        group_name_txt.setOnFocusChangeListener(new FocusChangedListener<EditText>(group_name_txt, getApplicationContext(), group_name_txt.getWindowToken()) {
        });
        /*
        user_name_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

        user_phone_number_txt.setOnFocusChangeListener(new FocusChangedListener<EditText>(user_phone_number_txt) {
            @Override
            public void onFocusChange(EditText targetField, View view, boolean hasFocus) {
                if (!hasFocus)
                    hideSoftKeyBoard();
            }
        });
        */
        /*group_name_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    hideSoftKeyBoard();
            }
        });
        */
        final PopupWindow myWindow = new PopupWindow(this);

        Button save_group_name_button = (Button) popUpView.findViewById(R.id.save_shopping_group_name);
        save_group_name_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //backendStorage = new MyBacckendStorageManager(getApplicationContext());
                String app_instance_id=getInstanceID();

                String group_name = group_name_txt.getText().toString();
                String user_name = user_name_txt.getText().toString();
                String user_phone_number = user_phone_number_txt.getText().toString();

                ShoppingGroupMember newShopping_groupMember = new ShoppingGroupMember();
                newShopping_groupMember.setInstanceId(app_instance_id);
                newShopping_groupMember.setDeviceId(deviceId);
                newShopping_groupMember.setUserName(user_name);
                newShopping_groupMember.setPhoneNumber(user_phone_number);
                newShopping_groupMember.setFirebaseDeviceToken(getFirebaseDeviceToken());
                ShoppingGroup newshoppingGroup = new ShoppingGroup();
                newshoppingGroup.setGroupName(group_name);
                //newShopping_groupMember.setShoppingGroup(newshoppingGroup);
                newshoppingGroup.addMemmber(newShopping_groupMember);
                ProductBackendHandlerService.startActionCreateNewShoppingGroup(getApplicationContext(), newshoppingGroup);
                //backendStorage.createNewShoppingGroup(newshoppingGroup);
                //hideSoftKeyBoard();
                myWindow.dismiss();
            }
        });

        myWindow.setContentView(popUpView);
        myWindow.setWidth(800);
        myWindow.setHeight(800);
        myWindow.setFocusable(true);

        myWindow.showAtLocation(popUpView, Gravity.CENTER_HORIZONTAL,0,0);
    }

    @Override
    public void showProductAmountPopUpwindow(Product product, final int gpPos, final int chPos, final View childView)
    {
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = layoutInflater.inflate(R.layout.product_amount_window, null, false);
        final Spinner productAmountSpinner = (Spinner) popUpView.findViewById(R.id.product_amount_spinner);
        Button product_amount_btn   = (Button) popUpView.findViewById(R.id.product_amount_ok_btn);
        final PopupWindow myWindow = new PopupWindow(this);

        String[] product_amount_arr = {"1","2","3","4","5","6","7","8","9"};
        ArrayAdapter product_amount_spinner_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,product_amount_arr );
        product_amount_spinner_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productAmountSpinner.setAdapter(product_amount_spinner_array_adapter);
        productAmountSpinner.setSelection(product.getProductAmount()-1);
        final Product productToUpdate = product;
        product_amount_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productToUpdate.setProductAmount((int) productAmountSpinner.getSelectedItemId()+1);
                MyStorageHandlerService.startActionUpdateProductInCart_TBL(getApplicationContext(), productToUpdate, gpPos, chPos, childView);
                //hideSoftKeyBoard();
                myWindow.dismiss();
            }
        });
        myWindow.setContentView(popUpView);
        myWindow.setWidth(500);
        myWindow.setHeight(450);
        myWindow.setFocusable(true);
        myWindow.showAtLocation(popUpView, Gravity.CENTER_HORIZONTAL,0,0);
    }

    @Override
    public void showNewProductPopUpwindow()
    {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popUpView = inflater.inflate(R.layout.add_new_product_pup_window,null,false);

        final EditText new_productName_txt = (EditText) popUpView.findViewById(R.id.productNmae_edit);
        final Spinner productTypeSpinner  = (Spinner)  popUpView.findViewById(R.id.productTypeSpinner);
        final Button addNewProductButton = (Button) popUpView.findViewById(R.id.addButton);
        final PopupWindow myWindow = new PopupWindow(this);

        new_productName_txt.setOnFocusChangeListener(new FocusChangedListener<EditText>(new_productName_txt,getApplicationContext(), new_productName_txt.getWindowToken()) {
        });
        Constants constant = Constants.getInstance(this);
        String[] spinnerStringArray = Constants.getProductTyPeSpinnerText(getApplicationContext());

        //ArrayAdapter productTypeSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),R.array.productTypeSpinner, android.R.layout.simple_spinner_item);
        ArrayAdapter productTypeSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, spinnerStringArray);
        productTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productTypeSpinner.setAdapter(productTypeSpinnerAdapter);


        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backendStorage = new MyBacckendStorageManager(getBaseContext());
                String new_productName = new_productName_txt.getText().toString();
                int productType = (int) productTypeSpinner.getSelectedItemId();
                if (!new_productName.equals("") && productType>0)
                {
                    Product newProduct = new Product(new_productName, 1, productType);
                    //storage = new MyStorage(getBaseContext());
                    //boolean inserted = storage.addProduct(newProduct);
                    /*addProductToList(newProduct); */
                    // comment this out later on because the additionals product should be saved only on local device not in Backend
                    // In Backend only verry common products are supposed to be saved by admin
                    //MyStorageHandlerService.startActionAddProductToCart_TBL(getApplicationContext(),newProduct);
                    if (isNetworkConnectionAvailable())
                        ProductBackendHandlerService.startActionSaveNewProduct(getApplicationContext(), newProduct);
                    else
                        showAlertDialog(R.layout.no_connection_available_in_action, 0);
                        //Toast.makeText(getApplicationContext(), "no connection available you are off line product could not be saved in backend", Toast.LENGTH_LONG).show();
                    hideSoftKeyBoard(new_productName_txt);
                    myWindow.dismiss();

                   /* //backendStorage.addProductToDB(pdct);
                    if (inserted) {
                        if (allProducts==null)
                        {
                            allProducts = new ArrayList<Product>();
                        }
                        allProducts.add(newProduct);
                        myWindow.dismiss();
                        Snackbar.make(get, new_productName + " "+getResources().getString(R.string.SAVED), Snackbar.LENGTH_SHORT).show();
                        refreshList(allProducts.indexOf(newProduct), "ADD_TO_LIST");
                    } else {
                        myWindow.dismiss();
                        Snackbar.make(getView(), new_productName + " " + getResources().getString(R.string.PRODUCT_ALREADY_EXISTS), Snackbar.LENGTH_LONG).show();
                    }
                    newProduct = null;
                    new_productName_txt.setText("");
                    new_productName_txt.setBackgroundColor(Color.WHITE);
                    updateListView();
                */
                }

                else if (new_productName =="" || new_productName.equals(""))
                {
                    Toast.makeText(getApplicationContext(), " "+getResources().getString(R.string.EMPTY_FIELD), Toast.LENGTH_SHORT).show();
                    new_productName_txt.setBackgroundColor(Color.RED);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), " "+getResources().getString(R.string.EMPTY_SPINNER), Toast.LENGTH_SHORT).show();
                }
            }
        });

        myWindow.setContentView(popUpView);
        myWindow.setWidth(550);
        myWindow.setHeight(450);
        myWindow.setFocusable(true);
        myWindow.showAtLocation(popUpView, Gravity.CENTER_HORIZONTAL,0,0);
    }

    @Override
    public String getInstanceID()
    {
        String appInstanceIDValue;
        //String appInstanceIDKey = "appUniqueKey";
        //mySharedPreferencesManager = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));

        appInstanceIDValue = mySharedPreferencesManager.getAppInstanceId(); //.readStringFromSharedPreferences(appInstanceIDKey);
        return appInstanceIDValue;
    }

    @Override
    public String getFirebaseDeviceToken()
    {
        String fireBbaseDeviceTokenValue;
        //String appInstanceIDKey = "appUniqueKey";
        //mySharedPreferencesManager = new MySharedPreferencesManager(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(this, getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));

        fireBbaseDeviceTokenValue = mySharedPreferencesManager.getFirebaseDeviceToken(); //.readStringFromSharedPreferences(appInstanceIDKey);
        return fireBbaseDeviceTokenValue;
    }

    public ShoppingGroupMember getShoppingGroupMember() {
        return this.shoppingGroupMember;
    }

    public void setShoppingGroupMember(ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroupMember = shoppingGroupMember;
    }

    public void hideSoftKeyBoard(EditText editText)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText() ) {
            //inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFeedBackFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
           /* if (sectionNumber==1) {
                ProductListFragment fragment = new ProductListFragment();
                //PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }
            else
            {
                */
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
            // }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =null;
            int resource_id=0;
            /*
            switch (this.ARG_SECTION_NUMBER)
            {
                case "1":
                    resource_id= R.layout.fragment_product_list;
                    rootView = inflater.inflate(resource_id, container, false);
                    break;
                default:
                    */
            resource_id = R.layout.fragment_my_shopping_cart;
            rootView = inflater.inflate(resource_id, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
              /*      break;
            }*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            if (position+1 == 1)
                return CartFragment.newInstance(null, null);
            else if(position+1 == 2)
                return ProductListFragment.newInstance(); //shoppingGroup, allProducts
                //else if(position+1 == 3)
             //   return AdminFragment.newInstance(null, null);
           // else if (position+1 == 3)return PlaceholderFragment.newInstance(position + 3);
            else return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.CART_PRODUCT_LIST);
                case 1:
                    return getResources().getText(R.string.PRODUCT_LIST);
                // case 2:
                 // return getResources().getText(R.string.ADMIN);
            }
            return null;
        }
    }
}

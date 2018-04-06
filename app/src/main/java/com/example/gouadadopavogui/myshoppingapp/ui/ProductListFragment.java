package com.example.gouadadopavogui.myshoppingapp.ui;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.adapter.MyAdapter;
import com.example.gouadadopavogui.myshoppingapp.adapter.MyExpandableProductListViewAdapter;
import com.example.gouadadopavogui.myshoppingapp.controller.ProductRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.dao.MyBacckendStorageManager;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ProductEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManager;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;
import com.example.gouadadopavogui.myshoppingapp.helpers.Validator;
import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;
import com.example.gouadadopavogui.myshoppingapp.services.CartBackendHandlerService;
import com.example.gouadadopavogui.myshoppingapp.services.ProductBackendHandlerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Product>> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SHAHRED_PREFERENCES_FILENAME = "com.example.gouadadopavogui.myshoppingapp.INSTANCE_ID";
    private static final String GIVEN_OWNER_ERROR = "Die von Ihnen angegebenen group owner Nummer existiert nicht bitte prüfen Sie die Nummer";

    public static ShoppingGroup shoppingGroup; // = new ShoppingGroupMember();
    public static List<Product> allProducts;
    public static List<String> headerList;

    public static boolean ADHESION_FAILED = false;
    public static String ownerNumber;
    public static ShoppingGroupMember newMember;

    public boolean INITIALIZING_USER = false;

    private MyStorage storage; // = new MyStorage(getActivity());
    private MyBacckendStorageManager backendStorage;
    private int[] headerListId;
    private RecyclerView myRecyclerView;
    private ExpandableListView myExpandableListView;
    private OnFragmentInteractionListener mListener;
    private ProductRetrofitClient mManager;
    //public static int passer;
    //private ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
    private ShoppingGroupMember shoppingGroupMember; // = new ShoppingGroupMember();
    private Cart currentShoppingCart;
    private Validator validator;

    //MySharedPreferencesManager sharedPreferences;
    //private  MySharedPreferencesManager mySharedPreferencesManager;
    private MySharedPreferencesManagerSingleton mySharedPreferencesManager;

    public static final String BACKEND_NOT_AVAILABLE_CATCHER="backend_not_available";
    private long shoppingGroupId = 0;
    //private static  String currentCartId ="";
    private long memberId = 0;
    private String appInstanceId="";
    public final int welcomeRequestIntent =1;

    public static int GROUP_TO_EXPAND=0;

    private MyAdapter myAdapter;
    private MyExpandableProductListViewAdapter myExpandableProductListViewAdapter;
    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of

     * this fragment using the provided parameters.

     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance() {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        //args.putSerializable(ARG_PARAM2, (Serializable) allProducts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /*
        if (mListener.isNetworkConnectionAvailable()) {
           ProductBackendHandlerService.startloadingProduct(getActivity());
        }
        else {
            allProducts = storage.getAllProducts();
            //new GetAllProductsTask().execute();
            Toast.makeText(getActivity(),  "failed to connect the server no connection available", Toast.LENGTH_LONG).show();
        }

        if (getArguments() != null) {
           // allProducts = (List<Product>) getArguments().getSerializable(ARG_PARAM2);
        }
        //mySharedPreferencesManager = new MySharedPreferencesManager(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        shoppingGroupId = mySharedPreferencesManager.getShoppingGroupId();
        memberId = mySharedPreferencesManager.getMemberId();
        appInstanceId = mySharedPreferencesManager.getAppInstanceId();
        */
        //updateListView();

        getActivity().getLoaderManager().initLoader(1, null,this);
    }

    public ShoppingGroupMember getShoppingGroupMember() {
        return shoppingGroupMember;
    }

    public void setShoppingGroupMember(ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroupMember = shoppingGroupMember;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        myExpandableListView = (ExpandableListView) rootView.findViewById(R.id.myExpandableProdctListView);


        //myRecyclerView = (RecyclerView)  rootView.findViewById(R.id.myRecyclerList);
       // myRecyclerView.setHasFixedSize(true);
        //myRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //Button addButton = (Button) rootView.findViewById(R.id.addButton);
        ImageButton addImgButton = (ImageButton) rootView.findViewById(R.id.addImgButton);

        /*
        final FloatingActionButton add_new_product_f_btn = (FloatingActionButton) rootView.findViewById(R.id.go_to_add_new_product_floating_btn);

        add_new_product_f_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showNewProductPopUpwindow();
            }
        });
        */
        refreshList(0, "initial");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().restartLoader(1,null,this);
/*
        if (mListener.isNetworkConnectionAvailable()) {
            ProductBackendHandlerService.startloadingProduct(getActivity());
       }
        else {
            storage = new MyStorage(getActivity());
            allProducts = storage.getAllProducts();
            //new GetAllProductsTask().execute();
            Toast.makeText(getActivity(),  "failed to connect the server no connection available", Toast.LENGTH_LONG).show();
        }
*/

        mySharedPreferencesManager =  MySharedPreferencesManagerSingleton.getInstance(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        String saved_device_lang = mySharedPreferencesManager.getDeviceLanguage();

        storage = new MyStorage(getActivity());
        String current_device_lang = Locale.getDefault().getLanguage();

        //load product list when device language change
        if(!current_device_lang.equals(saved_device_lang))
        {
            ProductBackendHandlerService.startloadingProduct(getActivity());
            CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroupId);
            storage.save_device_lang(Locale.getDefault().getLanguage());
            mySharedPreferencesManager.setDeviceLanguage(current_device_lang);
        }
        allProducts = storage.getAllProducts();
        updateListView();

        if(allProducts== null || (allProducts!= null && allProducts.size()==0)) {
            if (mListener.isNetworkConnectionAvailable()) {
                ProductBackendHandlerService.startloadingProduct(getActivity());
            } else {
                mListener.showAlertDialog(R.layout.no_connection_available, 0);
                //new GetAllProductsTask().execute();
                //Toast.makeText(getActivity(), "no connection available you are off line", Toast.LENGTH_LONG).show();
            }
        }



        if (getArguments() != null) {
            // allProducts = (List<Product>) getArguments().getSerializable(ARG_PARAM2);
        }
        //mySharedPreferencesManager = new MySharedPreferencesManager(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        shoppingGroupId = mySharedPreferencesManager.getShoppingGroupId();
        memberId = mySharedPreferencesManager.getMemberId();
        appInstanceId = mySharedPreferencesManager.getAppInstanceId();

        if (mListener.isNetworkConnectionAvailable()) {
           // if(!INITIALIZING_USER)
                ProductBackendHandlerService.startActionGetMemberShoppingGroup(getActivity(), mListener.getInstanceID());
            //mListener.showAlertDialog(R.layout.no_connection_available, 0);
        }
        else {
            mListener.showAlertDialog(R.layout.no_connection_available, 0);
            //Toast.makeText(getActivity(), "failled to connect the server no connection available", Toast.LENGTH_LONG).show();
        }
    }

   @Subscribe
    public void onGetMemberShoppingGroup(ShoppingGroupEvent shoppingGroupEvent)
    {
        shoppingGroup = shoppingGroupEvent.getShoppingGroup();
        if (shoppingGroupEvent.errorCode > 0) {
            switch (shoppingGroupEvent.getErrorCode())
            {
                case ErrorCodes.BACKEND_ERROR_GROUP_NOT_CREATED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_CREATED));
                    break;
                case ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                    break;
                case ErrorCodes.BACKEND_ERROR_ADHESION_FAILED:
                    newMember=shoppingGroupEvent.getShoppingGroupMember();
                    ownerNumber=shoppingGroupEvent.getGroupOwnerNumber();
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_ADHESION_FAILED));
                    break;
                case ErrorCodes.BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS:
                    newMember=shoppingGroupEvent.getShoppingGroupMember();
                    ownerNumber=shoppingGroupEvent.getGroupOwnerNumber();
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS));
                    break;
                case ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED));
                    break;
                default: break;
            }
        }
        else {
            shoppingGroupMember = shoppingGroupEvent.getShoppingGroupMember();
            shoppingGroup       = shoppingGroupEvent.getShoppingGroup();
            if (shoppingGroup == null && shoppingGroupMember == null) {
                INITIALIZING_USER = true;
                Intent initializationIntent = new Intent(getActivity(), MainActivity.class);
                initializationIntent.putExtra("APP-INSTANCE-ID", mListener.getInstanceID());
                initializationIntent.putExtra("APP-FIREBASE-INSTANCE-ID", mListener.getFirebaseDeviceToken());
                if (ADHESION_FAILED) {
                    initializationIntent.putExtra("ERROR_MESSAGE", GIVEN_OWNER_ERROR);
                    initializationIntent.putExtra("NEW_GROUP_MEMBER", newMember);
                    initializationIntent.putExtra("GIVEN_OWNER_NUMBER", (Serializable) ownerNumber);
                    Snackbar.make(getView(), R.string.GIVEN_OWNER_NOT_EXISTS, Snackbar.LENGTH_LONG).show();
                    ADHESION_FAILED = false;
                    newMember = null;
                    ownerNumber = "";
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.USER_NOT_EXISTS, Toast.LENGTH_LONG).show();
                }
                getActivity().startActivityForResult(initializationIntent, welcomeRequestIntent);
            } else {
                if (shoppingGroupId == 0) {
                    INITIALIZING_USER = true;
                    mySharedPreferencesManager.setShoppingGroupId(shoppingGroup.getGroupId());
                    mySharedPreferencesManager.setMemberId(shoppingGroupMember.getUserId());
                    if (shoppingGroupEvent.getCurrentCart() != null)
                        mySharedPreferencesManager.setCurrentCartId(shoppingGroupEvent.getCurrentCart().getCartId());
                    if(INITIALIZING_USER)
                        CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroup.getGroupId());
                }
                INITIALIZING_USER = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==welcomeRequestIntent)
        {
            if(resultCode == RESULT_OK) {
                ProductBackendHandlerService.startloadingProduct(getActivity());
            }
        }
    }

    @Subscribe
    public void onLoadAllProductEvent(ProductEvent productEvent)
    {
        try {
            allProducts = productEvent.getProductList();
            //initContent();
            Log.e("MESSAGE: ", "PRODUCT LIST LOADED " + "\n\n");

            updateListView();
        }catch (EventBusException e) {Log.d( "ERROR", e.getMessage()+e.getStackTrace().toString());}
        catch (Exception e){e.getMessage();}
    }

    @Subscribe
    public void onLocalDBEvent(LocalDBEvent localDBEvent)
    {
        if (localDBEvent.productInserted())
        {
            if (allProducts==null)
            {
                allProducts = new ArrayList<Product>();
            }
            allProducts.add(localDBEvent.getProduct());
            GROUP_TO_EXPAND = localDBEvent.getgPos();
            getActivity().getLoaderManager().restartLoader(1,null,this);
            Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + getResources().getString(R.string.SAVED), Snackbar.LENGTH_SHORT).show();
            //refreshList(allProducts.indexOf(localDBEvent.getProduct().getProductName()), "ADD_TO_LIST");
            //refreshList(0, "ADD_TO_LIST");
        }
        else if (localDBEvent.productDeleted())
        {
            allProducts = storage.getAllProducts();
            GROUP_TO_EXPAND = localDBEvent.getgPos();
            getActivity().getLoaderManager().restartLoader(1,null,this);
            //refreshList(0, "DELETE");
            Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + getResources().getText(R.string.DELETED), Snackbar.LENGTH_SHORT).show();;
        }
        else if (localDBEvent.isProduct_list_updated())
        {
            getActivity().getLoaderManager().restartLoader(1,null,this);
        }
        /*
        else if (localDBEvent.getOperation() == LocalDBEvent.ARCHIVE_CURRENT_CART && localDBEvent.isArchived_current_cart())
        {
            getActivity().getLoaderManager().restartLoader(1,null,this);
        }
        */
        /*
        else if (localDBEvent.getOperation() == LocalDBEvent.ADD_NEW_PRODUCT && !localDBEvent.isProduct_updated() )
        {
            Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + " " + getResources().getString(R.string.PRODUCT_ALREADY_EXISTS), Snackbar.LENGTH_LONG).show();
        }
        */
        else if(localDBEvent.getErrorCode()>0)
        {
            TextView tv = (TextView)getView().findViewById(R.id.txt_no_connection);
            if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_GROUP_NOT_CREATED)
               mListener.showAlertDialog(R.layout.group_not_created, 0);
            else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_PRODUCT_ALREADY_EXISTS) {
                GROUP_TO_EXPAND = localDBEvent.getProduct().getProductType();
                getActivity().getLoaderManager().restartLoader(1,null,this);
                if (this.isVisible())
                    Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + " "
                            + getResources().getString(R.string.PRODUCT_ALREADY_EXISTS) + " "+
                            getResources().getString(R.string.IN) + " "+
                            Constants.getInstance(getActivity()).getProductTypeById(localDBEvent.getProduct().getProductType()),
                            Snackbar.LENGTH_LONG).show();
                //getActivity().getLoaderManager().restartLoader(1,null,this);
            }
            else if (localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_PRODUCT_NOT_SAVED)
            {
                mListener.showAlertDialog(R.layout.no_connection_available_in_action, 0);
            }
            else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_ADHESION_FAILED)
            {
                //tv.setText("adhesion_ failed");
                //mListener.showAlertDialog(R.layout.given_owner_not_exists, 0);
                ADHESION_FAILED = true;
            }
            else if (localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS)
            {
               // tv.setText(getResources().getString(R.string.GIVEN_OWNER_NOT_EXISTS));
                //mListener.showAlertDialog(R.layout.given_owner_not_exists, 0);
                ADHESION_FAILED = true;
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           /* if (intent.getExtras().getString("Action").equals("LOAD_PRODUCTS")) {
                allProducts = (List<Product>) intent.getExtras().get("allproduts");
            }
            else*/
            refreshList(intent.getExtras().getInt("position"), intent.getExtras().getString("Action"));
        }
    };

    private void refreshList(int position, String action)
    {
        GROUP_TO_EXPAND = position;
        switch (action) {
            case "DELETE":
                //allProducts.remove(position);
                //allProducts.
                //updateListView();
                //myAdapter=new MyAdapter(allProducts, mListener);
                //myRecyclerView.setAdapter(myAdapter);
                break;
            case "ADD_TO_CART":
                //allProducts.remove(position);
                //updateListView();
                //myAdapter=new MyAdapter(allProducts, mListener);
                //myRecyclerView.setAdapter(myAdapter);
                break;
            case "SELECTION":
                if ((allProducts.get(position)).isProductSelected())
                    (allProducts.get(position)).deSelectProduct();
                else (allProducts.get(position)).selectProduct();
                break;
            case  "ADD_TO_LIST":
                getActivity().getLoaderManager().restartLoader(1,null,this);
                //  updateListView();
                break;
            default: updateListView(); break;
        }
    }

    public void updateListView()
    {
        if(allProducts != null)
        {
            //myAdapter=new MyAdapter(allProducts, mListener, getActivity());
            //myRecyclerView.setAdapter(myAdapter);
            storage = new MyStorage(getActivity());
            //new GetAllProductTypesTask().execute();
            headerListId = storage.getExistingProductTypes();
            myExpandableProductListViewAdapter = new MyExpandableProductListViewAdapter(getActivity(), headerListId, allProducts, mListener);
            myExpandableListView.setAdapter(myExpandableProductListViewAdapter);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getContext().registerReceiver(receiver, new IntentFilter (MyShoppingCartUI.BROADCAST_SENDER));
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(receiver);
        // getContext().unregisterReceiver(backendHandlerServiceReceiver);
    }

    @Override
    public Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Product>>(getActivity()) {
            {
                onContentChanged();
            }
            @Override
            protected void onStartLoading() {
                if (takeContentChanged())
                    forceLoad();
            }
            @Override
            public List<Product> loadInBackground() {
                return allProducts = new MyStorage(getActivity()).getAllProducts();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        myExpandableProductListViewAdapter = new MyExpandableProductListViewAdapter(getActivity(), headerListId, allProducts, mListener);
        myExpandableListView.setAdapter(myExpandableProductListViewAdapter);
        if (GROUP_TO_EXPAND > 0) {
            if (!myExpandableListView.isGroupExpanded(GROUP_TO_EXPAND-1))
                myExpandableListView.expandGroup(GROUP_TO_EXPAND-1);
            //else
               // myExpandableListView.collapseGroup(GROUP_TO_EXPAND-1);

            GROUP_TO_EXPAND=0;
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {
        MyExpandableProductListViewAdapter nuAdapter =null;
        myExpandableListView.setAdapter(nuAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void deleteProduct(String productName, int position);
        void addProductToCart(Product product, int position);
        void setProductIsChecked(Product product, boolean ischecked, int position);
        String getInstanceID();
        void showPopUpwindow();
        boolean isNetworkConnectionAvailable();
        String getFirebaseDeviceToken();
        void showNewProductPopUpwindow();
        void showProductAmountPopUpwindow(Product product, int gpos, int chpos, View childView);
        void addProductToList(Product newProduct);
        void showAlertDialog(int layoutId, int negativeButton);
    }
}

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.gouadadopavogui.myshoppingapp.adapter.MyCartListAdapter;
import com.example.gouadadopavogui.myshoppingapp.adapter.MyExpandableCartListViewAdapter;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.events.CartEvent;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.helpers.MessageProvider;
import com.example.gouadadopavogui.myshoppingapp.helpers.MySharedPreferencesManagerSingleton;
import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.CartProducts;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.services.CartBackendHandlerService;
import com.example.gouadadopavogui.myshoppingapp.services.MyStorageHandlerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnCartFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Product>>
        {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MyStorage storage;
    private ListView myListView;
    private List<Product> allProducts;
    private View rootView;
    private MyCartListAdapter myAdapter;

    private int[] headerListId;
    private ExpandableListView myCartProductsExpandableListView;
    private MyExpandableCartListViewAdapter myExpandableCartListViewAdapter;

    private static Cart currentShoppingCart;
    private ShoppingGroup shoppingGroup;

    private OnCartFragmentInteractionListener mListener;
    private  long shoppingGroupId = 0;
    private  long currentCartId = 0;
    private  long memberId = 0;
    private static String appInstanceId="";

            public static int GROUP_TO_EXPAND=0;
            //private  MySharedPreferencesManager mySharedPreferencesManager;
    private MySharedPreferencesManagerSingleton mySharedPreferencesManager;
            public static final String BACKEND_NOT_AVAILABLE_CATCHER="backend_not_available";

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCartFragmentInteractionListener) {
            mListener = (OnCartFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        lodProperties();
        getActivity().getLoaderManager().initLoader(0,null, this);

        //CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroupId);
       //updateView();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        myCartProductsExpandableListView = (ExpandableListView) rootView.findViewById(R.id.myExpandableCartProdctListView);

        //myListView = (ListView) rootView.findViewById(R.id.list);
        //allProducts=null;
        //myAdapter = new MyCartListAdapter(this.getContext(), allProducts, mListener);
        //myListView.setAdapter(myAdapter);

        storage = new MyStorage(getActivity());
        headerListId = storage.getExistingProductTypes();
        myExpandableCartListViewAdapter = new MyExpandableCartListViewAdapter(getActivity(), headerListId, allProducts, mListener);

        FloatingActionButton sendButton = (FloatingActionButton) rootView.findViewById(R.id.publish_currentCart_floating_btn);
        //Button sendButton = (Button) rootView.findViewById(R.id.publish_currentCart);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCartId == 0) {
                    currentShoppingCart = new Cart();
                    currentShoppingCart.setShoppingGroup(shoppingGroup);
                    currentShoppingCart.setStatus(true);
                    //currentShoppingCart.setProductsInCart(allProducts);
                    if (mListener.isNetworkConnectionAvailable())
                       CartBackendHandlerService.startActionCreateNew(getActivity(),shoppingGroupId,currentShoppingCart);
                    else
                        mListener.showAlertDialog(R.layout.no_connection_available_in_action, 0);
                        //Toast.makeText(getActivity(), "no connection available you are off line", Toast.LENGTH_LONG).show();
                }

                // Save groupId and Current cart Id in Shared preferences
                //currentShoppingCart.setProductsInCart(allProducts);
                //List<Product> currentAllPdtList = new ArrayList<Product>();
                if (saveCurrentCar()) {
                    if (mListener.isNetworkConnectionAvailable()) {
                        if (currentShoppingCart != null)
                            currentCartId = currentShoppingCart.getCartId();
                        CartBackendHandlerService.startActionSaveCurrentCart(getActivity(), shoppingGroupId,
                                currentCartId, memberId, allProducts);
                    }
                    //else mListener.showAlertDialog(R.layout.no_connection_available_in_action, 0); //Toast.makeText(getActivity(), "no connection available you are off line", Toast.LENGTH_LONG).show();

                }
            }
        });
        //refreshList("initial",0,null);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        storage = new MyStorage(getActivity());
        getContext().registerReceiver(receiver, new IntentFilter(MyShoppingCartUI.BROADCAST_SENDER));
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shoppingGroupId ==0)
            lodProperties();
        if (shoppingGroupId !=0)
        CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroupId);
    }

    @Subscribe
    public void onLoadCurrentCartEvent(CartEvent cartEvent)
    {
        if (cartEvent.getErrorCode() > 0 ) {
            lodProperties();
            switch (cartEvent.getErrorCode())
            {
                case ErrorCodes.BACKEND_ERROR_CART_NOT_CREATED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_CREATED));
                    break;
                case ErrorCodes.BACKEND_ERROR_CURRENT_CART_NOT_LOADED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_CURRENT_CART_NOT_LOADED));
                    break;
                case ErrorCodes.BACKEND_ERROR_CART_NOT_SAVED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_SAVED));
                    break;
                case ErrorCodes.BACKEND_ERROR_CART_NOT_ARCHIVED:
                    EventBus.getDefault().post(new LocalDBEvent(ErrorCodes.BACKEND_ERROR_CART_NOT_ARCHIVED));
                    break;
                default: break;
            }
        }
        else {
            if (cartEvent.getCurrentCart() != null) {
                currentShoppingCart = cartEvent.getCurrentCart();
                if (currentShoppingCart != null && currentShoppingCart.getCartId() != 0) {
                    //check if newer Cart has been created by another user, if so then update shared preferences, save new cartid
                    if (currentCartId != 0 && currentCartId != currentShoppingCart.getCartId()) {
                        currentCartId = currentShoppingCart.getCartId();
                        mySharedPreferencesManager.setCurrentCartId(currentCartId);
                        MyStorageHandlerService.startActionArchiveCurrentCart(getActivity());
                        //getActivity().getLoaderManager().restartLoader(0,null, this);
                        //mListener.showAlertDialog(); // need to create new dialog for this
                    } else if (currentCartId == 0) // IF user did not had currentcart, save newly created cart by another user in sharedpreferences
                    {
                        currentCartId = currentShoppingCart.getCartId();
                        mySharedPreferencesManager.setCurrentCartId(currentCartId);
                    }
                    //if (currentShoppingCart.getProductsInCart() != null)
                    //  allProducts = currentShoppingCart.getProductsInCart();
                    //allProducts = storage.load_cart(); //currentShoppingList; //
                }

                if (currentShoppingCart.getProductsInCart() != null && currentShoppingCart.getProductsInCart().size() > 0) {
                    final List<Product> currentShoppingList = new ArrayList<Product>(); //= cartEvent.getCartProducts();
                    Product tmpPdt = new Product();
                    for (CartProducts cartPdct : currentShoppingCart.getProductsInCart()) {
                        tmpPdt = cartPdct.getPrimaryKey().getProduct();
                        tmpPdt.setInCart(cartPdct.isProductInCart());
                        tmpPdt.setProductAmount(cartPdct.getProductAmount());
                        tmpPdt.setProductId(cartPdct.getProduct().getProductId());
                        currentShoppingList.add(tmpPdt);
                    }
                    storage.put_products_into_cart(currentShoppingList);
                    allProducts = storage.load_cart(); //currentShoppingList; //

                    //myAdapter = new MyCartListAdapter(getActivity(), allProducts, mListener);
                    //myListView.setAdapter(myAdapter);
                    refreshList("initial", 0, null);
                }
                if (!MessageProvider.message.equals("") && MessageProvider.message.equals(getResources().getString(R.string.CHANGES_SAVED_IN_BACKEND)))
                    Snackbar.make(rootView, getResources().getText(R.string.CHANGES_SAVED_IN_BACKEND_TEXT), Snackbar.LENGTH_LONG).show();
                else if (!MessageProvider.messageCode.equals("") && MessageProvider.messageCode.equals("ERROR") && MessageProvider.message.equals(getResources().getString(R.string.CHANGES_COULD_NOT_BE_SAVED_IN_BACKEND)))
                    Snackbar.make(rootView, getResources().getText(R.string.CHANGES_COULD_NOT_BE_SAVED_IN_BACKEND_TEXT), Snackbar.LENGTH_LONG).show();
            }
        }
       /*
        else if (cartEvent.getCartProducts()!=null && cartEvent.getCartProducts().size()>0)
        {
            final List<Product> currentShoppingList = cartEvent.getCartProducts();

            storage.put_products_into_cart(currentShoppingList);
            allProducts = storage.load_cart(); //currentShoppingList; //

            //myAdapter = new MyCartListAdapter(getActivity(), allProducts, mListener);
            //myListView.setAdapter(myAdapter);
            refreshList("initial", 0, null);
        }
        */
        //refreshList("initial", 0, null);

    }

            @Subscribe
            public void onLocalDBEventInCart(LocalDBEvent localDBEvent)
            {
                if (localDBEvent.isProduct_list_updated())
                {
                    getActivity().getLoaderManager().restartLoader(0,null,this);
                }
                else if (localDBEvent.getOperation() == LocalDBEvent.ARCHIVE_CURRENT_CART && localDBEvent.isArchived_current_cart())
                {
                    getActivity().getLoaderManager().restartLoader(0,null,this);

                    CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroupId);
                }
                else if (localDBEvent.isProduct_updated())
                {
                    GROUP_TO_EXPAND = localDBEvent.getgPos();
                    getActivity().getLoaderManager().restartLoader(0,null,this);
                }
                else if (localDBEvent.productInserted())
                {
                    GROUP_TO_EXPAND = localDBEvent.getgPos();
                    getActivity().getLoaderManager().restartLoader(1,null,this);
                    Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + getResources().getString(R.string.SAVED), Snackbar.LENGTH_SHORT).show();
                    //refreshList(allProducts.indexOf(localDBEvent.getProduct().getProductName()), "ADD_TO_LIST");
                    //refreshList(0, "ADD_TO_LIST");
                }
                else if(localDBEvent.isProduct_removed_from_cart())
                {
                   // .....
                }
                else if(localDBEvent.getErrorCode() > 0)
                {
                    if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_CART_NOT_CREATED)
                        mListener.showAlertDialog(R.layout.cart_not_created, 0);
                    else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_CURRENT_CART_NOT_LOADED)
                            mListener.showAlertDialog(R.layout.cart_not_loaded, 0);
                    else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_CART_NOT_SAVED)
                            mListener.showAlertDialog(R.layout.cart_not_saved, 0);
                    else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_CART_NOT_ARCHIVED)
                            mListener.showAlertDialog(R.layout.cart_not_archived, 0);
                    else if(localDBEvent.getErrorCode() == ErrorCodes.BACKEND_ERROR_PRODUCT_ALREADY_EXISTS) {
                        Constants constants = Constants.getInstance(getActivity());
                        GROUP_TO_EXPAND = localDBEvent.getProduct().getProductType();
                        getActivity().getLoaderManager().restartLoader(1,null,this);
                        Snackbar.make(getView(), localDBEvent.getProduct().getProductName() + " "
                                + getResources().getString(R.string.PRODUCT_ALREADY_EXISTS) +getString(R.string.IN)+
                                constants.getProductTypeById(localDBEvent.getProduct().getProductType()), Snackbar.LENGTH_LONG).show();
                    }

                }
            }

    public void updateView()
    {
        //storage = new MyStorage(this.getContext());
        allProducts = storage.load_cart();

        if(allProducts != null)
        {
            //myAdapter = new MyCartListAdapter(this.getContext(), allProducts, mListener);
            myExpandableCartListViewAdapter = new MyExpandableCartListViewAdapter(getActivity(), headerListId, allProducts,mListener);

            //myListView.setAdapter(myAdapter);
           /* if (currentShoppingCart == null && currentCartId != 0)
            {
                currentShoppingCart = new Cart();
                currentShoppingCart.setProductsInCart(allProducts);
                currentShoppingCart.setCartId(currentCartId);
            }
            */
        }
    }

    private void lodProperties() {
       // mySharedPreferencesManager = new MySharedPreferencesManager(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));
        mySharedPreferencesManager = MySharedPreferencesManagerSingleton.getInstance(getActivity(), getResources().getString(R.string.SHAHRED_PREFERENCES_FILENAME));

        shoppingGroupId = mySharedPreferencesManager.getShoppingGroupId();
        currentCartId = mySharedPreferencesManager.getCurrentCartId();
        memberId = mySharedPreferencesManager.getMemberId();
        appInstanceId = mySharedPreferencesManager.getAppInstanceId();
    }

    private boolean saveCurrentCar() {
        boolean saved = false;
        if (allProducts !=null) {
            if (allProducts.size() > 0) {


                // this is needed because the EditTextField for ProductAmount is not observed
                // changes done there in between may get lost while saving the car
                /*
                for (int i = 0; i < allProducts.size(); i++) {
                    EditText editText = (EditText) myListView.getChildAt(i).findViewById(R.id.productAmount);
                    allProducts.get(i).setProductAmount(Integer.parseInt(editText.getText().toString()));
                }
                */
                saved = true;
            }
        }
        return saved;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCartFragmentInteraction(uri);
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Product product = null;
            if (intent.getExtras().getInt("productAmount") > 0 || intent.getExtras().getString("productName")!= null)
            {
                product = new Product(intent.getExtras().getString("productName"), intent.getExtras().getInt("productAmount"));
                product.setProductId(intent.getExtras().getLong("productId"));
            }
            else if (intent.getExtras().getInt("productAmount") > 0 && intent.getExtras().getString("productName") == null)
            {
                product = new Product();
                product.setProductAmount(intent.getExtras().getInt("productAmount"));

            }
            else {
                product = (Product) intent.getExtras().get("product");
            }
            refreshList(intent.getExtras().getString("Action"), intent.getExtras().getInt("position"), product);
        }
    };

    private void refreshList(String action, int position, Product pdct) {
        GROUP_TO_EXPAND = position;
        switch (action) {
            case "DELETE_FROM_CART":
                //allProducts.remove(position);
                //allProducts.remove(pdct);
                if (allProducts.size() == 0)
                {
                    currentShoppingCart = null;
                    mySharedPreferencesManager.setCurrentCartId(0L);
                    CartBackendHandlerService.startActionArchiveCurrentCart(getActivity(), shoppingGroupId, currentCartId, memberId);
                }
                break;

            case "ADD_TO_CART":
                if (allProducts == null)
                    allProducts = new ArrayList<Product>();
                allProducts.add(pdct);
                if (allProducts.size() == 1 && currentCartId == 0) //L && currentShoppingCart == null)
                {
                    //CartBackendHandlerService.startActionLoadCurrentCart(getActivity(), shoppingGroupId);
                    currentShoppingCart = new Cart();
                    currentShoppingCart.setStatus(true);
                    //currentShoppingCart.setProductsInCart(allProducts);
                    if (shoppingGroupId ==0)
                        lodProperties();
                    CartBackendHandlerService.startActionCreateNew(getActivity(), shoppingGroupId, currentShoppingCart);
                }
                break;
            case "SHOPPING_DONE":
                mListener.showAlertDialog(R.layout.shopping_done_alert, R.string.NEGATIVE_BUTTON);
                break;
            default:
                break;
        }
        getActivity().getLoaderManager().restartLoader(0,null, this);

        //myExpandableCartListViewAdapter = new MyExpandableCartListViewAdapter(getActivity(), headerListId, allProducts, mListener);
            //myCartProductsExpandableListView.setAdapter(myExpandableCartListViewAdapter);
            //myAdapter = new MyCartListAdapter(this.getContext(), allProducts, mListener);
            //myListView.setAdapter(myAdapter);
      }

    @Override
    public void onPause()
    {
        super.onPause();
        if (saveCurrentCar())
        {
            //CartBackendHandlerService.startActionSaveCurrentCart(getActivity(), shoppingGroupId, currentCartId, memberId, allProducts);
            allProducts = null;
            List<Product> allProducts_new = new ArrayList<Product>();
            for (int k=0; k < myExpandableCartListViewAdapter.getGroupCount(); k++)
            {
                if (myExpandableCartListViewAdapter.getChildrenCount(k)>0)
                    allProducts_new.addAll( myExpandableCartListViewAdapter.getGroup(k));
        }
        storage.cleanUpCart();
        storage.put_products_into_cart(allProducts_new);
            Log.e("MESSAGE: ", "CURRENT CART SAVED BEFORE PAUSING");
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        getContext().unregisterReceiver(receiver);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                return allProducts = new MyStorage(getActivity()).load_cart();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> loader, List<Product> data) {
        myExpandableCartListViewAdapter = new MyExpandableCartListViewAdapter(getActivity(),headerListId,allProducts,mListener);
        myCartProductsExpandableListView.setAdapter(myExpandableCartListViewAdapter);
        if (GROUP_TO_EXPAND > 0) {
            if (!myCartProductsExpandableListView.isGroupExpanded(GROUP_TO_EXPAND-1))
                myCartProductsExpandableListView.expandGroup(GROUP_TO_EXPAND-1);
            //else
             //   myCartProductsExpandableListView.collapseGroup(GROUP_TO_EXPAND-1);
            GROUP_TO_EXPAND=0;

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Product>> loader) {
        MyExpandableCartListViewAdapter nuAdapter =null;
        myCartProductsExpandableListView.setAdapter(nuAdapter);
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
    public interface OnCartFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCartFragmentInteraction(Uri uri);
        void deleteProductFromCart(String productName, int gpPos, int position);
        //void updateProductAmount(int newProductAmount, int position, String productName);
        boolean isNetworkConnectionAvailable();
        void deleteProductFromCart(Product finalPdct, int groupPosition);

        void archivesCurrentCart();

        void showAlertDialog(int layoutId, int negativeButton);

        void updateProductInCart(Product productToUpdate, int gPos, int chldPos);
        void showProductAmountPopUpwindow(Product product, int gpPos, int chPos, View childView);

        void removeProductFromCart(Product productToUpdate, int gpPos, int chPos);
        //void saveNewProductAmountInList(int position, int productAmount);
    }
}

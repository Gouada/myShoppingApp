package com.example.gouadadopavogui.myshoppingapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.CartFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gouadadopavogui on 10.03.2017.
 */

public class MyExpandableCartListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private static List<Product> shoppingList;
    private CartFragment.OnCartFragmentInteractionListener mListener;

    private  List<String> headerList;
    private  int[] headerListId;
    //List of products  current shopping list
    private  List<Product> cartProductList;
    private  HashMap<String, List<Product>> sortedProductsHashMap = new HashMap<String, List<Product>>();
    //list of products already in cart
    private  HashMap<String, Integer> allProductInCart = new HashMap<String, Integer>();
    private MyStorage storage;
    private int numberOfProductsAlreadyInCart = 0;
    //private static int numberberOfProductsOnShoppingList = 0;


    public MyExpandableCartListViewAdapter(Context context, int[] headerListId, List<Product> cartProductList, CartFragment.OnCartFragmentInteractionListener mListener ) {
        this.context = context;
        this.mListener = mListener;
        this.headerListId = headerListId;
        this.cartProductList = cartProductList;
        //this.sortedProductsHashMap = sortedProductsHashMap;
        sortProductByType(context);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return headerList == null? 0 : headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sortedProductsHashMap.get(headerList.get(groupPosition)) == null? 0 : sortedProductsHashMap.get(headerList.get(groupPosition)).size();
    }

    @Override
    public List<Product> getGroup(int groupPosition) {
        return sortedProductsHashMap.get(headerList.get(groupPosition));
    }

    @Override
    public Product getChild(int groupPosition, int childPosition) {
        return sortedProductsHashMap.get(headerList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater groupLayoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            convertView = groupLayoutInflater.inflate(R.layout.separator_element,null);
        }
        onBindGroup(convertView, groupPosition);
        return convertView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater childLayoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            convertView = childLayoutInflater.inflate(R.layout.cart_list_element,null);
        }
        bindChild(convertView,groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //this methode is to prepare the expandable view on shopping list tab
    public void sortProductByType(Context context) {
        this.headerList = new ArrayList<String>();
        int counter=0;
        int groupPosition=-1;
        for (int j = 0; j < this.headerListId.length; j++) {
            switch (this.headerListId[j]) {
                case 1:
                    headerList.add(context.getResources().getString(R.string.MILK_PRODUCTS));
                    groupPosition++;
                    break;
                case 2:
                    headerList.add(context.getResources().getString(R.string.BREAKFAST_PRODUCTS));
                    groupPosition++;
                    break;
                case 3:
                    headerList.add(context.getResources().getString(R.string.REFRIGERATOR_PRODUCTS));
                    groupPosition++;
                    break;
                case 4:
                    headerList.add(context.getResources().getString(R.string.MEAT_PRODUCTS));
                    groupPosition++;
                    break;
                case 5:
                    headerList.add(context.getResources().getString(R.string.OBST_PRODUCTS));
                    groupPosition++;
                    break;
                case 6:
                    headerList.add(context.getResources().getString(R.string.FRUIT_PRODUCTS));
                    groupPosition++;
                    break;
                case 7:
                    headerList.add(context.getResources().getString(R.string.DRINKS_PRODUCTS));
                    groupPosition++;
                    break;
                case 8:
                    headerList.add(context.getResources().getString(R.string.SWEETY_PRODUCTS));
                    groupPosition++;
                    break;
                case 9:
                    headerList.add(context.getResources().getString(R.string.TOILET_PRODUCTS));
                    groupPosition++;
                    break;
                case 10:
                    headerList.add(context.getResources().getString(R.string.BABY_PRODUCTS));
                    groupPosition++;
                    break;
                case 11:
                    headerList.add(context.getResources().getString(R.string.OTHERS_PRODUCTS));
                    groupPosition++;
                    break;
                default:break;
            }
            if (this.cartProductList != null && this.cartProductList.size()>0) {
                List<Product> currentPdtList = new ArrayList<Product>();
                counter = 0;
                for (Product product : this.cartProductList) {
                    if (product.getProductType() == this.headerListId[j]) { // sort product by product type on the shopping list
                        currentPdtList.add(product);
                        if (product.isInCart())
                        {
                            counter++;
                        }
                    }
                }
                //adding a sorted product list to hashmap
                sortedProductsHashMap.put(headerList.get(groupPosition), currentPdtList);
                allProductInCart.put(headerList.get(groupPosition), counter);
                numberOfProductsAlreadyInCart = numberOfProductsAlreadyInCart + counter;
                currentPdtList = null;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void bindChild(View view, final int groupPosition, int childPosition) {
        final MyChildViewHolder holder = new MyChildViewHolder(view);
        Product pdct = null;
        pdct = sortedProductsHashMap.get(headerList.get(groupPosition)).get(childPosition);
        holder.mProductName.setText(pdct.getProductName());
        holder.mProductAmount.setText(String.valueOf(pdct.getProductAmount()));
        holder.mDelButton.setBackgroundColor(Color.TRANSPARENT);
        if (sortedProductsHashMap.get(headerList.get(groupPosition)).get(childPosition).isInCart())
        {
            holder.mProductName.setTextColor(Color.RED);
            holder.mProductName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.mProductName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.mProductName.setTextColor(Color.BLACK);
            //holder.mProductName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            //holder.mProdctName.setFontFeatureSettings();
            //holder.mDelButton.setBackgroundColor(context.getResources().getColor(R.color.btnColor));
//            holder.mDelButton.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.btnColor)));
        }
        final int chPos = childPosition;
        final int gpPos = groupPosition;
        final MyChildViewHolder holderf = holder;
        final Product finalPdct = sortedProductsHashMap.get(headerList.get(gpPos)).get(chPos);

        holder.mProductName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

        /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            */
                if (!(sortedProductsHashMap.get(headerList.get(gpPos)).get(chPos).isInCart()))  // if product is not already cart
                {
                    int z = allProductInCart.get(headerList.get(gpPos)) + 1; // increment the neúmber of product that are already in cart
                    allProductInCart.remove(headerList.get(gpPos));  // remove the list of product of this type
                    allProductInCart.put(headerList.get(gpPos), z);  // add the list of product of this type with the incremented amount by 1
                    numberOfProductsAlreadyInCart++;
                    (sortedProductsHashMap.get(headerList.get(gpPos))).get(chPos).setInCart(true);
                    holderf.mProductName.setTextColor(Color.RED);
                    holderf.mProductName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    Product productToUpdate = (sortedProductsHashMap.get(headerList.get(gpPos))).get(chPos);
                    //holder.mProdctName.setFontFeatureSettings();
                    //               holder.mDelButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    if (productToUpdate != null) {
                        productToUpdate.setInCart(true);
                        mListener.updateProductInCart(productToUpdate, gpPos, chPos);
                    }
                    //holderf.mDelButton.setClickable(false);
                    closeProductType(gpPos, v);
                    if (isShoppingDone()) {
                        mListener.archivesCurrentCart();
                    }
                }
                else
                {  // Product is already in cart it will get removed from cart
                    int z = allProductInCart.get(headerList.get(gpPos))-1;
                    allProductInCart.remove(headerList.get(gpPos));
                    allProductInCart.put(headerList.get(gpPos), z);
                    numberOfProductsAlreadyInCart--;
                    (sortedProductsHashMap.get(headerList.get(gpPos))).get(chPos).setInCart(false);
                    holderf.mProductName.setTextColor(Color.BLACK);
                    holderf.mProductName.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    Product productToUpdate = (sortedProductsHashMap.get(headerList.get(gpPos))).get(chPos);
                    if (productToUpdate!=null) {
                        productToUpdate.setInCart(false);
                        mListener.removeProductFromCart(productToUpdate, gpPos, chPos);
                    }
                }
               //mListener.deleteProductFromCart(finalPdct);
                return true;
            }
        });
        final View temporalView = view;
        holder.mProductAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showProductAmountPopUpwindow((sortedProductsHashMap.get(headerList.get(gpPos))).get(chPos), gpPos, chPos, temporalView);
            }
        });
        holder.mDelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteProductFromCart(finalPdct, gpPos+1);
            }
        });
    }

   /*
    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe
    public void onLocalDBEventProductAmountChanged(LocalDBEvent localDBEvent)
    {
        if (localDBEvent.getOperation() == "update_product") {
           // if (localDBEvent.getgPos() > 0 && localDBEvent.getChPos() > 0) {
            int gPos = localDBEvent.getgPos();
            int chPos = localDBEvent.getChPos();
            Product product = localDBEvent.getProduct();
            View childView = localDBEvent.getChildView();
            updateChildElementView(gPos, chPos, product, childView);
           // }
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateChildElementView(int gPos, int chPos, Product product, View childView)
    {
        MyExpandableCartListViewAdapter.MyChildViewHolder holder = new MyExpandableCartListViewAdapter.MyChildViewHolder(childView);
        //Toast.makeText(context, holder.mProductName.getText().toString(), Toast.LENGTH_SHORT).show();
        //Log.e("HHHH", holder.mProductName.getText().toString() + "  ....... " + holder.mProductAmount.getText() + " #####"+product.getProductAmount());
        holder.mProductAmount.setText(Integer.toString(product.getProductAmount()));
        sortedProductsHashMap.get(headerList.get(gPos)).get(chPos).setProductAmount(product.getProductAmount());
       // EventBus.getDefault().unregister(this);
    }
*/
    public void onBindGroup(View groupView, int groupPosition)
    {
        MyExpandableCartListViewAdapter.MyGroupViewHolder holder = new MyExpandableCartListViewAdapter.MyGroupViewHolder(groupView);
        holder.groupName.setText(headerList.get(groupPosition));
        int z = 0;
        if (allProductInCart !=null && allProductInCart.size()>0)
            z = allProductInCart.get(headerList.get(groupPosition));
        if (z>= getChildrenCount(groupPosition))
        {
            holder.groupName.setBackgroundColor(Color.GRAY);
        }
        else
        {
            holder.groupName.setBackgroundColor(ContextCompat.getColor(context, R.color.btnColor));
        }
    }

    public void closeProductType(int groupPosition, View childView)
    {
        View groupView = (View) childView.getParent();
        View groupView1 =  getGroupView(groupPosition,true,null, (ViewGroup) childView.getParent());
        onBindGroup(groupView1, groupPosition);
        //this.getGroupView(groupPosition,true,null,child);
        /*MyExpandableCartListViewAdapter.MyGroupViewHolder holder = new MyExpandableCartListViewAdapter.MyGroupViewHolder(groupView1);

        int z = allProductInCart.get(headerList.get(groupPosition));
        if (z>= getChildrenCount(groupPosition))
        {
            holder.groupName.setBackgroundColor(Color.GRAY);
        }
        */
    }

    public int getNumberOfProductAlreadyInCart()
    {
        return allProductInCart.size();
    }

    public boolean isShoppingDone()
    {
        if (numberOfProductsAlreadyInCart >= cartProductList.size()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public class MyChildViewHolder {
        TextView mProductName;
        Button mProductAmount;
        //Button mDelButton;
        ImageButton mDelButton;

        MyChildViewHolder(View v)
        {
            mProductName = (TextView) v.findViewById(R.id.productName);
            mProductAmount = (Button) v.findViewById(R.id.productAmount);
            mDelButton = (ImageButton) v.findViewById(R.id.delImgButton);

        }
    }
    class MyGroupViewHolder
    {
        TextView groupName;
        public MyGroupViewHolder(View itemView) {
            this.groupName =  (TextView )itemView.findViewById(R.id.separator);
        }
    }
}

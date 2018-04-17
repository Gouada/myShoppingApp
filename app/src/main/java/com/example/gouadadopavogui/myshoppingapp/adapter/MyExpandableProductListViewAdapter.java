package com.example.gouadadopavogui.myshoppingapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.ProductListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by gouadadopavogui on 09.03.2017.
 */

public class MyExpandableProductListViewAdapter extends BaseExpandableListAdapter {

    private List<String> headerList;
    private int[] headerListId;
    private List<Product> productList;
    private HashMap<String, List<Product>> sortedProductsHashMap = new HashMap<String, List<Product>>();

    private ProductListFragment.OnFragmentInteractionListener mListener;
    private List<Product> cartProductList;
    private MyStorage storage;
    Context context;

    public MyExpandableProductListViewAdapter(Context context, int[] headerListId, List<Product> productList,
                                              ProductListFragment.OnFragmentInteractionListener mListener)
    {
        this.headerList = headerList;
        this.productList = productList;
        this.context = context;
        this.mListener = mListener;
        this.headerListId=headerListId;
        storage = new MyStorage(context);
        sortProductByType(context);
    }

    public void sortProductByType(Context context) {
        this.headerList = new ArrayList<String>();
        int groupPosition=-1;
        for (int j = 0; j < this.headerListId.length; j++) {
            switch (headerListId[j]) {
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
            if (this.productList != null && this.productList.size() > 0) {
                List<Product> currentPdtList = new ArrayList<Product>();
                for (Product product : this.productList) {
                    if (product.getProductType() == headerListId[j]) {
                        currentPdtList.add(product);
                    }
                }
                sortedProductsHashMap.put(headerList.get(groupPosition), currentPdtList);
                currentPdtList = null;
            }
        }
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return sortedProductsHashMap.get(headerList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sortedProductsHashMap.get(headerList.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
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

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null)
        {
            LayoutInflater childLayoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            convertView = childLayoutInflater.inflate(R.layout.list_element,null);
        }
        onBindChild(convertView, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean isProductInCart(Product product)
    {
        final boolean[] isInCart = {false};

        /*
        cartProductList = storage.load_cart();
        int j=0;
        for (Product cartPdct:cartProductList)
        {
            if (cartPdct.getProductId()==product.getProductId())
            {
                //product.selectProduct();
                isInCart = true;
            }
        }
        j++;


        class IsProductInCartCheckerClass extends AsyncTask<Product, Void, Boolean>
        {

            @Override
            protected Boolean doInBackground(Product... product) {
                return storage.isProductInCart(product[0]);

            }

            @Override
            protected void onPostExecute(Boolean values) {
                super.onPostExecute(values);
                isInCart[0] = values.booleanValue();
            }
        }
        */
        //AsyncTask isProductInCartTask
        //return isInCart[0];
        return storage.isProductInCart(product);
    }



    public void onBindChild(View view, final int groupPosition, int childPosition)
    {
        final MyChildViewHolder holder = new MyChildViewHolder(view);

        holder.product = sortedProductsHashMap.get(headerList.get(groupPosition)).get(childPosition);
        //holder.product = productList.get(childPosition);

        //if (holder.product.getProductAmount() > 0)
        holder.productAmount.setText(String.valueOf(holder.product.getProductAmount()));
        holder.productAmount.setContentDescription(holder.product.getProductName() + " amount");
        holder.productName.setText(holder.product.getProductName());
        holder.productName.setContentDescription(holder.product.getProductName());
        //holder.productId.setText(holder.product.getProductId());

        if (isProductInCart(holder.product)) {
   //         holder.isChecked.setChecked(true);
           // holder.isChecked.setClickable(false);
            holder.addButton.setBackgroundColor(Color.GRAY);
            holder.addButton.setImageDrawable(context.getResources().getDrawable(R.drawable.cart_off_m));
            holder.addButton.setClickable(false);
        }
        else
        {
            holder.addButton.setBackgroundResource(R.color.btnColor);
            holder.addButton.setImageDrawable(context.getResources().getDrawable(R.drawable.cart_m));
            holder.addButton.setClickable(true);
        }
        holder.addButton.setContentDescription(holder.product.getProductName() + " add button");
        holder.delButton.setContentDescription(holder.product.getProductName() + " del button");

        final int chdPos = childPosition;
        final int gpPos = groupPosition;
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.product.setProductAmount(Integer.parseInt(holder.productAmount.getText().toString()));
              //  holder.isChecked.setChecked(true);
                holder.addButton.setBackgroundColor(Color.GRAY);
                holder.addButton.setImageDrawable(context.getResources().getDrawable(R.drawable.cart_off_m));
                holder.addButton.setClickable(false);
                mListener.addProductToCart(holder.product, chdPos);
            }
        });

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortedProductsHashMap.get(headerList.get(gpPos)).remove(chdPos);
                mListener.deleteProduct(holder.product.getProductName(), gpPos+1);
            }
        });
    }

    public void onBindGroup(View groupView, int groupPosition)
    {
        final MyGroupViewHolder holder = new MyGroupViewHolder(groupView);
         holder.groupName.setText(headerList.get(groupPosition));
    }

    class MyChildViewHolder
    {
        Product product;
        TextView productName;
        Button productAmount;
        //CheckBox isChecked;
        ImageButton addButton;
        ImageButton delButton;
        public MyChildViewHolder(View itemView) {
            productName =  (TextView )itemView.findViewById(R.id.productName);
            productAmount =  (Button) itemView.findViewById(R.id.productAmountL);
            //productAmount.setEditable(false);
          //  isChecked = (CheckBox) itemView.findViewById(R.id.checkbox);
            addButton = (ImageButton) itemView.findViewById(R.id.addImgButton);
            delButton = (ImageButton) itemView.findViewById(R.id.delImgButton);
            // product = new Product(productName, productAmount);

        }
    }

    class MyGroupViewHolder
    {
        TextView groupName;
        public MyGroupViewHolder(View itemView) {
            groupName =  (TextView )itemView.findViewById(R.id.separator);
        }
    }
}

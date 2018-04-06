package com.example.gouadadopavogui.myshoppingapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.dao.MyStorage;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.ProductListFragment;

import java.util.List;

/**
 * Created by GouadaDopavogui on 12.09.2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private static List<Product> productList;
    private ProductListFragment.OnFragmentInteractionListener mListener;
    private List<Product> cartProductList;
    private MyStorage storage;
    public MyAdapter(List<Product> productList, ProductListFragment.OnFragmentInteractionListener mListener, Context context) {
        this.productList = productList;
        this.mListener = mListener;
        storage = new MyStorage(context);
        //cartProductList = storage.load_cart();
/*
        int i =0;
        for (Product pdct:productList)
        {
            for (Product cartPdct:cartProductList)
            {
                if (cartPdct.getProductId()==pdct.getProductId())
                {
                    productList.get(i).selectProduct();
                    Log.e("..........", "i: "+i +"  " + pdct.getProductId()+"  "  +  cartPdct.getProductId());
                }
            }
            i++;
        }
        */
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.product = productList.get(position);

        //if (holder.product.getProductAmount() > 0)
        holder.productAmount.setText(String.valueOf(holder.product.getProductAmount()));
        holder.productName.setText(holder.product.getProductName());
        //holder.productId.setText(holder.product.getProductId());

        if (isProductInCart(holder.product)) {
            holder.isChecked.setChecked(true);
            holder.isChecked.setClickable(false);
            holder.addButton.setBackgroundColor(Color.GRAY);
            holder.addButton.setClickable(false);
        }
        else
        {
            holder.isChecked.setChecked(false);
            holder.isChecked.setClickable(false);
            holder.addButton.setBackgroundResource(R.color.btnColor);
            holder.addButton.setClickable(true);
        }

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.product.setProductAmount(Integer.parseInt(holder.productAmount.getText().toString()));
                holder.isChecked.setChecked(true);
                holder.addButton.setBackgroundColor(Color.GRAY);
                holder.addButton.setClickable(false);

                mListener.addProductToCart(holder.product, position);
            }
        });

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteProduct(holder.product.getProductName(), position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public boolean isProductInCart(Product product)
    {
        boolean isInCart = false;
        cartProductList = storage.load_cart();
        int j=0;
        for (Product cartPdct:cartProductList)
            {
                if (cartPdct.getProductId()==product.getProductId())
                {
                    //product.selectProduct();
                    Log.e("..........", "i: "+j +"  " + product.getProductId()+"  "  +  cartPdct.getProductId());
                    isInCart = true;
                }
            }
            j++;
            return isInCart;

    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        Product product;
        TextView productName;
        Button productAmount;
        CheckBox isChecked;
        Button addButton;
        ImageButton delButton;
        View productView;
        public ViewHolder(View itemView) {
            super(itemView);
            productName =  (TextView )itemView.findViewById(R.id.productName);
            productAmount =  (Button) itemView.findViewById(R.id.productAmountL);
            isChecked = (CheckBox) itemView.findViewById(R.id.checkbox);
            addButton = (Button) itemView.findViewById(R.id.addButton);
            delButton = (ImageButton) itemView.findViewById(R.id.delImgButton);
           // product = new Product(productName, productAmount);

        }

    }


}

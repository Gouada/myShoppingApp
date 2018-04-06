package com.example.gouadadopavogui.myshoppingapp.adapter;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gouadadopavogui.myshoppingapp.R;
import com.example.gouadadopavogui.myshoppingapp.helpers.TextChangedListener;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.ui.CartFragment;

import java.util.List;

/**
 * Created by gouadadopavogui on 19.09.2016.
 */
public class MyCartListAdapter extends ArrayAdapter<Product>
{
    private Context context;
    private List<Product> shoppingList;
    private CartFragment.OnCartFragmentInteractionListener mListener;

    public MyCartListAdapter(Context context, List<Product> shoppingList, CartFragment.OnCartFragmentInteractionListener mListener)
    {
        super(context,-1, shoppingList);
        this.shoppingList = shoppingList;
        this.mListener=mListener;
    }

    @Override
    public Product getItem(int position) {
        return shoppingList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        Product pdct = null;
        View view=convertView;
        pdct = shoppingList.get(position);
        MyViewHolder holder;
        // reusing view with Text watcher seems to be causing endless loop
        //if (view == null) {
            view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cart_list_element, parent, false);
            holder = new MyViewHolder(view, position);
            view.setTag(holder);
            //holder.bind(position);

            /*}
        else {
            holder = (MyViewHolder) view.getTag();
            //holder.bind(position);
        }*/
        holder.bind(position);

        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    public class MyViewHolder {
        TextView mProdctName;
        Button mProdctAmount;
        ImageButton mDelButton;

        MyViewHolder(View v, int position)
        {
            mProdctName = (TextView) v.findViewById(R.id.productName);
            mProdctAmount = (Button) v.findViewById(R.id.productAmount);
            mDelButton = (ImageButton) v.findViewById(R.id.delImgButton);

            //bind(position);
        }
        public void bind(final int position) {
            Product pdct = null;
            pdct = shoppingList.get(position);
            mProdctName.setText(pdct.getProductName());
            mProdctAmount.setText(String.valueOf(pdct.getProductAmount()));

            final int pos = position;
/*
            mProdctAmount.addTextChangedListener(new TextChangedListener<EditText>(mProdctAmount)
            {
                CharSequence valueBefore = null, valueAfter = null;
                int changedCharCount=0;

                @Override
                public void onTextChanged(EditText targetField, Editable s) {
                   // if ( s.length()> 0 && valueBefore.length()> 0 && valueBefore != s.toString())
                    if ( s.length()> 0)
                    {
                        Log.e("changedCharCount", "......"+changedCharCount);
                        getItem(position).setProductAmount(Integer.parseInt(s.toString()));
                        // mListener.saveNewProductAmountInList(position, getItem(position).getProductAmount());
                    }
                    mProdctAmount.requestFocus();

                }
            });
*/
            final Product finalPdct = getItem(position);
            mDelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.deleteProductFromCart(finalPdct.getProductName(), 1, pos);
                }
            });
        }
    }
}

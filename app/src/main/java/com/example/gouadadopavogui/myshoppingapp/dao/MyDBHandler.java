package com.example.gouadadopavogui.myshoppingapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.events.LocalDBEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.Constants;
import com.example.gouadadopavogui.myshoppingapp.model.Product;


import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by GouadaDopavogui on 14.09.2016.
 */
public class MyDBHandler {
    private static final String LOG_TAG ="MY_LOG" ;
    private Context context;

    public MyDBHandler(Context context) {
        this.context = context;

    }


    public static class MyProductDB implements BaseColumns {
        private static final String PRODUCTS_TABLE_NAME = "tbl_products";
        private static final String CART_TABLE_NAME = "tbl_cart";
        private static final String PRODUCT_TYPE_TABLE_NAME = "tbl_pdct_type";

        private static final String TABLE_LANG = "languages";
        private static final String TABLE_LANG_PRODUCT = "tbl_lang_product";

        private static final String FD_PRODUCT_NAME = "fd_name";
        private static final String FD_AMOUNT = "fd_amount";
        private static final String FD_P_B_ID = "fd_p_b_id";
        private static final String FD_PRODUCT_TYPE = "fd_type";
        private static final String FD_PRODUCT_TYPE_NAME = "fd_type_name";
        private static final String FD_IS_PRODUCT_IN_CART = "is_product_in_cart";

        private static final String FD_LANG = "fd_lang";
        private static final String FD_IS_STANDARD = "fd_standard_product";
        private static final String FD_LANG_B_ID = "fd_lang_b_id";

        public static String SQL_CREATE_PRODUCT_TBL_STMT = "CREATE  TABLE " + MyProductDB.PRODUCTS_TABLE_NAME +
                " ( "
                + MyProductDB._ID + " INTEGER PRIMARY KEY, "
                + MyProductDB.FD_P_B_ID + " LONG, "
                + MyProductDB.FD_PRODUCT_NAME + " STRING, "
                + MyProductDB.FD_PRODUCT_TYPE + " INTEGER, "
                + " FOREIGN KEY("+MyProductDB.FD_PRODUCT_TYPE+") REFERENCES "+MyProductDB.PRODUCT_TYPE_TABLE_NAME+" ("+MyProductDB.FD_PRODUCT_TYPE+")"
                +  ");";

        public static String SQL_CREATE_CART_TBL_STMT = "CREATE  TABLE " + MyProductDB.CART_TABLE_NAME +
                " ( "
                + MyProductDB._ID + " INTEGER PRIMARY KEY, "
                + MyProductDB.FD_P_B_ID + " LONG, "
                + MyProductDB.FD_PRODUCT_NAME + " STRING, "
                + MyProductDB.FD_AMOUNT + " INTEGER, "
                + MyProductDB.FD_IS_PRODUCT_IN_CART  + " INTEGER,"
                + MyProductDB.FD_PRODUCT_TYPE + " INTEGER, "
                + " FOREIGN KEY("+MyProductDB.FD_PRODUCT_TYPE+") REFERENCES "+MyProductDB.PRODUCT_TYPE_TABLE_NAME+" ("+MyProductDB.FD_PRODUCT_TYPE+")"
                + ");";

        public static String SQL_CREATE_PRODUCT_TYPES_TBL_STMT = "CREATE  TABLE " + MyProductDB.PRODUCT_TYPE_TABLE_NAME +
                " ( "
                + MyProductDB._ID + " INTEGER PRIMARY KEY, "
                + MyProductDB.FD_PRODUCT_TYPE + " INTEGER, "
                + MyProductDB.FD_PRODUCT_TYPE_NAME + " STRING "
                +  ");";
        public  static String SQL_CREATE_LANG_TABLE_STMT    =   "CREATE  TABLE " + MyProductDB.TABLE_LANG +
                " ("
                + MyProductDB._ID + " INTEGER PRIMARY KEY, "
                + MyProductDB.FD_LANG + " STRING "
                + MyProductDB.FD_LANG_B_ID + " INTEGER "
                +");";

        public  static String SQL_CREATE_LANG_PRODUCT_TABLE_STMT="CREATE TABLE "+ MyProductDB.TABLE_LANG_PRODUCT +
                " ( "
                + MyProductDB._ID + " INTEGER PRIMARY KEY, "
                + MyProductDB.FD_LANG_B_ID + " INTEGER, "
                + MyProductDB.FD_P_B_ID +  " LONG, "
                +" FOREIGN KEY ( "+ MyProductDB.FD_LANG_B_ID + ") REFERENCES "+ MyProductDB.TABLE_LANG + " ( "+ MyProductDB.FD_LANG_B_ID +" ) ON DELETE CASCADE ON UPDATE CASCADE, "
                + " FOREIGN KEY ( "+ MyProductDB.FD_P_B_ID + " ) REFERENCES "+ MyProductDB.PRODUCTS_TABLE_NAME + " ( "+ MyProductDB.FD_P_B_ID +" ) ON DELETE CASCADE ON UPDATE CASCADE "
                + ");";

        public static String SQL_UPDATE_STMT = " ALTER TABLE "+ MyProductDB.PRODUCTS_TABLE_NAME + " ADD "
                + MyProductDB.FD_PRODUCT_TYPE + " INTEGER";

        public static String SQL_UPDATE_CART_STMT = " ALTER TABLE "+ MyProductDB.CART_TABLE_NAME + " ADD "
                + MyProductDB.FD_PRODUCT_TYPE + " INTEGER";

        public static String SQL_UPDATE_ADD_FIELD = " ALTER TABLE " + MyProductDB.CART_TABLE_NAME + " ADD "
                + MyProductDB.FD_IS_PRODUCT_IN_CART + " INTEGER";


        public static String getTableDropSTMT(String table_name, int operation) {
            String SQL_DROP_STMT = "";
            if (operation == 1)
                SQL_DROP_STMT = "DROP TABLE IF EXISTS " + table_name + ";";
            else if (operation == 2)
                SQL_DROP_STMT = "DELETE FROM " + MyProductDB.PRODUCTS_TABLE_NAME + ";";
            return SQL_DROP_STMT;
        }
    }

    public static class MyDBHelper extends SQLiteOpenHelper {
        private static String DB_NAME = "DB_My_Products";
        private static int DB_Version = 5;

        public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, factory, DB_Version);
        }

        public MyDBHelper(Context context) {
            super(context, DB_NAME, null, DB_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(MyProductDB.SQL_TRUNCATE_STMT);
            db.execSQL(MyProductDB.SQL_CREATE_PRODUCT_TBL_STMT);
           // db.execSQL(MyProductDB.getTableDropSTMT(MyProductDB.CART_TABLE_NAME, 1));
            db.execSQL(MyProductDB.SQL_CREATE_CART_TBL_STMT);
            db.execSQL(MyProductDB.SQL_CREATE_PRODUCT_TYPES_TBL_STMT);
            db.execSQL(MyProductDB.SQL_CREATE_LANG_PRODUCT_TABLE_STMT);
            db.execSQL(MyProductDB.SQL_CREATE_LANG_TABLE_STMT);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (oldVersion ==1 && newVersion == 2) {
                db.execSQL(MyProductDB.SQL_UPDATE_STMT);
                db.execSQL(MyProductDB.SQL_UPDATE_CART_STMT);
                oldVersion = 2;
            }
                if (oldVersion ==2 && newVersion == 3)
                {
                    db.execSQL(MyProductDB.SQL_UPDATE_STMT);
                    db.execSQL(MyProductDB.SQL_UPDATE_CART_STMT);
                    oldVersion = 3;
                }
            if (oldVersion==3 && newVersion== 4)
            {
                db.execSQL(MyProductDB.SQL_UPDATE_ADD_FIELD);
                oldVersion = 4;
            }
            if(oldVersion == 4 && newVersion ==5)
            {
                db.execSQL(MyProductDB.SQL_CREATE_LANG_PRODUCT_TABLE_STMT);
                db.execSQL(MyProductDB.SQL_CREATE_LANG_TABLE_STMT);
            }
        }
    }


    private Object syncRoot = new Object();
    public String DEVICE_LANG = Locale.getDefault().getLanguage();


    public void fill_productTypesTbl() {
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);

            String[] product_types_arr = Constants.getProductTyPeSpinnerText(context);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            try {
                int i=0;
                for (String product_type_name: product_types_arr) {
                    if (i>0)
                    {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MyProductDB.FD_PRODUCT_TYPE, i);
                        contentValues.put(MyProductDB.FD_PRODUCT_TYPE_NAME, product_type_name);

                        db.insert(MyProductDB.PRODUCT_TYPE_TABLE_NAME, null, contentValues);
                    }
                    i=i+1;
                }
            }
            catch (Exception ex) {
                Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                //lastException = ex;
            } finally {
                db.close();
            }
        }
    }

    int[] select_product_types() {
        int[] pdct_type_list =null;
        SQLiteDatabase db = null;
        //ContentValues values = new ContentValues();
        int i = 0;
        int pdt_type;
        synchronized (syncRoot) {
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getReadableDatabase();
                //Cursor c = db.query(MyProductDB.PRODUCTS_TABLE_NAME, new String[]{MyProductDB.FD_PRODUCT_TYPE}, null, null,  MyProductDB.FD_PRODUCT_TYPE, null, null);
                Cursor cursor = db.rawQuery(" SELECT DISTINCT "+ MyProductDB.FD_PRODUCT_TYPE +" FROM " + MyProductDB.PRODUCT_TYPE_TABLE_NAME ,null);

                if (cursor.getCount() <= 0)
                {
                    fill_productTypesTbl();
                    cursor = db.rawQuery("SELECT DISTINCT "+ MyProductDB.FD_PRODUCT_TYPE +" FROM " + MyProductDB.PRODUCT_TYPE_TABLE_NAME ,null);

                }
                pdct_type_list = new int[cursor.getCount()];
                while (cursor.moveToNext()) {
                    pdt_type = cursor.getInt(0);
                    pdct_type_list[i]= pdt_type;
                    i=i+1;
                }

            }
            catch(Exception e)
            {
                Log.e("ERROR", e.getStackTrace()+e.getMessage()); //+e.getCause().toString()
            }
            finally {
                db.close();
            }
        }
        return pdct_type_list;
    }

    public void insert_current_LANG(String device_lang) {
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            try {
                boolean product_exists = false;
                DEVICE_LANG = Locale.getDefault().getLanguage();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MyProductDB.FD_LANG, DEVICE_LANG);
                Cursor result = db.query(MyProductDB.TABLE_LANG, null, MyProductDB.FD_LANG + " LIKE ", new String[]{DEVICE_LANG}, null, null, null);
                if (result!= null && result.getCount() >= 1)
                    db.insert(MyProductDB.TABLE_LANG, null, contentValues);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                //lastException = ex;
            } finally {
                db.close();
            }
        }
    }

    public Long insert_product(Product product) {
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            final Long id;
            final Product pdt = product;
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            try {
                ContentValues pdt_lang_contentValues = new ContentValues();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MyProductDB.FD_PRODUCT_NAME, product.getProductName());
                contentValues.put(MyProductDB.FD_P_B_ID, product.getProductId());
                contentValues.put(MyProductDB.FD_PRODUCT_TYPE, product.getProductType());

                pdt_lang_contentValues.put(MyProductDB.FD_P_B_ID, product.getProductId());
                pdt_lang_contentValues.put(MyProductDB.FD_LANG_B_ID, Locale.getDefault().getLanguage());
                //contentValues.put(MyProductDB.FD_AMOUNT, product.getProductAmount());

                id = db.insert(MyProductDB.PRODUCTS_TABLE_NAME, null, contentValues);

                //UPDATE MULTI_LANG
                db.insert(MyProductDB.TABLE_LANG_PRODUCT, null,pdt_lang_contentValues);
                /*
                db.execSQL("insert into "+MyProductDB.PRODUCTS_TABLE_NAME+ "( "
                +MyProductDB.FD_PRODUCT_NAME + ", "
                +MyProductDB.FD_PRODUCT_TYPE + ", "
                +MyProductDB.FD_P_B_ID +")  Values (\""+product.getProductName() +"\", "+ product.getProductId()+", "+product.getProductType()+")");
                */
                if (id >= 1)
                {
                    Log.e("Log", "SUCCESSFULLY INSERTED NEW PRODUCT");
                }
                else
                {
                    Log.e("Log", "ERROR: COULD NOT SUCCESSFULLY INSERT NEW PRODUCT");

                }
/*
                AsyncTask myAsyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        return null;
                    }
                };
                */
            }
                catch (Exception ex) {
                    Log.e(LOG_TAG, "Failed saving item : " + ex.getMessage(), ex);
                    //lastException = ex;
                    return null;
            } finally {
                db.close();
            }
            return id;
        }
    }

    public List<Product> retrieve_all_products() {
        synchronized (syncRoot) {
            List<Product> product_list = new ArrayList<Product>();
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = myDBHelper.getReadableDatabase();
            try { //, MyProductDB.FD_AMOUNT
                //UPdATE MULTI_LANG SELECT-JOIN OR USE REALM
                //db.insert(MyProductDB.TABLE_LANG_PRODUCT ...)
                /*
                Cursor cusor = db.query(MyProductDB.PRODUCTS_TABLE_NAME,
                        new String[]{MyProductDB.FD_PRODUCT_NAME, MyProductDB.FD_P_B_ID,
                                MyProductDB.FD_PRODUCT_TYPE }, null, null, null, null, MyProductDB.FD_PRODUCT_TYPE);
                */

                final String  SELECT_ALL_PRODUCTS = "SELECT " + MyProductDB.PRODUCTS_TABLE_NAME+"."+MyProductDB.FD_PRODUCT_NAME +", "+ MyProductDB.PRODUCTS_TABLE_NAME+"."+MyProductDB.FD_P_B_ID +", "+ MyProductDB.PRODUCTS_TABLE_NAME+"."+MyProductDB.FD_PRODUCT_TYPE + " FROM " + MyProductDB.PRODUCTS_TABLE_NAME + " JOIN " + MyProductDB.TABLE_LANG_PRODUCT + " ON " + MyProductDB.PRODUCTS_TABLE_NAME + "." + MyProductDB.FD_P_B_ID+ "="+ MyProductDB.TABLE_LANG_PRODUCT+"."+MyProductDB.FD_P_B_ID +" WHERE " + MyProductDB.TABLE_LANG_PRODUCT+"."+MyProductDB.FD_LANG_B_ID+" =?";

                Cursor cursor = db.rawQuery(SELECT_ALL_PRODUCTS, new String[]{DEVICE_LANG});

                while (cursor.moveToNext()) {
                    Product pdct = new Product();
                    pdct.setProductName(cursor.getString(0));
                    pdct.setProductId(cursor.getLong(1));
                    pdct.setProductType(cursor.getInt(2));
                    pdct.setProductAmount(1);
                    product_list.add(pdct);
                }
            } finally {
                db.close();
            }
            return product_list;
        }
    }

    public boolean deleteFromProductList(String productName)
    {
        synchronized (syncRoot) {
            boolean deleted=false;
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            values.put(MyProductDB.FD_PRODUCT_NAME, productName);
            try{
                db=myDBHelper.getWritableDatabase();
                int row_nr = db.delete(MyProductDB.PRODUCTS_TABLE_NAME, MyProductDB.FD_PRODUCT_NAME + " LIKE  ? ", new String[]{productName});
                if(row_nr >= 1)
                    deleted = true;
             }
            finally {
                db.close();
                return deleted;
            }
        }
    }

    public void deleteFromFromCart(String productName)
    {
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            values.put(MyProductDB.FD_PRODUCT_NAME, productName);
            try{
                db=myDBHelper.getWritableDatabase();
                db.delete(MyProductDB.CART_TABLE_NAME, MyProductDB.FD_PRODUCT_NAME + " LIKE  ? ", new String[]{productName});
            }
            finally {
                db.close();
            }
        }
    }

    public boolean cleanUpTable() {
        boolean cleanedup =false;
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            try {
                int count = db.delete(MyProductDB.PRODUCTS_TABLE_NAME, null, null);
                if(count >= 0)
                    cleanedup = true;
                //Toast.makeText(this.context, count, Toast.LENGTH_SHORT).show();
            }
            catch(Exception e) {
                cleanedup = false;
            }
            finally
             {
                db.close();
            }
        }
        return cleanedup;
    }

    public boolean cleanUpCart() {

        boolean cleanedup =false;
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            try {
                int count = db.delete(MyProductDB.CART_TABLE_NAME, null, null);
                if(count >=0)
                    cleanedup = true;
                //Toast.makeText(this.context, count, Toast.LENGTH_SHORT).show();
                Log.e("MESSAGE: ", "FROM DB-STORAGE DELETED");
            } finally {
                db.close();
            }
        }
        return cleanedup;
    }

    Boolean productExists(Product product) {
        Boolean exist = false;
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getReadableDatabase();
                Cursor cursor = db.query(MyProductDB.PRODUCTS_TABLE_NAME, new String[]{MyProductDB.FD_PRODUCT_NAME}, MyProductDB.FD_PRODUCT_NAME + " LIKE  ? ",
                        new String[]{product.getProductName()}, null, null, null, null);

                //db.query()
                if (cursor.getCount() > 0)
                    exist = true;
            } finally {
                db.close();
            }
            return exist;
        }
    }

    Boolean productExistsInCart(Product product) {
        Boolean exist = false;
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getReadableDatabase();
                Cursor cursor = db.query(MyProductDB.CART_TABLE_NAME, new String[]{MyProductDB.FD_P_B_ID},  MyProductDB.FD_P_B_ID + " =  ?  ",
                        new String[]{String.valueOf(product.getProductId())}, null, null, null, null);
                //db.query()
                if (cursor.getCount() > 0)
                    exist = true;
            } finally {
                db.close();
            }
            return exist;
        }
    }

    public void add_Products_to_cart(List<Product> pdct_list) {
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            boolean product_list_updated = false;
            ContentValues values = new ContentValues();
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getWritableDatabase();
                for (Product pdct : pdct_list) {
                    values.put(MyProductDB.FD_PRODUCT_NAME, pdct.getProductName());
                    values.put(MyProductDB.FD_AMOUNT, pdct.getProductAmount());
                    values.put(MyProductDB.FD_P_B_ID, pdct.getProductId());
                    values.put(MyProductDB.FD_PRODUCT_TYPE, pdct.getProductType());
                    if (pdct.isInCart())
                        values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 1);
                    else
                        values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);

                    if (this.productExistsInCart(pdct))
                        this.updateProductInCart(pdct);
                    else
                        db.insert(MyProductDB.CART_TABLE_NAME, null, values);
                    if (!productExists(pdct))
                    {
                        insert_product(pdct);
                        product_list_updated = true;
                    }
                }
                if (product_list_updated == true) {
                    EventBus.getDefault().post(new LocalDBEvent(LocalDBEvent.UPDATE_PRODUCT_LIST, null, true));
                }
            } finally {
                db.close();
            }
        }
    }

    public void updateProductInCart(Product pdct) {

        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;

            ContentValues values = new ContentValues();
            values.put(MyProductDB.FD_AMOUNT, pdct.getProductAmount());
            values.put(MyProductDB.FD_PRODUCT_TYPE, pdct.getProductType());
            if (pdct.isInCart()) {
                values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 1);
            }
           /* else {
                //values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);
            }*/
            try {
                db = myDBHelper.getWritableDatabase();
                int result = db.update(MyProductDB.CART_TABLE_NAME,values, MyProductDB.FD_P_B_ID + " = ?",
                        new String[] { String.valueOf(pdct.getProductId())});

                //  db.execSQL("UPDATE " + MyProductDB.CART_TABLE_NAME + " SET " + MyProductDB.FD_IS_PRODUCT_IN_CART + " = 1 WHERE " + MyProductDB.FD_P_B_ID + " = " + String.valueOf(pdct.getProductId()));
                /*if (result > 0)
                    Log.e("LOG DB: ", "Succefully updated the product ..."+ pdct.getProductId());
                else Log.e("LOG DB", "Dammn it did not work"); */
            } finally {
                db.close();
            }

        }
    }

    public void removeProductOutOfCart(Product pdct) {
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;

            ContentValues values = new ContentValues();
            if (pdct.isInCart()) {
                pdct.setInCart(false);
                values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);
            }
            else {
                values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);
            }
            try {
                db = myDBHelper.getWritableDatabase();
                int result = db.update(MyProductDB.CART_TABLE_NAME,values, MyProductDB.FD_P_B_ID + " = ?",
                        new String[] { String.valueOf(pdct.getProductId())});

                //  db.execSQL("UPDATE " + MyProductDB.CART_TABLE_NAME + " SET " + MyProductDB.FD_IS_PRODUCT_IN_CART + " = 1 WHERE " + MyProductDB.FD_P_B_ID + " = " + String.valueOf(pdct.getProductId()));
                /*if (result > 0)
                    Log.e("LOG DB: ", "Succefully updated the product ..."+ pdct.getProductId());
                else Log.e("LOG DB", "Dammn it did not work"); */
            } finally {
                db.close();
            }
        }
    }

    public void load_Products_into_device(List<Product> pdct_list) {
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getWritableDatabase();
                if(pdct_list != null && pdct_list.size()> 0)
                {
                    //kll
                    for (Product pdct : pdct_list) {
                        values.put(MyProductDB.FD_PRODUCT_NAME, pdct.getProductName());
                        values.put(MyProductDB.FD_P_B_ID, pdct.getProductId());
                        //values.put(MyProductDB.FD_AMOUNT, pdct.getProductAmount());
                        values.put(MyProductDB.FD_PRODUCT_TYPE, pdct.getProductType());
                        db.insert(MyProductDB.PRODUCTS_TABLE_NAME, null, values);
                    }
                }
            } finally {
                db.close();
            }
        }
    }

    List<Product> select_All_from_cart() {
        List<Product> pdct_list = new ArrayList<Product>();
        SQLiteDatabase db = null;
        ContentValues values = new ContentValues();
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            db = myDBHelper.getReadableDatabase();
            try {


                String  SELECT_ALL_PRODUCTS_FROM_CART = "SELECT "
                        + MyProductDB.CART_TABLE_NAME+"."+MyProductDB.FD_PRODUCT_NAME +", "+ MyProductDB.CART_TABLE_NAME+"."+MyProductDB.FD_AMOUNT+ ", " + MyProductDB.CART_TABLE_NAME+"."+MyProductDB.FD_P_B_ID +", "
                        + MyProductDB.CART_TABLE_NAME+"."+MyProductDB.FD_PRODUCT_TYPE + ", " + MyProductDB.CART_TABLE_NAME+"." + MyProductDB.FD_IS_PRODUCT_IN_CART
                        +  " FROM " + MyProductDB.CART_TABLE_NAME + " JOIN " + MyProductDB.TABLE_LANG_PRODUCT + " ON " + MyProductDB.CART_TABLE_NAME + "." + MyProductDB.FD_P_B_ID+ "="+ MyProductDB.TABLE_LANG_PRODUCT+"."+MyProductDB.FD_P_B_ID +" WHERE " + MyProductDB.TABLE_LANG_PRODUCT+"."+MyProductDB.FD_LANG_B_ID+" =?";

                Cursor cursor = db.rawQuery(SELECT_ALL_PRODUCTS_FROM_CART, new String[]{DEVICE_LANG});

                Cursor cursor2 = db.query(MyProductDB.CART_TABLE_NAME, new String[]{MyProductDB.FD_PRODUCT_NAME, MyProductDB.FD_AMOUNT, MyProductDB.FD_P_B_ID, MyProductDB.FD_PRODUCT_TYPE, MyProductDB.FD_IS_PRODUCT_IN_CART}, null, null, null, null, MyProductDB.FD_PRODUCT_TYPE);

                while (cursor.moveToNext()) {
                    Product pdt = new Product(cursor.getString(0), cursor.getInt(1), cursor.getInt(3));
                    pdt.setProductId(cursor.getLong(2));

                    if (cursor.getInt(4) == 1)
                        pdt.setInCart(true);
                    else pdt.setInCart(false);
                    pdct_list.add(pdt);
                }
            } finally {
                db.close();
            }
        }
        return pdct_list;
        }


    public boolean isProductInCart(Product product)
    {
        Boolean exist = false;
        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;
            try {
                db = myDBHelper.getReadableDatabase();
                Cursor cursor = db.query(MyProductDB.CART_TABLE_NAME, new String[]{MyProductDB.FD_PRODUCT_NAME}, MyProductDB.FD_PRODUCT_NAME + " LIKE  ? ", new String[]{product.getProductName()}, null, null, null, null);
                if (cursor.getCount() > 0)
                    exist = true;
            } finally {
                db.close();
            }
            return exist;
        }
    }

    public void insert_product_in_cart(Product product)
    {
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                db = myDBHelper.getWritableDatabase();
                values.put(MyProductDB.FD_PRODUCT_NAME, product.getProductName());
                if (product.getProductAmount() == 0)
                    product.setProductAmount(1);
                values.put(MyProductDB.FD_AMOUNT, product.getProductAmount());
                values.put(MyProductDB.FD_P_B_ID, product.getProductId());
                values.put(MyProductDB.FD_PRODUCT_TYPE, product.getProductType());
                if (product.isInCart())
                    values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 1);
                else values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);

                long inserted = db.insert(MyProductDB.CART_TABLE_NAME, null, values);
                if (inserted > 0)
                    Log.e("INSERTED", "  "+inserted);
                else
                    Log.e("NOT INSERTED", "  "+inserted);
            } finally {
                db.close();
            }
        }
    }


    public void reInitialyzeCurrentCart() {
        synchronized (syncRoot) {
            SQLiteDatabase db = null;
            ContentValues values = new ContentValues();
            try {
                MyDBHelper myDBHelper = new MyDBHelper(context);
                values.put(MyProductDB.FD_IS_PRODUCT_IN_CART, 0);
                db = myDBHelper.getWritableDatabase();
                db.update(MyProductDB.CART_TABLE_NAME, values, null, null);
            } finally {
                db.close();
            }
        }
    }

    public boolean archiveCurrentCart() {

        synchronized (syncRoot) {
            MyDBHelper myDBHelper = new MyDBHelper(context);
            SQLiteDatabase db = null;
            boolean archived=false;
            try{
                db=myDBHelper.getWritableDatabase();
               // db.execSQL("UPDATE "+ MyProductDB.CART_TABLE_NAME+" SET " + MyProductDB.FD_IS_PRODUCT_IN_CART+" = 1 ");
                int count = db.delete(MyProductDB.CART_TABLE_NAME, "1", null);
                if (count >= 1)
                    archived = true;

                //db.execSQL("DELETE FROM "+ MyProductDB.CART_TABLE_NAME);
            }
            catch (Exception e)
            {
                Log.e("ERROR", e.getStackTrace().toString());
            }
            finally {
                db.close();
                return archived;
            }
        }
    }
}
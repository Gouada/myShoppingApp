package com.example.gouadadopavogui.myshoppingapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.gouadadopavogui.myshoppingapp.controller.ProductRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.controller.ShoppingGroupRetrofitClient;
import com.example.gouadadopavogui.myshoppingapp.dao.MyDBHandler;
import com.example.gouadadopavogui.myshoppingapp.events.ProductEvent;
import com.example.gouadadopavogui.myshoppingapp.events.ShoppingGroupEvent;
import com.example.gouadadopavogui.myshoppingapp.helpers.ErrorCodes;
import com.example.gouadadopavogui.myshoppingapp.interfaces.ProductRestServivce;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class GroupBackendHandlerService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    //private static final String ACTION_LOAD_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.action.LOAD_PRODUCT";
    //private static final String ACTION_SAVE_NEW_PRODUCT = "com.example.gouadadopavogui.myshoppingapp.services.action.SAVE_NEW_PRODUCT";

    private static final String ACTION_GET_SHOPPING_GROUP_MEMBER = "com.example.gouadadopavogui.myshoppingapp.services.action.GET_SHOPPINGROUP_MEMBER";
    private static final String ACTION_GET_MEMBER_SHOPPING_GROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.GET_MEMBER_SHOPPINGROUP";
    private static final String ACTION_ADD_NEW_MEMBER_TO_SHOPPING_GROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.ADD_NEW_MEMBER_TO_SHOPPINGROUP";
    private static final String  ACTION_ADHERE_TO_SHOPPING_GROUP = "com.example.gouadadopavogui.myshoppingapp.services.action.ADHERE_TO_SHOPPINGROUP";


    // TODO: Rename parameters
    //private static final String EXTRA_PARAM1 = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM2";

    public static final String BROCAST_SENDER_BACKEND_HANDLER = "com.example.gouadadopavogui.myshoppingapp";
    private static final String EXTRA_PARAM_SHOPPING_GROUP_MEMBER_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP_MEMBER_ID";
    private static final String EXTRA_PARAM_SHOPPING_GROUP_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_SHOPPING_GROUP_ID";
    private static final String EXTRA_PARAM_NEW_SHOPPING_GROUP_MEMBER = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_NEW_SHOPPING_GROUP_MEMBER";
    private static final String EXTRA_PARAM_APP_INSTANCE_ID = "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_APP_INSTANCE_ID";
    private static final String EXTRA_PARAM_GROUP_OWNER_PHONE_NUMBER =  "com.example.gouadadopavogui.myshoppingapp.services.extra.PARAM_GROUP_OWNER_PHONE_NUMBER";



    private static Context activityContext;
    //private ProductRestServivce productRestServivce;
    //private ProductRetrofitClient mProductManager;


    public GroupBackendHandlerService() {
        super("GroupBackendHandlerService");
    }

    //private ProductRetrofitClient mManager= new ProductRetrofitClient();
    //public static List<Product> allProducts;

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionGetShoppingGroupMember(Context context, String appInsstanceID)
    {
        Intent intent = new Intent(context, GroupBackendHandlerService.class);
        intent.setAction(ACTION_GET_SHOPPING_GROUP_MEMBER);
        intent.putExtra(EXTRA_PARAM_APP_INSTANCE_ID, appInsstanceID);
        context.startService(intent);
    }

    public static  void startActionGetMemberShoppingGroup(Context context, String appInsstanceID)
    {
        Intent intent = new Intent(context,  GroupBackendHandlerService.class);
        intent.setAction(ACTION_GET_MEMBER_SHOPPING_GROUP);
        intent.putExtra(EXTRA_PARAM_APP_INSTANCE_ID, appInsstanceID);
        context.startService(intent);
    }


    public static void startActionAddNewUserToGroup(Context context, long shoppingGroupId, long memberId, ShoppingGroupMember newMember)
    {
        Intent intent = new Intent(context,  GroupBackendHandlerService.class);
        intent.setAction(ACTION_ADD_NEW_MEMBER_TO_SHOPPING_GROUP);
        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMBER_ID, memberId);
        intent.putExtra(EXTRA_PARAM_SHOPPING_GROUP_ID, shoppingGroupId);
        intent.putExtra(EXTRA_PARAM_NEW_SHOPPING_GROUP_MEMBER, (Serializable) newMember);

        context.startService(intent);
    }

    public static void startAdhereExistingGroup(Context context, String groupOwnerPhoneNumber, ShoppingGroupMember newAdherent)
    {
        Intent intent = new Intent(context,  GroupBackendHandlerService.class);
        intent.setAction(ACTION_ADHERE_TO_SHOPPING_GROUP);
        intent.putExtra(EXTRA_PARAM_GROUP_OWNER_PHONE_NUMBER, groupOwnerPhoneNumber);
        intent.putExtra(EXTRA_PARAM_NEW_SHOPPING_GROUP_MEMBER, (Serializable) newAdherent);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_SHOPPING_GROUP_MEMBER.equals(action))
            {
                final String appInstanceId = intent.getStringExtra(EXTRA_PARAM_APP_INSTANCE_ID);
                handleActionGetShoppingGroupMember(appInstanceId);
            }
            else if (ACTION_GET_MEMBER_SHOPPING_GROUP.equals(action))
            {
                final String appInstanceId = intent.getStringExtra(EXTRA_PARAM_APP_INSTANCE_ID);
                final Context context = getApplicationContext();
                handleActionGetMemberShoppingGroup(context, appInstanceId);
            }
            else if(ACTION_ADD_NEW_MEMBER_TO_SHOPPING_GROUP.equals(action))
            {
                final long groupId = intent.getLongExtra(EXTRA_PARAM_SHOPPING_GROUP_ID, 0);
                final long currentMemberId = intent.getLongExtra(EXTRA_PARAM_SHOPPING_GROUP_MEMBER_ID, 0);
                ShoppingGroupMember newMember = (ShoppingGroupMember) intent.getExtras().getSerializable(EXTRA_PARAM_NEW_SHOPPING_GROUP_MEMBER);
                final Context context = getApplicationContext();
                handleActionAddNewMemberToGroup(context, groupId, currentMemberId, newMember);
            }
            else if (ACTION_ADHERE_TO_SHOPPING_GROUP.equals(action))
            {
                final String group_owner_phone_nr = intent.getExtras().getString(EXTRA_PARAM_GROUP_OWNER_PHONE_NUMBER);
                ShoppingGroupMember newMember = (ShoppingGroupMember) intent.getExtras().getSerializable(EXTRA_PARAM_NEW_SHOPPING_GROUP_MEMBER);
                final Context context = getApplicationContext();
                handleActionAdhereGroup(context, group_owner_phone_nr,newMember);
            }
        }
    }

    private void handleActionAddNewMemberToGroup(Context context, long groupId, long currentMemberId, ShoppingGroupMember newMember)
    {
        ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
        //Call<ShoppingGroupMember> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().addNewMemberToGroup(groupId, currentMemberId, newMember);
        Call<ShoppingGroupMember> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().addNewMemberToGroup(groupId, newMember);
        igroupMember.enqueue(new Callback<ShoppingGroupMember>() {
            @Override
            public void onResponse(Call<ShoppingGroupMember> call, Response<ShoppingGroupMember> response) {
                if (response.isSuccessful())
                {
                    Log.e("MESSAGE", "Yeak worked");
                }
                else
                    Log.e("MESSAGE", "hell no");
            }

            @Override
            public void onFailure(Call<ShoppingGroupMember> call, Throwable t) {
                Log.e("ERROR", t.getStackTrace().toString());
            }
        });
    }

    private void handleActionAdhereGroup(Context context, final String group_owner_phone_nr, final ShoppingGroupMember newMember)
    {
        ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
        //Call<ShoppingGroupMember> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().addNewMemberToGroup(groupId, currentMemberId, newMember);
        Call<ShoppingGroup> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().adhereGroup(group_owner_phone_nr, newMember);
        igroupMember.enqueue(new Callback<ShoppingGroup>() {
            @Override
            public void onResponse(Call<ShoppingGroup> call, Response<ShoppingGroup> response) {
                if (response.isSuccessful())
                {
                    if (response.code() == 201) {
                        EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                        Log.e("MESSAGE", "NEW ADHERENT HAS BEEN SUCCEFULLY ADDED");
                    }
                    else if(response.code() == 204)
                    {
                        Log.e("MESSAGE", "GIVEN OWNER TEL. NR WAS INCORRECT");
                        EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS));
                    }
                }
                else {
                    Log.e("MESSAGE", "ADHESION FAILED");
                    EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GIVEN_GROUP_OWNER_NOT_EXISTS, newMember, group_owner_phone_nr));
                }
            }

            @Override
            public void onFailure(Call<ShoppingGroup> call, Throwable t) {
                EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_ADHESION_FAILED));
                Log.e("ERROR", t.getStackTrace().toString());
            }
        });
    }


    public void handleActionGetShoppingGroupMember(String appInsstanceID)
    {
        //Check if the current app_instance id is saved in backend
        // This method send a request to the backend part to check if user exists
        ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();

        Call<ShoppingGroupMember> igroupMember = mShoppingGroupRetrofitClient.getIGrouP().getShoppingGroupMember(appInsstanceID);
            igroupMember.enqueue(new Callback<ShoppingGroupMember>() {
                @Override
                public void onResponse(Call<ShoppingGroupMember> call, Response<ShoppingGroupMember> response) {
                    if (response.isSuccessful())
                    {
                        EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                        //setShoppingGroupMember(response.body());
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_LONG).show();
                        Log.e("RESPONSE_CODE", " " + response.code());
                        EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                    }
                }

                @Override
                public void onFailure(Call<ShoppingGroupMember> call, Throwable t) {
                    Log.e("ERROR", " " + t.getStackTrace().toString());
                    EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                }
            });
    }

    private void handleActionGetMemberShoppingGroup(Context context, String appInstanceId)
    {
        Log.e("APPID", " " + appInstanceId);
        ShoppingGroupRetrofitClient mShoppingGroupRetrofitClient = new ShoppingGroupRetrofitClient();
        Call<ShoppingGroup> iShoppingGroup = mShoppingGroupRetrofitClient.getIGrouP().getMemberShoppingGroup(appInstanceId);
        iShoppingGroup.enqueue(new Callback<ShoppingGroup>() {
            @Override
            public void onResponse(Call<ShoppingGroup> call, Response<ShoppingGroup> response) {
                if(response.isSuccessful()) {
                    EventBus.getDefault().post(new ShoppingGroupEvent(response.body()));
                }
                else {
                    Log.e("ERROR: ", "NO Shoppinggroup available ............" + response.code());
                    EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                }
            }
            @Override
            public void onFailure(Call<ShoppingGroup> call, Throwable t) {
                EventBus.getDefault().post(new ShoppingGroupEvent(ErrorCodes.BACKEND_ERROR_GROUP_NOT_LOADED));
                Log.e("ERROR: ", "NO Shoppinggroup available " + t.getStackTrace().toString()+"    "+ t.getMessage()+"  ......"+t.getCause());

            }
        });
    }
}

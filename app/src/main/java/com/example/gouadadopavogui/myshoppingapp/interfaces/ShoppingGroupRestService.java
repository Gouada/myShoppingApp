package com.example.gouadadopavogui.myshoppingapp.interfaces;

import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by GouadaDopavogui on 19.11.2016.
 */

public interface ShoppingGroupRestService {

    @GET("group/instanceId/{instanceId}")
    Call<ShoppingGroupMember> getShoppingGroupMember(@Path("instanceId") String instanceId);

    @POST("group")
    Call<ShoppingGroup> createNewShoppingGroup(@Body ShoppingGroup shoppingGroup);

    //get the shoppin group
    @GET("group/memberShoppingGroup")
    Call<ShoppingGroup> getMemberShoppingGroup(@Query("instanceId") String instanceId);

    @POST("group/{groupId}/member/saveToken")
    Call<Boolean> FirebaseDeviceToken(@Body String firebaseDeviceToken, @Path("memberId") long memberId);

    /*
    @POST("group/{groupId}/newmember/{currentMemberId}")
    Call<ShoppingGroupMember> addNewMemberToGroup(@Path("groupId") long groupId, @Path("currentMemberId") long currentMemberId, @Body ShoppingGroupMember newMember);
    */

    @POST("group/{groupId}")
    Call<ShoppingGroupMember> addNewMemberToGroup(@Path("groupId") long groupId, @Body ShoppingGroupMember newMember);

    @POST("group/groupOwnerPhoneNumber/{groupOwnerPhoneNumber}")
    Call<ShoppingGroup> adhereGroup(@Path("groupOwnerPhoneNumber") String groupOwnerPhoneNumber,  @Body ShoppingGroupMember newMember);
}

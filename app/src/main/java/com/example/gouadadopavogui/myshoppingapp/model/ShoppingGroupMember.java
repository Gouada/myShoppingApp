package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by GouadaDopavogui on 01.11.2016.
 */
public class ShoppingGroupMember implements Serializable
{
    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("instanceId")
    @Expose
    private String instanceId;

    @SerializedName("firebaseDeviceToken")
    @Expose
    private String firebaseDeviceToken;

    @SerializedName("shoppingGroup")
    @Expose(serialize = false)
    private ShoppingGroup shoppingGroup;

    public ShoppingGroupMember() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Date getCreated() {
        return this.created;
    }

    public ShoppingGroup getShoppingGroup() {
        return this.shoppingGroup;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroup = shoppingGroup;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public void setFirebaseDeviceToken(String firebaseDeviceToken) {
        this.firebaseDeviceToken = firebaseDeviceToken;
    }

    public String getFirebaseDeviceToken() {
        return firebaseDeviceToken;
    }

    public String getInstanceId() {
        return instanceId;
    }
}

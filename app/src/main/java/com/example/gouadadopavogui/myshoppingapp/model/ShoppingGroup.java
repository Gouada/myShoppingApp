package com.example.gouadadopavogui.myshoppingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by GouadaDopavogui on 01.11.2016.
 */
public class ShoppingGroup implements Serializable
{
    @SerializedName("groupId")
    @Expose
    private long groupId;

    @SerializedName("groupName")
    @Expose
    private String groupName;

    @SerializedName("created")
    @Expose
    private Date created;

    @SerializedName("currentShoppingCart")
    @Expose
    private Cart currentShoppingCart;

    @SerializedName("groupMembers")
    @Expose
    private List<ShoppingGroupMember> groupMembers;// = new ArrayList<ShoppingGroupMember>();

    @SerializedName("carts")
    @Expose
    private List<Cart> carts;

    public ShoppingGroup() {
    }

    public long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Date getCreated() {
        return created;
    }

    public Cart getCurrentShoppingCart() {
        return currentShoppingCart;
    }

    public List<ShoppingGroupMember> getGroupMembers() {
        return this.groupMembers;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setCurrentShoppingCart(Cart currentShoppingCart) {
        this.currentShoppingCart = currentShoppingCart;
        //currentShoppingCart.setShoppingGroup(this);
    }

    public void setGroupMembers(List<ShoppingGroupMember> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public void addMemmber(ShoppingGroupMember newShopping_groupMember)
    {
        //newShopping_groupMember.setShoppingGroup(this);
        if (this.groupMembers == null)
            this.groupMembers = new ArrayList<ShoppingGroupMember>();
        this.groupMembers .add(newShopping_groupMember);
    }
}

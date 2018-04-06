package com.example.gouadadopavogui.myshoppingapp.events;

import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroupMember;

/**
 * Created by gouadadopavogui on 29.11.2016.
 */

public class ShoppingGroupEvent {

    private ShoppingGroup shoppingGroup;
    private ShoppingGroupMember shoppingGroupMember;
    private Cart currentCart;
    public  int errorCode=0;
    private String groupOwnerNumber;

    public ShoppingGroupEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public ShoppingGroupEvent(int errorCode, ShoppingGroupMember newShoppingGroupMember, String groupOwnerNumber)
    {
        this.setErrorCode(errorCode);
        this.setShoppingGroupMember(newShoppingGroupMember);
        this.setGroupOwnerNumber(groupOwnerNumber);
    }
    public ShoppingGroup getShoppingGroup() {
        return shoppingGroup;
    }

    public void setShoppingGroup(ShoppingGroup shoppingGroup) {
        this.shoppingGroup = shoppingGroup;
        if (shoppingGroup.getGroupMembers()!=null)
        {
          setShoppingGroupMember(shoppingGroup.getGroupMembers().get(0));
        }

        if (shoppingGroup != null && shoppingGroup.getCarts() != null && shoppingGroup.getCarts().size() > 0)
        {
            for (Cart cart: shoppingGroup.getCarts())
            {
                if (cart.isStatus())
                    setCurrentCart(cart);
            }
        }
    }

    public ShoppingGroupMember getShoppingGroupMember() {
        return shoppingGroupMember;
    }

    public void setShoppingGroupMember(ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroupMember = shoppingGroupMember;
    }

    public ShoppingGroupEvent() {
    }

    public ShoppingGroupEvent(ShoppingGroup shoppingGroup, ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroup = shoppingGroup;
        this.shoppingGroupMember=shoppingGroupMember;
    }

    public ShoppingGroupEvent(ShoppingGroup shoppingGroup)
    {
        setShoppingGroup(shoppingGroup);
    }

    public ShoppingGroupEvent(ShoppingGroupMember shoppingGroupMember) {
        this.shoppingGroupMember = shoppingGroupMember;
    }

    public Cart getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(Cart currentCart) {
        this.currentCart = currentCart;
    }

    public  int getErrorCode() {
        return this.errorCode;
    }

    public  void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getGroupOwnerNumber() {
        return groupOwnerNumber;
    }

    public void setGroupOwnerNumber(String groupOwnerNumber) {
        this.groupOwnerNumber = groupOwnerNumber;
    }
}

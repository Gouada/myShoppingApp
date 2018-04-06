package com.example.gouadadopavogui.myshoppingapp.interfaces;

import com.example.gouadadopavogui.myshoppingapp.model.Cart;
import com.example.gouadadopavogui.myshoppingapp.model.CartProducts;
import com.example.gouadadopavogui.myshoppingapp.model.Product;
import com.example.gouadadopavogui.myshoppingapp.model.ShoppingGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by GouadaDopavogui on 31.10.2016.
 */
public interface CartRestService {

    @POST("group/{groupId}/cart/createNewCart")
    Call<Cart> createNewCart(@Path("groupId") long groupId, @Body Cart newCart);

    @GET("group/{groupId}/cart/{cartId}/currentList/language/{language}")
    Call<List<Product>> getCurrentShoppingCartList(@Path("groupId") long groupId, @Path("cartId") long cartId, @Path("language") String language);

    @GET("group/{groupId}/cart/currentCart/language/{language}")
    Call<Cart> getCurrentShoppingCart(@Path("groupId") long groupId, @Path("language") String language);

    //@POST("group/{groupId}/cart/{cartId}/member/{memberId}")
    //Call<Cart> saveCurrentCart(@Path("groupId") long groupId, @Path("cartId") long cartId, @Path("memberId") long memberId, @Body List<Product> cartProducts);


    @POST("group/{groupId}/cart/{cartId}/member/{memberId}/language/{language}")
    Call<Cart> saveCurrentCart(@Path("groupId") long groupId, @Path("cartId") long cartId, @Path("memberId") long memberId,
                               @Path("language") String language, @Body List<CartProducts> cartProducts);


    @GET("group/{groupId}/cart/archive/{cartId}/member/{memberId}/language/{language}")
    Call<Boolean> archiveCurrentCart(@Path("groupId") long groupId, @Path("cartId") long cartId,
                                     @Path("memberId") long memberId, @Path("language") String language);
}

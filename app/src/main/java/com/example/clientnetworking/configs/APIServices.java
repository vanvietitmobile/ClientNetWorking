package com.example.clientnetworking.configs;

import androidx.annotation.NonNull;

import com.example.clientnetworking.models.Data;
import com.example.clientnetworking.models.ModelgetPost;
import com.example.clientnetworking.models.PostusModel;
import com.example.clientnetworking.models.RestModel;
import com.example.clientnetworking.models.UserInfor;
import com.example.clientnetworking.models.UserModels;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIServices {
    @NonNull
    @POST("api/login-account")
    @FormUrlEncoded
    Call<UserModels> loginUser(@Field("email") String email,@Field("hash") String hash);
    @NonNull
    @POST("api/register-account/new")
    @FormUrlEncoded
    Call<UserModels> postUser(@Field("fullname") String fullname,@Field("email") String email,@Field("hash") String hash);
    @NonNull
    @POST("api/get-key-account")
    @FormUrlEncoded
    Call<UserInfor> getKey(@Field("email") String email);
    @Multipart
    @POST("api/edit-user")
    Call<UserInfor> updateUser( @Part("_id") RequestBody _id,
                                @Part("fullname") RequestBody fullname,
                                @Part("birthday") RequestBody birthday,
                                @Part("address") RequestBody address,
                                @Part("phone") RequestBody phone,
                                @Part MultipartBody.Part image,
                                @Part("image") RequestBody images); // name la de request len database
    @POST("api/rest-account")
    @FormUrlEncoded
    Call<RestModel> sendEmail(@Field("email") String email);
    @POST("api/change-account")
    @FormUrlEncoded
    Call<RestModel> changPassByID(@Field("_id") String id,@Field("hash") String hash);
    @POST("api/getID-account")
    @FormUrlEncoded
    Call<Data> detailUser(@Field("_id") String id);
    @Multipart
    @POST("product/api/create-product")
    Call<PostusModel> createProduct(@Part("email") RequestBody email,
                                    @Part("fullname") RequestBody fullname,
                                    @Part("title") RequestBody title,
                                    @Part("content") RequestBody content,
                                    @Part("address") RequestBody address,
                                    @Part("date") RequestBody date,
                                    @Part MultipartBody.Part image,
                                    @Part("image") RequestBody images);
    @GET("product/api/get-all-product")
    Call<ModelgetPost> getAllProduct();
    @POST("product/api/change-active")
    @FormUrlEncoded
    Call<ModelgetPost> changeActive(@Field("_id") String id,@Field("active") Boolean active);
    @POST("product/api/get-product-user")
    @FormUrlEncoded
    Call<ModelgetPost> getProductUser(@Field("email") String email);
    @POST("product/api/delete-product")
    @FormUrlEncoded
    Call<PostusModel> deleteProduct(@Field("_id") String id);
    @POST("product/api/getID-product")
    @FormUrlEncoded
    Call<PostusModel> getIDProducts(@Field("_id") String id);
    @Multipart
    @POST("product/api/update-product")
    Call<ModelgetPost> updateProduct(@Part("_id") RequestBody id,
                                    @Part("title") RequestBody title,
                                    @Part("content") RequestBody content,
                                    @Part("address") RequestBody address,
                                    @Part("date") RequestBody date,
                                    @Part MultipartBody.Part image,
                                    @Part("image") RequestBody images);
}

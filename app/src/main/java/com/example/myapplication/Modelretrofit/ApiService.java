package com.example.myapplication.Modelretrofit;

import com.example.myapplication.Model.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {
    @POST("user/login")
    Call<UserDTO> login(@Body RequestBody body);
    @GET("bris")
    Call<List<Bris>> getBris();
    // bris by userID
    @GET("bris")
    Call<List<Bris>> getUserBris(@Query("userID") String userID);
    @POST("new/user")
    Call<ResponseBody> registerUser(@Body RequestBody body);
}
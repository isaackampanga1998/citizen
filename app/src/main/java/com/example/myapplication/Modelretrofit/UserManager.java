package com.example.myapplication.Modelretrofit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class UserManager {

    private Context context;

    public UserManager(Context context) {
        this.context = context;
    }

    public void fetchUsers() {
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);
    }
}
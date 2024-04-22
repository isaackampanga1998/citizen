package com.example.myapplication.Modelretrofit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BrisManager {

    private Context context;

    public BrisManager(Context context) {
        this.context = context;
    }
    public class UserManager {
        private Context context;
        private ApiService apiService;

        public UserManager(Context context) {
            this.context = context;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://sturdy-thoracic-health.glitch.me/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            this.apiService = retrofit.create(ApiService.class);
        }

        public void fetchBris() {
            Call<List<Bris>> call = apiService.getBris();
            call.enqueue(new Callback<List<Bris>>() {
                @Override
                public void onResponse(Call<List<Bris>> call, Response<List<Bris>> response) {
                    if (response.isSuccessful()) {
                        List<Bris> brisList = response.body();
                        // Handle the list of Bris objects
                    } else {
                        // Handle the error
                    }
                }

                @Override
                public void onFailure(Call<List<Bris>> call, Throwable t) {
                    // Handle the error
                }
            });
        }
        public void fetchUserBris(String userID) {
            Call<List<Bris>> call = apiService.getUserBris(userID);
            call.enqueue(new Callback<List<Bris>>() {
                @Override
                public void onResponse(Call<List<Bris>> call, Response<List<Bris>> response) {
                    if (response.isSuccessful()) {
                        List<Bris> userBrisList = response.body();
                        // Gérer la liste d'objets Bris associés à l'utilisateur
                    } else {
                        // Gérer l'erreur
                    }
                }

                @Override
                public void onFailure(Call<List<Bris>> call, Throwable t) {
                    // Gérer l'erreur
                }
            });
        }
    }
}
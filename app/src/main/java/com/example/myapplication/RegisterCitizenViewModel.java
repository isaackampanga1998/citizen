package com.example.myapplication;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;
import com.example.myapplication.Modelretrofit.UserDTO;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCitizenViewModel extends ViewModel {
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void loginAutentificationWithRetrofit(String email, String password) {
        // Create the JSON object and RequestBody
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("courriel", email);
            paramObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());

        // Create the API service
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);

        // Make the POST request
        Call<UserDTO> call = apiService.login(body);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful()) {
                    UserDTO userDTO = response.body();
                    // Handle the successful login
                    User user = new User(45, userDTO.getNom(), userDTO.getPrenom(), userDTO.getAdresse(), userDTO.getCourriel(), "1234567890", "password");
                    user.setRole(userDTO.getRolesId());
                    user.setUid(userDTO.getId());
                    userLiveData.setValue(user);
                } else {
                    // Handle the unsuccessful login
                    userLiveData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                // Handle the error
                userLiveData.setValue(null);
            }
        });
    }
}
package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForgetPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPassword extends Fragment {

    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    private EditText confirm_password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        // Récupérer une référence au bouton
        Button button = view.findViewById(R.id.reset_acount_buton);
        email = view.findViewById(R.id.editTextEmail);
        password = view.findViewById(R.id.editTextPassword);
        phoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        confirm_password = view.findViewById(R.id.editTextPasswordConfirm);
        // Ajouter un écouteur de clic au bouton
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    forgotPassword();

        }});

        return view;

    }
    /**
     * Vérifie si les champs sont valides
     * @return  Retourne true si la validation est réussie, sinon false
     */
    public boolean areFieldsValid() {
        boolean hasError = true;
        if (!email.getText().toString().contains("@") || !email.getText().toString().contains(".")) {
            email.setError("L'email n'est pas valide");
            hasError = false;
        }
        String phoneNumberText = phoneNumber.getText().toString().trim();
        if (phoneNumberText.isEmpty()) {
            phoneNumber.setError("Le numéro de téléphone est requis");
            hasError = false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            password.setError("Le mot de passe est requis");
            hasError = false;
        }
        if (confirm_password.getText().toString().trim().isEmpty()) {
            confirm_password.setError("La confirmation du mot de passe est requise");
            hasError = false;
        } else if (!(password.getText().toString().trim()).equals(confirm_password.getText().toString().trim())) {
            confirm_password.setError("Les mots de passe ne correspondent pas"+password.getText().toString()+confirm_password.getText().toString());
            hasError = false;
        }
        return hasError;
    }
    public void forgotPassword() {
        if (areFieldsValid()) {
            // Create the JSON object and RequestBody
            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("courriel", email.getText().toString());
                paramObject.put("telephone", phoneNumber.getText().toString());
                paramObject.put("newPassword", password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());

            // Create the API service
            ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);

            // Make the PUT request
            Call<ResponseBody> call = apiService.forgotPassword(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Remplacer le fragment actuel par un nouveau fragment
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, new RegisterCitizen());
                        fragmentTransaction.addToBackStack(null);  // Ajouter la transaction à la pile de retour
                        fragmentTransaction.commit();
                        Toast.makeText(getContext(), "Réinitialisation du mot de passe réussie", Toast.LENGTH_SHORT).show();

                    } else {
                        String errorMessage = "Échec de la réinitialisation du mot de passe";
                        try {
                            errorMessage += ": " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
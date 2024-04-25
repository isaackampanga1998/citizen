package com.example.myapplication;

import static java.lang.Integer.parseInt;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;
import com.example.myapplication.Modelretrofit.UserDTO;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_Up#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_Up extends Fragment {

    private EditText firstName, lastName, email, address, city, agentNumber, phoneNumber, password, confirmPassword;
    private Button signUpButton;
    private int userRole = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initializeViews(view);
        setupTabLayout(view);
        setupSignUpButton();
        return view;
    }

    private void initializeViews(View view) {
        firstName = view.findViewById(R.id.editTextPrenom);
        lastName = view.findViewById(R.id.editTextNom);
        email = view.findViewById(R.id.editTextCourriel);
        address = view.findViewById(R.id.editTextAdresse);
        city = view.findViewById(R.id.editTextCity);
        agentNumber = view.findViewById(R.id.editAgentNumber);
        phoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        password = view.findViewById(R.id.editTextMotDePasse);
        confirmPassword = view.findViewById(R.id.editTextConfirmationMotDePasse);
        signUpButton = view.findViewById(R.id.singUp_button);

    }

    private void setupTabLayout(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        city.setVisibility(View.GONE);
        agentNumber.setVisibility(View.GONE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    //Citoyen 2
                    address.setVisibility(View.VISIBLE);
                    city.setVisibility(View.GONE);
                    agentNumber.setVisibility(View.GONE);
                    userRole = 1;
                } else {
                    //Agent 1
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.VISIBLE);
                    agentNumber.setVisibility(View.VISIBLE);
                    userRole = 2;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupSignUpButton() {
        signUpButton.setOnClickListener(v -> {
            if (areFieldsValid()) {
                User user = createUser();
                registerUser();
             //   new InsertUserAsyncTask().execute(user);
               // replaceFragment(new RegisterCitizen());
                //Toast.makeText(getContext(), "Inscription réussie ! Bienvenue " + user.getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean areFieldsValid() {
        boolean hasError = true;
        // Validation des champs ic tient compte de ca  pour les verification
        // Vérifier si les champs sont vides
        if (userRole == 2) {
            String agentNumberText = agentNumber.getText().toString().trim();
            if (agentNumberText.isEmpty()) {
                agentNumber.setError("Le numero d'agent est requis");
                hasError = false;
            } else if (!agentNumberText.matches("\\d+")) {
                agentNumber.setError("Le numero d'agent doit être composé uniquement de chiffres");
                hasError = false;
            }
        }
        if (firstName.getText().toString().trim().isEmpty()) {
            firstName.setError("Le prénom est requis");
            hasError = false;
        }
        if (lastName.getText().toString().trim().isEmpty()) {
            lastName.setError("Le nom est requis");
            hasError = false;
        }
        String emailText = email.getText().toString().trim();
        if (emailText.isEmpty()) {
            email.setError("L'email est requis");
            hasError = false;
        }
        if (!emailText.contains("@") || !emailText.contains(".")) {
            email.setError("L'email n'est pas valide");
            hasError = false;
        }
        String phoneNumberText = phoneNumber.getText().toString().trim();
        if (phoneNumberText.isEmpty()) {
            phoneNumber.setError("Le numéro de téléphone est requis");
            hasError = false;
        }
        if (!PhoneNumberUtils.isGlobalPhoneNumber(phoneNumberText)) {
            phoneNumber.setError("Le numéro de téléphone n'est pas valide");
            hasError = false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            password.setError("Le mot de passe est requis");
            hasError = false;
        }
        if (confirmPassword.getText().toString().trim().isEmpty()) {
            confirmPassword.setError("La confirmation du mot de passe est requise");
            hasError = false;
        } else if (!(password.getText().toString().trim()).equals(confirmPassword.getText().toString().trim())) {
            confirmPassword.setError("Les mots de passe ne correspondent pas"+password.getText().toString()+confirmPassword.getText().toString());
            hasError = false;
        }

            // Si une erreur a été trouvée, arrêter l'exécution de la méthode
            return hasError; // Retourne true si la validation est réussie, sinon false

    }

    private User createUser() {
        User user = new User();
        user.setFirstName(firstName.getText().toString());
        user.setLastName(lastName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setAddress(address.getText().toString());
        user.setPassword(password.getText().toString());
        return user;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            // Utiliser Room pour insérer l'utilisateur dans la base de données
            MyApp.db.userDao().insertUser(users[0]);
            return null;
        }
    }
    private void registerUser() {
        // Create the JSON object
        String addressDTO = "";
        int numeroAgent = 0;
        if(userRole == 2){
            addressDTO = city.getText().toString();
            numeroAgent= parseInt(agentNumber.getText().toString());
        }else {
            addressDTO = address.getText().toString();
        }
        JSONObject paramObject = new JSONObject();
        UserDTO user = new UserDTO(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString(), address.getText().toString(), phoneNumber.getText().toString(), userRole,password.getText().toString());
        try {
            paramObject.put("nom", lastName.getText().toString());
            paramObject.put("prenom", firstName.getText().toString());
            paramObject.put("adresse", addressDTO);
            paramObject.put("ville", city.getText().toString());
            paramObject.put("courriel", email.getText().toString());
            paramObject.put("telephone", phoneNumber.getText().toString());
            paramObject.put("no_agent", numeroAgent);
            paramObject.put("photo", "https://www.google.com");
            paramObject.put("roles_id", userRole);
            paramObject.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), paramObject.toString());

        // Create the API service
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);

        // Make the POST request
        Call<ResponseBody> call = apiService.registerUser(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Inscription réussie ! Bienvenue " + user.formatted(), Toast.LENGTH_SHORT).show();
                    replaceFragment(new RegisterCitizen());
                } else {
                    String errorMessage = "Échec de l'inscription";
                    try {
                        errorMessage += ": " + response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("Error", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}



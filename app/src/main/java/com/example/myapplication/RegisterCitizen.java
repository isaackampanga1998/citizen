package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;
import com.example.myapplication.Modelretrofit.UserDTO;
import com.example.myapplication.Modelretrofit.UserManager;
import com.example.myapplication.databinding.FragmentRegisterCitizenBinding;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterCitizen extends Fragment {

    // Declare TextViews as instance variables
    private TextView u_email;
    FragmentRegisterCitizenBinding binding;
    private RegisterCitizenViewModel viewModel;
    private TextView u_pass;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Utiliser le binding de l'interface utilisateur pour gonfler la mise en page
         binding = FragmentRegisterCitizenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        viewModel = new ViewModelProvider(this).get(RegisterCitizenViewModel.class);

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Handle the successful login
                Bundle userData = new Bundle();
                userData.putString("user", String.valueOf(user.formatted()));
                Intent intent = new Intent(getActivity(), HomePage.class);
                intent.putExtras(userData);
                startActivity(intent);
            } else {
                // Handle the unsuccessful login
                Toast.makeText(getActivity(), "Échec de la connexion", Toast.LENGTH_SHORT).show();
            }
        });

        // Récupérer une référence au bouton
        binding.registerSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remplacer le fragment actuel par un nouveau fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new Sign_Up());
                fragmentTransaction.addToBackStack(null);  // Ajouter la transaction à la pile de retour
                fragmentTransaction.commit();
            }
        });

        // Ajouter un écouteur de clic au bouton de connexion
        binding.connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Votre logique de connexion ici avec ROOM
                // String email = binding.uEmail.getText().toString();
                // String password = binding.uPassword.getText().toString();
                // new LoginAsyncTask().execute(email, password);

                // Login avec Retrofit
                String email = binding.uEmail.getText().toString();
                String password = binding.uPassword.getText().toString();
                if(!areFieldsValid()){
                viewModel.loginAutentificationWithRetrofit(email, password);
                }
            }
        });

        // Ajouter un écouteur de clic pour le lien de mot de passe oublié
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remplacer le fragment actuel par un nouveau fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new ForgetPassword());
                fragmentTransaction.addToBackStack(null);  // Ajouter la transaction à la pile de retour
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    private class LoginAsyncTask extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... credentials) {
            // Utiliser Room pour vérifier les informations de connexion dans la base de données
            return MyApp.db.userDao().getUserByEmailAndPassword(credentials[0], credentials[1]);
        }

        @Override
        protected void onPostExecute(User user) {
            // Le résultat de la tâche asynchrone, user est null si la connexion a échoué
            if (user != null) {
                // Connexion réussie, l'utilisateur existe dans la base de données
                // Ajoutez ici le code pour gérer la suite du processus de connexion
                Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();

                // Créer un Bundle pour passer les données de l'utilisateur à l'activité HomePage
                Bundle userData = new Bundle();
                userData.putString("user", String.valueOf(user.formatted()));

                // Intent pour lancer l'activité HomePage
                Intent intent = new Intent(getActivity(), HomePage.class);
                intent.putExtras(userData);
                startActivity(intent);

            } else {
                // Échec de la connexion, l'utilisateur n'existe pas ou les informations sont incorrectes
                // Ajoutez ici le code pour gérer l'échec de la connexion

                // Create a new User instance
                User user2 = new User(45, "John", "Doe", "123 Main St", "john.doe@example.com", "1234567890", "password");

                // Créer un Bundle pour passer les données de l'utilisateur à l'activité HomePage
                Bundle userData = new Bundle();
                userData.putString("user", String.valueOf(user2.formatted()));

                // Intent pour lancer l'activité HomePage
                Intent intent = new Intent(getActivity(), HomePage.class);
                intent.putExtras(userData);
                startActivity(intent);


                // Créer une instance de UserManager
                UserManager userManager = new UserManager(getContext());

                // Appeler la méthode fetchUsers
                userManager.fetchUsers();

                Toast.makeText(getContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean areFieldsValid() {
        boolean hasError = false;
        // Validation des champs ic tient compte de ca  pour les verification
        // Vérifier si les champs sont vides

        String emailText = binding.uEmail.getText().toString().trim();
        if (emailText.isEmpty()) {
            binding.uEmail.setError("L'email est requis");
            hasError = true;
        }

        if (binding.uPassword.getText().toString().trim().isEmpty()) {
            binding.uPassword.setError("Le mot de passe est requis");
            hasError = true;
        }

        // Si une erreur a été trouvée, arrêter l'exécution de la méthode
        return hasError; // Retourne true si la validation est réussie, sinon false

    }

    public void loginAutentificationWithRetrofit() {
        if (areFieldsValid()) return;

        // Create the JSON object and RequestBody
        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("courriel", binding.uEmail.getText().toString());
            paramObject.put("password", binding.uPassword.getText().toString());
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
                    Bundle userData = new Bundle();
                    User user = new User(45, userDTO.getNom(), userDTO.getPrenom(), userDTO.getAdresse(), userDTO.getCourriel(), "1234567890", "password");
                    user.setRole(userDTO.getRolesId());
                    user.setUid(userDTO.getId());
                    userData.putString("user", String.valueOf(user.formatted()));
                    Intent intent = new Intent(getActivity(), HomePage.class);
                    intent.putExtras(userData);
                    startActivity(intent);
                } else {
                    // Handle the unsuccessful login
                    Toast.makeText(getActivity(), "Échec de la connexion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                // Handle the error
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}

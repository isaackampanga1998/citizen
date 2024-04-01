package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.User;

public class RegisterCitizen extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_citizen, container, false);

        // Récupérer une référence au bouton
        Button button_refactor = view.findViewById(R.id.sign_in_button);
        Button button_connexion = view.findViewById(R.id.connexion_button);
        TextView textViewForgetenPassWord = view.findViewById(R.id.forgot_password);

        TextView u_email = view.findViewById(R.id.u_email);
        TextView u_pass = view.findViewById(R.id.u_password);

        textViewForgetenPassWord.setOnClickListener(new View.OnClickListener() {
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

        button_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Votre logique de connexion ici
                String email = u_email.getText().toString();
                String password = u_pass.getText().toString();

                new LoginAsyncTask().execute(email, password);
            }
        });

        // Ajouter un écouteur de clic au bouton
        button_refactor.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getContext(), "Échec de la connexion", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

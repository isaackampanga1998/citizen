package com.example.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_Up#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_Up extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Récupérer une référence au bouton
        Button button = view.findViewById(R.id.singUp_button);
        TextView firstName = view.findViewById(R.id.editTextPrenom);
        TextView lastName = view.findViewById(R.id.editTextNom);
        TextView email = view.findViewById(R.id.editTextCourriel);
        TextView address = view.findViewById(R.id.editTextAdresse);

        TextView password = view.findViewById(R.id.editTextMotDePasse);
        TextView confirm_pass = view.findViewById(R.id.editTextConfirmationMotDePasse);

        // Ajouter un écouteur de clic au bouton
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Créer un utilisateur avec les données du formulaire

                String passconfirm = confirm_pass.getText().toString();

                if(passconfirm.equals(password.getText().toString())){


                    User user = new User();
                    user.setFirstName(firstName.getText().toString());
                    user.setLastName(lastName.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setAddress(address.getText().toString());
                    user.setPassword(password.getText().toString());



                    // Exécuter la tâche asynchrone pour insérer l'utilisateur dans la base de données
                    new InsertUserAsyncTask().execute(user);

                    // Remplacer le fragment actuel par un nouveau fragment
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, new RegisterCitizen());
                    fragmentTransaction.addToBackStack(null);  // Ajouter la transaction à la pile de retour
                    fragmentTransaction.commit();
                    Toast.makeText(getContext(), "Inscription réussie ! Bienvenue " + user.getFirstName(), Toast.LENGTH_SHORT).show();
                }else{
                    // Afficher un message d'alerte indiquant que les mots de passe ne correspondent pas
                    Toast.makeText(getContext(), "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            // Utiliser Room pour insérer l'utilisateur dans la base de données
            MyApp.db.userDao().insertUser(users[0]);
            return null;
        }
    }
}
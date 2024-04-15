package com.example.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.User;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sign_Up#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_Up extends Fragment {

    private EditText firstName, lastName, email, address, city, agentNumber, phoneNumber, password, confirmPassword;
    private Button signUpButton;

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
                    address.setVisibility(View.VISIBLE);
                    city.setVisibility(View.GONE);
                    agentNumber.setVisibility(View.GONE);
                } else {
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.VISIBLE);
                    agentNumber.setVisibility(View.VISIBLE);
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
                new InsertUserAsyncTask().execute(user);
                replaceFragment(new RegisterCitizen());
                Toast.makeText(getContext(), "Inscription réussie ! Bienvenue " + user.getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean areFieldsValid() {
        boolean hasError = true;
        // Validation des champs ic tient compte de ca  pour les verification
        // Vérifier si les champs sont vides
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
        } else if (!password.equals(confirmPassword)) {
            confirmPassword.setError("Les mots de passe ne correspondent pas");
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
}



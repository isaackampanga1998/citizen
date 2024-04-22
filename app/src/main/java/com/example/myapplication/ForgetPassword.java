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
                if (areFieldsValid()){
                // Remplacer le fragment actuel par un nouveau fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, new RegisterCitizen());
                fragmentTransaction.addToBackStack(null);  // Ajouter la transaction à la pile de retour
                fragmentTransaction.commit();}
            }
        });

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
}
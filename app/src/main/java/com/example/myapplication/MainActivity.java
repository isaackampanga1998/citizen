package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Activer les bords de l'écran
        EdgeToEdge.enable(this);

        // Créer une instance de votre fragment
        RegisterCitizen monFragment = new RegisterCitizen();

        // Obtenir le gestionnaire de fragments et commencer une transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Remplacer le conteneur par votre fragment
        fragmentTransaction.replace(R.id.container, monFragment);
        fragmentTransaction.commit();
    }
}

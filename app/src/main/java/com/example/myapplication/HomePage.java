package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.User;
import com.example.myapplication.databinding.ActivityHomePageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {

    private User  user ;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new List());

        // Récupérer les données de l'utilisateur de l'intent
        Bundle userData = getIntent().getExtras();
        if (userData != null) {
            String userDataString = userData.getString("user");
            // Faites ce que vous voulez avec ces données...
            this.user = User.parseFormatted(userDataString);

        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {


            if (item.getItemId() == R.id.list) {
                replaceFragment(new List());
                // Actions à effectuer lorsque l'élément Accueil est sélectionné
                return true;
            } else if (item.getItemId() == R.id.location) {
                replaceFragment(new LocationFragment());
                // Actions à effectuer lorsque l'élément Dashboard est sélectionné
                return true;
            } else if (item.getItemId() == R.id.profile) {
                // Actions à effectuer lorsque l'élément Notifications est sélectionné
                replaceFragment(new Profile(this.user) );
                return true;
            }

            return false;
        });


    }

    private void replaceFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);  // Permet d'ajouter la transaction à la pile de retour arrière
        transaction.commit();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.list) {
            // Action à effectuer lorsque le premier élément du menu est sélectionné
            Toast.makeText(this, "Fragment 1 sélectionné", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.location) {
            // Action à effectuer lorsque le deuxième élément du menu est sélectionné
            Toast.makeText(this, "Fragment 2 sélectionné", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.profile) {
            // Action à effectuer lorsque le troisième élément du menu est sélectionné
            Toast.makeText(this, "Fragment 3 sélectionné", Toast.LENGTH_SHORT).show();
            // Remplacer le contenu actuel par le fragment de profil
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new Profile()) // Remplace R.id.container par l'ID de votre conteneur de fragments
                    .addToBackStack(null) // Permet de revenir en arrière
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}

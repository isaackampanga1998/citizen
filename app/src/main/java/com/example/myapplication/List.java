package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment List.
     */
    // TODO: Rename and change types and number of parameters
    public static List newInstance(String param1, String param2) {
        List fragment = new List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        ImageButton boutonImage = rootView.findViewById(R.id.addBtnImg);

        // Ajoutez un écouteur d'événements pour détecter le clic sur le bouton
        boutonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le code à exécuter lorsque le bouton est cliqué
                // Par exemple, afficher un message Toast
                replaceFragment(new RepportFragment());

            }
        });


        // Répéter le contenu de ScrollView pour TabLayout "publics" 5 fois
        LinearLayout listeContentLayout = rootView.findViewById(R.id.liste_content);




        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Vérifier l'onglet sélectionné et afficher un message correspondant
                if (tab.getText().equals("Mes Bris")) {
                    listeContentLayout.removeAllViews();

                    // Répéter le contenu de ScrollView pour TabLayout "bris" 3 fois
                    for (int i = 0; i < 1; i++) {
                        View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                        listeContentLayout.addView(view);
                    }

                } else if (tab.getText().equals("Publics")) {
                    listeContentLayout.removeAllViews();

                    for (int i = 0; i < 3; i++) {
                        View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                        listeContentLayout.addView(view);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Ne rien faire lorsque l'onglet est désélectionné
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Ne rien faire lorsque l'onglet est à nouveau sélectionné
            }
        });

        return rootView;
    }

    // Méthode pour remplacer le fragment actuel par un autre fragment
    private void replaceFragment(Fragment newFragment) {
        // Obtenir le FragmentManager parent
        FragmentManager fragmentManager = getParentFragmentManager();

        // Commencer une nouvelle transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Remplacer le fragment actuel par le nouveau fragment
        fragmentTransaction.replace(R.id.container, newFragment);

        // Ajouter la transaction à la pile de retour
        fragmentTransaction.addToBackStack(null);

        // Valider la transaction
        fragmentTransaction.commit();
    }

}

package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class List extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private int role = 2;

    public List() {}

    public static List newInstance(String param1, String param2) {
        List fragment = new List();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ImageButton boutonImage = rootView.findViewById(R.id.addBtnImg);
        boutonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RepportFragment());
            }
        });

        LinearLayout listeContentLayout = rootView.findViewById(R.id.liste_content);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);

        if(role == 1){
            setListeContentLayout(listeContentLayout, inflater);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getText().equals("Mes Bris")) {
                        setListeContentLayout(listeContentLayout, inflater);
                    } else if (tab.getText().equals("Publics")) {
                        listeContentLayout.removeAllViews();
                        for (int i = 0; i < 3; i++) {
                            View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                            listeContentLayout.addView(view);
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        } else if(role == 2){
            tabLayout.removeTabAt(0);
            tabLayout.removeTabAt(0);
            TabLayout.Tab newTab = tabLayout.newTab().setText("La liste de bris");
            tabLayout.addTab(newTab);
            listeContentLayout.removeAllViews();
            for (int i = 0; i < 4; i++) {
                View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                listeContentLayout.addView(view);
                ImageView img = view.findViewById(R.id.image_main);

                img.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Vous avez cliqué ", Toast.LENGTH_SHORT).show();
                        replaceFragment(new BrisInfo());
                    }
                });
            }

        }
        return rootView;
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public  void setListeContentLayout(LinearLayout listeContentLayout, LayoutInflater inflater){
        listeContentLayout.removeAllViews();
        for (int i = 0; i < 1; i++) {
            View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
            listeContentLayout.addView(view);
            ImageView img = view.findViewById(R.id.image_main);
            img.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Supprimer Le Bris ?");
                    builder.setMessage("Voulez-vous supprimer Le Bris X ?");
                    builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Vous avez supprimé", Toast.LENGTH_SHORT).show();
                            setListeContentLayout(listeContentLayout, inflater);
                        }
                    });
                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "Vous avez annulé", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
    }
}

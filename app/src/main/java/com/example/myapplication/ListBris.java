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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.Bris;
import com.example.myapplication.Modelretrofit.BrisManager;
import com.example.myapplication.Modelretrofit.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBris extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private int role;

    User user;
    private BrisManager brisManager;

    public ListBris() {}

    public ListBris(User user){
        this.role = user.getRole();
        this.user = user;
    }

    public static ListBris newInstance(String param1, String param2) {
        ListBris fragment = new ListBris();
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
        brisManager = new BrisManager(getContext());
        brisManager.fetchBris();

        ImageButton boutonImage = rootView.findViewById(R.id.addBtnImg);
        boutonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RepportFragment());
            }
        });

        LinearLayout listeContentLayout = rootView.findViewById(R.id.liste_content);
        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        BrisListMySelf(listeContentLayout, inflater);
        if(role == 1){
            BrisListMySelf(listeContentLayout, inflater);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getText().equals("Mes Bris")) {
                        BrisListMySelf(listeContentLayout, inflater);
                    } else if (tab.getText().equals("Publics")) {
                        listeContentLayout.removeAllViews();
                        BrisListPublic(listeContentLayout, inflater);
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
            BrisListAgent(listeContentLayout, inflater);
            boutonImage.setVisibility(View.INVISIBLE);
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
/*
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
                            BrisListMySelf(listeContentLayout, inflater);
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
*/
    public void BrisListAgent(LinearLayout listeContentLayout, LayoutInflater inflater) {
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);
        Call<List<Bris>> call = apiService.getBris();
        call.enqueue(new Callback<List<Bris>>() {
            @Override
            public void onResponse(Call<List<Bris>> call, Response<List<Bris>> response) {
                if (response.isSuccessful()) {
                    List<Bris> brisList = response.body();
                    for (int i = 0; i < brisList.size(); i++) {
                        Bris bris = brisList.get(i);
                        if (!bris.getEtat().equals("Terminer")) {



                            View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                            listeContentLayout.addView(view);
                            ImageView img = view.findViewById(R.id.image_main);
                            TextView name = view.findViewById(R.id.bris_name);
                            TextView state = view.findViewById(R.id.bris_etat);
                            TextView username = view.findViewById(R.id.username);
                            TextView time = view.findViewById(R.id.time);

                            name.setText(bris.getNomBris());
                            state.setText(bris.getEtat());
                            bris.setDate(extraireDate(bris.getDate()));
                            time.setText(bris.getDate());
                            username.setText(bris.getUsername());
                            bris.setImage(bris.getImage().replace("http", "https"));
                            Picasso.get().load(bris.getImage()).into(img);

                            img.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Toast.makeText(getContext(), "Vous avez cliqué " + bris.getImage(), Toast.LENGTH_SHORT).show();
                                    replaceFragment(new BrisInfo(bris, user));
                                }
                            });
                        }

                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve bris list", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Bris>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void BrisListPublic(LinearLayout listeContentLayout, LayoutInflater inflater) {
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);
        Call<List<Bris>> call = apiService.getBris();
        call.enqueue(new Callback<List<Bris>>() {
            @Override
            public void onResponse(Call<List<Bris>> call, Response<List<Bris>> response) {
                if (response.isSuccessful()) {
                    List<Bris> brisList = response.body();
                    for (int i = 0; i < brisList.size(); i++) {
                        Bris bris = brisList.get(i);
                        if (!bris.getEtat().equals("Terminer")) {
                            if(!bris.getIdUtilisateur().equals(user.getUid())){
                            View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                            listeContentLayout.addView(view);
                            ImageView img = view.findViewById(R.id.image_main);
                            TextView name = view.findViewById(R.id.bris_name);
                            TextView state = view.findViewById(R.id.bris_etat);
                            TextView username = view.findViewById(R.id.username);
                            TextView time = view.findViewById(R.id.time);

                            name.setText(bris.getNomBris());
                            state.setText(bris.getEtat());
                            bris.setDate(extraireDate(bris.getDate()));
                            time.setText(bris.getDate());
                            username.setText(bris.getUsername());
                            bris.setImage(bris.getImage().replace("http", "https"));
                            Picasso.get().load(bris.getImage()).into(img);

                        }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve bris list", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Bris>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void BrisListMySelf(LinearLayout listeContentLayout, LayoutInflater inflater) {
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);
        Call<List<Bris>> call = apiService.getBris();
        call.enqueue(new Callback<List<Bris>>() {
            @Override
            public void onResponse(Call<List<Bris>> call, Response<List<Bris>> response) {
                if (response.isSuccessful()) {
                    listeContentLayout.removeAllViews();
                    List<Bris> brisList = response.body();
                    for (int i = 0; i < brisList.size(); i++) {
                        Bris bris = brisList.get(i);
                        if (!bris.getEtat().equals("Terminer")) {
                            if(bris.getIdUtilisateur().equals(user.getUid())){


                            View view = inflater.inflate(R.layout.info_content, listeContentLayout, false);
                            listeContentLayout.addView(view);
                            ImageView img = view.findViewById(R.id.image_main);
                            TextView name = view.findViewById(R.id.bris_name);
                            TextView state = view.findViewById(R.id.bris_etat);
                            TextView username = view.findViewById(R.id.username);
                            TextView time = view.findViewById(R.id.time);

                            name.setText(bris.getNomBris());
                            state.setText(bris.getEtat());
                            bris.setDate(extraireDate(bris.getDate()));
                            time.setText(bris.getDate());
                            username.setText(bris.getUsername());
                            bris.setImage(bris.getImage().replace("http", "https"));
                            Picasso.get().load(bris.getImage()).into(img);

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
                                            BrisListMySelf(listeContentLayout, inflater);
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
                } else {
                    Toast.makeText(getActivity(), "Failed to retrieve bris list", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Bris>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  String extraireDate(String chaine) {
        try {
            SimpleDateFormat formatEntree = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat formatSortie = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatEntree.parse(chaine);
            return formatSortie.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

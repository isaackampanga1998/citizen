package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.Bris;
import com.example.myapplication.Modelretrofit.RetrofitClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrisInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrisInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bris bris;
    private User user;

    public BrisInfo() {
        // Required empty public constructor

    }

    public BrisInfo(Bris bris, User user) {
        // Required empty public constructor
        this.bris = bris;
        this.user = user;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrisInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static BrisInfo newInstance(String param1, String param2) {
        BrisInfo fragment = new BrisInfo();
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
        View view = inflater.inflate(R.layout.fragment_bris_info, container, false);

        // Définir les valeurs pour le Spinner
        String[] spinnerValues = {"En analyse", "En double", "Terminer"};

        // Obtenir une référence vers le Spinner
        Spinner spinner = view.findViewById(R.id.bris_state_spinner);

        // Créer un ArrayAdapter et le lier au Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView username = view.findViewById(R.id.user_name);
        username.setText("Utilisateur: " + bris.getUsername());

        TextView user_id = view.findViewById(R.id.user_id);
        user_id.setText("ID : " + bris.getId());

        TextView nomBris = view.findViewById(R.id.bris_name);
        nomBris.setText("Nom du bris : " + bris.getNomBris());

        TextView date = view.findViewById(R.id.bris_date);
        date.setText("Date de publication : " + bris.getDate());

        ImageView img = view.findViewById(R.id.imageView);
        Picasso.get().load(bris.getImage()).into(img);

        Button del = view.findViewById(R.id.bris_delete_button);

       del.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v) {
               deleteBris();
           }
       });

       Button submit = view.findViewById(R.id.bris_submit_button);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String selectedValue = spinner.getSelectedItem().toString();
                updateBris(selectedValue);
                // Afficher la valeur sélectionnée dans un Toast
                Toast.makeText(getActivity(), "Selected value: " + selectedValue, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void deleteBris() {
        // Create the API service
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);

        // Make the DELETE request to delete the bris
        Call<ResponseBody> call = apiService.deleteBris(bris.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle successful deletion
                    Toast.makeText(getActivity(), "Bris deleted successfully", Toast.LENGTH_SHORT).show();
                    replaceFragment(new ListBris(user));
                    // Optionally, you may want to refresh the list of bris here
                } else {
                    // Handle unsuccessful deletion
                    Toast.makeText(getActivity(), "Failed to delete bris", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle the error
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBris(String newBrisState) {
        // Create the API service
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);

        // Create a JSON object to hold the new state
        JsonObject newStateJson = new JsonObject();
        newStateJson.addProperty("newState", newBrisState);

        // Convert the JSON object to a RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), newStateJson.toString());

        // Make the PUT request to update the bris
        Call<ResponseBody> call = apiService.updateBris(bris.getId(), requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle successful update
                    Toast.makeText(getActivity(), "Bris updated successfully", Toast.LENGTH_SHORT).show();
                    replaceFragment(new ListBris(user));
                    // Optionally, you may want to refresh the UI or fetch the updated bris data
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(getActivity(), "Failed to update bris", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle the error
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
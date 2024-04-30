package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RepportFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_SELECTION = 102;

    private ImageView imageView;
    private Spinner selectSpinner;
    private Button takePhotoBtn, choosePhotoBtn, saveFormBtn;

    // Variable pour stocker le chemin de l'image
    private String imagePath;

    private User user;

    public RepportFragment() {
        // Required empty public constructor
    }

    public RepportFragment(User user) {
        // Required empty public constructor
        this.user = user;
    }

    public static RepportFragment newInstance(String param1, String param2) {
        RepportFragment fragment = new RepportFragment();
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
        return inflater.inflate(R.layout.fragment_repport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imageView);
        selectSpinner = view.findViewById(R.id.select);
        takePhotoBtn = view.findViewById(R.id.take_photo_btn);
        choosePhotoBtn = view.findViewById(R.id.choise_photo_btn);
        saveFormBtn = view.findViewById(R.id.save_form_btn);

        // Populating spinner with items
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("En analyse");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpinner.setAdapter(spinnerAdapter);

        // Action to take photo
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Action to choose photo
        choosePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelectionIntent();
            }
        });

        // Action to save form
        saveFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageSelectionIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_SELECTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    imageView.setImageBitmap((android.graphics.Bitmap) extras.get("data"));
                }
            } else if (requestCode == REQUEST_IMAGE_SELECTION && data != null) {
                // Récupérer le chemin de l'image sélectionnée
                android.net.Uri selectedImageUri = data.getData();
                imagePath = selectedImageUri.toString();
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveForm() {
        // Créer un objet JSON pour stocker les données
        JSONObject reportData = new JSONObject();
        try {
            // Ajouter les données pertinentes à l'objet JSON
            reportData.put("nomBris", ((EditText) getView().findViewById(R.id.nomRepport)).getText().toString());
            reportData.put("adresse", ((EditText) getView().findViewById(R.id.Adresse)).getText().toString());
            reportData.put("state", selectSpinner.getSelectedItem().toString());
            reportData.put("username", user.getFirstName() + " " + user.getLastName().toUpperCase().charAt(0));
            reportData.put("userID", user.getUid());
            // Ajouter le chemin de l'image si elle a été sélectionnée

                File imageFile = new File(imagePath);
                RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);





            // Enregistrer l'objet JSON dans un fichier ou l'envoyer à l'API
            // Pour enregistrer dans un fichier
            //saveJSONToFile(reportData.toString());

            // Pour envoyer à l'API, utilisez reportData.toString() pour obtenir la chaîne JSON
            //sendFormDataToServer(reportData);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), reportData.toString());

            // Create the API service
            ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);


            // Make the POST request
            //Call<ResponseBody> call = apiService.registerBris(body);
            Call<ResponseBody> call = apiService.registerBris(imagePart, RequestBody.create(MediaType.parse("application/json"), reportData.toString()));







            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Inscription réussie ! Bienvenue " + user.formatted(), Toast.LENGTH_SHORT).show();
                        replaceFragment(new ListBris(user));
                    } else {
                        String errorMessage = "Échec de l'inscription";
                        try {
                            errorMessage += ": " + response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("Error", errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveJSONToFile(String jsonString) {
        // Logique pour enregistrer la chaîne JSON dans un fichier
        try {
            FileOutputStream fos = requireContext().openFileOutput("form_data.json", Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
            Toast.makeText(requireContext(), "Form data saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendFormDataToServer(JSONObject formData) {
        // Créer une requête POST avec Volley
        String url = "https://sturdy-thoracic-health.glitch.me/upload"; // Remplacez URL_DU_SERVEUR par l'URL de votre serveur
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, formData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Gérer la réponse du serveur
                        Toast.makeText(requireContext(), "Form data uploaded successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérer les erreurs de la requête
                        Toast.makeText(requireContext(), "Error uploading form data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Ajouter la requête à la file d'attente Volley
        Volley.newRequestQueue(requireContext()).add(request);
    }
    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
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

import com.example.myapplication.Model.User;
import com.example.myapplication.Modelretrofit.ApiService;
import com.example.myapplication.Modelretrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                // Récupérer le chemin de l'image sélectionnée à partir de l'URI
                android.net.Uri selectedImageUri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d("Image Path", "Image Path: " + imagePath);
                } else {
                    Log.e("Error", "Cursor is null");
                }
                imageView.setImageURI(selectedImageUri);
            }
        }
    }

    private void saveForm() {
        // Créer un objet JSON pour stocker les données
        JSONObject reportData = new JSONObject();
        try {
            // Ajouter les données pertinentes à l'objet JSON
            reportData.put("nameBris", ((EditText) getView().findViewById(R.id.nomRepport)).getText().toString());
            reportData.put("address", ((EditText) getView().findViewById(R.id.AdresseBris)).getText().toString());
            reportData.put("state", selectSpinner.getSelectedItem().toString());
            reportData.put("username", user.getFirstName() + " " + user.getLastName().toUpperCase().charAt(0));
            reportData.put("userID", user.getUid());
            // Ajouter le chemin de l'image si elle a été sélectionnée
            if (imagePath != null && !imagePath.isEmpty()) {
                // Convertir le fichier en un objet RequestBody
                File file = new File(imagePath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                // Ajouter l'image à la requête

                Log.e("chaine", reportData.toString());
                uploadImage(reportData.toString(), imagePart);
            } else {
                // Gérer le cas où aucune image n'est sélectionnée
                Toast.makeText(requireContext(), "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void uploadImage(String data, MultipartBody.Part imagePart) {
        // Créer une instance de l'API service
        ApiService apiService = RetrofitClient.getClient("https://sturdy-thoracic-health.glitch.me/").create(ApiService.class);
        //ApiService apiService = RetrofitClient.getClient("http:/127.0.0.1:4000/").create(ApiService.class);

        // Créer un objet RequestBody pour les autres données JSON
        RequestBody dataRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), data);

        // Appeler la méthode d'API pour télécharger l'image
        Call<ResponseBody> call = apiService.registerBris(imagePart, dataRequestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Image uploaded successfully
                    Toast.makeText(requireContext(), "Image téléchargée avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(requireContext(), "Erreur: " + errorBody, Toast.LENGTH_SHORT).show();
                        Log.e("UNE ERREUR", errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network failure
                Toast.makeText(requireContext(), "Échec de la connexion réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ECHEC", t.getMessage());
            }
        });
    }


}

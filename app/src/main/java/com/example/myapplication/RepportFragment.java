package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    public RepportFragment() {
        // Required empty public constructor
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
        spinnerItems.add("En cours de traitement");
        spinnerItems.add("Pas reaction");
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
                imageView.setImageURI(data.getData());
            }
        }
    }

    private void saveForm() {
        // Logic to save form data
        // Créer un objet JSON pour stocker les données
        JSONObject reportData = new JSONObject();
        try {
            // Ajouter les données pertinentes à l'objet JSON
            reportData.put("selectedItem", selectSpinner.getSelectedItem().toString());
            // Vous pouvez également ajouter d'autres données, telles que l'image sélectionnée
            // par exemple, reportData.put("imagePath", imagePath);

            // Convertir l'objet JSON en chaîne
            String jsonString = reportData.toString();

            // Écrire la chaîne JSON dans un fichier
            writeJSONToFile(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void writeJSONToFile(String jsonString) {
        try {
            // Créer un objet JSON à partir de la chaîne JSON
            JSONObject jsonObject = new JSONObject(jsonString);

            // Lire le fichier JSON existant ou créer un nouveau tableau JSON
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(FileUtils.readFromFile(getContext(), "report.json"));
            } catch (JSONException e) {
                jsonArray = new JSONArray();
            }

            // Ajouter le nouvel objet JSON au tableau JSON
            jsonArray.put(jsonObject);

            // Écrire le tableau JSON dans le fichier
            FileWriter file = new FileWriter(getContext().getFilesDir().getPath() + "/report.json");
            file.write(jsonArray.toString());
            file.flush();
            file.close();

            Toast.makeText(requireContext(), "Form saved", Toast.LENGTH_SHORT).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}


class FileUtils {

    // Méthode pour écrire dans un fichier
    public static void writeToFile(Context context, String filename, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lire à partir d'un fichier
    public static String readFromFile(Context context, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

package com.example.myapplication;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Model.User;
import com.example.myapplication.databinding.FragmentProfileBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    private User user;
    private FragmentProfileBinding binding;

    public Profile(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.textViewNom.setText(this.user.getLastName() + " " + this.user.getRole());
        binding.textViewPrenom.setText(this.user.getFirstName());
        binding.textViewEmail.setText(this.user.getEmail());
        binding.textViewAdresse.setText(this.user.getAddress());

        binding.deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirmation")
                        .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // L'utilisateur a confirmé, lancez l'activité RegisterCitizen
                                Intent intent = new Intent(getActivity(), RegisterCitizen.class);
                                startActivity(intent);
                                getActivity().finish(); // Fermez l'activité actuelle
                            }
                        })
                        .setNegativeButton(android.R.string.no, null) // L'utilisateur a refusé, ne faites rien
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
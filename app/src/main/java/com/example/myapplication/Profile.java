package com.example.myapplication;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView textViewNom = view.findViewById(R.id.textViewNom);
        textViewNom.setText(this.user.getLastName());

        TextView textViewPrenom = view.findViewById(R.id.textViewPrenom);
        textViewPrenom.setText(this.user.getFirstName());

        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewEmail.setText(this.user.getEmail());

        TextView textViewAddress = view.findViewById(R.id.textViewAdresse);
        textViewAddress.setText(this.user.getAddress());


        // Inflate the layout for this fragment
        return view;
    }

    public Profile(User user){
        this.user = user;
    }





}
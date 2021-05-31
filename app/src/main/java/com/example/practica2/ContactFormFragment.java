package com.example.practica2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.practica2.Utils.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ContactFormFragment extends Fragment {

    private EditText nameET;
    private EditText emailET;
    private EditText messageET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_form, container, false);

        nameET = rootView.findViewById(R.id.edittextName);
        emailET = rootView.findViewById(R.id.edittextEmail);
        messageET = rootView.findViewById(R.id.edittextMessage);

        rootView.findViewById(R.id.send).setOnClickListener(view -> {
            submitForm();
        });

        return rootView;
    }

    private void submitForm() {
        ArrayList<String> errors = new ArrayList<>();

        if(nameET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.name));
        }

        if(emailET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.email));
        }

        if(messageET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.message));
        }

        if(errors.size() > 0){
            StringBuilder message = new StringBuilder();
            for (int counter = 0; counter < errors.size(); counter++) {
                message.append(" - ").append(errors.get(counter)).append("\n");
            }

            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.form_incomplete))
                    .setMessage(message.toString())
                    .setPositiveButton(android.R.string.yes, null)
                    .show();
        }else{
            sendDetailsEmail();
        }
    }

    private void sendDetailsEmail(){
        StringBuilder message = new StringBuilder();

        message.append(getResources().getString(R.string.name)).append(": ")
                .append(nameET.getText().toString().trim()).append("\n");
        message.append(getResources().getString(R.string.email)).append(": ")
                .append(emailET.getText().toString().trim()).append("\n");
        message.append(getResources().getString(R.string.message)).append(": ")
                .append(messageET.getText().toString().trim()).append("\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Detalles del contacto");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@games.cat"});
        intent.putExtra(Intent.EXTRA_TEXT, message.toString());

        startActivity(Intent.createChooser(intent, "Enviar detalles por Email"));
    }
}
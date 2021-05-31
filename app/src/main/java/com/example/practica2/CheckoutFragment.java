package com.example.practica2;

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


public class CheckoutFragment extends Fragment {
    private Cursor cursor;

    private String paymentMethod;
    private EditText nameET;
    private EditText addressET;
    private EditText phoneET;
    private EditText emailET;

    private TextView totalTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.paymentMethod);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.paymentMastercard:
                        paymentMethod = getResources().getString(R.string.mastercard);
                        break;
                    case R.id.paymentVisa:
                        paymentMethod = getResources().getString(R.string.visa);
                        break;
                }
            }
        });

        totalTV = rootView.findViewById(R.id.totalText);
        totalTV.setText(getTotal(cursor));

        nameET = rootView.findViewById(R.id.edittextName);
        addressET = rootView.findViewById(R.id.edittextAddress);
        phoneET = rootView.findViewById(R.id.edittextPhone);
        emailET = rootView.findViewById(R.id.edittextEmail);

        rootView.findViewById(R.id.send).setOnClickListener(view -> {
            submitForm();
        });

        return rootView;
    }

    private String getTotal(Cursor cursor){
        cursor.moveToFirst();
        double total = 0;

        for(int i = 0; i < cursor.getCount(); i++){
            total += cursor.getDouble(DatabaseHelper.GameTable.PRICE) *
                    cursor.getInt(DatabaseHelper.GameTable.CART_COUNT)  ;
            cursor.moveToNext();
        }

        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(total);
    }

    public void setData(Cursor cursor){
        this.cursor = cursor;
    }

    private void submitForm() {
        ArrayList<String> errors = new ArrayList<>();

        if(nameET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.name));
        }

        if(addressET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.address));
        }

        if(phoneET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.phone));
        }

        if(emailET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.email));
        }

        if(paymentMethod == null || paymentMethod.equals("")){
            errors.add(getResources().getString(R.string.patment_method));
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
        message.append(getResources().getString(R.string.address)).append(": ")
                .append(addressET.getText().toString().trim()).append("\n");
        message.append(getResources().getString(R.string.phone)).append(": ")
                .append(phoneET.getText().toString().trim()).append("\n");
        message.append(getResources().getString(R.string.email)).append(": ")
                .append(emailET.getText().toString().trim()).append("\n");

        message.append("\n");
        message.append("Items: ").append("\n");

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            DecimalFormat format = new DecimalFormat("#.##");
               message.append(" - ").append(cursor.getString(DatabaseHelper.GameTable.NAME))
                       .append("\t");
               message.append(format.format(cursor.getDouble(DatabaseHelper.GameTable.PRICE)))
                       .append("€").append("\t");
               message.append(cursor.getDouble(DatabaseHelper.GameTable.CART_COUNT))
                       .append(" uds.").append("\t").append("\n");

           cursor.moveToNext();
        }

        message.append("\n");
        message.append("Total: ").append(totalTV.getText().toString()).append("\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Detalles de la compra");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailET.getText().toString().trim()});
        intent.putExtra(Intent.EXTRA_TEXT, message.toString());

        startActivity(Intent.createChooser(intent, "Enviar detalles por Email"));
    }
}
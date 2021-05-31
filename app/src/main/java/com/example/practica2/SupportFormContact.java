package com.example.practica2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.practica2.Utils.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class SupportFormContact extends Fragment {

    private EditText nameET;
    private EditText emailET;
    private EditText invoiceET;
    private EditText messageET;

    private ImageView mImageView;
    private Uri mImageUri;

    private final int REQUEST_GALLERY = 1;
    private final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_support_form_contact, container, false);

        nameET = rootView.findViewById(R.id.edittextName);
        emailET = rootView.findViewById(R.id.edittextEmail);
        invoiceET = rootView.findViewById(R.id.edittextInvoice);
        messageET = rootView.findViewById(R.id.edittextMessage);

        mImageView = rootView.findViewById(R.id.supportImage);

        rootView.findViewById(R.id.send).setOnClickListener(view -> {
            submitForm();
        });

        rootView.findViewById(R.id.imageButton).setOnClickListener(view -> {
            addImage();
        });

        return rootView;
    }

    private void addImage() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.image_option))
                .setPositiveButton(getResources().getString(R.string.gallery), (dialog, id) -> {
                    dispatchOpenGalleryIntent();
                })
                .setNegativeButton(getResources().getString(R.string.camera), (dialog, id) -> {
                    dispatchTakePictureIntent();
                })
                .show();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.error))
                    .setPositiveButton(getResources().getString(android.R.string.yes),null)
                    .show();
        }

    }

    public void dispatchOpenGalleryIntent() {
        Intent takePictureIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        try {
            startActivityForResult(takePictureIntent, REQUEST_GALLERY);
        } catch (ActivityNotFoundException e) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.error))
                    .setPositiveButton(getResources().getString(android.R.string.yes),null)
                    .show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            for (String key: extras.keySet())
            {
                Log.d ("myApplication", key + " is a key in the bundle");
            }
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setImage(imageBitmap);

        }
        else if (requestCode == REQUEST_GALLERY && resultCode == getActivity().RESULT_OK)
        {
            Uri imageUri = data.getData();

            InputStream imageStream = null;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                setImage(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void setImage(Bitmap bm){
        String destFolder = getActivity().getCacheDir().getAbsolutePath();

        FileOutputStream out = null;
        File file = new File(destFolder + "/myBitamp.png");
        try {
            //out = new FileOutputStream(destFolder + "/myBitamp.png");
            out = new FileOutputStream (file, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        Uri uri = Uri.fromFile(file);
        mImageView.setImageURI(null);
        mImageView.setImageURI(uri);
        mImageUri = uri;
    }

    private void submitForm() {
        ArrayList<String> errors = new ArrayList<>();

        if(nameET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.name));
        }

        if(emailET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.email));
        }

        if(invoiceET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.invoice_number));
        }

        if(messageET.getText().toString().trim().equals("")){
            errors.add(getResources().getString(R.string.message));
        }

        if(mImageUri == null){
            errors.add(getResources().getString(R.string.image));
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
        message.append(getResources().getString(R.string.invoice_number)).append(": ")
                .append(invoiceET.getText().toString().trim()).append("\n");
        message.append(getResources().getString(R.string.message)).append(": ")
                .append(messageET.getText().toString().trim()).append("\n");

        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                getActivity().getApplicationContext().getPackageName() + ".provider",
                new File(mImageUri.getPath()));


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Detalles del soporte");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@games.cat"});
        intent.putExtra(Intent.EXTRA_TEXT, message.toString());
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivity(Intent.createChooser(intent, "Enviar detalles por Email"));
    }
}
package com.example.inchkyle.cypherback;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by inchkyle on 11/1/16.
 */

//This Activity allows users to choose if they want to use the camera or type the barcode out.
//This is a popup window
public class ScanOrTypeActivity extends Activity{
    static final int TYPE_METHOD_REQUEST = 30;  // The request code for typing
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan_or_type_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

//        int height = dm.heightPixels;
//        int width = dm.widthPixels;

        int height = 1090;
        int width = 900;

        ColorDrawable dw = new ColorDrawable(0xb0000000);



        getWindow().setLayout((int) (width*.8), (int) (height*.8));
//        getWindow().setBackgroundDrawable(dw);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //THIS SHOULD BE FOR TYPING

        // Check which request we're responding to
        if (requestCode == TYPE_METHOD_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                String barcode = intent.getStringExtra("result");

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", barcode);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }

        else {
            //BELOW IS FOR THE CAMERA
            //retrieve scan result
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult.getContents() != null) {

                String scanContent = scanningResult.getContents();
//            String scanFormat = scanningResult.getFormatName();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", scanContent);

                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        }
    }

    public void type_barcode(View v){
        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");
            System.out.println("Got the data");
        }
        System.out.println("Got the data");


        Intent intent = new Intent(ScanOrTypeActivity.this, TypeBarcodeActivity.class);
        intent.putExtra("User", user);

        startActivityForResult(intent, 30);

    }

    public void camera_scan(View v){
        int current_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If we have permission to use the Camera use it!
        if (current_permission == PackageManager.PERMISSION_GRANTED) {
            System.out.println("We have the permissions");
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();

        }

        //We dont have permission... Just tell them to go add permissions
        else {
            Toast.makeText(this, "Go to Settings and turn Camera Permissions On!",
                    Toast.LENGTH_SHORT).show();
        }

    }
}


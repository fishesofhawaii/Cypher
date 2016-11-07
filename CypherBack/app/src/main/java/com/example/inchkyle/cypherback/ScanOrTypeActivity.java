package com.example.inchkyle.cypherback;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan_or_type_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.8));
        System.out.println("pop it up!");
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
            System.out.println("Request code : " + requestCode + " Result Code : " + resultCode);
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
        Intent intent = new Intent(ScanOrTypeActivity.this, TypeBarcodeActivity.class);
        startActivityForResult(intent, 30);

    }

    public void camera_scan(View v){
        int current_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //If we have permission to use the Camera use it!
        if (current_permission == PackageManager.PERMISSION_GRANTED) {
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


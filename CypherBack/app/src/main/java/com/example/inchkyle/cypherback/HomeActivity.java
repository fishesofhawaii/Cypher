package com.example.inchkyle.cypherback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by inchkyle on 10/31/16.
 */

public class HomeActivity extends AppCompatActivity {
    static String url = "http://10.0.0.117:8000/questions/questionsbyuser";
    static final int BARCODE_METHOD_REQUEST = 20;  // The request code

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");
            user.set_locations();
        }
    }

    //Clicking the update update button should retrieve database values for the employee again
    public void update_click(View v) {
        Toast.makeText(this, "Another post to get user spec data", Toast.LENGTH_SHORT).show();
    }

    //Go to the history page Eventually
    public void history_click(View v) {
        Toast.makeText(this, "HistoryClick", Toast.LENGTH_SHORT).show();
    }

    //User clicked on Scan barcode, bring up popup
    public void scan_barcode_click(View v) {
        Intent typeLocation_intent = new Intent(HomeActivity.this, ScanOrTypeActivity.class);
        startActivityForResult(typeLocation_intent, BARCODE_METHOD_REQUEST);
        System.out.println("Scan barcode click");

    }

    //The request code doesnt matter so long as it is an okay.
    //If the barcode is in the users database we can go to the Items page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("result");

            if (user.check_location(barcode)) {

                user.populate_location(barcode);

                //Start activity that pulls up location questions
                Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);

                intent.putExtra("User", user);
//Broken, I think arraylist isnt serializable
//                System.out.println("Put it in the intent");
//                startActivity(intent);
            }
            else {
                Toast.makeText(this, "This Location is not on your route!" +
                        "\nScan a Location that is on your route!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
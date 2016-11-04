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
    static String url = "http://35.12.211.131:8000/questions/questionsbyuser";
    static final int BARCODE_METHOD_REQUEST = 20;  // The request code

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            Bundle bundle = home_intent.getExtras();

            String payroll_id = bundle.getString("ID");
            String json_str = bundle.getString("JSON_str");

            user = new User(payroll_id, json_str);

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

    }

    //The request code doesnt matter so long as it is an okay.
    //If the barcode is in the users database we can go to the Items page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("result");
            System.out.println("Here yoooooooo " + barcode);
            if (user.check_location(barcode)) {

                //Start activity that pulls up devices
                Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("ID", user.payroll_id);
                bundle.putString("JSON_str", user.employee_json);

                intent.putExtras(bundle);
                startActivity(intent);



            }
            else {
                Toast.makeText(this, "This Location is not on your route!" +
                        "\nScan a Location that is on your route!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

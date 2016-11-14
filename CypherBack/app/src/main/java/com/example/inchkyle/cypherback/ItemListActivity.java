package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by inchkyle on 11/3/16.
 */

//The purpose of this activity is to show all the Items, when clicked user will get popup
//to scan or type, then will be redirected to specific item questions
public class ItemListActivity extends AppCompatActivity {
    User user;

    static final int BARCODE_METHOD_REQUEST = 20;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_screen);

        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        new ArrayList<String>(user.get_location(
                                user.location_barcode).get_device_names()));

        ListView listview = (ListView)findViewById(R.id.item_listview);
        listview.setAdapter(itemsAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set the current barcode to the barcode of the item we clicked in the list
                user.set_current_barcode(user.get_location(user.
                        location_barcode).get_valid_items().get(position));
                Intent typeLocation_intent = new Intent(ItemListActivity.this, ScanOrTypeActivity.class);
                startActivityForResult(typeLocation_intent, BARCODE_METHOD_REQUEST);

            }
        });

    }

    public void submit_answers(View v) {
        Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("result");

            //SO... if the result from the popup is equal to the barcode ID in the database
            if (user.current_barcode.equals(barcode)) {

                //Need to populate item here
                user.get_location(user.location_barcode).get_item(barcode).populate_item_questions();
                //Start activity that pulls up location questions
                Intent intent = new Intent(ItemListActivity.this, QuestionActivity.class);

                intent.putExtra("User", user);
                startActivity(intent);
            }
            else {
                //Maybe instead of blocking here we need to figure out if it is the right item
                Toast.makeText(this, "This Location is not on your route!" +
                        "\nScan a Location that is on your route!", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

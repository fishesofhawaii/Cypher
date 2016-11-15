package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by inchkyle on 11/3/16.
 */

//The purpose of this activity is to show all the Items, when clicked user will get popup
//to scan or type, then will be redirected to specific item questions
public class ItemListActivity extends AppCompatActivity {
    User user;

    static final int BARCODE_METHOD_REQUEST = 20;  // The request code

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "ItemListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_screen);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RVAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);


        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

        }

//        Replace with cardview
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

                Location location = user.get_location(user.location_barcode);
                user.set_valid_item_barcodes(location.get_valid_items());


//                user.set_current_barcode(user.get_location(user.
//                        location_barcode).get_valid_items().get(position));



                Intent typeLocation_intent = new Intent(ItemListActivity.this, ScanOrTypeActivity.class);
                startActivityForResult(typeLocation_intent, BARCODE_METHOD_REQUEST);

            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        ((RVAdapter) mAdapter).setOnItemClickListener(new RVAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<Item.Object> getDataSet() {
        ArrayList results = new ArrayList<Item.Object>();
        for (int index = 0; index < 5; index++) {
            Item.Object obj = new Item.Object("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index, obj);
        }
        return results;
    }

    public void submit_answers(View v) {
        Location location = user.get_location(user.location_barcode);
        HashMap<String, String> location_answers = location.get_location_question_answer_map();

        System.out.println("LOCATION QUESTIONS:");
        for (HashMap.Entry<String, String> entry : location_answers.entrySet()) {
            System.out.println("Q : " + entry.getKey() + "\tA : " + entry.getValue());
        }
        System.out.println("ITEM QUESTIONS:");
        for (HashMap.Entry<String, Item> entry : location.items.entrySet()){
            Item i = entry.getValue();
            HashMap<String, String> item_answers = i.get_item_question_answer_map();

            for (HashMap.Entry<String, String> e : item_answers.entrySet()) {
                System.out.println("q : " + e.getKey() + "\ta : " + e.getValue());
            }
        }



        Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("result");

            //SO... if the result from the popup is equal to the barcode ID in the database

            if (user.get_valid_item_barcodes().contains(barcode)) {
                //Need to populate item here
                user.get_location(user.location_barcode).get_item(barcode).populate_item_questions();
                user.set_current_barcode(barcode);
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

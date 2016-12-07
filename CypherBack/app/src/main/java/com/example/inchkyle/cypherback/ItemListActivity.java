package com.example.inchkyle.cypherback;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by inchkyle on 11/3/16.
 */

//The purpose of this activity is to show all the Items, when clicked user will get popup
//to scan or type, then will be redirected to specific item questions
public class ItemListActivity extends Activity {
    User user;

    static final int BARCODE_METHOD_REQUEST = 20;  // The request code
    int item_count = 0;

    // RecyclerView
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private static String LOG_TAG = "ItemListActivity";

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Button Disabled on this Page", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_screen);


        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

        }

        Location location = user.get_location(user.location_barcode);

        // Change title here
        String title = location.loc_barcode_name + " Devices";
        setTitle(title);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, R.layout.item_text_view,
                        new ArrayList<>(user.get_location(
                                user.location_barcode).get_device_names()));

        ListView listview = (ListView)findViewById(R.id.item_listview);
        listview.setAdapter(itemsAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set the current barcode to the barcode of the item we clicked in the list

                Location location = user.get_location(user.location_barcode);
                Item this_item = location.get_item_by_unique(location.get_valid_items().get(position));
                if (this_item.get_possible_barcodes().size() == 0){
                    Toast.makeText(ItemListActivity.this, "ASK YOUR ADMIN TO ADD BARCODES FOR THIS " +
                            "DEVICE", Toast.LENGTH_SHORT).show();
                }
                else {
                    user.set_current_barcode(this_item.get_possible_barcodes().get(0));

                    user.set_valid_item_barcodes(this_item.get_possible_barcodes());

                    Intent typeLocation_intent = new Intent(ItemListActivity.this, ScanOrTypeActivity.class);
                    typeLocation_intent.putExtra("User", user);

                    startActivityForResult(typeLocation_intent, BARCODE_METHOD_REQUEST);
                }
                //This sets the valid barcodes for the current item


//                user.set_current_barcode(user.get_location(user.
//                        location_barcode).get_valid_items().get(position));




            }
        });

    }



//    @Override
//    protected void onResume() {
//        super.onResume();
//        ((ItemAdapter) mAdapter).setOnItemClickListener(new ItemAdapter
//                .MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                Log.i(LOG_TAG, " Clicked on Item " + position);
//            }
//        });
//    }

//    private ArrayList<Item.Object> getDataSet() {
//        ArrayList results = new ArrayList<Item.Object>();
//        for (int index = 0; index < 5; index++) {
//            Item.Object obj = new Item.Object("Some Primary Text " + index,
//                    "Secondary " + index);
//            results.add(index, obj);
//        }
//        return results;
//    }



    public void submit_answers(View v) {
        Location location = user.get_location(user.location_barcode);
        String location_id = location.get_location_id();
        String time_answered = location.get_timestamp();

        HashMap<String, String> location_answers = location.get_location_question_answer_map();
        HashMap<String, String> question_id_map = location.get_location_question_id_map();

        ArrayList<Answer> answers = new ArrayList<>();

        item_count = 0;

        System.out.println("LOCATION QUESTIONS:");

        for (HashMap.Entry<String, String> entry : location_answers.entrySet()) {
            String question = entry.getKey();
            String answer = entry.getValue();
            String question_id = question_id_map.get(question);

            //String q_id, String a, String location_id, String time_a, String user_name

            //Question id and answer text
            System.out.println("!!!!!!!" + location_id);
            Answer ans = new Answer(question_id, answer, location_id, time_answered, user.payroll_id);
            answers.add(ans);
        }
        System.out.println("ITEM QUESTIONS:");
        for (HashMap.Entry<String, Item> entry : location.items.entrySet()){
            Item i = entry.getValue();
            HashMap<String, String> item_answers = i.get_item_question_answer_map();
            HashMap<String, String> item_question_id_map = i.get_item_question_id_map();

            if (!(item_answers.size() == 0)) {
                item_count += 1;
            }

            System.out.println("********");
            for (HashMap.Entry<String, String> e : item_answers.entrySet()) {
//                System.out.println("q : " + e.getKey() + "\ta : " + e.getValue());
                String question = e.getKey();
                String answer = e.getValue();
                String question_id = item_question_id_map.get(question);


                Answer ans = new Answer(question_id, answer, location_id, time_answered, user.payroll_id);
                answers.add(ans);
            }
        }
        // So after we go through all of the answers to the items we need to see if they actually
        // anything, if itemcount = 0, lets just varify if they really want to discard the questions


        if (item_count == 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            reject_answers();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //Do nothing, they have the opportunity to answer item questions
                            break;
                    }
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(ItemListActivity.this, R.style.AlertDialogTheme);
            ab.setMessage("You have not inspected any items at this location, all progress at this " +
                    "location will be discarded...\n\nCONTINUE ANYWAY?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        else {
            user.add_local_items_to_push(item_count);
            user.add_answers(answers);

            //Code to save answers in memory
            try {

                //Create the new file
                File dir = getFilesDir();
                File file = new File(dir, "t.tmp");
                file.delete();

                dir = getFilesDir();
                file = new File(dir, "t.tmp");

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(user.get_answer_list());
                oos.writeInt(user.get_local_items_to_push_count());

                oos.close();

            }
            catch (IOException e) {
                System.out.println("Couldnt save for some reason...");
                e.printStackTrace();
            }

            Toast.makeText(this, "You have " + user.get_local_items_to_push_count() +
                    " items responses to push", Toast.LENGTH_SHORT).show();
            for (Answer a : user.get_answer_list()) {
                a.print();
            }

            Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
            user.home_page_reset();
            intent.putExtra("User", user);
            startActivity(intent);
        }
    }

    public void reject_answers() {
        Intent intent = new Intent(ItemListActivity.this, HomeActivity.class);
        user.home_page_reset();
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

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                continue_anyway();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                //Do nothing, they have the opportunity to answer item questions
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(ItemListActivity.this, R.style.AlertDialogTheme);
                ab.setMessage("This Location is not assigned to you...\n\nCONTINUE ANYWAY?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        }

    }

    private void continue_anyway() {

        Intent intent = new Intent(ItemListActivity.this, QuestionActivity.class);

        intent.putExtra("User", user);
        startActivity(intent);


    }


}

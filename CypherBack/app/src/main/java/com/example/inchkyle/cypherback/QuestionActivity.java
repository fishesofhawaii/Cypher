package com.example.inchkyle.cypherback;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by inchkyle on 11/3/16.
 */

//this is set aside for questions to be displayed on the screen, answers will be recorded here
public class QuestionActivity extends AppCompatActivity {
    Item is_item = null;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_screen);


        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

        }
        TextView textView = (TextView) findViewById(R.id.textView);

        //If location barcode is equal to the scanned barcode we are looking at location questions
        if (user.location_barcode.equals(user.current_barcode)) {
            //Need to change this to try/catch for null pointers
            Location location = user.get_location(user.current_barcode);

            textView.setText(location.loc_barcode_name + " Location Questions " +
                    "\n(Barcode #" + user.current_barcode + ")");

            ListView listview = (ListView) findViewById(R.id.questions_listview);
            QuestionAdapter adapter = new QuestionAdapter(getApplicationContext(), R.layout.question_layout);

            //This goes through all the questions and adds them to the adapter
            for (String question : location.get_location_question_answer_map().keySet()) {
                QuestionDataProvider provider = new QuestionDataProvider(0, question, "NA");
                adapter.add(provider);
            }

            listview.setAdapter(adapter);
        }
        //Otherwise we are looking at an item barcode
        else {
            //Need to change this to a try/catch for null pointers
            Item item = user.get_location(user.location_barcode).get_item(user.current_barcode);
            is_item = item;
            //example for below : "Fire Extinguisher Questions (Barcode #20)"
            textView.setText(item.device_name + " Questions \n(Barcode #" + user.current_barcode + ")");

            ListView listview = (ListView)findViewById(R.id.questions_listview);
            QuestionAdapter adapter = new QuestionAdapter(getApplicationContext(), R.layout.question_layout);

            //This goes through all the questions and adds them to the adapter
            for (String question : item.item_question_answer_map.keySet()) {
                QuestionDataProvider provider = new QuestionDataProvider(0, question, "NA");
                adapter.add(provider);
            }

            listview.setAdapter(adapter);

        }
    }

    public void confirm_answers(View v) {

        ListView listView = (ListView) findViewById(R.id.questions_listview);
        QuestionAdapter adapter = (QuestionAdapter) listView.getAdapter();

        HashMap<String, String> answers = adapter.get_question_answer_map();

        // If there is an unanswered question tell the user
        if (answers.containsValue("NA")) {
            Toast.makeText(this, "Please answer ALL of the questions",
                    Toast.LENGTH_SHORT).show();
        }
        //Otherwise we can send it forward to the item list
        else {
            //Sets all the answers that we just put in place
            //if item is still null, that means we had a location
            Location location = user.get_location(user.location_barcode);
            if (is_item == null){
                System.out.println("Setting the location answer map!");
                location.set_location_question_answer_map(answers);
//                location.set_location_question_list(answers);
            }
            else {
                System.out.println("Setting the item answer map!");
                is_item.set_item_question_answer_map(answers);
            }
            Intent intent = new Intent(QuestionActivity.this, ItemListActivity.class);
            intent.putExtra("User", user);

            startActivity(intent);
        }


    }
}

package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inchkyle on 11/3/16.
 */

//this is set aside for questions to be displayed on the screen, answers will be recorded here
public class QuestionActivity extends AppCompatActivity {

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
            for (String question : location.location_question_answer_map.keySet()) {
                QuestionDataProvider provider = new QuestionDataProvider(0, question, "NA");
                adapter.add(provider);
                System.out.println("```" + question);
            }

            listview.setAdapter(adapter);
        }
        //Otherwise we are looking at an item barcode
        else {
            //Need to change this to a try/catch for null pointers
            Item item = user.get_location(user.location_barcode).get_item(user.current_barcode);

            //example for below : "Fire Extinguisher Questions (Barcode #20)"
            textView.setText(item.device_name + " Questions \n(Barcode #" + item.barcode_num + ")");

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
        Intent intent = new Intent(QuestionActivity.this, ItemListActivity.class);
        intent.putExtra("User", user);

        ListView listView = (ListView) findViewById(R.id.questions_listview);
        QuestionAdapter adapter = (QuestionAdapter) listView.getAdapter();

//        for (int i = 0; i < adapter.getCount(); i++){
////            QuestionDataProvider provider = (QuestionDataProvider) adapter.getItem(i);
////
////            System.out.println("Question is " + i + " : " + provider.getQuestion());
//            View row = adapter.getView(i, listView.getChildAt(i), listView);
//
//            RadioButton yesButton = (RadioButton) row.findViewById(R.id.yesButton);
//            RadioButton noButton = (RadioButton) row.findViewById(R.id.noButton);
//            TextView questionText = (TextView) row.findViewById(R.id.questionText);
//
//
//        }
        //Adapter.getAnsers


        System.out.println("Going on...");
        startActivity(intent);
    }
}

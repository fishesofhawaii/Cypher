package com.example.inchkyle.cypherback;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by inchkyle on 11/13/16.
 */

// TODO: Figure out way to use RadioButtons

public class QuestionAdapter extends ArrayAdapter {
    List list = new ArrayList();
    HashMap<String, String> question_answer_map = new HashMap<>();



    public QuestionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public class DataHandler {
        TextView q;
        TextView response;
        Button yesButton;
        Button noButton;
        ImageView yesCheck;
        ImageView noCheck;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final DataHandler handler;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            row = inflater.inflate(R.layout.question_layout, parent, false);


            handler = new DataHandler();
            handler.q = (TextView) row.findViewById(R.id.questionText);
            handler.yesButton = (Button) row.findViewById(R.id.yesButton);
            handler.noButton = (Button) row.findViewById(R.id.noButton);
//            handler.response = (TextView) row.findViewById(R.id.responseText);
            handler.yesCheck = (ImageView) row.findViewById(R.id.yesCheck);
            handler.noCheck = (ImageView) row.findViewById(R.id.noCheck);



            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();

        }

        final QuestionDataProvider provider;
        provider = (QuestionDataProvider) this.getItem(position);

        final String question = provider.getQuestion().toString();


        //If the question answer map doesnt have this question, add it as not answered
        if (!question_answer_map.containsKey(question)){
            question_answer_map.put(question, "NA");
        }

        handler.q.setText(question);

        if (provider.getAnswer().equals("1")) {
//            handler.response.setText("YES");
            handler.yesCheck.setVisibility(View.VISIBLE);
            handler.noCheck.setVisibility(View.INVISIBLE);
        }
        else if (provider.getAnswer().equals("0")) {
//            handler.response.setText("NO");
            handler.noCheck.setVisibility(View.VISIBLE);
            handler.yesCheck.setVisibility(View.INVISIBLE);
        }
        else if (provider.getAnswer().equals("NA")) {
//            handler.response.setText("");
            handler.yesCheck.setVisibility(View.INVISIBLE);
            handler.noCheck.setVisibility(View.INVISIBLE);

        }
        else {
            System.out.println("This isnt good");
        }










        handler.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.response.setText("YES");
                handler.yesCheck.setVisibility(View.VISIBLE);
                handler.noCheck.setVisibility(View.INVISIBLE);
                provider.setAnswer("1");
                question_answer_map.put(question, "1");

            }
        });





        handler.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.response.setText("NO");
                handler.noCheck.setVisibility(View.VISIBLE);
                handler.yesCheck.setVisibility(View.INVISIBLE);
                provider.setAnswer("0");
                question_answer_map.put(question, "0");

            }
        });


        return row;
    }







    public HashMap<String, String> get_question_answer_map() {
        return question_answer_map;
    }





}

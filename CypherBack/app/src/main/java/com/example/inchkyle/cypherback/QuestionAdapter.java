package com.example.inchkyle.cypherback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class QuestionAdapter extends ArrayAdapter {
    List list = new ArrayList();
    HashMap<String, String> question_answer_map = new HashMap<>();

    public QuestionAdapter(Context context, int resource) {
        super(context, resource);
    }

    public class DataHandler {
        ImageView image;
        TextView q;
        TextView response;
        Button yesButton;
        Button noButton;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final DataHandler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.question_layout, parent, false);

            handler = new DataHandler();
            handler.image = (ImageView) row.findViewById(R.id.questionImage);
            handler.q = (TextView) row.findViewById(R.id.questionText);
            handler.yesButton = (Button) row.findViewById(R.id.yesButton);
            handler.noButton = (Button) row.findViewById(R.id.noButton);
            handler.response = (TextView) row.findViewById(R.id.responseText);

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
        handler.image.setImageResource(provider.getResource());

        if (provider.getAnswer().equals("1")) {
            handler.response.setText("YES");
        }
        else if (provider.getAnswer().equals("0")) {
            handler.response.setText("NO");
        }
        else if (provider.getAnswer().equals("NA")) {
            handler.response.setText("");
        }
        else {
            System.out.println("This isnt good");
        }

        handler.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.response.setText("YES");
                provider.setAnswer("1");
                question_answer_map.put(question, "1");
            }
        });
        handler.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.response.setText("NO");
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

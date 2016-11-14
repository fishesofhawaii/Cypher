package com.example.inchkyle.cypherback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    static class DataHandler {
        ImageView image;
        TextView q;
        RadioGroup radioGroup;
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
            handler.radioGroup = (RadioGroup) row.findViewById(R.id.radio_group);

            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();

        }

        final QuestionDataProvider provider;
        provider = (QuestionDataProvider) this.getItem(position);

        handler.q.setText(provider.getQuestion().toString());
        handler.image.setImageResource(provider.getResource());


        handler.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selected_button = (RadioButton) group.findViewById(checkedId);

                if (selected_button.equals("Yes")) {
                    System.out.println(handler.q.getText() + " : " + "TRUE");
                }
                else if (selected_button.equals("No")) {
                    System.out.println(handler.q.getText() + " : " + "FALSE");

                }
                else {
                    System.out.println(checkedId);
                    System.out.println("Not bueno");
                }
            }
        });



        return row;
    }

}

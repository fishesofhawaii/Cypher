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

    public class DataHandler {
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
            System.out.println("IT WAS NULL THIS TIME " + position);
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
            System.out.println("IT WAS NOT NULL! A MIRACLE " + position);
            handler = (DataHandler) row.getTag();

        }

        final QuestionDataProvider provider;
        provider = (QuestionDataProvider) this.getItem(position);

        handler.q.setText(provider.getQuestion().toString());
        handler.image.setImageResource(provider.getResource());

        RadioButton yes = (RadioButton) handler.radioGroup.findViewById(R.id.yesButton);
        RadioButton no = (RadioButton) handler.radioGroup.findViewById(R.id.noButton);

        if (provider.getAnswer().equals("NA")) {
            //This is the case it hasnt been answered, both buttons are off
            yes.setChecked(false);
            no.setChecked(false);
        }
        else if (provider.getAnswer().equals("1")){
            //This is the case that the row is set to YES
            yes.setChecked(true);
            no.setChecked(false);
        }
        else if (provider.getAnswer().equals("0")){
            //This is the case that the row is set to NO
            yes.setChecked(false);
            no.setChecked(true);
        }

        handler.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selected_button = (RadioButton) group.findViewById(checkedId);
                System.out.println(selected_button.getText()  + "------------");

                if (selected_button.getText().equals(R.id.yesButton)) {
                    System.out.println(handler.q.getText() + " : " + "TRUE");
                    provider.setAnswer("1");
                }
                else if (selected_button.equals(R.id.noButton)) {
                    System.out.println(handler.q.getText() + " : " + "FALSE");
                    provider.setAnswer("0");

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

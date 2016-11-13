package com.example.inchkyle.listviewyesno;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inchkyle on 11/13/16.
 */

public class ItemAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler {
        ImageView image;
        TextView q;
        RadioButton yes_button;
        RadioButton no_button;

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
        DataHandler handler;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.row_layout, parent, false);

            handler = new DataHandler();
            handler.image = (ImageView) row.findViewById(R.id.itemImage);
            handler.q = (TextView) row.findViewById(R.id.questionText);
            handler.yes_button = (RadioButton) row.findViewById(R.id.yesButton);
            handler.no_button = (RadioButton) row.findViewById(R.id.noButton);

            row.setTag(handler);
        }
        else {
            handler = (DataHandler) row.getTag();

        }

        ItemDataProvider provider;
        provider = (ItemDataProvider) this.getItem(position);

        System.out.println("provider question!!!: " + provider.getQuestion());

        handler.q.setText(provider.getQuestion().toString());
        handler.image.setImageResource(provider.getResource());
        handler.yes_button.setChecked(false);
        handler.no_button.setChecked(false);

        return row;
    }

}

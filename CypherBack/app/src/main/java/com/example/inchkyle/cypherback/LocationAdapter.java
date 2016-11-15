package com.example.inchkyle.cypherback;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sweetkidsabo5 on 11/15/16.
 */

public class LocationAdapter extends ArrayAdapter<Location.Object> {
    public LocationAdapter(Context context, ArrayList<Location.Object> locations) {
        super(context, 0, locations);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Location.Object location = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_upcoming_view, parent, false);
        }
        // Lookup view for data population
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvCity = (TextView) convertView.findViewById(R.id.tvCity);
        TextView tvPlant = (TextView) convertView.findViewById(R.id.tvPlant);

        // Populate the data into the template view using the data object
        tvDate.setText(location.date);
        tvCity.setText(location.city);
        tvPlant.setText(location.plant);

        // Return the completed view to render on screen
        return convertView;
    }




}

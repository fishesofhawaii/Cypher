package com.example.inchkyle.cypherback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by inchkyle on 12/5/16.
 */

public class HistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);


        // Construct the data source
        ArrayList<Location.Object> arrayOfLocations = new ArrayList<>();
        // Create the adapter to convert the array to views
        LocationAdapter adapter = new LocationAdapter(this, arrayOfLocations);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);


        setTitle("History");

        File dir = getFilesDir();
        File file = new File(dir, "times.tmp");

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ArrayList<HistoryObject> ans = (ArrayList<HistoryObject>) ois.readObject();

            if (ans.size() == 0) {
                Location.Object newLocation = new Location.Object("","You have no pushed answers!",
                        "Inspect an item and push it to the Database!");
                adapter.add(newLocation);
            }
            else {
                Collections.reverse(ans);

                for (HistoryObject h : ans) {
                    String sentence = "";
                    if (h.isPassing()) {
                        sentence = "This location passed all location and item questions on this push!";
                    }
                    else {
                        sentence = "This location DID NOT pass all location and item questions on this push!";

                    }
                    long t = Long.parseLong(h.getTime_stamp());

                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").
                            format(new java.util.Date (t*1000));


                    Location.Object newLocation = new Location.Object("LOC " + h.getLoc_id(),date, sentence);
                    adapter.add(newLocation);

                }

            }


        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            Location.Object newLocation = new Location.Object("","You have no pushed answers!",
                    "Inspect an item and push it to the Database!");
            adapter.add(newLocation);

        }

    }

}

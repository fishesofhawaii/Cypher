package com.example.inchkyle.listviewyesno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    int[] images = {R.mipmap.aed, R.mipmap.ladder};
    String[] questions = {"Is this a thing?", "Is this the other thing?"};
    ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ItemAdapter(getApplicationContext(), R.layout.row_layout);

        ItemDataProvider provider = new ItemDataProvider(images[0], questions[0]);
        ItemDataProvider provider2 = new ItemDataProvider(images[1], questions[1]);

        adapter.add(provider);
        adapter.add(provider2);

        provider = new ItemDataProvider(images[0], questions[0]);
        provider2 = new ItemDataProvider(images[1], questions[1]);

        adapter.add(provider);
        adapter.add(provider2);

        provider = new ItemDataProvider(images[0], questions[0]);
        provider2 = new ItemDataProvider(images[1], questions[1]);

        adapter.add(provider);
        adapter.add(provider2);

        provider = new ItemDataProvider(0, questions[0]);
        adapter.add(provider);


        listView.setAdapter(adapter);

    }
}

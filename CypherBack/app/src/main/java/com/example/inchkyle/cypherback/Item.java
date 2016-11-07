package com.example.inchkyle.cypherback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by inchkyle on 11/7/16.
 */

public class Item {
    String json_string;
    String item_name;
    String barcode_num;
    ArrayList<String> location_questions;

    Item(String barcode_num, String item_name, String json_string) {
        this.barcode_num = barcode_num;
        this.item_name = item_name;
        this.json_string = json_string;

    }

    // When called, the JSON string will be parsed for the questions associated with the item
    public void populate_item_questions() {
        try {
            JSONObject location_json = new JSONObject(this.json_string);



        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

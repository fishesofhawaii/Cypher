package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by inchkyle on 11/3/16.
 */

//Locations -> Items -> Questions
//Locations also have questions
public class Location {
    String json_string;
    String loc_barcode_name;
    String barcode_num;
    ArrayList<String> location_questions;

    Location(String barcode_num, String loc_barcode_name, String json_string) {
        this.barcode_num = barcode_num;
        this.loc_barcode_name = loc_barcode_name;
        this.json_string = json_string;

    }
    public String print_items(){
        String ret_string = "\t-loc_barcode_name: " + loc_barcode_name;

        return ret_string;
    }

    public void populate_location_questions() {
        try {
            JSONObject location_json = new JSONObject(this.json_string);
            JSONArray question_ary = location_json.getJSONArray("loc_questions");

            System.out.println("Populating the question array!");

            //Iterates through questions?
            for (int i = 0; i < question_ary.length(); i++) {
                System.out.println(question_ary.get(i).toString());
            }


        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by inchkyle on 11/7/16.
 */

public class Item implements Serializable{
    String admin;
    String item_type;
    String barcode_num;
    String user_assigned;

    String json_string;

    HashMap<String, String> item_question_answer_map = new HashMap<>();


    Item(String barcode_num, String json_string) {
        this.barcode_num = barcode_num;
        this.json_string = json_string;

    }

    // When called, the JSON string will be parsed for the questions associated with the item
    public void populate_item_questions() {
        try {
            JSONArray question_ary = new JSONObject(this.json_string).getJSONArray("questions");

            System.out.println("Populating the Items question array!");

            //Iterates through questions, places empty string as answer for now
            for (int i = 0; i < question_ary.length(); i++) {
                JSONObject question_JSON = new JSONObject(question_ary.get(i).toString());
                item_question_answer_map.put(question_JSON.get("question_text").toString(), "NA");

                System.out.println(question_JSON.get("question_text"));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void set_admin(String a) {
        this.admin = a;
    }
    public void set_item_type(String i_name) {
        this.item_type = i_name;
    }
    public void set_user_assigned(String name) {
        this.user_assigned = name;
    }

}

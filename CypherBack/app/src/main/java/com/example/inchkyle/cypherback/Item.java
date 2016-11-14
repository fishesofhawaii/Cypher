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
    int model_num;
    String manu;
    String type_equip;
    String device_name;

    String barcode_num;

    String json_string;

    HashMap<String, String> item_question_answer_map = new HashMap<>();
    ArrayList<String> possible_barcodes;

    Item(String barcode_num, String json_string) {
        this.barcode_num = barcode_num;
        this.json_string = json_string;

    }

    // When called, the JSON string will be parsed for the questions associated with the item
    public void populate_item_questions() {
        try {
            JSONArray question_ary = new JSONObject(this.json_string).getJSONArray("questions");

            //Iterates through questions, places empty string as answer for now
            for (int i = 0; i < question_ary.length(); i++) {
                JSONObject question_JSON = new JSONObject(question_ary.get(i).toString());
                item_question_answer_map.put(question_JSON.get("question_text").toString(), "NA");

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void set_type_equip(String _type_equip) {
        this.type_equip = _type_equip;
    }

    public void set_manu(String _manu) {
        this.manu = _manu;
    }

    public void set_model_num(int _model_num) {
        this.model_num = _model_num;
    }

    public void set_possible_barcodes(ArrayList<String> _possible_barcodes) {
        this.possible_barcodes = _possible_barcodes;
    }
}

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
    String model_num;
    String manu;
    String type_equip;
    String device_name;

    String barcode_num;
    String json_string;

    HashMap<String, String> item_question_answer_map = new HashMap<>();
    HashMap<String, String> item_question_id_map = new HashMap<>();


    ArrayList<String> possible_barcodes = new ArrayList<>();

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

                String question_text = question_JSON.get("question_text").toString();
                int question_id = question_JSON.getInt("question_id");

                item_question_answer_map.put(question_text, "NA");
                item_question_id_map.put(question_text, Integer.toString(question_id));

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

    public void set_model_num(String _model_num) {
        this.model_num = _model_num;
    }

    public void set_possible_barcodes(ArrayList<String> _possible_barcodes) {
        this.possible_barcodes = _possible_barcodes;
    }
    public ArrayList<String> get_possible_barcodes() {
        return possible_barcodes;
    }

    public void set_device_name(String _device_name) {
        this.device_name = _device_name;
    }

    public void set_item_question_answer_map(HashMap<String, String> answers) {
        this.item_question_answer_map = answers;
    }

    public HashMap<String, String> get_item_question_answer_map() {
        return this.item_question_answer_map;
    }

    public HashMap<String, String> get_item_question_id_map() {
        return this.item_question_id_map;
    }


    public static class Object{

        private String mText1;
        private String mText2;

        Object (String text1, String text2){
            mText1 = text1;
            mText2 = text2;
        }

        public String getmText1() {
            return mText1;
        }

        public void setmText1(String mText1) {
            this.mText1 = mText1;
        }

        public String getmText2() {
            return mText2;
        }

        public void setmText2(String mText2) {
            this.mText2 = mText2;
        }

    }

}


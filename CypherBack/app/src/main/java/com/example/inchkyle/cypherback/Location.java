package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by inchkyle on 11/3/16.
 */

//Locations -> Items -> Questions
//Locations also have questions
public class Location implements Serializable {
    String json_string;
    String loc_barcode_name;
    String barcode_num;
    int location_id;
    String user_assigned;
    long timestamp;

    HashMap<String, String> location_question_answer_map = new HashMap<>();
    HashMap<String, Item> items = new HashMap<>();

    ArrayList<String> valid_items = new ArrayList<>();
    ArrayList<String> device_names = new ArrayList<>();


    Location(String barcode_num, String json_string) {
        this.barcode_num = barcode_num;
        this.json_string = json_string;

    }

    public void print_location() {
        for (HashMap.Entry<String, Item> entry : items.entrySet()) {
            String key = entry.getKey();
            Item value = entry.getValue();

            System.out.println("Barcode: " + key);
            System.out.println("Type: " + value.device_name);


        }
    }

    //This is where the questions associated with the location itself are populated
    public void populate_location_questions() {
        try {
            JSONArray question_ary = new JSONObject(this.json_string).getJSONArray("loc_questions");

            //Iterates through questions, places empty string as answer for now
            for (int i = 0; i < question_ary.length(); i++) {
                JSONObject question_JSON = new JSONObject(question_ary.get(i).toString());
                location_question_answer_map.put(question_JSON.get("question_text").toString(), "NA");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //This sets the basic information for each item at each location such as name and type
    //NOTE: this does not populate the questions
    public void set_items() {

        try {
            JSONObject json_obj = new JSONObject(json_string);
            JSONArray json_ary;

            json_ary = json_obj.getJSONArray("devices");

            ArrayList<String> possible_barcodes = new ArrayList<>();
            possible_barcodes.add("1");
            possible_barcodes.add("2");
            possible_barcodes.add("3");

            valid_items = possible_barcodes;


            for (int j = 0; j < json_ary.length(); j++) {
                JSONObject item = new JSONObject(json_ary.get(j).toString());

                int model_num = item.getInt("model_num");
                String manu = item.get("manu").toString();
                String type_equip = item.get("type_equip").toString();
                String device_name = item.get("device_name").toString();

                JSONArray barcode_array = item.getJSONArray("barcodes");

//                String item_type = String.valueOf(item.getString("item_type"));
//                String barcode_num = String.valueOf(item.getString("barcode_num"));
//                String user_assigned = String.valueOf(item.getString("user_assigned"));

                device_names.add(device_name);

                Item i = new Item(barcode_num, item.toString());
                i.set_model_num(model_num);
                i.set_manu(manu);
                i.set_type_equip(type_equip);
                i.set_device_name(device_name);

                //Remove below with real data once ally finishes
                ArrayList<String> poss_barcodes = new ArrayList<>();
                poss_barcodes.add("1");
                poss_barcodes.add("2");
                poss_barcodes.add("3");
                // ABOVE

                i.set_possible_barcodes(poss_barcodes);

                this.items.put(barcode_num, i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Basic setter for initialization
    public void set_loc_barcode_name(String name) {
        this.loc_barcode_name = name;
    }

    //Gives all of the types of the items so we can display them on the ItemListActivity easily
    public ArrayList<String> get_device_names() {
        return device_names;
    }

    public ArrayList<String> get_valid_items() {
        return valid_items;
    }

    public Item get_item(String barcode) {
        System.out.println("Looking for " + barcode);
        for (Item i : items.values()) {
            System.out.println("item possible barcodes: " + i.get_possible_barcodes().toString());
            if (i.get_possible_barcodes().contains(barcode)) {
                return i;
            }
        }
        System.out.println("returning null");
        return null;

//        return items.get(barcode);
    }

    public void set_location_id(int _location_id) {
        this.location_id = _location_id;
    }

    public void set_user_assigned(String _user_assigned) {
        this.user_assigned = _user_assigned;
    }

    public void set_location_question_answer_map(HashMap<String, String> qs_as) {

        for (HashMap.Entry<String, String> entry : qs_as.entrySet()) {
            System.out.println("Question: " + entry.getKey() + "\nAnswer: " + entry.getValue() + "\n~~~~~~");
        }


        this.location_question_answer_map = qs_as;
    }

    public HashMap<String, String> get_location_question_answer_map() {
        return this.location_question_answer_map;
    }

    public void set_timestamp(long _timestamp) {
        System.out.println("TIME : " + _timestamp);
        this.timestamp = _timestamp;
    }
}

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
    HashMap<String, String> location_question_id_map = new HashMap<>();

    ArrayList<String> device_names = new ArrayList<>();
    //type equip + model number will be the way to uniquely identify an item
    ArrayList<String> valid_items = new ArrayList<>();


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

                String question_text = question_JSON.get("question_text").toString();
                int question_id = question_JSON.getInt("question_id");

                location_question_id_map.put(question_text, Integer.toString(question_id));
                location_question_answer_map.put(question_text, "NA");
            }
        }
        catch (JSONException e) {
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




            for (int j = 0; j < json_ary.length(); j++) {
                JSONObject item = new JSONObject(json_ary.get(j).toString());

                String model_num = item.get("model_num").toString();
                String manu = item.get("manu").toString();
                String type_equip = item.get("type_equip").toString();
                String device_name = item.get("device_name").toString();

                JSONArray barcode_array = item.getJSONArray("barcodes");

                ArrayList<String> possible_barcodes = new ArrayList<>();

                //populates the possible barcode array.
                for (int i = 0; i < barcode_array.length(); i++){
                    possible_barcodes.add(barcode_array.get(i).toString());
                }



//                String item_type = String.valueOf(item.getString("item_type"));
//                String barcode_num = String.valueOf(item.getString("barcode_num"));
//                String user_assigned = String.valueOf(item.getString("user_assigned"));

                device_names.add(device_name);

                Item i = new Item(barcode_num, item.toString());
                i.set_model_num(model_num);
                i.set_manu(manu);
                i.set_type_equip(type_equip);
                i.set_device_name(device_name);
                i.set_possible_barcodes(possible_barcodes);

                valid_items.add(type_equip + model_num);

                this.items.put(type_equip + model_num, i);

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

    public ArrayList<String> get_valid_items() {
        return this.valid_items;
    }

    public void set_location_id(int _location_id) {
        this.location_id = _location_id;
    }

    public String get_location_id() {
        return Integer.toString(location_id);
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

    //Get the item by the unique identifier type_equip + model_number
    public Item get_item_by_unique(String s) {
        return items.get(s);
    }

    public void set_user_assigned(String _user_assigned) {
        this.user_assigned = _user_assigned;
    }

    public String get_user_assigned() {
        return user_assigned;
    }


    public static class Object {
        public String date;
        public String city;
        public String plant;

        public Object(String date, String city, String plant) {
            this.date = date;
            this.city = city;
            this.plant = plant;
        }
    }

    public HashMap<String, String> get_location_question_id_map() {
        return location_question_id_map;
    }

    public String get_timestamp() {
        return Long.toString(this.timestamp);
    }

}

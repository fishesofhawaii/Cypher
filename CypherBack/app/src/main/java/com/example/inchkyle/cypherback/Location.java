package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by inchkyle on 11/3/16.
 */

//Locations -> Items -> Questions
//Locations also have questions
public class Location implements Serializable {
    String json_string;
    String loc_barcode_name;
    String barcode_num;

    HashMap<String, String> location_question_answer_map = new HashMap<>();
    HashMap<String, Item> items = new HashMap<>();

    ArrayList<String> valid_items = new ArrayList<>();
    ArrayList<String> item_types = new ArrayList<>();


    Location(String barcode_num, String json_string) {
        this.barcode_num = barcode_num;
        this.json_string = json_string;

    }

    public void print_location() {
        for (HashMap.Entry<String, Item> entry : items.entrySet()) {
            String key = entry.getKey();
            Item value = entry.getValue();

            System.out.println("Barcode: " + key);
            System.out.println("Type: " + value.item_type + " Admin: " + value.admin);


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

            json_ary = json_obj.getJSONArray("items");

            for (int j = 0; j < json_ary.length(); j++) {
                JSONObject item = new JSONObject(json_ary.get(j).toString());

                String admin = String.valueOf(item.getString("admin"));
                String item_type = String.valueOf(item.getString("item_type"));
                String barcode_num = String.valueOf(item.getString("barcode_num"));
                String user_assigned = String.valueOf(item.getString("user_assigned"));

                valid_items.add(barcode_num);
                item_types.add(item_type);

                Item i = new Item(barcode_num, item.toString());
                i.set_admin(admin);
                i.set_item_type(item_type);
                i.set_user_assigned(user_assigned);

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
    public ArrayList<String> get_item_types() {
        return item_types;

    }

    public ArrayList<String> get_valid_items() {
        return valid_items;
    }

    public Item get_item(String barcode) {
        return items.get(barcode);
    }
}

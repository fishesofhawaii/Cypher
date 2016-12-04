package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by inchkyle on 11/1/16.
 */

//User is the biggest class. It contains the data that this employee needs.
//User has locations assigned, locations have items, and items have questions.
public class User implements Serializable{
    String BASE_URL;

    String payroll_id;
    String employee_json;
    String current_barcode;
    String location_barcode;

    ArrayList<String> valid_item_barcodes = new ArrayList<>();
    ArrayList<String> valid_locations = new ArrayList<>();
    HashMap<String, Location> locations = new HashMap<>();

    int local_items_to_push_count = 0;
    ArrayList<Answer> answer_list = new ArrayList<>();


    User(String id, String json_str) {
        this.payroll_id = id;
        this.employee_json = json_str;

    }

    //This populates the hashmap that has the Locations in it. It also populates "valid_locations"
    public void set_locations() {

        try {
            JSONObject json_obj = new JSONObject(employee_json);
            JSONArray json_ary;

            json_ary = json_obj.getJSONArray("data");

            for (int i = 0; i < json_ary.length(); i++) {
                JSONObject location = new JSONObject(json_ary.get(i).toString());

                String loc_barcode_num = location.get("loc_barcode_num").toString();
                String loc_barcode_name = location.get("loc_barcode_name").toString();
                int location_id = location.getInt("location_id");
//                String user_assigned = location.get("user_assigned").toString();

                valid_locations.add(loc_barcode_num);

                //Create the location
                Location l = new Location(loc_barcode_num, location.toString());
                //And then set extras
                l.set_loc_barcode_name(loc_barcode_name);
                l.set_location_id(location_id);
//                l.set_user_assigned(user_assigned);

                this.locations.put(loc_barcode_num, l);

            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // Establish whether this location is updatable based on if it is in the users route
    public boolean check_location(String barcode_given){

        if (valid_locations.contains(barcode_given)) {
            return true;
        }

        return false;
    }

    //the given location will populate it's questions for answering, this is called after
    //the location is scanned
    public void populate_location(String barcode) {
        //calls the particular loaction to fill it's questions
        location_barcode = barcode;
        locations.get(barcode).populate_location_questions();
    }


    public void set_current_barcode(String barcode) {
        current_barcode = barcode;
    }

    public void check_item(String barcode) {
        Location l = locations.get(location_barcode);
        //Maybe something other than "contains"
//        return l.contains(barcode);
    }

    public Location get_location(String barcode) {
        return locations.get(barcode);
    }

    public void set_BASE_URL(String url) {
        BASE_URL = url;
    }

    public void set_valid_item_barcodes(ArrayList<String> barcodes) {
        this.valid_item_barcodes = barcodes;
    }
    public ArrayList<String> get_valid_item_barcodes() {
        return this.valid_item_barcodes;
    }

    public int get_local_items_to_push_count() {
        return this.local_items_to_push_count;
    }

    public void add_local_items_to_push (int count) {
        this.local_items_to_push_count += count;
    }

    public void add_answers(ArrayList<Answer> _answer_list) {

        for (Answer a : _answer_list) {
            this.answer_list.add(a);
        }
    }

    //This will be called after all the answers are pushed up
    public void clear_answers() {
        this.answer_list.clear();
        this.local_items_to_push_count = 0;
    }

    public void load_answers() throws IOException, ClassNotFoundException {

    }

    //If there are no answers, return true
    public boolean no_answers() {
        return this.answer_list.size() == 0;
    }

    public ArrayList<Answer> get_answer_list() {
        return this.answer_list;
    }

    public String get_payroll_id() {
        return payroll_id;
    }

    public void set_local_items_to_push_count(int i) {
        this.local_items_to_push_count = i;
    }

    public void set_answer_list(ArrayList<Answer> a) {
        this.answer_list = a;
    }

    public String get_BASE_URL() {
        return this.BASE_URL;
    }

    public StringEntity get_JSON_entity() throws JSONException, UnsupportedEncodingException {

        JSONObject data = new JSONObject();
        JSONObject answer_json = new JSONObject();
        JSONArray answer_ary = new JSONArray();

        for (Answer a : this.answer_list) {
            JSONObject json_answer = new JSONObject();

            String answer_text = a.getAnswer_text();
            System.out.println("~~~~ " + answer_text);

            String loc_id = a.getLoc_id();
            String question_id = a.getQuestion_id();
            String time_answered = a.getTime_answered();
            String usr = a.getUser();

            if (answer_text.equals("1")){
                answer_text = "yes";
            }
            else if (answer_text.equals("0")) {
                answer_text = "no";
            }

            json_answer.put("answer_text", answer_text);
            json_answer.put("loc_id", loc_id);
            json_answer.put("question_id", question_id);
            json_answer.put("time_answered", time_answered);
            json_answer.put("user", usr);

            answer_ary.put(json_answer);

        }

        answer_json.put("answers", answer_ary);
        data.put("data", answer_json);

        StringEntity _JSON_entity = new StringEntity(data.toString());
        System.out.println(data.toString(1));

        return _JSON_entity;
    }
}

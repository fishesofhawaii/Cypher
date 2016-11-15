package com.example.inchkyle.cypherback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
}

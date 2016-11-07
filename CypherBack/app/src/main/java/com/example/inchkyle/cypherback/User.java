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
    String payroll_id;
    String employee_json;
    ArrayList<String> valid_locations = new ArrayList<>();
    HashMap<String, Location> locations = new HashMap<>();

    User(String id, String json_str) {
        this.payroll_id = id;
        this.employee_json = json_str;

    }

    //Iterate through the user's information and print everything possible
    public void print_locations() {

        for (HashMap.Entry<String, Location> entry : locations.entrySet()) {
            String key = entry.getKey();
            Location value = entry.getValue();
            System.out.println("Location Barcode, " + key + "\n" + value.print_items());
        }
    }

    //This populates the hashmap that has the Locations in it. It also populates "valid_locations"
    public void set_locations() {

        try {
            JSONObject json_obj = new JSONObject(employee_json);
            JSONArray json_ary;

            json_ary = json_obj.getJSONArray("data");

            for (int i = 0; i < json_ary.length(); i++) {
                JSONObject location = new JSONObject(json_ary.get(i).toString());

                String barcode_num = String.valueOf(location.getInt("barcode_num"));
                String loc_barcode_name = location.get("loc_barcode_name").toString();

                valid_locations.add(barcode_num);

                Location l = new Location(barcode_num, loc_barcode_name, location.toString());
                this.locations.put(barcode_num, l);

            }

            print_locations();
        }

        catch (JSONException e) {
            e.printStackTrace();
        }

    }
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
        locations.get(barcode).populate_location_questions();
    }

}

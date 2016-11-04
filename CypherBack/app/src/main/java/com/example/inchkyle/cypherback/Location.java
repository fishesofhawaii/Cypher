package com.example.inchkyle.cypherback;

/**
 * Created by inchkyle on 11/3/16.
 */

//Locations -> Items -> Questions
//Locations also have questions
public class Location {
    String json_string;
    String loc_barcode_name;
    String barcode_num;

    Location(String barcode_num, String loc_barcode_name, String json_string) {
        this.barcode_num = barcode_num;
        this.loc_barcode_name = loc_barcode_name;
        this.json_string = json_string;

    }
    public String print_items(){
        String ret_string = "\t-loc_barcode_name: " + loc_barcode_name;

        return ret_string;
    }


}

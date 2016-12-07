package com.example.inchkyle.cypherback;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Vibrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.util.EntityUtils;

import static android.R.id.list;

/**
 * Created by inchkyle on 10/31/16.
 */

public class HomeActivity extends AppCompatActivity {
    static final int BARCODE_METHOD_REQUEST = 20;  // The request code
    User user;
    Boolean POST_SUCCESS = false;
    ProgressDialog progressDialog;

    Vibrator v;

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Button Disabled on this Page", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);




        v = (Vibrator) this.getApplicationContext().
                getSystemService(Context.VIBRATOR_SERVICE);

        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

            user.set_locations();

            //If there are no answers, we are going to try to load some
            if (user.no_answers()){
                try {

                    //LOADING the previous data
                    File dir = getFilesDir();
                    File file = new File(dir, "t.tmp");

                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ArrayList<Answer> ans = (ArrayList<Answer>) ois.readObject();
                    int item_count = ois.readInt();

                    System.out.println("Item count is : " + item_count);
                    for (Answer a : ans) {
                        a.print();
                    }

                    user.set_local_items_to_push_count(item_count);
                    user.set_answer_list(ans);

                    ois.close();
                }
                catch (IOException | ClassNotFoundException e) {
                    System.out.println("Couldnt load for some reason");
                    e.printStackTrace();
                }
            }
        }


        // Construct the data source
        ArrayList<Location.Object> arrayOfLocations = new ArrayList<Location.Object>();
        // Create the adapter to convert the array to views
        LocationAdapter adapter = new LocationAdapter(this, arrayOfLocations);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);




        for (Location l : user.get_my_locations()) {
            System.out.println("You have location " + l.get_location_id());
            String items = "";

            for (Item i : l.items.values()) {
                items += i.device_name + ", ";
            }
            //Get rid of the extra comma and space
            items = items.substring(0, items.length() - 2);

            Location.Object newLocation = new Location.Object("LOC " + l.get_location_id(),
                    l.loc_barcode_name, items);

            adapter.add(newLocation);
        }
        if (user.get_my_locations().size() == 0){
            Location.Object newLocation = new Location.Object("",
                    "You currently have no locations assigned to you.",
                    "Please ensure that you entered your id correctly: \n" +
                            "You are logged in as: '" + user.payroll_id + "'");
            adapter.add(newLocation);

        }


    }

    //Clicking the update update button should retrieve database values for the employee again
    public void update_click(final View v) throws JSONException, UnsupportedEncodingException {
        System.out.println("GOING TO UPDATE THE DATABASE\n");

        if (user.answer_list.size() == 0) {
            Toast.makeText(this, "No answers found", Toast.LENGTH_SHORT).show();
            return;
        }

        String BASE_URL = user.get_BASE_URL();
        final StringEntity entity = user.get_JSON_entity();

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String POST_address = BASE_URL + "/questions/addanswers/";
        //This is the post with the employee id (the payroll_id)


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        // onLoginSuccess();
                        // onLoginFailed();
                        client.post(getBaseContext(), POST_address,
                                entity, "application/json", new ResponseHandlerInterface() {

                                    @Override
                                    public void sendResponseMessage(HttpResponse response) throws IOException {
                                        System.out.println("1");
                                        POST_SUCCESS = true;

                                    }

                                    @Override
                                    public void sendStartMessage() {

                                    }

                                    @Override
                                    public void sendFinishMessage() {
                                        //Last called in a success AND failure
                                        if (POST_SUCCESS) {
                                            post_success();

                                        }

                                    }

                                    @Override
                                    public void sendProgressMessage(long bytesWritten, long bytesTotal) {

                                    }

                                    @Override
                                    public void sendCancelMessage() {

                                    }

                                    @Override
                                    public void sendSuccessMessage(int statusCode, Header[] headers, byte[] responseBody) {

                                    }

                                    @Override
                                    public void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        //Second to last called in a failure
                                        System.out.println("Send fail");
                                        progressDialog.dismiss();

                                    }

                                    //           TODO: Display message if user is unable to log in

                                    @Override
                                    public void sendRetryMessage(int retryNo) {
                                        //Gets called every attempt to send POST
                                        System.out.println("Send Retry");
                                        if (retryNo > 1) {
                                            client.getHttpClient().getConnectionManager().shutdown();

                                            POST_SUCCESS = false;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(HomeActivity.this,
                                                            "Post failed, check connection and try again",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public URI getRequestURI() {
                                        return null;
                                    }

                                    @Override
                                    public void setRequestURI(URI requestURI) {

                                    }

                                    @Override
                                    public Header[] getRequestHeaders() {
                                        return new Header[0];
                                    }

                                    @Override
                                    public void setRequestHeaders(Header[] requestHeaders) {

                                    }

                                    @Override
                                    public boolean getUseSynchronousMode() {
                                        return false;
                                    }

                                    @Override
                                    public void setUseSynchronousMode(boolean useSynchronousMode) {

                                    }

                                    @Override
                                    public boolean getUsePoolThread() {
                                        return false;
                                    }

                                    @Override
                                    public void setUsePoolThread(boolean usePoolThread) {

                                    }

                                    @Override
                                    public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                                    }

                                    @Override
                                    public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {

                                    }

                                    @Override
                                    public Object getTag() {
                                        return null;
                                    }

                                    @Override
                                    public void setTag(Object TAG) {

                                    }
                                });

                    }
                }, 1500);


        progressDialog = new ProgressDialog(HomeActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Connecting to our Servers...");
        progressDialog.show();

    }

    //Go to the history page Eventually
    public void history_click(View v) {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);

    }

    //User clicked on Scan barcode, bring up popup
    public void scan_barcode_click(View v) {
        Intent type_location_intent = new Intent(HomeActivity.this, ScanOrTypeActivity.class);
        type_location_intent.putExtra("User", user);

        startActivityForResult(type_location_intent, BARCODE_METHOD_REQUEST);
        System.out.println("Scan barcode click");

    }

    //The request code doesnt matter so long as it is an okay.
    //If the barcode is in the users database we can go to the Items page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            final String barcode = data.getStringExtra("result");
            final long timestamp = System.currentTimeMillis()/1000;

            //Valid location, lets go to the location questions
            if (user.check_location(barcode)) {

                boolean ACTUALLY_MINE = false;
                for (Location l : user.get_my_locations()) {
                    if (l.barcode_num.equals(barcode)){
                        ACTUALLY_MINE = true;
                    }
                }


                if (!ACTUALLY_MINE) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    continue_anyway(barcode, timestamp);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    //Do nothing, they have the opportunity to answer item questions
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder ab = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                    ab.setMessage("This Location is not assigned to you...\n\nCONTINUE ANYWAY?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }

                else {
                    user.get_location(barcode).set_timestamp(timestamp);

                    user.populate_location(barcode);
                    user.set_current_barcode(barcode);

                    user.get_location(barcode).set_items();
                    user.get_location(barcode).print_location();
                    //Start activity that pulls up location questions
                    Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);

                    intent.putExtra("User", user);

                    System.out.println("Put it in the intent");
                    startActivity(intent);

                }

            }
            else {
                Toast.makeText(this, "This barcode is not a valid location...", Toast.LENGTH_SHORT).show();
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        }

    }
    public void post_success() {
        progressDialog.dismiss();

        System.out.println("IS IT IN POST SUCCESS");

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.sync_btn);
        System.out.println("Successful post");

        //Below Gets the History objects together
        ArrayList<String> ts_already_added = new ArrayList<String>();
        final ArrayList<HistoryObject> passing_obs = new ArrayList<HistoryObject>();
        final ArrayList<HistoryObject> failing_obs = new ArrayList<HistoryObject>();

        for (Answer a : user.get_answer_list()) {
            //If the timestamp has already been added to a History object
            if (ts_already_added.contains(a.getTime_answered())){
                if (a.answer_text.equals("0")) {
                    for (HistoryObject h : passing_obs) {
                        //Basically moves the failing obj to the failing array
                        if (h.getTime_stamp().equals(a.getTime_answered())) {
                            int index_to_del = passing_obs.indexOf(h);
                            passing_obs.remove(index_to_del);

                            h.setFailing();
                            failing_obs.add(h);

                        }
                    }
                }

            }
            else {
                ts_already_added.add(a.getTime_answered());
                HistoryObject h_obj = new HistoryObject(a.getTime_answered(),
                        a.getLoc_id(), a.getAnswer_text());


                if (h_obj.isPassing()) {
                    passing_obs.add(h_obj);
                }
                else {
                    failing_obs.add(h_obj);
                }


            }

        }

        System.out.println("There are " + passing_obs.size() +
                " passing objs, " + failing_obs.size() + " failing obs");




        runOnUiThread(new Runnable(){

            @Override
            public void run(){
                //update ui here
                // display toast here
                Toast.makeText(getApplicationContext(), "Successfully Updated the Database", Toast.LENGTH_SHORT).show();

                try {

                    //Create the new file
                    File dir = getFilesDir();
                    File file = new File(dir, "times.tmp");

                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    ArrayList<HistoryObject> ans = (ArrayList<HistoryObject>) ois.readObject();

                    for (HistoryObject h : failing_obs)  {
                        ans.add(h);
                    }
                    for (HistoryObject h : passing_obs) {
                        ans.add(h);
                    }


                    Collections.sort(ans, new Comparator<HistoryObject>() {
                        @Override public int compare(HistoryObject p1, HistoryObject p2) {
                            return Integer.parseInt(p1.getTime_stamp()) - Integer.parseInt(p2.getTime_stamp()); // Ascending
                        }

                    });

                    //Only saves the last 50
                    if (ans.size() > 49){
                        for (int i = ans.size() - 1; i > 49; i--){
                            ans.remove(i);
                        }
                    }


                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(ans);

                    oos.close();

                }
                catch (IOException e) {
                    System.out.println("Does not exist yet");

                    ArrayList<HistoryObject> ans = new ArrayList<HistoryObject>();

                    for (HistoryObject h : failing_obs)  {
                        ans.add(h);
                    }
                    for (HistoryObject h : passing_obs) {
                        ans.add(h);
                    }

                    try {
                        File dir = getFilesDir();
                        File file = new File(dir, "times.tmp");

                        FileOutputStream fos = new FileOutputStream(file);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(user.get_answer_list());

                    }
                    catch (FileNotFoundException e1) {
                        System.out.println("JUST GIVE UP");
                        e1.printStackTrace();
                    }
                    catch (IOException e1) {
                        System.out.println("JUST GIVE UP");
                        e1.printStackTrace();
                    }


                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

        user.clear_answers();
        //Delete the file
        File dir = getFilesDir();
        File file = new File(dir, "t.tmp");
        file.delete();


    }
    public void continue_anyway(String barcode, long timestamp) {
        user.get_location(barcode).set_timestamp(timestamp);

        user.populate_location(barcode);
        user.set_current_barcode(barcode);

        user.get_location(barcode).set_items();
        user.get_location(barcode).print_location();
        //Start activity that pulls up location questions
        Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);

        intent.putExtra("User", user);

        System.out.println("Put it in the intent");
        startActivity(intent);

    }

}

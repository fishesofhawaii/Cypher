package com.example.inchkyle.cypherback;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

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


    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        // Construct the data source
        ArrayList<Location.Object> arrayOfLocations = new ArrayList<Location.Object>();
        // Create the adapter to convert the array to views
        LocationAdapter adapter = new LocationAdapter(this, arrayOfLocations);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);


        // Add item to adapter
        Location.Object newLocation0 = new Location.Object("30 days", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation0);
        Location.Object newLocation1 = new Location.Object("1 year", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation1);
        Location.Object newLocation2 = new Location.Object("3 months", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation2);
        Location.Object newLocation3 = new Location.Object("30 days", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation3);
        Location.Object newLocation4 = new Location.Object("1 year", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation4);
        Location.Object newLocation5 = new Location.Object("3 months", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation5);
        Location.Object newLocation6 = new Location.Object("30 days", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation6);
        Location.Object newLocation7 = new Location.Object("1 year", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation7);
        Location.Object newLocation8 = new Location.Object("3 months", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation8);
        Location.Object newLocation9 = new Location.Object("30 days", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation9);
        Location.Object newLocation10 = new Location.Object("1 year", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation10);
        Location.Object newLocation11 = new Location.Object("3 months", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation11);
        Location.Object newLocation12 = new Location.Object("30 days", "This view will be populated with location details", "Plant A");
        adapter.add(newLocation12);



//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("change color");
//                FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.sync_btn);
//                btn.setBackgroundTintList(ColorStateList.valueOf(R.color.AMRedDark));
//            }
//        }, 2000);




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
    }

    //Clicking the update update button should retrieve database values for the employee again
    public void update_click(View v) throws JSONException, UnsupportedEncodingException {
        System.out.println("GOING TO UPDATE THE DATABASE\n");

        String BASE_URL = user.get_BASE_URL();
        StringEntity entity = user.get_JSON_entity();

        final AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();

        final String POST_address = BASE_URL + "/questions/addanswers/";
        //This is the post with the employee id (the payroll_id)

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
                        if (POST_SUCCESS){
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
                    }

    //           TODO: Display message if user is unable to log in

                    @Override
                    public void sendRetryMessage(int retryNo) {
                        //Gets called every attempt to send POST
                        System.out.println("Send Retry");

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

    //Go to the history page Eventually
    public void history_click(View v) {
        user.clear_answers();

        //Delete the file
        File dir = getFilesDir();
        File file = new File(dir, "t.tmp");
        file.delete();

        Toast.makeText(this, "deleted file", Toast.LENGTH_SHORT).show();
    }

    //User clicked on Scan barcode, bring up popup
    public void scan_barcode_click(View v) {
        Intent typeLocation_intent = new Intent(HomeActivity.this, ScanOrTypeActivity.class);
        startActivityForResult(typeLocation_intent, BARCODE_METHOD_REQUEST);
        System.out.println("Scan barcode click");

    }

    //The request code doesnt matter so long as it is an okay.
    //If the barcode is in the users database we can go to the Items page
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to

        if (resultCode == RESULT_OK){
            String barcode = data.getStringExtra("result");
            long timestamp = System.currentTimeMillis()/1000;

            //Valid location, lets go to the location questions
            if (user.check_location(barcode)) {

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
            else {
                Toast.makeText(this, "This Location is not on your route!" +
                        "\nScan a Location that is on your route!", Toast.LENGTH_SHORT).show();
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        }

    }
    public void post_success() {
        System.out.println("IS IT IN POST SUCCESS");

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.sync_btn);
        btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00aa00")));

        user.clear_answers();
        //Delete the file
        File dir = getFilesDir();
        File file = new File(dir, "t.tmp");
        file.delete();


    }

}

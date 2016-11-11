package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.util.EntityUtils;

public class LoginActivity extends AppCompatActivity {
    String payroll_id = "nick"; //This is their payroll id
    String json_string;
    Boolean LOGIN_SUCCESS = false;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



    }


    public void log_in(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

//        params.put("question_text", q);
        params.put("user", payroll_id);

        //This is the post with the employee id (the payroll_id)
        client.post("http://35.14.84.166:8000/questions/questionsbyuser",

                params, new ResponseHandlerInterface() {

            @Override
            public void sendResponseMessage(HttpResponse response) throws IOException {
                json_string = EntityUtils.toString(response.getEntity());
                System.out.println(json_string);
                LOGIN_SUCCESS = true;

            }

            @Override
            public void sendStartMessage() {

            }

            @Override
            public void sendFinishMessage() {
                //Last called in a success AND failure
                if (LOGIN_SUCCESS){
                    go_to_home();
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


    //If the post was successful (employee exists) then we can login to the app
    public void go_to_home() {

        Intent login_intent = new Intent(LoginActivity.this, HomeActivity.class);
        user = new User(payroll_id, json_string);

        login_intent.putExtra("User", user);

        startActivity(login_intent);

    }

}

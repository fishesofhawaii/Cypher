package com.example.inchkyle.cypherback;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by inchkyle on 11/3/16.
 */

//This activity is set aside for typing the barcode. Enter text in the EditText
//Once confirm is pressed, it is sent to the home activity for analysis
// (after going through scanner type)
public class TypeBarcodeActivity extends Activity {
    EditText barcode_entered;
    User user;
    boolean is_item = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.type_barcode);

        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");
            System.out.println("Added user data");
        }
        System.out.println("After adding");

        if (user.location_barcode != null) {
            is_item = true;
        }

        barcode_entered = (EditText) findViewById(R.id.barcode_txt);
//        barcode_shown = (TextView) findViewById(R.id.displayed_barcode_txt);


        barcode_entered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Not Necessary
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //We want to update "barcode shown"
//                barcode_shown.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Not Necessary
            }
        });

        barcode_entered.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    confirm();
                }
                return handled;
            }
        });
    }

    public void confirm() {
        String entered_code = barcode_entered.getText().toString();


        if (is_item) {
            Item i = user.get_location(user.location_barcode).get_item(entered_code);

            if (i == null) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                continue_anyway();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                //Do nothing, they have the opportunity to answer item questions
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(TypeBarcodeActivity.this, R.style.AlertDialogTheme);
                ab.setMessage("This barcode doesn't match our records...\n\nCONTINUE ANYWAY?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
            else {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", entered_code);
                System.out.println("Giving : " + entered_code);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

        }
        else if (!is_item) {
            Location l = user.get_location(entered_code);

            if (l == null) {
                Toast.makeText(this, "This is an invalid location, try again...", Toast.LENGTH_SHORT).show();
            }
            else {
                if (l.get_user_assigned().toLowerCase().equals(user.get_payroll_id())){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", entered_code);
                    System.out.println("Giving : " + entered_code);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                }
                else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    continue_anyway();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    //Do nothing, they have the opportunity to answer item questions
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder ab = new AlertDialog.Builder(TypeBarcodeActivity.this, R.style.AlertDialogTheme);
                    ab.setMessage("This Location is not assigned to you...\n\nCONTINUE ANYWAY?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }

            }
        }




    }

    private void continue_anyway() {
        Intent returnIntent = new Intent();
        String entered_code = barcode_entered.getText().toString();

        //So they asserted us that the barcode that they gave is truly the device, we will let them
        //Override
        if (is_item) {
            if (user.get_valid_item_barcodes().size() != 0) {
                returnIntent.putExtra("result", user.get_valid_item_barcodes().get(0));
                System.out.println("Giving : " + user.get_valid_item_barcodes().get(0));

            }
            else {
                Toast.makeText(this, "There are no valid barcodes for this item... " +
                        "fix that on the admin side", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            returnIntent.putExtra("result", entered_code);
            System.out.println("Giving : " + entered_code);
        }

        setResult(Activity.RESULT_OK, returnIntent);
        finish();


    }

}
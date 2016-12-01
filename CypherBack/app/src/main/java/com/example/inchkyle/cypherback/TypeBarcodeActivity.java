package com.example.inchkyle.cypherback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by inchkyle on 11/3/16.
 */

//This activity is set aside for typing the barcode. Enter text in the EditText
//Once confirm is pressed, it is sent to the home activity for analysis
// (after going through scanortype)
public class TypeBarcodeActivity extends Activity {
    EditText barcode_entered;
//    TextView barcode_shown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.type_barcode);


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
    }

    public void confirm(View v) {

        Intent returnIntent = new Intent();

//        returnIntent.putExtra("result", barcode_shown.getText().toString());
//        System.out.println("Giving : " + barcode_shown.getText().toString());
        returnIntent.putExtra("result", barcode_entered.getText().toString());
        System.out.println("Giving : " + barcode_entered.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
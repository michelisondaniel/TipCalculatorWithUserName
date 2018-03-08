package com.murach.tipcalculator;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class TipCalculatorActivity extends Activity
        implements OnEditorActionListener, OnClickListener {

    // define variables for the widgets
    private EditText billAmountEditText;
    private TextView percentTextView;
    private Button   percentUpButton;
    private Button   percentDownButton;
    private TextView tipTextView;
    private TextView totalTextView;
    private TextView displayUsername;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables that should be saved
    private String billAmountString = "";
    private float tipPercent = .15f;

    // declare a constant for the tag parameter
    private static final String TAG = "TipCalculatorActivity";

    //define constants for rounding
    private final int ROUND_NONE = 0;
    private final int ROUND_TIP = 1;
    private final int ROUND_TOTAL = 2;

    // setup the preferences
    private SharedPreferences prefs;
    private boolean rememberTipPercent = true;
    private int rounding = ROUND_NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        // get references to the widgets
        billAmountEditText = (EditText) findViewById(R.id.billAmountEditText);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        percentUpButton = (Button) findViewById(R.id.percentUpButton);
        percentDownButton = (Button) findViewById(R.id.percentDownButton);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);
        displayUsername = (TextView) findViewById(R.id.displayUsername);


        // set the listeners
        billAmountEditText.setOnEditorActionListener(this);
        percentUpButton.setOnClickListener(this);
        percentDownButton.setOnClickListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //set default values for the prefs
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPrefs object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // add logcat trace
        Log.d(TAG, "onCreate method executed");

        // add toast (small gray thing in app
        Toast t = Toast.makeText(this, "onCreate Method", Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onPause() {
        // save the instance variables       
        Editor editor = savedValues.edit();
        editor.putString("billAmountString", billAmountString);
        editor.putFloat("tipPercent", tipPercent);
        editor.commit();

        super.onPause();

        Log.d(TAG, "onPause method executed");

        Toast t = Toast.makeText(this, "onPause Method", Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        billAmountString = savedValues.getString("billAmountString", "");
        tipPercent = savedValues.getFloat("tipPercent", 0.15f);

        // set the bill amount on its widget
        billAmountEditText.setText(billAmountString);

        // get the preferences when app resumes, saved preferences from the preferences.xml
        rememberTipPercent = prefs.getBoolean("pref_forget_percent", true);
        rounding = Integer.parseInt(prefs.getString("pref_rounding", "0"));
        displayUsername.setText(prefs.getString("edit_text_username", "John Smith"));

        // calculate and display
        calculateAndDisplay();

        Log.d(TAG, "onResume method executed");

        Toast t = Toast.makeText(this, "onResume Method", Toast.LENGTH_LONG);
        t.show();
    }

    public void calculateAndDisplay() {

        // get the bill amount
        billAmountString = billAmountEditText.getText().toString();
        float billAmount;
        if (billAmountString.equals("")) {
            billAmount = 0;
        }
        else {
            billAmount = Float.parseFloat(billAmountString);
        }

        float tipAmount = 0;
        float totalAmount = 0;
        float tipPercentDisplay = 0;

        if(rounding == ROUND_NONE){
            tipAmount = billAmount * tipPercent;
            totalAmount = billAmount + tipAmount;
        } else if (rounding == ROUND_TIP){
            tipAmount = StrictMath.round(billAmount * tipPercent);
            totalAmount = billAmount + tipAmount;
            tipPercentDisplay = tipAmount / billAmount;
        } else if (rounding == ROUND_TOTAL){
            float tipNotRounded = billAmount * tipPercent;
            tipAmount = tipNotRounded;
            totalAmount = StrictMath.round(billAmount + tipNotRounded);
        }

        // calculate tip and total 
        // float tipAmount = billAmount * tipPercent;
        // float totalAmount = billAmount + tipAmount;

        // display the other results with formatting
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        tipTextView.setText(currency.format(tipAmount));
        totalTextView.setText(currency.format(totalAmount));

        NumberFormat percent = NumberFormat.getPercentInstance();
        percentTextView.setText(percent.format(tipPercent));

        Log.d(TAG, "CalculateAndDisplay method executed");

        Toast t = Toast.makeText(this, "CalculateAndDisplay Method", Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.percentDownButton:
                tipPercent = tipPercent - .01f;
                calculateAndDisplay();
                break;
            case R.id.percentUpButton:
                tipPercent = tipPercent + .01f;
                calculateAndDisplay();
                break;
        }
    }
    // inflate the menu to show on the application
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.tip_calc_menu, menu);
        return true;
    }

    // create item selected interactions by item id
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.tip_calc_menu:
                startActivity(new Intent(getApplicationContext(),
                SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
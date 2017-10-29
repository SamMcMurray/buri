package com.example.samuelmcmurray.experimenting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Filter is a class that helps narrow down the restaurant options.
 * @author Sam McMurray
 * May 15, 2017
 */
public class Filter extends AppCompatActivity {


    /**
     * This method starts the instance
     * @param savedInstanceState Caller return
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


    }

    /**
     * This method defines which file of food options to pull when a button is selected
     * @param view Filter that was selected
     */
    public void newFilter(View view){
        String fileName;

        switch(view.getId()){
            case R.id.Quick_Bite:
                fileName = "quick";
                break;
            case R.id.sports:
                fileName = "sport";
                break;
            case R.id.sweet:
                fileName = "sweet";
                break;
            case R.id.bar:
                fileName = "bar";
                break;
            case R.id.greasy:
                fileName = "greasy";
                break;
            case R.id.coffee:
                fileName = "coffee";
                break;
            case R.id.fine_dining:
                fileName = "fine";
                break;
            case R.id.rando:
                fileName = "food";
                break;
            default:
                fileName = "food";
        }
        Intent intent = new Intent();
        intent.putExtra("edittextvalue",fileName);
        setResult(RESULT_OK,intent);
        finish();
    }
}

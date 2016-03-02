package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andreaskarinam.mylibrary.FakeData;

public class Main_mobile extends AppCompatActivity {

    Button enter_zipcode;
    Button current_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mobile);

        enter_zipcode = (Button) findViewById(R.id.button);
        current_location = (Button) findViewById(R.id.button2);
    }

    public void switch_to_zipcode_view(View view) {
        Intent intent = new Intent(this, zipcode_mobile.class);
        startActivity(intent);
    }

    public void switch_to_congressional_view(View view) {
        Intent intent = new Intent(this, congressional_mobile.class);
        intent.putExtra(FakeData.COUNTY_INDEX_KEY, 0);
        startActivity(intent);
    }
}

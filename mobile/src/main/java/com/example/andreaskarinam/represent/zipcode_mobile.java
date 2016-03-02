package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andreaskarinam.mylibrary.FakeData;

public class zipcode_mobile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_mobile);
    }

    public void switch_to_congressional_view(View view) {
        Intent intent = new Intent(this, congressional_mobile.class);
        intent.putExtra(FakeData.COUNTY_INDEX_KEY, 1);
        startActivity(intent);
    }
}

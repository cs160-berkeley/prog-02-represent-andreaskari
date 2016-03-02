package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreaskarinam.mylibrary.County;
import com.example.andreaskarinam.mylibrary.FakeData;
import com.example.andreaskarinam.mylibrary.Representative;

public class detailed_mobile extends AppCompatActivity {

    public int county_index;
    public int representative_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_mobile);

        FakeData data = new FakeData();

        Intent intent = getIntent();
        if (intent != null) {
            county_index = intent.getIntExtra(FakeData.COUNTY_INDEX_KEY, 0);
            representative_index = intent.getIntExtra(FakeData.REPRESENTATIVE_INDEX_KEY, 0);
        }

        County county = data.counties.get(county_index);
        Representative representative = county.representatives.get(representative_index);

        TextView rep_name_text = (TextView) findViewById(R.id.name_text);
        rep_name_text.setText(representative.rep_name);

        ImageView lineColorCode = (ImageView) findViewById(R.id.party_image);
        GradientDrawable party_circle = (GradientDrawable) lineColorCode.getBackground();
        party_circle.setColor(representative.getColor());

        TextView rep_email_text = (TextView) findViewById(R.id.party_text);
        if (representative.party.equals("D")) {
            rep_email_text.setText("Democratic Party");
        } else {
            rep_email_text.setText("Republican Party");
        }

        ImageView rep_image = (ImageView) findViewById(R.id.rep_image);
        if (representative.rep_name.equals("Barbara Boxer")) {
            rep_image.setImageResource(R.drawable.boxer);
        } else if (representative.rep_name.equals("Diane Feinstein")) {
            rep_image.setImageResource(R.drawable.feinstein);
        } else {
            rep_image.setImageResource(R.drawable.mcclintock);
        }

//        rep_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), detailed_mobile.class);
//
//                intent.putExtra(FakeData.COUNTY_INDEX_KEY, county_index);
//                intent.putExtra(FakeData.REPRESENTATIVE_INDEX_KEY, representative_index);
//
//                startActivity(intent);
//            }
//        });
    }
}

package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andreaskarinam.mylibrary.FakeData;

public class zipcode_mobile extends AppCompatActivity {

    EditText zipcode_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_mobile);

        zipcode_text = (EditText) findViewById(R.id.zipcode_text);
    }

    public void switch_to_congressional_view(View view) {
        String message = "/Zipcode";
        String message_contents = zipcode_text.getText().toString();

        // Add code to validate the zipcode entered?

        if (message_contents.length() != 5) {
            Toast toast = Toast.makeText(getBaseContext(), "Invalid zipcode", Toast.LENGTH_SHORT);
            toast.show();
        } else {
//            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//            sendIntent.putExtra(message, message_contents);
//            startService(sendIntent);

            Intent intent = new Intent(this, congressional_mobile.class);
            intent.putExtra(message, message_contents);
            startActivity(intent);
        }
    }
}

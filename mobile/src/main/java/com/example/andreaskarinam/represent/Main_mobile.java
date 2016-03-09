package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andreaskarinam.mylibrary.FakeData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class Main_mobile extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button enter_zipcode;
    Button current_location;

    Location mLastLocation;
    String mLatitudeText;
    String mLongitudeText;

    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mobile);

        enter_zipcode = (Button) findViewById(R.id.button);
        current_location = (Button) findViewById(R.id.button2);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onRestart()
    {  // After a pause OR at startup
        mGoogleApiClient.connect();
        super.onRestart();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("Trying to get last location");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            System.out.println(mLastLocation.getLatitude());
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void switch_to_zipcode_view(View view) {
        Intent intent = new Intent(this, zipcode_mobile.class);
        startActivity(intent);
    }

    public void switch_to_congressional_view(View view) {
        String message = "/Latitude and Longitude";
        String[] message_contents = {mLatitudeText, mLongitudeText};
        System.out.println(message_contents);

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra(message, message_contents);
//        sendIntent.putExtra(FakeData.COUNTY_INDEX_KEY, 0);
        startService(sendIntent);

        Intent intent = new Intent(this, congressional_mobile.class);
//        intent.putExtra(FakeData.COUNTY_INDEX_KEY, 0);
        intent.putExtra(message, message_contents);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}

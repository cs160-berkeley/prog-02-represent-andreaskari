package com.example.andreaskarinam.represent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.andreaskarinam.mylibrary.FakeData;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class PhoneListenerService extends WearableListenerService {
    
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        System.out.println("Received message from watch");
        if (messageEvent.getPath().equalsIgnoreCase(FakeData.MESSAGE)) {
            System.out.println("Received important message from watch");

            String indices_string = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            String[] indices = indices_string.split(" ");
            int county_index = Integer.getInteger(indices[0]);
            int representative_index = Integer.getInteger(indices[1]);

            Intent intent = new Intent(this, detailed_mobile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(FakeData.COUNTY_INDEX_KEY, county_index);
            intent.putExtra(FakeData.REPRESENTATIVE_INDEX_KEY, representative_index);
            startActivity(intent);

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}

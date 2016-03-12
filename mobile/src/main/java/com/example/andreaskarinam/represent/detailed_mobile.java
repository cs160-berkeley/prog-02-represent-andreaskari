package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class detailed_mobile extends AppCompatActivity {

    public static String DEMOCRAT_COLOR = "#2F80ED";
    public static String REPUBLICAN_COLOR = "#E44A4A";
    public static String INDEPENDENT_COLOR = "#FFFFFF";

    public int county_index;

    private String bioguide_id;
    private int representative_index;

    private String title;
    private String full_name;
    private String party;
    private String term_end;
    private String profile_url;

    public static JSONObject currentJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_mobile);

        Intent intent = getIntent();
        String committees_api_call = "";
        String bills_api_call = "";
        if (intent != null) {
            final String detailedMessage = "/Representative Index";
            if (intent.hasExtra(detailedMessage)) {
                representative_index = intent.getIntExtra(detailedMessage, 0);
                title = (String) congressional_mobile.data.get(representative_index).get(0);
                full_name = (String) congressional_mobile.data.get(representative_index).get(1);
                party = (String) congressional_mobile.data.get(representative_index).get(4);
                term_end = (String) congressional_mobile.data.get(representative_index).get(5);
                bioguide_id = (String) congressional_mobile.data.get(representative_index).get(6);
                profile_url = (String) congressional_mobile.data.get(representative_index).get(8);
                try {
                    committees_api_call = "http://congress.api.sunlightfoundation.com/committees?member_ids="
                            + URLEncoder.encode(bioguide_id, "UTF-8")
                            + "&apikey=47a2503bbd494437916cc6acfbdf80fe";

                    bills_api_call = "http://congress.api.sunlightfoundation.com/bills?sponsor_id="
                            + URLEncoder.encode(bioguide_id, "UTF-8")
                            + "&apikey=47a2503bbd494437916cc6acfbdf80fe";
                } catch (UnsupportedEncodingException ex) {
                    System.out.println("Can't encode api call");
                }
            }
        }


        new DownloadTask().execute(committees_api_call);

        while (currentJSON == null) {
            // make progress bar?
        }

        String committees_string = "";
        try {
            JSONArray committeesJSONArray = currentJSON.getJSONArray("results");
            for (int i = 0; i < committeesJSONArray.length(); i++) {
                JSONObject repJSON = committeesJSONArray.getJSONObject(i);
                committees_string += repJSON.getString("name") + "\n\n";
            }
        } catch (JSONException ex) {
            System.out.println("Can't get repJSON Object");
        }
        committees_string = committees_string.trim();
        currentJSON = null;

        new DownloadTask().execute(bills_api_call);

        while (currentJSON == null) {
            // make progress bar?
        }

        String bills_string = "";
        try {
            JSONArray billsJSONArray = currentJSON.getJSONArray("results");
            for (int i = 0; i < billsJSONArray.length(); i++) {
                JSONObject repJSON = billsJSONArray.getJSONObject(i);
                bills_string += repJSON.getString("official_title") + "\n(" + this.formatDate(repJSON.getString("introduced_on")) + ")\n\n";
            }
        } catch (JSONException ex) {
            System.out.println("Can't get repJSON Object");
        }
        bills_string = bills_string.trim();
        currentJSON = null;

        TextView rep_name_text = (TextView) findViewById(R.id.name_text);
        if (this.title.equals("Rep")) {
            rep_name_text.setText("Representative\n" + this.full_name);
        } else {
            rep_name_text.setText("Senator\n" + this.full_name);
        }

        ImageView lineColorCode = (ImageView) findViewById(R.id.party_image);
        GradientDrawable party_circle = (GradientDrawable) lineColorCode.getBackground();
        party_circle.setColor(this.getColor());

        TextView party_text = (TextView) findViewById(R.id.party_text);
        if (this.party.equals("D")) {
            party_text.setText("Democratic Party");
        } else if (this.party.equals("R")) {
            party_text.setText("Republican Party");
        } else {
            party_text.setText("Independent");
        }

        TextView term_text = (TextView) findViewById(R.id.term_text);
        term_text.setText("Term ends " + this.formatDate(this.term_end));
        System.out.println(this.term_end);

        TextView committees_text = (TextView) findViewById(R.id.committees_text);
        committees_text.setText(committees_string);

        TextView bills_text = (TextView) findViewById(R.id.bills_text);
        bills_text.setText(bills_string);

        ImageView rep_image = (ImageView) findViewById(R.id.rep_image);
        new DownloadImageTask(rep_image).execute(this.profile_url);
    }

    private int getColor() {
        if (this.party.equals("D")) {
            return Color.parseColor(DEMOCRAT_COLOR);
        } else if (this.party.equals("R")) {
            return Color.parseColor(REPUBLICAN_COLOR);
        }
        return Color.parseColor(INDEPENDENT_COLOR);
    }

    private String formatDate(String date) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        String[] date_parts = date.split("-");

        String month = monthNames[Integer.parseInt(date_parts[1]) - 1];
        int day = Integer.parseInt(date_parts[2]);
        return month + " " + day + ", " + date_parts[0];
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    currentJSON = new JSONObject(data);

                    return data;
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Failure";
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
//            Toast.makeText(congressional_mobile.this, result, Toast.LENGTH_LONG).show();
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

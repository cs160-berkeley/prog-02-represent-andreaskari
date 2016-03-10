package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreaskarinam.mylibrary.County;
import com.example.andreaskarinam.mylibrary.FakeData;
import com.example.andreaskarinam.mylibrary.Representative;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class congressional_mobile extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    // The {@link ViewPager} that will host the section contents.
    private ViewPager mViewPager;
    public static int county_index;
    public static JSONObject currentJSON;
    public static JSONArray repJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_mobile);

        setTitle("Represent");

        Intent intent = getIntent();
        String api_call = "";
        if (intent != null) {
            String lat_long_message = "/Latitude and Longitude";
            String zipcode_message = "/Zipcode";
            if (intent.hasExtra(lat_long_message)) {
                String[] lat_long = intent.getStringArrayExtra(lat_long_message);
                System.out.println(lat_long);
                try {
                    api_call = "http://congress.api.sunlightfoundation.com/legislators/locate?latitude="
                            + URLEncoder.encode(lat_long[0], "UTF-8") + "&longitude="
                            + URLEncoder.encode(lat_long[1], "UTF-8")
                            + "&apikey=47a2503bbd494437916cc6acfbdf80fe";
                } catch (UnsupportedEncodingException ex) {
                    System.out.println("Can't encode api call");
                }
            } else if (intent.hasExtra(zipcode_message)) {
                String zipcode = intent.getStringExtra(zipcode_message);
                System.out.println(zipcode);
                try {
                    api_call = "http://congress.api.sunlightfoundation.com/legislators/locate?zip="
                            + URLEncoder.encode(zipcode, "UTF-8")
                            + "&apikey=47a2503bbd494437916cc6acfbdf80fe";
                } catch (UnsupportedEncodingException ex) {
                    System.out.println("Can't encode api call");
                }
            }
            county_index = intent.getIntExtra(FakeData.COUNTY_INDEX_KEY, 0);
        }

        new DownloadTask().execute(api_call);

        while (currentJSON == null) {
            // make progress bar?
        }

        try {
            repJSONArray = currentJSON.getJSONArray("results");
        } catch (JSONException ex) {
            System.out.println("Can't get repJSON Object");
        }
        currentJSON = null;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static String DEMOCRAT_COLOR = "#2F80ED";
        public static String REPUBLICAN_COLOR = "#E44A4A";
        public static String INDEPENDENT_COLOR = "#FFFFFF";

        private static final String ARG_SECTION_NUMBER = "section_number";
        private int county_index;
//        private int representative_index;
        private County county;
        private Representative representative;

        private int zipcode;
        private int latitude;
        private int longitude;
        private String district;
        private String bioguide_id;
        private int representative_index;

        private String title;
        private String full_name;
        private String email;
        private String website;
        private String party;
        private String term_end;

        public PlaceholderFragment(int index) {
//            this.county_index = county_index;
//            this.representative_index = rep_index;
//            this.county = data.counties.get(county_index);
//            this.representative = county.representatives.get(rep_index);

            this.representative_index = index;
            try {
                JSONObject repJSON = repJSONArray.getJSONObject(index);
                String first_name = repJSON.getString("first_name");
                String middle_name = repJSON.getString("middle_name");
                String last_name = repJSON.getString("last_name");
                this.title = repJSON.getString("title");
                this.full_name = first_name + " " + last_name;
                if (!middle_name.equals("null")) {
                    this.full_name = first_name + " " + middle_name + " " + last_name;
                }
                this.email = repJSON.getString("oc_email");
                this.website = repJSON.getString("website");
                this.party = repJSON.getString("party");
                this.term_end = repJSON.getString("term_end");
                this.bioguide_id = repJSON.getString("bioguide_id");
            } catch (JSONException ex) {
                System.out.println("Error retrieving candidate JSON data");
            }
        }

        public int getColor() {
            if (this.party.equals("D")) {
                return Color.parseColor(DEMOCRAT_COLOR);
            } else if (this.party.equals("R")) {
                return Color.parseColor(REPUBLICAN_COLOR);
            }
            return Color.parseColor(INDEPENDENT_COLOR);
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
//            FakeData data = new FakeData();
//            PlaceholderFragment fragment = new PlaceholderFragment(data, congressional_mobile.county_index, sectionNumber);
            PlaceholderFragment fragment = new PlaceholderFragment(sectionNumber);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_congressional_mobile, container, false);

            LinearLayout rl = (LinearLayout) rootView.findViewById(R.id.lin_layout);
            rl.setBackgroundColor(this.getColor());

            TextView rep_name_text = (TextView) rootView.findViewById(R.id.name_text);
            rep_name_text.setText(this.title + ". " + this.full_name + " (" + this.party + ")");

            TextView rep_email_text = (TextView) rootView.findViewById(R.id.email_text);
            rep_email_text.setText(this.email);

            TextView rep_website_text = (TextView) rootView.findViewById(R.id.website_text);
            rep_website_text.setText(this.website);

            TextView rep_tweet_text = (TextView) rootView.findViewById(R.id.tweet_content_text);
//            rep_tweet_text.setText(this.representative.last_tweet);

            ImageView rep_image = (ImageView) rootView.findViewById(R.id.rep_image);
//            if (representative.rep_name.equals("Barbara Boxer")) {
//                rep_image.setImageResource(R.drawable.boxer);
//            } else if (representative.rep_name.equals("Diane Feinstein")) {
//                rep_image.setImageResource(R.drawable.feinstein);
//            } else if (representative.rep_name.equals("Barbara Lee")) {
//                rep_image.setImageResource(R.drawable.lee);
//            } else if (representative.rep_name.equals("Tom Udall")) {
//                rep_image.setImageResource(R.drawable.udall);
//            } else if (representative.rep_name.equals("Martin Heinrich")) {
//                rep_image.setImageResource(R.drawable.heinrich);
//            } else if (representative.rep_name.equals("Stevan Pearce")) {
//                rep_image.setImageResource(R.drawable.pearce);
//            } else {
//                rep_image.setImageResource(R.drawable.mcclintock);
//            }

            final String bioguide_id = this.bioguide_id;
            final String title = this.title;
            final String full_name = this.full_name;
            final String party = this.party;
            final String term_end = this.term_end;
            rep_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), detailed_mobile.class);
                    intent.putExtra("/Bioguide", bioguide_id);
                    intent.putExtra("/Title", title);
                    intent.putExtra("/Full_Name", full_name);
                    intent.putExtra("/Party", party);
                    intent.putExtra("/Term_End", term_end);
                    startActivity(intent);
                }
            });

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}

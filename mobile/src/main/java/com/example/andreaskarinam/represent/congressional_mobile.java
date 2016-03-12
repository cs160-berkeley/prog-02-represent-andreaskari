package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.UserTimeline;

import retrofit.http.GET;
import retrofit.http.Query;

public class congressional_mobile extends AppCompatActivity {

    private static final String TWITTER_KEY = "7sIEH0sd2ieymR8e9i9HV3BnE";
    private static final String TWITTER_SECRET = "WhWUsVkDiLFTJCpM3wDwf2VsaxQ4GptKPogbBHA4mKPXykYlo2";

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
    public static Bitmap currentBitmap;
    public static JSONArray repJSONArray;

    public static ArrayList<ArrayList> data;

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
            data = new ArrayList<ArrayList>(repJSONArray.length());
            for (int i = 0; i < repJSONArray.length(); i++) {
                ArrayList<String> repData = new ArrayList<String>(repJSONArray.length());

                JSONObject repJSON = repJSONArray.getJSONObject(i);
                String first_name = repJSON.getString("first_name");
                String middle_name = repJSON.getString("middle_name");
                String last_name = repJSON.getString("last_name");
                repData.add(repJSON.getString("title"));
                if (!middle_name.equals("null")) {
                    repData.add(first_name + " " + middle_name + " " + last_name);
                } else {
                    repData.add(first_name + " " + last_name);
                }
                repData.add(repJSON.getString("oc_email"));
                repData.add(repJSON.getString("website"));
                repData.add(repJSON.getString("party"));
                repData.add(repJSON.getString("term_end"));
                repData.add(repJSON.getString("bioguide_id"));
                repData.add(repJSON.getString("twitter_id"));
                repData.add("https://github.com/unitedstates/images/blob/gh-pages/congress/450x550/"
                        + repData.get(6) + ".jpg?raw=true");
                data.add(repData);
            }
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


        String[] titles = new String[data.size()];
        String[] names = new String[data.size()];
        String[] parties = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            titles[i] = (String) data.get(i).get(0);
            names[i] = (String) data.get(i).get(1);
            parties[i] = (String) data.get(i).get(4);
            System.out.println((String) data.get(i).get(0));
            System.out.println((String) data.get(i).get(1));
            System.out.println((String) data.get(i).get(4));
        }
        int[] vote_data = {45, 69};
        String county = "Some county";

        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("/Sending Candidate Titles", titles);
        sendIntent.putExtra("/Sending Candidate Names", names);
        sendIntent.putExtra("/Sending Candidate Parties", parties);
        sendIntent.putExtra("/Sending 2012 Vote Data", vote_data);
        sendIntent.putExtra("/Sending County", county);
        startService(sendIntent);
    }

    private static class DownloadTask extends AsyncTask<String, Void, String> {

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

    private static class DownloadTweetTask extends AsyncTask<String, Void, Tweet> {
        TweetView tweetView;
        Tweet tweet;

        public DownloadTweetTask(TweetView tweetView) {
            this.tweetView = tweetView;
        }

        protected Tweet doInBackground(String... twitter_ids) {
            String twitter_id = twitter_ids[0];
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            Twitter.getApiClient().getStatusesService().userTimeline(null, twitter_id, null, 1L, null, null, null, null, null, new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> tweets) {

                    tweet = tweets.data.get(0);
                    System.out.println(tweet.text);

                }
                @Override
                public void failure(TwitterException e) {

                }

            });
            System.out.println(tweet);
            while (tweet == null) {

            }
            Tweet received = tweet;
            tweet = null;
            return received;
        }

        protected void onPostExecute(Tweet result) {
            tweetView.setTweet(result);
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
        private String twitter_id;
        private Tweet tweet;
        private String profile_image_URL;
        private Bitmap profile_image;

        public PlaceholderFragment(int index) {

            this.representative_index = index;
            this.title = (String) data.get(index).get(0);
            this.full_name = (String) data.get(index).get(1);
            this.email = (String) data.get(index).get(2);
            this.website = (String) data.get(index).get(3);
            this.party = (String) data.get(index).get(4);
            this.term_end = (String) data.get(index).get(5);
            this.bioguide_id = (String) data.get(index).get(6);
            this.twitter_id = (String) data.get(index).get(7);
            this.profile_image_URL = (String) data.get(index).get(8);
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

            ImageView rep_image = (ImageView) rootView.findViewById(R.id.rep_image);
            new DownloadImageTask(rep_image).execute(this.profile_image_URL);

            TweetView tweet_view = (TweetView) rootView.findViewById(R.id.tweet);
            new DownloadTweetTask(tweet_view).execute(this.twitter_id);

            final String bioguide_id = this.bioguide_id;
            final String title = this.title;
            final String full_name = this.full_name;
            final String party = this.party;
            final String term_end = this.term_end;
            final String profile_url = this.profile_image_URL;
            rep_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), detailed_mobile.class);
                    intent.putExtra("/Bioguide", bioguide_id);
                    intent.putExtra("/Title", title);
                    intent.putExtra("/Full_Name", full_name);
                    intent.putExtra("/Party", party);
                    intent.putExtra("/Term_End", term_end);
                    intent.putExtra("/Profile URL", profile_url);
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
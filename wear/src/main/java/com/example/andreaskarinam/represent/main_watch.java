package com.example.andreaskarinam.represent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class main_watch extends Activity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    public static int num_reps;
    public static String[] titles;
    public static String[] names;
    public static String[] parties;
    public static int romney_percent;
    public static int obama_percent;
    public static String county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_watch);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                final String shakeMessage = "/Received shake";
                if (!ShakeEventListener.shake_made_message) {
                    System.out.println("Shake!!");
                    Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra(shakeMessage, "");
                    startService(sendIntent);
                }
                ShakeEventListener.shake_made_message = true;
//
//                Intent watchIntent = new Intent(getBaseContext(), main_watch.class);
//                watchIntent.putExtra(FakeData.COUNTY_INDEX_KEY, 2);
//                startActivity(watchIntent);
            }
        });

        Intent intent = getIntent();
        String message = "/All Watch Data";
        if (intent != null && intent.hasExtra(message)) {
            String data_string = intent.getStringExtra(message);
            String[] data_tokens = data_string.split("\n");
            int length = Integer.parseInt(data_tokens[0]);
            num_reps = length;
            titles = new String[length];
            names = new String[length];
            parties = new String[length];
            for (int i = 0; i < length; i++) {
                titles[i] = data_tokens[3*i + 1];
                names[i] = data_tokens[3*i + 2];
                parties[i] = data_tokens[3*i + 3];
            }
            romney_percent = Integer.parseInt(data_tokens[3 * length + 1]);
            obama_percent = Integer.parseInt(data_tokens[3 * length + 2]);
            county = data_tokens[3 * length + 3];

            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));
        } else {
            // load another screen
            Intent watchIntent = new Intent(getBaseContext(), WelcomeActivity_wear.class);
            startActivity(watchIntent);
        }
//        county = data.counties.get(county_index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

        private final Context mContext;
        private List mRows;
        private Fragment vote_fragment = new VoteFragment();

        public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            mContext = ctx;
        }


        // Maybe create set of views/pages here:
        // Create a static set of pages in a 2D array

        // Obtain the UI fragment at the specified position
        @Override
        public Fragment getFragment(int row, int col) {
            Fragment fragment = vote_fragment;
            if (row < num_reps) {
                fragment = new RepresentativeFragment(row);
            }
            return fragment;
        }

        @Override
        public int getRowCount() {
            return num_reps + 1;
        }

        @Override
        public int getColumnCount(int rowNum) {
            return 1;
        }
    }

    public static class RepresentativeFragment extends Fragment {

        public static String DEMOCRAT_COLOR = "#2F80ED";
        public static String REPUBLICAN_COLOR = "#E44A4A";
        public static String INDEPENDENT_COLOR = "#FFFFFF";

        private int representative_number;
        private String title;
        private String name;
        private String party;

        public RepresentativeFragment(int rep_number) {
            this.representative_number = rep_number;
            if (titles[rep_number].equals("Rep")) {
                this.title = "Representative";
            } else {
                this.title = "Senator";
            }
            this.name = names[rep_number];
            this.party = parties[rep_number];
        }

        // Returns a new instance of this fragment for the given section number
        public static RepresentativeFragment newInstance(int sectionNumber) {
            RepresentativeFragment fragment = new RepresentativeFragment(sectionNumber);
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        public int getColor() {
            if (this.party.equals("D")) {
                return Color.parseColor(DEMOCRAT_COLOR);
            } else if (this.party.equals("R")) {
                return Color.parseColor(REPUBLICAN_COLOR);
            }
            return Color.parseColor(INDEPENDENT_COLOR);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.watch_representative_layout, container, false);

            LinearLayout rl = (LinearLayout) rootView.findViewById(R.id.lin_layout);
            rl.setBackgroundColor(this.getColor());

            TextView rep_name_text = (TextView) rootView.findViewById(R.id.name_text);
            rep_name_text.setText(this.title + "\n" +  this.name
                    + " (" + this.party + ")");

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("Click");
                    Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                    sendIntent.putExtra("/Representative Index", representative_number);
                    getActivity().startService(sendIntent);
                }
            });

            return rootView;
        }
    }

    public static class VoteFragment extends Fragment {

        public VoteFragment() {
        }

        // Returns a new instance of this fragment for the given section number
        public static VoteFragment newInstance(int sectionNumber) {
            VoteFragment fragment = new VoteFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.vote_layout, container, false);

            TextView county_text = (TextView) rootView.findViewById(R.id.county_text);
            county_text.setText(county);

            TextView romney_percent_text = (TextView) rootView.findViewById(R.id.romney_percent_text);
            romney_percent_text.setText(romney_percent + "%");

            TextView obama_percent_text = (TextView) rootView.findViewById(R.id.obama_percent_text);
            obama_percent_text.setText(obama_percent + "%");

            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            progressBar.setProgress(romney_percent);

            return rootView;
        }
    }
}

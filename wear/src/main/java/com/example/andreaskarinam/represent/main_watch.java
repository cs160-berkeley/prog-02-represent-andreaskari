package com.example.andreaskarinam.represent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
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

import com.example.andreaskarinam.mylibrary.County;
import com.example.andreaskarinam.mylibrary.FakeData;
import com.example.andreaskarinam.mylibrary.Representative;

import java.util.ArrayList;
import java.util.List;

public class main_watch extends Activity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    public static int county_index;
    public static County county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_watch);

        FakeData data = new FakeData();

        Intent intent = getIntent();
        if (intent != null) {
            county_index = intent.getIntExtra(FakeData.COUNTY_INDEX_KEY, 0);
        }
        county = data.counties.get(county_index);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra(FakeData.COUNTY_INDEX_KEY, 2);
                startService(sendIntent);

                Intent watchIntent = new Intent(getBaseContext(), main_watch.class);
                watchIntent.putExtra(FakeData.COUNTY_INDEX_KEY, 2);
                startActivity(watchIntent);
            }
        });
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
            if (row < county.representatives.size()) {
                fragment = new RepresentativeFragment(row);
            }
            return fragment;
        }

        @Override
        public int getRowCount() {
            return county.representatives.size() + 1;
        }

        @Override
        public int getColumnCount(int rowNum) {
            return 1;
        }
    }

    public static class RepresentativeFragment extends Fragment {

        private int representative_number;
        private Representative representative;

        public RepresentativeFragment(int rep_number) {
            this.representative_number = rep_number;
            this.representative = county.representatives.get(rep_number);
        }

        // Returns a new instance of this fragment for the given section number
        public static RepresentativeFragment newInstance(int sectionNumber) {
            RepresentativeFragment fragment = new RepresentativeFragment(sectionNumber);
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.watch_representative_layout, container, false);

            LinearLayout rl = (LinearLayout) rootView.findViewById(R.id.lin_layout);
            rl.setBackgroundColor(this.representative.getColor());

            TextView rep_name_text = (TextView) rootView.findViewById(R.id.name_text);
            rep_name_text.setText(this.representative.title + "\n" +  this.representative.rep_name
                    + " (" + this.representative.party + ")");

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
                    sendIntent.putExtra(FakeData.COUNTY_INDEX_KEY, county_index);
                    sendIntent.putExtra(FakeData.REPRESENTATIVE_INDEX_KEY, representative_number);
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
            county_text.setText(county.county_name);

            TextView romney_percent_text = (TextView) rootView.findViewById(R.id.romney_percent_text);
            romney_percent_text.setText(county.percent_romney + "%");

            TextView obama_percent_text = (TextView) rootView.findViewById(R.id.obama_percent_text);
            obama_percent_text.setText(county.percent_obama + "%");

            ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            progressBar.setProgress(county.percent_romney);

            return rootView;
        }
    }
}

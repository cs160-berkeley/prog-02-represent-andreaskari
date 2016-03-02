package com.example.andreaskarinam.represent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.example.andreaskarinam.mylibrary.County;
import com.example.andreaskarinam.mylibrary.FakeData;
import com.example.andreaskarinam.mylibrary.Representative;

import java.util.ArrayList;
import java.util.List;

public class main_watch extends Activity {

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
                Representative rep = county.representatives.get(row);
                fragment = new RepresentativeFragment(rep);
            }

            // Advanced settings (card gravity, card expansion/scrolling)
//            fragment.setCardGravity(page.cardGravity);
//            fragment.setExpansionEnabled(page.expansionEnabled);
//            fragment.setExpansionDirection(page.expansionDirection);
//            fragment.setExpansionFactor(page.expansionFactor);
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

        private Representative rep;

        public RepresentativeFragment(Representative r) {
            this.rep = r;
        }

        // Returns a new instance of this fragment for the given section number
        public static RepresentativeFragment newInstance(int sectionNumber) {
            RepresentativeFragment fragment = new RepresentativeFragment(county.representatives.get(sectionNumber));
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.watch_representative_layout, container, false);

            LinearLayout rl = (LinearLayout) rootView.findViewById(R.id.lin_layout);
            rl.setBackgroundColor(this.rep.getColor());

            TextView rep_name_text = (TextView) rootView.findViewById(R.id.name_text);
            rep_name_text.setText(this.rep.rep_name + " (" + this.rep.party + ")");

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

            return rootView;
        }
    }
}

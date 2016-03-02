package com.example.andreaskarinam.represent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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

import com.example.andreaskarinam.mylibrary.Representative;

import java.util.ArrayList;
import java.util.List;

public class main_watch extends Activity {

//    private TextView mTextView;

    public static ArrayList<Representative> representatives = new ArrayList<Representative>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Representative boxer = new Representative(
                "Barbara Boxer",
                "D",
                "senator@boxer.senate.gov",
                "www.boxer.senate.gov/",
                "@SenatorMajLdr: McConnell says he wants the Senate working again. Now he's choosing politics over over of our most important obligations.",
                "Boxer.png"
        );

        Representative feinstein = new Representative(
                "Diane Feinstein",
                "D",
                "senator@feinstein.senate.gov",
                "www.feinstein.senate.gov/",
                "Love seeing the new signage at the new Castle Mountains National Monument! #ProtectCADesert",
                "Feinstein.png"
        );

        Representative mcclintock = new Representative(
                "Tom McClintock",
                "R",
                "representative@mcclintock.house.gov",
                "www.mcclintock.house.gov/",
                "S.J. Res. 22 - Disapproving the #EPA #WOTUS (Waters of the U.S.) Rule http://1.usa.gov/200QA5x",
                "McClintock.png"
        );

        representatives.add(boxer);
        representatives.add(feinstein);
        representatives.add(mcclintock);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_watch);

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
            if (row < representatives.size()) {
                Representative rep = representatives.get(row);
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
            return 4;
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
            RepresentativeFragment fragment = new RepresentativeFragment(representatives.get(sectionNumber));
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
            county_text.setText("Los Angeles County, CA");

            return rootView;
        }
    }
}

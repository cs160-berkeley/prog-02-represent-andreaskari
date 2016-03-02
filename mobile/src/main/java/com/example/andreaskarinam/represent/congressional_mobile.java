package com.example.andreaskarinam.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

//    public static ArrayList<Representative> representatives = new ArrayList<Representative>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_mobile);

//        Representative boxer = new Representative(
//                "Barbara Boxer",
//                "D",
//                "senator@boxer.senate.gov",
//                "www.boxer.senate.gov/",
//                "@SenatorMajLdr: McConnell says he wants the Senate working again. Now he's choosing politics over over of our most important obligations.",
//                "Boxer.png"
//        );
//
//        Representative feinstein = new Representative(
//                "Diane Feinstein",
//                "D",
//                "senator@feinstein.senate.gov",
//                "www.feinstein.senate.gov/",
//                "Love seeing the new signage at the new Castle Mountains National Monument! #ProtectCADesert",
//                "Feinstein.png"
//        );
//
//        Representative mcclintock = new Representative(
//                "Tom McClintock",
//                "R",
//                "representative@mcclintock.house.gov",
//                "www.mcclintock.house.gov/",
//                "S.J. Res. 22 - Disapproving the #EPA #WOTUS (Waters of the U.S.) Rule http://1.usa.gov/200QA5x",
//                "McClintock.png"
//        );
//
//        representatives.add(boxer);
//        representatives.add(feinstein);
//        representatives.add(mcclintock);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private int county_index;
        private int representative_index;
        private County county;
        private Representative representative;

        public PlaceholderFragment(FakeData data, int county_index, int rep_index) {
            this.county_index = county_index;
            this.representative_index = rep_index;
            this.county = data.counties.get(county_index);
            this.representative = county.representatives.get(rep_index);
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            FakeData data = new FakeData();
            PlaceholderFragment fragment = new PlaceholderFragment(data, 1, sectionNumber);
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
            rl.setBackgroundColor(this.representative.getColor());

            TextView rep_name_text = (TextView) rootView.findViewById(R.id.name_text);
            rep_name_text.setText(this.representative.rep_name + " (" + this.representative.party + ")");

            TextView rep_email_text = (TextView) rootView.findViewById(R.id.email_text);
            rep_email_text.setText(this.representative.email);

            TextView rep_website_text = (TextView) rootView.findViewById(R.id.website_text);
            rep_website_text.setText(this.representative.website);

            TextView rep_tweet_text = (TextView) rootView.findViewById(R.id.tweet_content_text);
            rep_tweet_text.setText(this.representative.last_tweet);

            ImageView rep_image = (ImageView) rootView.findViewById(R.id.rep_image);
            if (this.representative.rep_name.equals("Barbara Boxer")) {
                rep_image.setImageResource(R.drawable.boxer);
            } else if (this.representative.rep_name.equals("Diane Feinstein")) {
                rep_image.setImageResource(R.drawable.feinstein);
            } else {
                rep_image.setImageResource(R.drawable.mcclintock);
            }

            rep_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), detailed_mobile.class);

                    intent.putExtra(FakeData.COUNTY_INDEX_KEY, county_index);
                    intent.putExtra(FakeData.REPRESENTATIVE_INDEX_KEY, representative_index);

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

package com.example.andreaskarinam.mylibrary;

import java.util.ArrayList;

/**
 * Created by andreaskarinam on 3/1/16.
 */
public class FakeData {
    public final static String COUNTY_INDEX_KEY = "county_index";
    public final static String REPRESENTATIVE_INDEX_KEY = "representative_key";

    public ArrayList<County> counties;

    public FakeData() {
        ArrayList<Representative> berkeley_reps = new ArrayList<Representative>(3);
        ArrayList<Representative> los_angeles_reps = new ArrayList<Representative>(3);

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

        Representative lee = new Representative(
                "Barbara Lee",
                "D",
                "representative@lee.house.gov",
                "www.lee.house.gov/",
                "Don’t forget - this Sunday, I’m hosting a screening & discussion of @findingsamlowe w/ @pamadison. See you there!",
                "Lee.png"
        );

        Representative mcclintock = new Representative(
                "Tom McClintock",
                "R",
                "representative@mcclintock.house.gov",
                "www.mcclintock.house.gov/",
                "S.J. Res. 22 - Disapproving the #EPA #WOTUS (Waters of the U.S.) Rule http://1.usa.gov/200QA5x",
                "McClintock.png"
        );

        berkeley_reps.add(boxer);
        berkeley_reps.add(feinstein);
        berkeley_reps.add(lee);

        los_angeles_reps.add(boxer);
        los_angeles_reps.add(feinstein);
        los_angeles_reps.add(mcclintock);

        County berkeley = new County("Alameda County, CA", berkeley_reps, 65);
        County los_angeles = new County("Los Angeles County, CA", los_angeles_reps, 55);

        counties = new ArrayList<County>(2);
        counties.add(berkeley);
        counties.add(los_angeles);
    }
}

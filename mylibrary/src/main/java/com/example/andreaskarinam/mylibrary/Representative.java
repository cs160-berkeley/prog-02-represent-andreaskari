package com.example.andreaskarinam.mylibrary;

import android.graphics.Color;

/**
 * Created by andreaskarinam on 2/29/16.
 */
public class Representative {

    public static String DEMOCRAT_COLOR = "#2F80ED";
    public static String REPUBLICAN_COLOR = "#E44A4A";
    public static String INDEPENDENT_COLOR = "#FFFFFF";

    public String rep_name;
    public String title;
    public String party;
    public String email;
    public String website;
    public String last_tweet;
    public String image_name;
    public String term_string;
    public String committees;
    public String bills;

    public Representative(String name, String title, String party, String email, String website, String tweet, String image, String term, String committees, String bills) {
        this.rep_name = name;
        this.title = title;
        this.party = party;
        this.email = email;
        this.website = website;
        this.last_tweet = tweet;
        this.image_name = image;
        this.term_string = term;
        this.committees = committees;
        this.bills = bills;
    }

    public int getColor() {
        if (this.party.equals("D")) {
            return Color.parseColor(DEMOCRAT_COLOR);
        } else if (this.party.equals("R")) {
            return Color.parseColor(REPUBLICAN_COLOR);
        }
        return Color.parseColor(INDEPENDENT_COLOR);
    }
}

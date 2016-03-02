package com.example.andreaskarinam.mylibrary;

import java.util.ArrayList;

/**
 * Created by andreaskarinam on 3/1/16.
 */
public class County {

    public String county_name;
    public int percent_obama;
    public int percent_romney;
    public ArrayList<Representative> representatives;

    public County(String name, ArrayList<Representative> reps, int percent_obama) {
        this.county_name = name;
        this.representatives = reps;
        this.percent_obama = percent_obama;
        this.percent_romney = 100 - percent_obama;
    }
}

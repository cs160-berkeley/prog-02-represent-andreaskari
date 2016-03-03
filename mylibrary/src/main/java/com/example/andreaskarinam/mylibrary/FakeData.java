package com.example.andreaskarinam.mylibrary;

import java.util.ArrayList;

/**
 * Created by andreaskarinam on 3/1/16.
 */
public class FakeData {
    public final static String COUNTY_INDEX_KEY = "county_index";
    public final static String REPRESENTATIVE_INDEX_KEY = "representative_key";
    public final static String MESSAGE = "message";

    public ArrayList<County> counties;

    public FakeData() {
        String committees = "Subcommittee on Clean Air and Nuclear Safety\n" +
                "\n" +
                "Subcommittee on Superfund, Waste Management, and Regulatory Oversight\n" +
                "\n" +
                "Subcommittee on Transportation and Infrastructure, Ranking Member\n" +
                "\n" +
                "Subcommittee on Fisheries, Water, and Wildlife";

        String bills = "S 1356 - the National Defense Authorization Act for Fiscal Year 2016 (November 10, 2015)\n" +
                "\n" +
                "HR 1314 - Bipartisan Budget Act of 2015\n" +
                "(October 30, 2015)\n" +
                "\n" +
                "HR 2048 - the Uniting and Strengthening America by Fulfilling Rights and Ensuring Effective Discipline Over Monitoring Act of 2015 or the USA FREEDOM Act of 2015\n" +
                "(June 2, 2015)";

        Representative boxer = new Representative(
                "Barbara Boxer",
                "D",
                "senator@boxer.senate.gov",
                "www.boxer.senate.gov/",
                "@SenatorMajLdr: McConnell says he wants the Senate working again. Now he's choosing politics over over of our most important obligations.",
                "Boxer.png",
                "Ends term January 3, 2017",
                committees,
                bills
        );

        Representative feinstein = new Representative(
                "Diane Feinstein",
                "D",
                "senator@feinstein.senate.gov",
                "www.feinstein.senate.gov/",
                "Love seeing the new signage at the new Castle Mountains National Monument! #ProtectCADesert",
                "Feinstein.png",
                "Ends term January 3, 2019",
                committees,
                bills
        );

        Representative lee = new Representative(
                "Barbara Lee",
                "D",
                "representative@lee.house.gov",
                "www.lee.house.gov/",
                "Don’t forget - this Sunday, I’m hosting a screening & discussion of @findingsamlowe w/ @pamadison. See you there!",
                "Lee.png",
                "Ends term January 3, 2017",
                committees,
                bills
        );

        Representative mcclintock = new Representative(
                "Tom McClintock",
                "R",
                "representative@mcclintock.house.gov",
                "www.mcclintock.house.gov/",
                "S.J. Res. 22 - Disapproving the #EPA #WOTUS (Waters of the U.S.) Rule http://1.usa.gov/200QA5x",
                "McClintock.png",
                "Ends term January 3, 2017",
                committees,
                bills
        );

        Representative udall = new Representative(
                "Tom Udall",
                "D",
                "representative@udall.house.gov",
                "www.udall.house.gov/",
                "The FEC has outlived its usefulness—we need a new watchdog empowered to crack down on #campaignfinance violations: 1.usa.gov/1REyPEY",
                "Udall.png",
                "Ends term January 3, 2021",
                committees,
                bills
        );

        Representative heinrich = new Representative(
                "Martin Heinrich",
                "D",
                "representative@heinrich.house.gov",
                "www.heinrich.house.gov/",
                "Women have made enormous contributions to our country & we must continue our fight for gender equality. #WomensHistoryMonth #EqualPay",
                "Heinrich.png",
                "Ends term January 3, 2019",
                committees,
                bills
        );

        Representative pearce = new Representative(
                "Stevan Pearce",
                "R",
                "representative@pierce.house.gov",
                "www.pierce.house.gov/",
                "Thanks @RepLarryBucshon for introducing a bipartisan bill to strengthen Medicaid!",
                "Pierce.png",
                "Ends term January 3, 2017",
                committees,
                bills
        );

        ArrayList<Representative> berkeley_reps = new ArrayList<Representative>(3);
        ArrayList<Representative> los_angeles_reps = new ArrayList<Representative>(3);
        ArrayList<Representative> socorro_reps = new ArrayList<Representative>(3);

        berkeley_reps.add(boxer);
        berkeley_reps.add(feinstein);
        berkeley_reps.add(lee);

        los_angeles_reps.add(boxer);
        los_angeles_reps.add(feinstein);
        los_angeles_reps.add(mcclintock);

        socorro_reps.add(udall);
        socorro_reps.add(heinrich);
        socorro_reps.add(pearce);

        County berkeley = new County("Alameda County, CA", berkeley_reps, 60);
        County los_angeles = new County("Los Angeles County, CA", los_angeles_reps, 55);
        County socorro = new County("Socorro County, NM", socorro_reps, 40);

        counties = new ArrayList<County>(3);
        counties.add(berkeley);
        counties.add(los_angeles);
        counties.add(socorro);
    }
}

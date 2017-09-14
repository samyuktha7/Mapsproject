package com.example.samyuktha.mapsprac;

import android.graphics.Bitmap;

/**
 * Created by samyuktha on 9/11/2017.
 */

public class Datafromhere {

    private Double latsdata;
    private Double lngsdata;
    private String icon1;
    private String name1;
    private boolean opennow1;
    private double rating1;
    private String vicinity1;

    Datafromhere(Double a, Double b,String icon,String name,boolean opennow,double rating,String vicinity)
    {
        this.latsdata=a;
        this.lngsdata=b;
        this.icon1=icon;
        this.name1=name;
        this.opennow1=opennow;
        this.rating1=rating;
        this.vicinity1=vicinity;
    }

    public Double getLatsdata() {
        return latsdata;
    }

    public Double getLngsdata() {
        return lngsdata;
    }

    public String getIcon1() {
        return icon1;
    }

    public String getName1() {
        return name1;
    }

    public boolean isOpennow1() {
        return opennow1;
    }

    public double getRating1() {
        return rating1;
    }

    public String getVicinity1() {
        return vicinity1;
    }
}

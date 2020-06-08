package com.example.mymovie.Data;

public class MapInfo implements Comparable<MapInfo> {

    private String theaterNm;
    private String theaterAddress;
    //private String theaterDistance;
    //private String theaterPh;

    private double thLatitude;
    private double thLongitude;

    private int theaterDistance;

    public int getTheaterDistance() {
        return theaterDistance;
    }

    public void setTheaterDistance(int theaterDistance) {
        this.theaterDistance = theaterDistance;
    }

    public MapInfo(String theaterNm, String theaterAddress, double thLatitude, double thLongitude , int theaterDistance) {
        this.theaterNm = theaterNm;
        this.theaterAddress = theaterAddress;
        this.thLatitude = thLatitude;
        this.thLongitude = thLongitude;
        this.theaterDistance = theaterDistance;
    }

    public String getTheaterNm() {
        return theaterNm;
    }

    public void setTheaterNm(String theaterNm) {
        this.theaterNm = theaterNm;
    }

    public String getTheaterAddress() {
        return theaterAddress;
    }

    public void setTheaterAddress(String theaterAddress) {
        this.theaterAddress = theaterAddress;
    }

    public double getThLatitude() {
        return thLatitude;
    }

    public void setThLatitude(double thLatitude) {
        this.thLatitude = thLatitude;
    }

    public double getThLongitude() {
        return thLongitude;
    }

    public void setThLongitude(double thLongitude) {
        this.thLongitude = thLongitude;
    }

    @Override
    public int compareTo(MapInfo o) {
        if (this.theaterDistance < o.getTheaterDistance()) {
            return -1;
        } else if (this.theaterDistance > o.getTheaterDistance()) {
            return 1;
        }
        return 0;
    }
}

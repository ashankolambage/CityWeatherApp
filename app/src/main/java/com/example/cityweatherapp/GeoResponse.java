package com.example.cityweatherapp;

public class GeoResponse {
    private String name;
    private double lat;
    private double lon;
    private String country;
    private String state;

    @Override
    public String toString() {
        return "GeoResponse{" +
                "name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }
}

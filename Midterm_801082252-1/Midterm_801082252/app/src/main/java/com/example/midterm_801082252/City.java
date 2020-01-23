package com.example.midterm_801082252;

import java.io.Serializable;

public class City implements Serializable {

    String city;
    String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        country = country;
    }

    @Override
    public String toString() {
        return city + "," + country;
    }
}

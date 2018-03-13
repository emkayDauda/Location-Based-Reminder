package com.apps.emkaydee.dawud07.testgeofence;

public class Locations{
    private double latitude, longitude;
    private String name;

    public Locations(String home, double v, double v1) {
        latitude = v;
        longitude = v1;
        name = home;
    }


    public  void setName(String newName){
                this.name = newName;
    }
    public String getName(){
        return name;
    }
    public double getLongitude(){

        return longitude;
    }

    public double getLatitude(){

        return latitude;
    }
    
    public void setLatitude(double newVal){
        this.latitude = newVal;
    }

    public void setLongitude(double newVal){
        this.longitude = newVal;
    }
        
}

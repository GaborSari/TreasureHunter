package hu.szte.kotprog;

import java.util.Objects;

public class MPoint {

    private double latitude = 0;  //x
    private double longitude = 0; //y

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPoint mPoint = (MPoint) o;
        return Double.compare(mPoint.latitude, latitude) == 0 &&
                Double.compare(mPoint.longitude, longitude) == 0;
    }


    @Override
    public String toString(){
        return "latitude: " +  this.latitude + ", longitude:" + this.longitude + "\n";
    }


    public boolean unset(){
        return latitude == 0 &&
                longitude == 0;
    }

}


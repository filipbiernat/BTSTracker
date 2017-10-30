package pl.edu.agh.student.fbierna.btstracker.data;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Filip on 10.09.2017.
 */

public class Bts {
    private final int networkGeneration;
    private final String region;
    private final String town;
    private final String location;
    private final String operatorName;
    private final String networkType;
    private final LatLng latLng;
    private long timeAttachedLong;
    private String timeAttached;
    private int rotation = 0;

    private static final String CSV_SPLIT_BY = ";";

    public Bts(String csvData, String operatorName, int networkGeneration){
        this.operatorName = operatorName;
        this.networkType = parseNetworkType(networkGeneration);
        this.networkGeneration = networkGeneration;
        timeAttachedLong = 0;
        timeAttached = "Obecnie";

        String[] data = csvData.split(CSV_SPLIT_BY);

        region = data[5];
        town = data[6];
        location = data[7];
        double lat = Double.parseDouble(data[8]);
        double lng = Double.parseDouble(data[9]);
        latLng = new LatLng(lat, lng);
    }//todo remove region + connect operator + network

    public Bts(Bts other) {
        if (null != other) {
            networkGeneration = other.networkGeneration;
            region = other.region;
            town = other.town;
            location = other.location;
            operatorName = other.operatorName;
            networkType = other.networkType;
            latLng = other.latLng;
            timeAttachedLong = other.timeAttachedLong;
            timeAttached = other.timeAttached;
            rotation = other.rotation;
        } else {
            networkGeneration = 2;
            region = " - ";
            town = " - ";
            location = " - ";
            operatorName = " - ";
            networkType = " - ";
            latLng = null;
            timeAttachedLong = 0;
            timeAttached = " - ";
            rotation = 0;
        }
    }

    private String parseNetworkType(int networkType){
        switch(networkType){
            case 2: return "GSM";
            case 3: return "WCDMA";
            case 4: return "LTE";
            default: return "Unknown";//exception
        }
    }

    public void detach(Date timeOfAttach, Date currentTime){
        timeAttachedLong += currentTime.getTime() - timeOfAttach.getTime();
        Log.d("LOGFILIP", "time2 || " + timeOfAttach + " | " + currentTime);
        long d = TimeUnit.MILLISECONDS.toDays(timeAttachedLong);
        long h = TimeUnit.MILLISECONDS.toHours(timeAttachedLong) % 24;
        long m = TimeUnit.MILLISECONDS.toMinutes(timeAttachedLong) % 60;
        long s = TimeUnit.MILLISECONDS.toSeconds(timeAttachedLong) % 60;

        Log.d("LOGFILIP", "timeAttachedLong || " + timeAttachedLong + " d " + d + " m " + m + " s " + s);

        String dd = convertTimeDelta(d, "d");
        String ddhh = dd + convertTimeDelta(h, "h");
        String ddhhmm = ddhh + convertTimeDelta(m, "min");
        timeAttached = ddhhmm + convertTimeDelta(s, "s");
        Log.d("LOGFILIP", "timeAttached || " + timeAttached);
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }
    public int getRotation(){
        return rotation;
    }

    private String convertTimeDelta(long timeDelta, String unit){
        String res = "";
        if (timeDelta > 0){
            res = Long.toString(timeDelta) + unit + (unit.equals("s")  ? "" : " ");
        }
        return res;
    }


    private int getMarkerHue(){
        if (networkGeneration == 2){
            return 340; //make final static
        }
        if (networkGeneration == 3){
            return 231;
        }
        if (networkGeneration == 4){
            return 4;
        }
        return 0;
        //exception
    }
    public MarkerOptions getMarkerOptions(){
        //TODO separete getter
        String operatorAndNetwork =  getOperatorName() + " \u2022 " + getNetworkType();

        Log.d("LOGFILIP marker", town + " " + location + " " + latLng.toString());
        return new MarkerOptions().position(latLng)
                .title(town)
                .snippet(timeAttached+";;"+location+";;"+operatorAndNetwork)
                .icon(BitmapDescriptorFactory.defaultMarker(getMarkerHue()))
                .rotation(rotation);
    }



    private String handleNullString(String string){
        return null == string ? "–" : string;
    }


    public String getTown(){
        return handleNullString(town);
    }
    public LatLng getLatLng(){
        return latLng;
    }
    public String getOperatorName(){
        return handleNullString(operatorName);
    }
    public String getRegion(){
        return handleNullString(region);
    }
    public String getNetworkType(){
        return handleNullString(networkType);
    }
    public int getNetworkGeneration(){
        return networkGeneration;
    }
    public String getTimeAttached(){
        return handleNullString(timeAttached);
    }

    public boolean sameLngLat(LatLng other){
        double distanceInMeters = getLocation(latLng).distanceTo(getLocation(other));
        return distanceInMeters < 1; // static final
    }

    public String getLocation(){
        return handleNullString(location);
    }

    private Location getLocation(LatLng latLng){
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Bts other = (Bts) obj;
        return sameLngLat(other.latLng) && networkGeneration == other.networkGeneration;
    }
}

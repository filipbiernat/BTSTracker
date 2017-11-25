package pl.edu.agh.student.fbierna.btstracker.data;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.R.attr.description;
import static android.R.attr.name;

public class Bts {
    private final int networkGeneration;
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
        timeAttached = "Now";

        String[] data = csvData.split(CSV_SPLIT_BY);

        town = data[5];
        location = data[6];
        double lat = Double.parseDouble(data[7]);
        double lng = Double.parseDouble(data[8]);
        latLng = new LatLng(lat, lng);
    }

    public Bts(String outFileCsvString){
        String[] data = outFileCsvString.split(CSV_SPLIT_BY);

        networkGeneration = Integer.valueOf(data[0]);
        town = data[1];
        location = data[2];
        operatorName = data[3];
        networkType = data[4];
        double lat = Double.parseDouble(data[5]);
        double lng = Double.parseDouble(data[6]);
        latLng = new LatLng(lat, lng);
        timeAttachedLong =  Long.valueOf(data[7]);
        timeAttached = data[8];
        rotation = Integer.valueOf(data[9]);
    }

    public Bts(Bts other) {
        if (null != other) {
            networkGeneration = other.networkGeneration;
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

        final long d = TimeUnit.MILLISECONDS.toDays(timeAttachedLong);
        final long h = TimeUnit.MILLISECONDS.toHours(timeAttachedLong) % 24;
        final long m = TimeUnit.MILLISECONDS.toMinutes(timeAttachedLong) % 60;
        final long s = TimeUnit.MILLISECONDS.toSeconds(timeAttachedLong) % 60;

        String dd = convertTimeDelta(d, "d");
        String ddhh = dd + convertTimeDelta(h, "h");
        String ddhhmm = ddhh + convertTimeDelta(m, "min");
        timeAttached = ddhhmm + convertTimeDelta(s, "s");
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
        if (latLng == null){
            return null;
        }
        return new MarkerOptions().position(latLng)
                .title(town)
                .snippet(timeAttached+";;"+location+";;"+getOperatorAndNetwork())
                .icon(BitmapDescriptorFactory.defaultMarker(getMarkerHue()))
                .rotation(rotation);
    }

    public String getCsvString(){
        String latLngString = String.valueOf(latLng.latitude) + CSV_SPLIT_BY + String.valueOf(latLng.longitude);
        return String.valueOf(networkGeneration) +
                CSV_SPLIT_BY + town +
                CSV_SPLIT_BY + location +
                CSV_SPLIT_BY + operatorName +
                CSV_SPLIT_BY + networkType +
                CSV_SPLIT_BY + latLngString +
                CSV_SPLIT_BY + String.valueOf(timeAttachedLong) +
                CSV_SPLIT_BY + timeAttached +
                CSV_SPLIT_BY + String.valueOf(rotation) + "\n";
    }

    public String getKmlString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("      <Placemark>\n");
        stringBuilder.append("        <name>" + String.valueOf(networkGeneration) + "G: " + town + "</name>\n");
        stringBuilder.append("        <description><![CDATA[" + location + "<br>");
        stringBuilder.append(getOperatorAndNetwork() + "<br>");
        stringBuilder.append("Attached: " + timeAttached + "]]></description>\n");
        stringBuilder.append("        <styleUrl>#bts" + String.valueOf(networkGeneration) + "G</styleUrl>\n");
        stringBuilder.append("        <Point>\n");
        stringBuilder.append("          <coordinates>" + String.valueOf(latLng.longitude) + "," +
                        String.valueOf(latLng.latitude) + ",0</coordinates>\n");
        stringBuilder.append("        </Point>\n");
        stringBuilder.append("      </Placemark>\n");
        return stringBuilder.toString();
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
    public String getOperatorAndNetwork(){
        return handleNullString(operatorName) + " \u2022 " + handleNullString(networkType);
    }
    public int getNetworkGeneration(){
        return networkGeneration;
    }
    public String getTimeAttached(){
        return handleNullString(timeAttached);
    }

    public boolean sameLngLat(LatLng other){
        if (latLng == null || other == null){
            return false;
        }
        double distanceInMeters = getLocation(latLng).distanceTo(getLocation(other));
        return distanceInMeters < 1;
    }

    public String getLocation(){
        return handleNullString(location);
    }

    private Location getLocation(LatLng latLng){
        Location location = new Location("");
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

package pl.edu.agh.student.fbierna.btstracker.data;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsId;
import pl.edu.agh.student.fbierna.btstracker.airscanner.Network;

import static android.R.attr.data;

/**
 * Created by Filip on 10.09.2017.
 */

public class Bts {
    //private int mcc;
    //private int lac;
    //private int id;
    private int networkGeneration;
    private String region;
    private String town;
    private String location;
    private String operatorName;
    private String networkType;
    private LatLng latLng;

    private static final String CSV_SPLIT_BY = ";";

    public Bts(String csvData, String operatorName, int networkType, int networkGeneration){
        this.operatorName = operatorName;
        this.networkType = parseNetworkType(networkType);
        this.networkGeneration = networkGeneration;

        String[] data = csvData.split(CSV_SPLIT_BY);

        region = data[5];
        town = data[6];
        location = data[7];
        double lat = Double.parseDouble(data[8]);
        double lng = Double.parseDouble(data[9]);
        latLng = new LatLng(lat, lng);

        Log.d("LOGFILIP testowy", this.operatorName + " " + this.networkType + " " + this.networkGeneration + " " +
                region+ " " + town + " " + location + " " + latLng.toString());
    }

    private String parseNetworkType(int networkType){
        return Integer.toString(networkType);//FIXME FB
    }
    public MarkerOptions getMarkerOptions(){
        Log.d("LOGFILIP marker", town + " " + location + " " + latLng.toString());
        return new MarkerOptions().position(latLng)
                .title(town + " " + location)
                .snippet("Lorem Ipsum")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }

    public String getNetworkOperator(){
        return operatorName;
    } //FIXME refactor

    public String getTownAndRegion(){
        return town + ", " + region;
    }

    public String getLocation(){
        return location;
    }

    public int getNetworkMode(){
        return networkGeneration;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bts other = (Bts) obj;
        if (latLng == other.latLng &&
                networkGeneration == other.networkGeneration &&
                operatorName == other.operatorName) {
            return true;
        } else {
            return false;
        }
    }
}

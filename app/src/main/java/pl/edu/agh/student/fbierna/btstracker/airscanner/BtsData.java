package pl.edu.agh.student.fbierna.btstracker.airscanner;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Filip on 27.08.2017.
 */

public class BtsData {
    private BtsId btsId;
    private String networkOperator;
    private String region;
    private String town;
    private String location;
    public /*private*/ LatLng latLng; //FIXME

    private static final String CSV_SPLIT_BY = ";";

    public BtsData(BtsId btsId, String csvData){
        this.btsId = btsId;

        String[] data = csvData.split(CSV_SPLIT_BY);
        networkOperator = data[2];
        region = data[3];
        town = data[4];
        location = data[5];
        double lng = Double.parseDouble(data[7]);
        double lat = Double.parseDouble(data[8]);
        latLng = new LatLng(lat, lng);
    }

    public MarkerOptions getMarkerOptions(){
        Log.d("LOGFILIP marker", town + " " + location + " " + latLng.toString());
        return new MarkerOptions().position(latLng)
                .title(town + " " + location)
                .snippet("Lorem Ipsum")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }

    public String getNetworkOperator(){
        return networkOperator;
    }

    public String getTownAndRegion(){
        return town + ", " + region;
    }

    public String getLocation(){
        return location;
    }

    public Network getNetworkMode(){
        return btsId.network;
    }
}

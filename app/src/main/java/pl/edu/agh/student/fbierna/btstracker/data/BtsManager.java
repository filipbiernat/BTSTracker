package pl.edu.agh.student.fbierna.btstracker.data;

import android.telephony.CellInfo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;


public class BtsManager {

    private class BtsList extends LinkedList<Bts>{
        private BtsList(){
            super();
        }

        Boolean addBts(Bts bts){
            if (btsList.contains(bts)){
                Bts existingBts = get(indexOf(bts));
                Bts existingBtsCopy = new Bts(existingBts);
                remove(existingBts);
                add(existingBtsCopy);
                return false;
            } else {
                btsList.add(bts);
                return true;
            }
        }
    }

    private final BtsList btsList;
    private final BtsSearcher btsSearcher;

    private Date timeOfAttach;

    public BtsManager(BtsTracker btsTracker){
        btsList = new BtsList();
        btsSearcher = new BtsSearcher(btsTracker);
    }

    public void reset(){
        btsList.clear();
    }

    public void switchToCell(CellInfo cellInfo, String operatorName, int networkType){
        Bts newBts = btsSearcher.search(cellInfo, operatorName);

        detachPresentBts();

        boolean isAlreadyAdded = btsList.addBts(newBts);

        if (isAlreadyAdded) {
            for (Bts bts : btsList) {
                if (btsList.getLast() != null &&
                        !bts.equals(btsList.getLast()) &&
                        newBts.sameLngLat(bts.getLatLng()) &&
                        bts.getRotation() == 0) {
                    bts.setRotation(30);
                    newBts.setRotation(-30);
                }
            }
        }
    }

    private void detachPresentBts(){
        Date currentTime = Calendar.getInstance().getTime();
        if (btsList.size() > 0)
        {
            Bts presentBts = btsList.getLast();
            if (null != presentBts){
                presentBts.detach(timeOfAttach, currentTime);
            }
        }
        timeOfAttach = currentTime;
    }

    public Bts get(int position) {
        int inversedPosition = size() - 1 - position;
        return btsList.get(inversedPosition);
    }

    public int size() {
        return btsList.size();
    }

    public ArrayList<MarkerOptions> getMarkerOptions(){
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        for (Bts bts : btsList) {
            MarkerOptions options = bts.getMarkerOptions();
            if (options.getPosition() != null) {
                markerOptions.add(options);
            }
        }
        return markerOptions;
    }

    public LatLng getTopBtsLatLng(){
        return get(0).getLatLng();
    }

    public LinkedList<Bts> getList() {
        return btsList;
    }
}
package pl.edu.agh.student.fbierna.btstracker.data;

import android.content.res.AssetManager;
import android.telephony.CellInfo;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;

import static android.R.id.list;

/**
 * Created by Filip on 10.09.2017.
 */


public class BtsManager {

    private class BtsList extends LinkedList<Bts>{
        private BtsList(){
            super();
        }

        protected void addBts(Bts bts){
            if (list.contains(bts)){
                Bts existingBts = get(indexOf(bts));
                Bts existingBtsCopy = new Bts(existingBts);
                remove(existingBts);
                add(existingBtsCopy);
            } else {
                list.add(bts);
            }
        }
    }

    private BtsTracker btsTracker;
    private BtsList list;
    private BtsSearcher btsSearcher;

    private Date timeOfAttach;

    public BtsManager(BtsTracker btsTracker){
        this.btsTracker = btsTracker;
        list = new BtsList();
        btsSearcher = new BtsSearcher(btsTracker);


    }

    public void switchToCell(CellInfo cellInfo, String operatorName, int networkType){
        Bts newBts = btsSearcher.search(cellInfo, operatorName, networkType);

        detachPresentBts();

        list.addBts(newBts);
    }

    private void detachPresentBts(){
        Date currentTime = Calendar.getInstance().getTime();
        if (list.size() > 0)
        {
            Bts presentBts = list.getLast();
            if (null != presentBts){
                presentBts.detach(timeOfAttach, currentTime);
            }
        }
        timeOfAttach = currentTime;
    }

    public Bts get(int position) {
        int inversedPosition = size() - 1 - position;
        return list.get(inversedPosition);
    }

    public int size() {
        return list.size();
    }

    public ArrayList<MarkerOptions> getMarkerOptions(){
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        for (Bts bts : list) {
            MarkerOptions options = bts.getMarkerOptions();
            if (options.getPosition() != null) {
                markerOptions.add(options);
            }
        }
        return markerOptions;
    }



}

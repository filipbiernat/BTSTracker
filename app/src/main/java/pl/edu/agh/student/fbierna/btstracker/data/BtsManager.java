package pl.edu.agh.student.fbierna.btstracker.data;

import android.content.res.AssetManager;
import android.telephony.CellInfo;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;

/**
 * Created by Filip on 10.09.2017.
 */


public class BtsManager {

    private BtsTracker btsTracker;
    private List<Bts> list;
    private BtsSearcher btsSearcher;

    public BtsManager(BtsTracker btsTracker){
        this.btsTracker = btsTracker;
        list = new LinkedList<Bts>();
        AssetManager assetManager = btsTracker.getApplicationContext().getAssets();
        btsSearcher = new BtsSearcher(assetManager);


    }

    public void switchToCell(CellInfo cellInfo, String operatorName, int networkType){
        Bts newBts = btsSearcher.search(cellInfo, operatorName, networkType);
        list.add(newBts);
    }

    public Bts get(int position) {
        int inversedPosition = size() - 1 - position;
        return list.get(inversedPosition);
    }

    public int size() {
        return list.size();
    }



}

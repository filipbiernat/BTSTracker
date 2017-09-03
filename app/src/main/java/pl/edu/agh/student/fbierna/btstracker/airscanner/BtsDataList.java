package pl.edu.agh.student.fbierna.btstracker.airscanner;

import java.util.ArrayList;

/**
 * Created by Filip on 15.08.2017.
 */

public class BtsDataList {
    private ArrayList<BtsData> list = new ArrayList<>();

    public boolean add(BtsData btsData){
        if (!list.contains(btsData)){
            return list.add(btsData);
        } else {
            return false;
        }

    }

    public final ArrayList<BtsData> get(){
        return list;
    }//FIXME

    public void clear(){
        list.clear();
    }
    public int size(){
        return list.size();
    } //remove
}

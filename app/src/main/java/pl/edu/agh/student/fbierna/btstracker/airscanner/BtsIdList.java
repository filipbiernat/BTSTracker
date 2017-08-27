package pl.edu.agh.student.fbierna.btstracker.airscanner;

import java.util.ArrayList;

/**
 * Created by Filip on 15.08.2017.
 */

public class BtsIdList {

    private ArrayList<BtsId> list = new ArrayList<>();

    public boolean add(BtsId btsId){
        if (!list.contains(btsId)){
            return list.add(btsId);
        } else {
            return false;
        }

    }

    public final ArrayList<BtsId> get(){
        return list;
    }

    public void clear(){
        list.clear();
    }
    public int size(){
        return list.size();
    } //remove
}

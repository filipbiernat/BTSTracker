package pl.edu.agh.student.fbierna.btstracker.airscanner;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

/**
 * Created by Filip on 14.08.2017.
 */

public class BtsChangeListener extends PhoneStateListener {

    private Context context;
    private BtsIdList btsIdList;

    public BtsChangeListener(Context context, BtsIdList btsIdList) {
        this.context = context;
        this.btsIdList = btsIdList;

    }

    public void onCellLocationChanged (CellLocation location) {
        Log.d("LOGFILIP", "scan");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<CellInfo> cellInfoList = tm.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList)
        {
            if (!cellInfo.isRegistered()){
                continue;
            }
            BtsId btsId = BtsId.generateBtsId(cellInfo);
            if (null != btsId){
                btsIdList.add(btsId);
                //btsTracker.addToBtsIdList(btsId);
            }

        }

    }

}






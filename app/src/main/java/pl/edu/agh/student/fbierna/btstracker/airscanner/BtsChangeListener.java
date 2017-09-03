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
    private BtsDataList btsDataList;
    private BtsSearcher btsSearcher;

    public BtsChangeListener(Context context, BtsIdList btsIdList, BtsDataList btsDataList) {
        this.context = context;
        this.btsIdList = btsIdList;
        this.btsDataList = btsDataList;
        btsSearcher = new BtsSearcher(context);



        //fill temp
        BtsId btsId = new BtsId(Network.GSM, 20, 10, 2201, 6241, 1);
        BtsData btsData = btsSearcher.search(btsId);
        if (btsData != null){
            btsDataList.add(btsData);
        }
        btsId = new BtsId(Network.WCDMA, 20, 10, 207, 2547, 1);
        btsData = btsSearcher.search(btsId);
        if (btsData != null){
            btsDataList.add(btsData);
        }
        btsId = new BtsId(Network.LTE, 20, 10, 0, 228653, 1);
        btsData = btsSearcher.search(btsId);
        if (btsData != null){
            btsDataList.add(btsData);
        }

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
                btsIdList.add(btsId);//sprawdzaj czy jest na liscie
                BtsData btsData = btsSearcher.search(btsId);
                if (btsData != null){
                    btsDataList.add(btsData);
                }

            }

        }

    }

}






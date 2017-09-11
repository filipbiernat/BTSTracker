package pl.edu.agh.student.fbierna.btstracker.scan;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsData;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsDataList;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsId;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsIdList;
import pl.edu.agh.student.fbierna.btstracker.airscanner.Network;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;
import pl.edu.agh.student.fbierna.btstracker.data.BtsSearcher;

/**
 * Created by Filip on 14.08.2017.
 */

public class AttachListener extends PhoneStateListener {

    private BtsManager btsManager;
    private TelephonyManager telephonyManager;

    public AttachListener(BtsManager btsManager, TelephonyManager telephonyManager) {
        this.btsManager = btsManager;
        this.telephonyManager = telephonyManager;

    }

    public void onCellLocationChanged (CellLocation location) {
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList)
        {
            if (cellInfo.isRegistered()){
                String operatorName = telephonyManager.getNetworkOperatorName();
                int networkType = telephonyManager.getNetworkType();
                btsManager.switchToCell(cellInfo, operatorName, networkType);
            }
        }
    }

}






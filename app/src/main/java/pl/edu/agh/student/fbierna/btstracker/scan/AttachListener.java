package pl.edu.agh.student.fbierna.btstracker.scan;

import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.List;

import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

class AttachListener extends PhoneStateListener {

    private final BtsManager btsManager;
    private final TelephonyManager telephonyManager;

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






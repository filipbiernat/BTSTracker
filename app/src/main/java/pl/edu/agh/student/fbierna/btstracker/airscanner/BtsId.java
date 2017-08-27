package pl.edu.agh.student.fbierna.btstracker.airscanner;

import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;

/**
 * Created by Filip on 15.08.2017.
 */

public class BtsId {

    private Network network;
    private int mcc;
    private int mnc;
    private int lac;
    private int id;
    private int ss;

    /*
    private BtsId(Network network, int mcc, int mnc, int lac, int id, int ss) {
        this.network = network;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.id = id;
        this.ss = ss;
    }*/
    public BtsId() {
        this.network = Network.GSM;
        this.mcc = 1;
        this.mnc = 1;
        this.lac = 1;
        this.id = 1;
        this.ss = 1;
    }



    private BtsId(CellInfoLte cellInfoLte) {
        network = Network.LTE;
        CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();

        mcc = cellIdentityLte.getMcc();
        mnc = cellIdentityLte.getMnc();
        lac = Integer.MAX_VALUE;
        id = cellIdentityLte.getCi() >> 8;
        ss = cellInfoLte.getCellSignalStrength().getLevel();
    }

    private BtsId(CellInfoWcdma cellInfoWcdma) {
        network = Network.WCDMA;
        CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();

        mcc = cellIdentityWcdma.getMcc();
        mnc = cellIdentityWcdma.getMnc();
        lac = cellIdentityWcdma.getLac();
        id = (cellIdentityWcdma.getCid() & 0xFFFF) / 10;
        ss = cellInfoWcdma.getCellSignalStrength().getLevel();
    }
    private BtsId(CellInfoGsm cellInfoGsm) {
        network = Network.GSM;
        CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();

        mcc = cellIdentityGsm.getMcc();
        mnc = cellIdentityGsm.getMnc();
        lac = cellIdentityGsm.getLac();
        id = cellIdentityGsm.getCid() / 10;
        ss = cellInfoGsm.getCellSignalStrength().getLevel();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BtsId other = (BtsId) obj;
        if (network == other.network &&
                mcc == other.mcc &&
                mnc == other.mnc &&
                lac == other.lac &&
                id == other.id) {
            return true;
        } else {
            return false;
        }
    }


    public static BtsId generateBtsId(CellInfo cellInfo) {
        BtsId btsId = null;
        if (cellInfo instanceof CellInfoGsm){
            btsId = new BtsId((CellInfoGsm) cellInfo);
        } else if (cellInfo instanceof CellInfoWcdma){
            btsId = new BtsId((CellInfoWcdma) cellInfo);
        } else if (cellInfo instanceof CellInfoLte){
            btsId = new BtsId((CellInfoLte) cellInfo);
        } else {
            //exception
        }
        return btsId;
    }
}

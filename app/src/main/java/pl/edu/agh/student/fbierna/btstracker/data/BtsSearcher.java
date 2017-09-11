package pl.edu.agh.student.fbierna.btstracker.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsData;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsId;
import pl.edu.agh.student.fbierna.btstracker.airscanner.Network;

import static android.R.attr.data;

/**
 * Created by Filip on 27.08.2017.
 */


public class BtsSearcher {

    private AssetManager assetManager;
    private BufferedReader reader = null;
    private static final String BTS_DATA_PATH = "btsData.csv";
    private static final String CSV_SPLIT_BY = ";";
    private static final int CELL_ID_CSV_LOCATION = 4;
    private static final int NETWORK_GENERATION_2G = 2;
    private static final int NETWORK_GENERATION_3G = 3;
    private static final int NETWORK_GENERATION_4G = 4;

    int mcc;
    int lac;
    int id;
    int networkGeneration;


    public BtsSearcher(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Bts search(CellInfo cellInfo, String operatorName, int networkType){
        processCellInfo(cellInfo);
        String csvQuery = getCsvQuery();
        String csvBtsData = scanCsv(csvQuery);
        if (null == csvBtsData){
            //exception
            return null;
        }
        return new Bts(csvBtsData, operatorName, networkType, networkGeneration);
    }

    private void processCellInfo(CellInfo cellInfo){
        if (cellInfo instanceof CellInfoGsm){
            networkGeneration = NETWORK_GENERATION_2G;

            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
            CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();

            mcc = cellIdentityGsm.getMcc();
            lac = cellIdentityGsm.getLac();
            id = cellIdentityGsm.getCid();

        } else if (cellInfo instanceof CellInfoWcdma){
            networkGeneration = NETWORK_GENERATION_3G;

            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
            CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();

            mcc = cellIdentityWcdma.getMcc();
            lac = cellIdentityWcdma.getLac();
            id = (cellIdentityWcdma.getCid() & 0xFFFF);

        } else if (cellInfo instanceof CellInfoLte){
            networkGeneration = NETWORK_GENERATION_4G;

            CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
            CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();

            mcc = cellIdentityLte.getMcc();
            lac = 0;
            id = cellIdentityLte.getCi() >> 8;

        } else {
            //exception
        }
    }

    private String getCsvQuery(){
        int btsId = NETWORK_GENERATION_4G == networkGeneration ? id : id/10;
        return mcc + ";" + networkGeneration + ";" + lac + ";" + btsId;
    }

    private String scanCsv(String query){
        String line = null;
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(assetManager.open(BTS_DATA_PATH), "UTF-8");
            reader = new BufferedReader(inputStreamReader);

            while ((line = reader.readLine()) != null) {

                if (line.startsWith(query)) {
                    if (hasCorrectCellId(line)){
                        return line;
                    }
                }
            }

        } catch (IOException e) {
            //log the exception
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    //log the exception
                }
            }
            return line;
        }
    }

    private Boolean hasCorrectCellId(String line) {
        if (NETWORK_GENERATION_4G == networkGeneration){
            return true;
        } else {
            String[] lineSplitted = line.split(CSV_SPLIT_BY);
            String cellIds = lineSplitted[CELL_ID_CSV_LOCATION];
            return cellIds.contains("*") || cellIds.contains(Integer.toString(id % 10));
        }

    }
}

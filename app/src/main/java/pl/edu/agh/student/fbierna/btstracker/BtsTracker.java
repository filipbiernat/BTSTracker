package pl.edu.agh.student.fbierna.btstracker;

import android.app.Application;
import android.content.res.Configuration;

import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsDataList;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsIdList;

/**
 * Created by Filip on 15.08.2017.
 */

public class BtsTracker extends Application {
    public BtsIdList btsIdList = new BtsIdList();
    public BtsDataList btsDataList = new BtsDataList();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
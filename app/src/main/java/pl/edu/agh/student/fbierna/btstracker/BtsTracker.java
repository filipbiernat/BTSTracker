package pl.edu.agh.student.fbierna.btstracker;

import android.app.Application;
import android.content.res.Configuration;

import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

/**
 * Created by Filip on 15.08.2017.
 */

public class BtsTracker extends Application {
    private BtsManager btsManager = new BtsManager(this);

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

    public BtsManager getBtsManager(){
        return btsManager;
    }
}

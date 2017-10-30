package pl.edu.agh.student.fbierna.btstracker.scan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

public class ScanService extends Service {


    final class ScanThread implements Runnable{
        private final int serviceId;

        ScanThread(int serviceId){
            this.serviceId = serviceId;
        }

        @Override
        public void run() {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(attachListener, PhoneStateListener.LISTEN_CELL_LOCATION);
        }
    }

    private Thread scanThread;
    private AttachListener attachListener;

    public ScanService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        BtsTracker btsTracker = (BtsTracker) getApplicationContext();
        BtsManager btsManager = btsTracker.getBtsManager();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        attachListener = new AttachListener(btsManager, telephonyManager);
   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scanThread = new Thread(new ScanThread(startId));
        scanThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        scanThread.interrupt();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

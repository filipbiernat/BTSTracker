package pl.edu.agh.student.fbierna.btstracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsChangeListener;

public class ScanService extends Service {


    final class ScanThread implements Runnable{
        int serviceId;

        ScanThread(int serviceId){
            this.serviceId = serviceId;
        }

        @Override
        public void run() {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(btsChangeListener, PhoneStateListener.LISTEN_CELL_LOCATION);
        }
    }


    public ScanService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        btsChangeListener = new BtsChangeListener(this, ((BtsTracker) getApplicationContext()).btsIdList);

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

    private Thread scanThread;
/*r = new AirScanner();

    public PhoneStateListener phoneStateListener = new PhoneStateListener() {

        AirScanner airScanne
        @Override
        public void onCellLocationChanged (CellLocation location) {
            airScanner.scan(ScanService.this);
        }
    };*/

    private BtsChangeListener btsChangeListener;// = new BtsChangeListener(this, a);

}

package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.scan.ScanService;

/**
 * Created by Filip on 24.10.2017.
 */

public class FloatingActionButtonOnClickListener implements View.OnClickListener {

    private FloatingActionButton button;
    private boolean serviceEnabled;

    public FloatingActionButtonOnClickListener(FloatingActionButton button) {
        this.button = button;
        serviceEnabled = true;
        switchState(button.getContext());
    }

    @Override
    public void onClick(View view) {
        switchState(view.getContext());
    }

    private void switchState(Context context){
        if (serviceEnabled){
            stopService(context);
            Log.d("LOGFILIP", "stopService");
            //button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_send));
        } else {
            startService(context);
            Log.d("LOGFILIP", "startService");
            //button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_camera));
        }
        //button.setRippleColor(Color.WHITE);
        serviceEnabled = !serviceEnabled;
    }

    private void startService(Context context) {
        Intent intent = new Intent(context, ScanService.class);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.startService(intent);
        Toast.makeText(context, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopService(Context context) {
        Intent intent = new Intent(context, ScanService.class);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.stopService(intent);
        Toast.makeText(context, "Service stoped", Toast.LENGTH_SHORT).show();
    }
}
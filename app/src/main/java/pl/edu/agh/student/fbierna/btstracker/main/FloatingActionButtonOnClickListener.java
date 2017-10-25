package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.scan.ScanService;

/**
 * Created by Filip on 24.10.2017.
 */

public class FloatingActionButtonOnClickListener implements View.OnClickListener {

    private MainActivity mainActivity;
    private boolean serviceEnabled;

    public FloatingActionButtonOnClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        serviceEnabled = false;
        switchState(mainActivity);
    }

    @Override
    public void onClick(View view) {
        switchState(view.getContext());
        switchDrawable(view, view.getContext()); //TODO variable context
    }

    private void switchState(Context context){
        if (serviceEnabled) {
            stopService(context);
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
        mainActivity.startService(intent);
        Toast.makeText(context, "Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopService(Context context) {
        Intent intent = new Intent(context, ScanService.class);
        mainActivity.stopService(intent);
        Toast.makeText(context, "Service stoped", Toast.LENGTH_SHORT).show();
    }

    private void switchDrawable(View view, Context context){
        FloatingActionButton floatingActionButton = (FloatingActionButton) view;
        int id = serviceEnabled ? R.drawable.fab_stop : R.drawable.fab_start;
        Drawable drawable = context.getResources().getDrawable(id, context.getTheme());
        floatingActionButton.setImageDrawable(drawable);
    }

}
package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.scan.ScanService;

class FloatingActionButtonOnClickListener implements View.OnClickListener {

    private final MainActivity mainActivity;
    private boolean serviceEnabled;

    public FloatingActionButtonOnClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        serviceEnabled = false;
        switchState(mainActivity, true);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        switchState(context, true);
        switchDrawable(view, context);
    }

    protected FloatingActionButtonOnClickListener switchState(Context context, Boolean enableToast){
        if (serviceEnabled) {
            stopService(context, enableToast);
        } else {
            startService(context, enableToast);
        }
        serviceEnabled = !serviceEnabled;
        return this;
    }

    private void startService(Context context, Boolean enableToast) {
        Intent intent = new Intent(context, ScanService.class);
        mainActivity.startService(intent);
        if (enableToast){
            Toast.makeText(context, "Scanning enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopService(Context context, Boolean enableToast) {
        Intent intent = new Intent(context, ScanService.class);
        mainActivity.stopService(intent);
        if (enableToast) {
            Toast.makeText(context, "Scanning disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchDrawable(View view, Context context){
        FloatingActionButton floatingActionButton = (FloatingActionButton) view;
        int id = serviceEnabled ? R.drawable.fab_stop : R.drawable.fab_start;
        Drawable drawable = context.getResources().getDrawable(id, context.getTheme());
        floatingActionButton.setImageDrawable(drawable);
    }

}
package pl.edu.agh.student.fbierna.btstracker.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Filip on 18.11.2017.
 */

class PermissionsManager {
    private final Activity activity;

    private static final String[] permissionsRequired = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.ACCESS_COARSE_LOCATION,
            //Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionsManager(Activity activity){
        this.activity = activity;
    }

    public Boolean hasPermissions() {
        Boolean anyPermissionDenied = false;
        for (String permissionsToCheck: permissionsRequired){
            if (permissionDenied(permissionsToCheck)){
                anyPermissionDenied = true;
                break;
            }
        }
        if (anyPermissionDenied) {
            ActivityCompat.requestPermissions(activity, permissionsRequired, 1);
        }
        return !anyPermissionDenied;
    }


    public Boolean onRequestPermissionsResult(@NonNull int[] grantResults) {
        Boolean anyPermissionDenied = grantResults.length < permissionsRequired.length;

        for (int grantResult : grantResults){
            if (grantResult != PackageManager.PERMISSION_GRANTED){
                anyPermissionDenied = true;
            }
        }

        if (anyPermissionDenied){
            Toast.makeText(activity.getApplicationContext(),
                    "Please grant all necessary permissions to use the application.", Toast.LENGTH_LONG).show();
            hasPermissions();
        }
        return !anyPermissionDenied;
    }

    private Boolean permissionDenied(String permission) {
        return (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED);
    }
}

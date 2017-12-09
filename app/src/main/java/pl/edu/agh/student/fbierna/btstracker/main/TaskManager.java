package pl.edu.agh.student.fbierna.btstracker.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;

/**
 * Created by Filip on 18.11.2017.
 */

class TaskManager {
    public static final class AccessToken { private  AccessToken() {} } //enables access to fab
    private static final  AccessToken token = new AccessToken();

    private FileManager fileManager;
    private MainActivity activity;

    TaskManager(MainActivity activity){
        fileManager = new FileManager();
        this.activity = activity;
    }

    public void open() {
        final Boolean openNew = true;
        promptReset(openNew);
    }

    public void save(){
        final Boolean toCsv = true;
        store(toCsv);
    }

    public void export(){
        final Boolean toCsv = false;
        store(toCsv);
    }

    public void reset(){
        final Boolean openNew = false;
        promptReset(openNew);
    }

    private void store(final Boolean toCsv){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    String filename = input.getText().toString();
                    if (filename.length() > 0){
                        BtsTracker btsTracker = (BtsTracker) activity.getApplication();
                        filename += toCsv ? ".btst" : ".kml";
                        if (toCsv) {
                            fileManager.saveToBtstFile(activity, btsTracker.getBtsManager().getList(),
                                    filename);
                        } else {
                            fileManager.saveToKmlFile(activity, btsTracker.getBtsManager().getList(),
                                    filename);
                        }
                    } else {
                        Toast.makeText(activity, "Incorrect filename. Try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        builder.setMessage("Please choose filename:")
                .setPositiveButton("OK", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setIcon(toCsv ? R.drawable.ic_menu_save : R.drawable.ic_menu_export)
                .setTitle(toCsv ? "Save" : "Export")
                .show();


    }

    private void promptReset(final Boolean openNew){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    BtsTracker btsTracker = (BtsTracker) activity.getApplication();
                    btsTracker.getBtsManager().reset();

                    if (openNew) {
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                openFile(activity);
                            }
                        });
                    } else {
                        refreshActivity();
                    }

                }
            }
        };

        builder.setMessage("You are going to discard all your data. Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .setIcon(openNew ? R.drawable.ic_menu_open : R.drawable.ic_menu_reset)
                .setTitle(openNew ? "Open" : "Reset")
                .show();
    }

    public void openFile(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        final ArrayAdapter<String> arrayAdapter = fileManager.getBtstPaths(activity);
        if (arrayAdapter.isEmpty()){
            Toast.makeText(activity, "No *.btst file found at " + fileManager.getDirPath(),
                    Toast.LENGTH_LONG).show();
            refreshActivity();
            return;
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != DialogInterface.BUTTON_NEGATIVE) {
                    String filename = arrayAdapter.getItem(which);
                    BtsTracker btsTracker = (BtsTracker) activity.getApplication();
                    fileManager.openFromBtstFile(context, filename, btsTracker.getBtsManager().getList());
                    refreshActivity();
                }
            }
        };
        builder.setAdapter(arrayAdapter, dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setIcon(R.drawable.ic_menu_open)
                .setTitle("Open file")
                .show();
    }

    private void refreshActivity(){
        activity.doubleSwitchFabState(token);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new ListFragment());
                fragmentTransaction.commit();
            }
        }, 2000);
    }

}

package pl.edu.agh.student.fbierna.btstracker;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

public class BtsTracker extends Application {
    private final BtsManager btsManager = new BtsManager(this);

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                String s = writer.toString();
                writeToFile(getApplicationContext(), "stackTrace.txt", s);
            }
        });
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

    private void writeToFile(Context context, String fileName, String data) {

        Writer writer;

        String filepath = Environment.getExternalStorageDirectory().getPath() + File.separator + "BTS_Tracker";
        File dir = new File(filepath);

        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        try {
            if (!dir.isDirectory()) {
                throw new IOException(
                        "Unable to access directory " + filepath);
            }
            File outputFile = new File(dir, fileName);
            final Boolean appendText = true;
            writer = new BufferedWriter(new FileWriter(outputFile, appendText));
            writer.write(data); // DATA WRITE TO FILE
            Toast.makeText(context.getApplicationContext(),
                    "Successfully saved to: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage() + " Unable to access the storage.", Toast.LENGTH_LONG).show();
        }
    }
}

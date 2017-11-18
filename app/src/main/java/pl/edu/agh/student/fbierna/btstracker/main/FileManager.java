package pl.edu.agh.student.fbierna.btstracker.main;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.LinkedList;

import pl.edu.agh.student.fbierna.btstracker.data.Bts;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

import static android.R.attr.path;

/**
 * Created by Filip on 18.11.2017.
 */

public class FileManager {
    public static final String DIR_NAME = "BTS_Tracker";

    public void exportToFile(Context context, LinkedList<Bts> btsList, String filename){
        StringBuilder stringBuilder = new StringBuilder();

        for (Bts bts : btsList) {
            stringBuilder.append(bts.getCsvString());
            stringBuilder.append("\n");
        }

        writeToFile(context, filename, stringBuilder.toString());
    }

    public ArrayAdapter<String> getBtstPaths(Activity activity){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_singlechoice);

        File dir = new File(getDirPath());
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            File file = files[i];
            if (file.getName().endsWith(".btst")) {
                arrayAdapter.add(file.getName());
            }
        }

        return arrayAdapter;
    }


    public void importFromFile(String filename, LinkedList<Bts> btsList) {
        File file = new File(getDirPath()+ File.separator + filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                Log.d("LOGFILIP", line);
                Bts newBts = new Bts(line);
                btsList.add(newBts);
            }
            br.close();
        } catch (IOException e) {
            //exception
        }
    }

    public String getDirPath(){
        return Environment.getExternalStorageDirectory().getPath() + File.separator + DIR_NAME;
    }

    private void writeToFile(Context context, String fileName, String data) {

        Writer writer;

        String dirPath = getDirPath();
        File dir = new File(dirPath);

        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        try {
            if (!dir.isDirectory()) {
                throw new IOException(
                        "Unable to access directory " + dirPath);
            }
            File outputFile = new File(dir, fileName);
            final Boolean appendText = false;
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

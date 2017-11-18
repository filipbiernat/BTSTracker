package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

import pl.edu.agh.student.fbierna.btstracker.data.Bts;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

/**
 * Created by Filip on 18.11.2017.
 */

public class FileManager {

    public void exportToFile(Context context, LinkedList<Bts> btsList, String filename){
        StringBuilder stringBuilder = new StringBuilder();

        for (Bts bts : btsList) {
            stringBuilder.append(bts.getCsvString());
            stringBuilder.append("\n");
        }

        writeToFile(context, filename, stringBuilder.toString());
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

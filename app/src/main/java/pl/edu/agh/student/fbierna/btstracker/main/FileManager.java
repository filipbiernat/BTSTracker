package pl.edu.agh.student.fbierna.btstracker.main;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;

import pl.edu.agh.student.fbierna.btstracker.data.Bts;

/**
 * Created by Filip on 18.11.2017.
 */

class FileManager {
    private static final String DIR_NAME = "BTS_Tracker";

    public void saveToBtstFile(Context context, LinkedList<Bts> btsList, String filename){
        StringBuilder stringBuilder = new StringBuilder();

        for (Bts bts : btsList) {
            stringBuilder.append(bts.getCsvString());
        }

        writeToFile(context, filename, stringBuilder.toString());
    }

    public void saveToKmlFile(Context context, LinkedList<Bts> btsList, String filename){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(KML_PREFIX);
        stringBuilder.append("      <name>" + DIR_NAME + " " + filename + "</name>\n");

        for (Bts bts : btsList) {
            stringBuilder.append(bts.getKmlString());
        }

        stringBuilder.append(KML_SUFFIX);
        writeToFile(context, filename, stringBuilder.toString());
    }

    public ArrayAdapter<String> getBtstPaths(Activity activity){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.select_dialog_singlechoice);

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


    public void openFromBtstFile(Context context, String filename, LinkedList<Bts> btsList) {
        File file = new File(getDirPath()+ File.separator + filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                try {
                Bts newBts = new Bts(line);
                btsList.add(newBts);
                } catch (Exception e) {
                    Toast.makeText(context, "Warning! Error occurred while reading single BTS data!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            br.close();
        } catch (Exception e) {
            Toast.makeText(context, "Warning! Error occurred while reading data from BTST file!",
                    Toast.LENGTH_SHORT).show();
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

    private static final String KML_PREFIX =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
            "  <Document>\n" +
            "    <name/>\n" +
            "    <description/>\n" +
            "    <Style id=\"bts2G\">\n" +
            "      <IconStyle>\n" +
            "        <color>ff8140ff</color>\n" +
            "        <Icon>\n" +
            "          <href>http://www.gstatic.com/mapspro/images/stock/503-wht-blank_maps.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"16\" xunits=\"pixels\" y=\"32\" yunits=\"insetPixels\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "    <Style id=\"bts3G\">\n" +
            "      <IconStyle>\n" +
            "        <color>ffb5513f</color>\n" +
            "        <Icon>\n" +
            "          <href>http://www.gstatic.com/mapspro/images/stock/503-wht-blank_maps.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"16\" xunits=\"pixels\" y=\"32\" yunits=\"insetPixels\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "	<Style id=\"bts4G\">\n" +
            "      <IconStyle>\n" +
            "        <color>ff3643f4</color>\n" +
            "        <Icon>\n" +
            "          <href>http://www.gstatic.com/mapspro/images/stock/503-wht-blank_maps.png</href>\n" +
            "        </Icon>\n" +
            "        <hotSpot x=\"16\" xunits=\"pixels\" y=\"32\" yunits=\"insetPixels\"/>\n" +
            "      </IconStyle>\n" +
            "    </Style>\n" +
            "    <Folder>\n";

    private static final String KML_SUFFIX =
            "    </Folder>\n" +
            "  </Document>\n" +
            "</kml>\n";
}

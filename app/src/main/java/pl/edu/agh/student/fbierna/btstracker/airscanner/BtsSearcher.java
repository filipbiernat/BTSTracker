package pl.edu.agh.student.fbierna.btstracker.airscanner;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Filip on 27.08.2017.
 */


public class BtsSearcher {

    private BufferedReader reader = null;
    private static final String BTS_DATA_PATH = "btsData.csv";
    private Context context;

    public BtsSearcher(Context context) {
        this.context = context;
    }

    public BtsData search(BtsId btsId) {
        String id = btsId.getStringId();
        Log.d("LOGFILIP id", id);
        String dataFromCsv = scanCsv(id);
        if (dataFromCsv != null){
            return new BtsData(btsId, dataFromCsv);
        }
        return null;
    }


    private String scanCsv(String id){
        String line = null;
        try {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(context.getAssets().open(BTS_DATA_PATH), "UTF-8");
            reader = new BufferedReader(inputStreamReader);

            // do reading, usually loop until end of file reading
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(id)) {
                    return line;
                }
            }

        } catch (IOException e) {
            //log the exception
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    //log the exception
                }
            }
            return line;
        }
    }
}

package pl.edu.agh.student.fbierna.btstracker.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.airscanner.Network;

/**
 * Created by Filip on 03.09.2017.
 */

public class ViewHolder  extends RecyclerView.ViewHolder{
    public Button networkButton;
    public TextView textViewName;
    public TextView textViewDescription;
    public TextView textViewPrice;
    public ViewHolder(View v) {
        super(v);
        networkButton = (Button) v.findViewById(R.id.buttonNetwork);
        textViewName = (TextView) v.findViewById(R.id.textViewName);
        textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);
    }

    public void setNetworkMode(Network network){
        if (network == Network.GSM) {
            networkButton.setBackgroundResource(R.drawable.circle_light);
            networkButton.setText("2G");
        } else if (network == Network.WCDMA) {
            networkButton.setBackgroundResource(R.drawable.circle_medium);
            networkButton.setText("3G");
        } else if (network == Network.LTE) {
            networkButton.setBackgroundResource(R.drawable.circle_dark);
            networkButton.setText("4G");
        } else {
            //exception
        }
    }
}
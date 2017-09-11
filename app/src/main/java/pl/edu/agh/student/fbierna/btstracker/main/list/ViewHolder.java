package pl.edu.agh.student.fbierna.btstracker.main.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.student.fbierna.btstracker.R;

/**
 * Created by Filip on 03.09.2017.
 */

public class ViewHolder  extends RecyclerView.ViewHolder{
    public Button networkButton;
    public TextView textViewTown;
    public TextView textViewLocation;
    public TextView textViewOperatorName;
    public TextView textViewRegion;
    public TextView textViewTimeAttached;
    public TextView textViewNetworkType;
    public ViewHolder(View v) {
        super(v);
        networkButton = (Button) v.findViewById(R.id.buttonNetwork);
        textViewTown = (TextView) v.findViewById(R.id.textViewTown);
        textViewLocation = (TextView) v.findViewById(R.id.textViewLocation);
        textViewOperatorName = (TextView) v.findViewById(R.id.textViewOperatorName);
        textViewRegion = (TextView) v.findViewById(R.id.textViewRegion);
        textViewTimeAttached = (TextView) v.findViewById(R.id.textViewTimeAttached);
        textViewNetworkType = (TextView) v.findViewById(R.id.textViewNetworkType);
    }

    public void setNetworkMode(int network){
        if (network == 2) { //FIXME refactor
            networkButton.setBackgroundResource(R.drawable.circle_light);
            networkButton.setText("2G");
        } else if (network == 3) {
            networkButton.setBackgroundResource(R.drawable.circle_medium);
            networkButton.setText("3G");
        } else if (network == 4) {
            networkButton.setBackgroundResource(R.drawable.circle_dark);
            networkButton.setText("4G");
        } else {
            //exception
        }
    }
}
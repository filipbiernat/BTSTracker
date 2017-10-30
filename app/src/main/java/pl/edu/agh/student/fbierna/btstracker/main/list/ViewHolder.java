package pl.edu.agh.student.fbierna.btstracker.main.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.student.fbierna.btstracker.R;

public class ViewHolder  extends RecyclerView.ViewHolder{
    private final Button networkButton;
    public final TextView textViewTown;
    public final TextView textViewLocation;
    public final TextView textViewOperatorName;//todo rename
    // --Commented out by Inspection (30.10.2017 19:56):public TextView textViewRegion;
    public final TextView textViewTimeAttached;
    //public TextView textViewNetworkType;
    public ViewHolder(View v) {
        super(v);
        networkButton = (Button) v.findViewById(R.id.buttonNetwork);
        textViewTown = (TextView) v.findViewById(R.id.textViewTown);
        textViewLocation = (TextView) v.findViewById(R.id.textViewLocation);
        textViewOperatorName = (TextView) v.findViewById(R.id.textViewOperatorAndNetwork);
        //textViewRegion = (TextView) v.findViewById(R.id.textViewRegion);
        textViewTimeAttached = (TextView) v.findViewById(R.id.textViewTimeAttached);
        //textViewNetworkType = (TextView) v.findViewById(R.id.textViewNetworkType);
    }

    public void setNetworkMode(int network){
        if (network == 2) { //FIXME refactor
            networkButton.setBackgroundResource(R.drawable.circle_2g);
            networkButton.setText("2G");
        } else if (network == 3) {
            networkButton.setBackgroundResource(R.drawable.circle_3g);
            networkButton.setText("3G");
        } else if (network == 4) {
            networkButton.setBackgroundResource(R.drawable.circle_4g);
            networkButton.setText("4G");
        } else {
            //exception
        }
    }
}
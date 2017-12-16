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
    public final TextView textViewOperatorAndNetwork;
    public final TextView textViewTimeAttached;

    public ViewHolder(View view) {
        super(view);
        networkButton = (Button) view.findViewById(R.id.buttonNetwork);
        textViewTown = (TextView) view.findViewById(R.id.textViewTown);
        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
        textViewOperatorAndNetwork = (TextView) view.findViewById(R.id.textViewOperatorAndNetwork);
        textViewTimeAttached = (TextView) view.findViewById(R.id.textViewTimeAttached);
    }

    public void setNetworkMode(int network){
        networkButton.setText(Integer.toString(network) + "G");
        if (network == 2) {
            networkButton.setBackgroundResource(R.drawable.circle_2g);
        } else if (network == 3) {
            networkButton.setBackgroundResource(R.drawable.circle_3g);
        } else if (network == 4) {
            networkButton.setBackgroundResource(R.drawable.circle_4g);
        }
    }
}
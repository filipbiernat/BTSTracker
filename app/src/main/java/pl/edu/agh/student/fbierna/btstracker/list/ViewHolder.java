package pl.edu.agh.student.fbierna.btstracker.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import pl.edu.agh.student.fbierna.btstracker.R;

/**
 * Created by Filip on 03.09.2017.
 */

public class ViewHolder  extends RecyclerView.ViewHolder{
    public TextView textViewName;
    public TextView textViewDescription;
    public TextView textViewPrice;
    public ViewHolder(View v) {
        super(v);
        textViewName = (TextView) v.findViewById(R.id.textViewName);
        textViewDescription = (TextView) v.findViewById(R.id.textViewDescription);
        textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);
    }
}
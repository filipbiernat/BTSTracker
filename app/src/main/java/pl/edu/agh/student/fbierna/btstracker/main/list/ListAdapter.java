package pl.edu.agh.student.fbierna.btstracker.main.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.data.Bts;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;


public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context mContext;
    private final BtsManager btsManager;

    public ListAdapter(Context context) {
        mContext = context;
        BtsTracker btsTracker = (BtsTracker) mContext.getApplicationContext();
        btsManager = btsTracker.getBtsManager();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bts bts = btsManager.get(position);
        if (null == bts){
            holder.textViewTown.setText("NULL CELL");
            holder.textViewLocation.setText(" - ");
            holder.textViewOperatorName.setText(" - ");
           // holder.textViewRegion.setText(" - ");
            holder.textViewTimeAttached.setText(" - ");
            //holder.textViewNetworkType.setText(" - ");
            holder.setNetworkMode(2);//fix
        } else {
            holder.textViewTown.setText(bts.getTown());
            holder.textViewLocation.setText(bts.getLocation());

            String operatorAndNetwork = bts.getOperatorName() + " \u2022 " +bts.getNetworkType();

            holder.textViewOperatorName.setText(operatorAndNetwork);
            holder.textViewTimeAttached.setText(bts.getTimeAttached());
            //holder.textViewNetworkType.setText(bts.getNetworkType());
            holder.setNetworkMode(bts.getNetworkGeneration());//rename
        }
    }

    @Override
    public int getItemCount() {
        return btsManager.size();
    }


}
package pl.edu.agh.student.fbierna.btstracker.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsData;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsDataList;
import pl.edu.agh.student.fbierna.btstracker.R;

/**
 * Created by Filip on 03.09.2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context mContext;
    ArrayList<BtsData> btsDataArrayList;
    LayoutInflater mInflater;

    public ListAdapter(Context context) {
        mContext = context;
        BtsTracker btsTracker = (BtsTracker) mContext.getApplicationContext();
        BtsDataList btsDataList = btsTracker.btsDataList;
        btsDataArrayList = btsDataList.get();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BtsData btsData = btsDataArrayList.get(position);
        holder.textViewName.setText(btsData.getNetworkOperator());
        holder.textViewDescription.setText(btsData.getTownAndRegion());
        holder.textViewPrice.setText(btsData.getLocation());
        holder.setNetworkMode(btsData.getNetworkMode());
    }

    @Override
    public int getItemCount() {
        return btsDataArrayList.size();
    }


}
package pl.edu.agh.student.fbierna.btstracker.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;
import pl.edu.agh.student.fbierna.btstracker.main.list.ListAdapter;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListAdapter listAdapter;
    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter(getActivity());
        recyclerView.setAdapter(listAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        warnIfDetached();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                listAdapter.notifyDataSetChanged();
                warnIfDetached();
            }
        }, 500);
    }

    private void warnIfDetached(){
        BtsTracker btsTracker = (BtsTracker) getActivity().getApplicationContext();
        BtsManager btsManager = btsTracker.getBtsManager();
        if (btsManager.isDetached()){
            Toast.makeText(getActivity(), "Warning! Currently connected to an unknown cell!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

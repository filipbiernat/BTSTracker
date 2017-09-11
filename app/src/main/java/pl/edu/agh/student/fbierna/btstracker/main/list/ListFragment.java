package pl.edu.agh.student.fbierna.btstracker.main.list;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.scan.ScanService;
import pl.edu.agh.student.fbierna.btstracker.main.list.ListAdapter;


public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(getActivity());
        recyclerView.setAdapter(adapter);




        Log.d("LOGFILIP", "h1");
        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab_home);
        Log.d("LOGFILIP", "h2");
        fab.setOnClickListener(new FloatingActionButtonOnClickListener(fab));
        Log.d("LOGFILIP", "h3");

    }

    public class FloatingActionButtonOnClickListener implements View.OnClickListener {

        private FloatingActionButton button;
        private boolean serviceEnabled;

        public FloatingActionButtonOnClickListener(FloatingActionButton button) {
            this.button = button;
            serviceEnabled = true;
            switchState();
        }

        @Override
        public void onClick(View arg0) {
            switchState();
        }

        private void switchState(){
            if (serviceEnabled){
                stopService();
                Log.d("LOGFILIP", "stopService");
                //button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_send));
            } else {
                startService();
                Log.d("LOGFILIP", "startService");
                //button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_camera));
            }
            //button.setRippleColor(Color.WHITE);
            serviceEnabled = !serviceEnabled;
        }
    }

    public void startService() {
        Intent intent = new Intent(getActivity(), ScanService.class);
        getActivity().startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(getActivity(), ScanService.class);
        getActivity().stopService(intent);
    }
}

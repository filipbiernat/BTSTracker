package pl.edu.agh.student.fbierna.btstracker.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.scan.ScanService;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);




        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

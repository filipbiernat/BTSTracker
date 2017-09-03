package pl.edu.agh.student.fbierna.btstracker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsData;
import pl.edu.agh.student.fbierna.btstracker.airscanner.BtsDataList;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;


    public MapFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getActivity().setTitle("Menu 1");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //mMap.setMyLocationEnabled(true);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getActivity();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);
                info.setBackgroundColor(Color.WHITE);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;

            }
        });


        LatLng bochnia= new LatLng(49.965611, 20.454008);

        BtsDataList btsDataList = ((BtsTracker) getActivity().getApplication()).btsDataList;

        for(BtsData btsData : btsDataList.get()){
            Marker marker = mMap.addMarker(btsData.getMarkerOptions());
        }

        Marker bochniaMarker = mMap.addMarker(new MarkerOptions().position(bochnia)
                .title("Marker in Bochnia")
                .snippet("Lorem Ipsum")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(bochnia, 15);
        mMap.animateCamera(zoom);


        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new FloatingActionButtonOnClickListener(fab, bochniaMarker));









    }

    public class FloatingActionButtonOnClickListener implements View.OnClickListener {

        private FloatingActionButton button;
        private Marker bochniaMarker;
        private boolean infoWindowEnabled;

        public FloatingActionButtonOnClickListener(FloatingActionButton button, Marker bochniaMarker) {
            this.button = button;
            this.bochniaMarker = bochniaMarker;
            infoWindowEnabled = false;
            switchState();
        }

        @Override
        public void onClick(View arg0) {
            switchState();
        }

        private void switchState(){
            if (infoWindowEnabled){
                bochniaMarker.hideInfoWindow();
                button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_send));
            } else {
                bochniaMarker.showInfoWindow();
                button.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_camera));
            }
            button.setRippleColor(Color.WHITE);
            infoWindowEnabled = !infoWindowEnabled;
        }
    }
}
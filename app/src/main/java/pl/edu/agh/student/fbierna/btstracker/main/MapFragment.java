package pl.edu.agh.student.fbierna.btstracker.main;

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

import java.util.ArrayList;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

import static pl.edu.agh.student.fbierna.btstracker.R.id.fab;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map_fab_center;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private BtsManager btsManager;

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


        BtsTracker btsTracker = (BtsTracker) getActivity().getApplicationContext();
        btsManager = btsTracker.getBtsManager();

        FloatingActionButton mapFabCenter = (FloatingActionButton) getActivity().findViewById(map_fab_center);
        mapFabCenter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 11);
                mMap.animateCamera(zoom);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //mMap.setMyLocationEnabled(true); FIXME FB enable

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


        ArrayList<MarkerOptions> markerOptionsList = btsManager.getMarkerOptions();
        for (MarkerOptions markerOptions : markerOptionsList){
            mMap.addMarker(markerOptions); //Marker marker =
        }


        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 11);
        mMap.moveCamera(zoom);


    }

}
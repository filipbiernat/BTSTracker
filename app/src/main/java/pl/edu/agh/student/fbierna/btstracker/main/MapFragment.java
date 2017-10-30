package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

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

import static pl.edu.agh.student.fbierna.btstracker.R.drawable.marker;
import static pl.edu.agh.student.fbierna.btstracker.R.id.fab;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map_fab_center;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map_fab_refresh;
import static pl.edu.agh.student.fbierna.btstracker.R.id.textViewLocation;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private BtsManager btsManager;
    private ArrayList<Marker> markers;

    public MapFragment() {
        markers = new ArrayList<>();
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
                getChildFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);


        BtsTracker btsTracker = (BtsTracker) getActivity().getApplicationContext();
        btsManager = btsTracker.getBtsManager();

        FloatingActionButton mapFabRefresh = (FloatingActionButton) getActivity().findViewById(map_fab_refresh);
        mapFabRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                mMap.clear();
                onMapReady(mMap);

            }
        });

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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            mMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setPadding(0, 64, 0, 0);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = getActivity();

                View view = getActivity().getLayoutInflater().inflate(R.layout.map_view_item, null);

                TextView textViewTown = (TextView) view.findViewById(R.id.textViewTown);
                textViewTown.setText(marker.getTitle());

                TextView textViewTimeAttached = (TextView) view.findViewById(R.id.textViewTimeAttached);
                TextView textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
                TextView textViewOperatorAndNetwork = (TextView) view.findViewById(R.id.textViewOperatorAndNetwork);

                String snippet = marker.getSnippet();
                String[] snippetSeparated = snippet.split(";;");
                textViewTimeAttached.setText(snippetSeparated[0]);
                textViewLocation.setText(snippetSeparated[1]);
                textViewOperatorAndNetwork.setText(snippetSeparated[2]);

                return view;
            }
        });


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
            }
        });


        markers.clear();
        ArrayList<MarkerOptions> markerOptionsList = btsManager.getMarkerOptions();
        for (MarkerOptions markerOptions : markerOptionsList){
            Marker marker = mMap.addMarker(markerOptions);
            markers.add(marker);
        }
        showInfoWindows();

        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 11);
        mMap.moveCamera(zoom);


    }

    private void hideInfoWindows(){
        for (Marker marker : markers){
            marker.hideInfoWindow();
        }
    }

    private void showInfoWindows(){
        for (Marker marker : markers){
            marker.showInfoWindow();
        }
    }
}
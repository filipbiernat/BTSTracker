package pl.edu.agh.student.fbierna.btstracker.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

import static pl.edu.agh.student.fbierna.btstracker.R.id.map;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map_fab_center;
import static pl.edu.agh.student.fbierna.btstracker.R.id.map_fab_refresh;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private BtsManager btsManager;
    private final ArrayList<Marker> markers;

    public MapFragment() {
        markers = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_map, container, false);
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
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 12);
                mMap.animateCamera(zoom);

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        setZoomControls();

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
            marker.showInfoWindow();
            markers.add(marker);
        }

        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 12);
        mMap.moveCamera(zoom);


    }

    private void setZoomControls() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            @SuppressWarnings("all")
            View zoomControls = mapFragment.getView().findViewById(1);
            if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                // ZoomControl is inside of RelativeLayout
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics());
                params.setMargins(0, 0, margin, 0);
            }
        }
        catch (Exception e){
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }
}
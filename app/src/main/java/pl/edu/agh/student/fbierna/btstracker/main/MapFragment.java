package pl.edu.agh.student.fbierna.btstracker.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;
import pl.edu.agh.student.fbierna.btstracker.data.BtsManager;

import static android.R.attr.data;
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
        mMap.setPadding(96, 0, 0, 0);

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

        try {
            markers.clear();
            ArrayList<MarkerOptions> markerOptionsList = btsManager.getMarkerOptions();
            for (MarkerOptions markerOptions : markerOptionsList){
                Marker marker = mMap.addMarker(markerOptions);
                marker.showInfoWindow();
                markers.add(marker);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "ERROR! Check stackTrace.txt",
                    Toast.LENGTH_LONG).show();
            Writer writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            String stackTrace = writer.toString() + "\n\n";
            writeToFile(getActivity(), "stackTrace.txt", stackTrace);
        }

        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(btsManager.getTopBtsLatLng(), 12);
        mMap.moveCamera(zoom);


    }

    private void setZoomControls() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            View zoomControls = mapFragment.getView().findViewById(1);
            if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                        getResources().getDisplayMetrics());
                params.setMargins(margin, margin, margin, margin);
            }
        }
        catch (Exception e){
            mMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    public void writeToFile(Context context, String fileName, String data) {

        Writer writer;

        String filepath = Environment.getExternalStorageDirectory().getPath() + File.separator + "BTS_Tracker";
        File dir = new File(filepath);

        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        try {
            if (!dir.isDirectory()) {
                throw new IOException(
                        "Unable to access directory " + filepath);
            }
            File outputFile = new File(dir, fileName);
            final Boolean appendText = true;
            writer = new BufferedWriter(new FileWriter(outputFile, appendText));
            writer.write(data); // DATA WRITE TO FILE
            Toast.makeText(context.getApplicationContext(),
                    "Successfully saved to: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage() + " Unable to access the storage.", Toast.LENGTH_LONG).show();
        }
    }
}
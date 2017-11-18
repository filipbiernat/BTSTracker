package pl.edu.agh.student.fbierna.btstracker.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import pl.edu.agh.student.fbierna.btstracker.BtsTracker;
import pl.edu.agh.student.fbierna.btstracker.R;

import static android.R.id.input;
import static pl.edu.agh.student.fbierna.btstracker.R.id.fab;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButtonOnClickListener fabListener;
    private PermissionsManager permissionsManager;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new ListFragment());
        fragmentTransaction.commit();

        fileManager = new FileManager();
        permissionsManager = new PermissionsManager(this);
        Boolean permissionsGranted = permissionsManager.hasPermissions();
        finalizeOnCreate(permissionsGranted);
    }

    private void finalizeOnCreate(Boolean permissionsGranted){
        if (permissionsGranted){
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fabListener = new FloatingActionButtonOnClickListener(this);
            fab.setOnClickListener(fabListener);
        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_list) {
            fragment = new ListFragment();
        } else if (id == R.id.nav_map) {
            fragment = new MapFragment();
        } else if (id == R.id.nav_save) {
            save();
        } else if (id == R.id.nav_reset) {
            reset();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Boolean permissionsGranted = permissionsManager.onRequestPermissionsResult(grantResults);
        finalizeOnCreate(permissionsGranted);
    }

    private void save(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    String filename = input.getText().toString();
                    if (filename.length() >0 ){
                        filename += ".btst";
                        BtsTracker btsTracker = (BtsTracker) getApplication();
                        fileManager.exportToFile(MainActivity.this, btsTracker.getBtsManager().getList(),
                                filename);
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect filename. Try again.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        builder.setMessage("Please choose filename:")
                .setPositiveButton("OK", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .setIcon(R.drawable.ic_menu_save)
                .setTitle("Save")
                .show();


    }

    private void reset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    BtsTracker btsTracker = (BtsTracker) getApplication();
                    btsTracker.getBtsManager().reset();

                    fabListener.switchState(MainActivity.this).switchState(MainActivity.this);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, new HomeFragment());
                    fragmentTransaction.commit();
                }
            }
        };

        builder.setMessage("You are going to discard all your data. Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .setIcon(R.drawable.ic_menu_reset)
                .setTitle("Reset")
                .show();
    }
}

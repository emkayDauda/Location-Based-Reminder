package com.apps.emkaydee.dawud07.testgeofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class definedGeos extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status>,
        GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient client;
    Context context = this;
    protected static String Geofence_Message = "";

    Map<String, Locations> GeoMap;
    String[] Locations = {"Home", "Random", "Another Home", "UnderG Gate"};
    double[][] latlon = {{10.58705248, 7.42795363}, {12.5870218, 9.4279314}, {10.58705248, 7.42795363}, {8.16478,4.26768}};
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defined_geos);
        ArrayList<Double> i = new ArrayList<>();





//        ArrayList<Locations> locations = new ArrayList<>();
//        final Locations loc = new Locations ("Home",10.5870,7.4279);
//        locations.add(loc);
//
//        i.add(0, 10.5870);
//        i.add(1, 7.4279);
//
//        GeoMap = new HashMap<>();
//        GeoMap.put("Home", loc);


        createClient();
        client.connect();

        lv = (ListView) findViewById(R.id.listView2);
        CustomAdapter adapter = new CustomAdapter(this, Locations, latlon);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final double latitude = loc.getLatitude();
//                final double longitude = loc.getLongitude();
//                Toast.makeText(definedGeos.this, String.valueOf(loc.getLatitude()), Toast.LENGTH_LONG).show();
//
//
//                final String Geofence_Name = loc.getName();
//                double latitude = x.get(0);
//                double longitude = x.get(1);

                TextView Geofence_Id = (TextView)view.findViewById(R.id.location);
                TextView Geofence_Latitude = (TextView)view.findViewById(R.id.lattext);
                TextView Geofence_Longitude = (TextView)view.findViewById(R.id.lontext);

                final String Geofence_Name = String.valueOf(Geofence_Id.getText());
                final double latitude = Double.parseDouble(String.valueOf(Geofence_Latitude.getText()));
                final double longitude = Double.parseDouble(String.valueOf(Geofence_Longitude.getText()));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add a Message...");
                final EditText input = new EditText(context);
//                input.setText("Remember To: ");
                input.setTextKeepState("Remember To: ");
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                builder.setView(input);
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Geofence_Message = input.getText().toString();

                        Toast.makeText(definedGeos.this, Geofence_Message, Toast.LENGTH_SHORT).show();

                        if (ActivityCompat.checkSelfPermission(definedGeos.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.GeofencingApi.addGeofences(client, listviewGeofenceRequest(Geofence_Name, latitude, longitude), getGeofencePendingIntent())
                                .setResultCallback(definedGeos.this);

                        beginLocationUpdates();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();




            }
        });

    }

    public void createClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected LocationRequest createRequest() {
        return new LocationRequest()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void beginLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, createRequest(), this);
    }



    protected Geofence listviewGeofence(String ID, double latitude, double longitude){
        return new Geofence.Builder()
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setRequestId(ID)
                .setCircularRegion(latitude, longitude, 100)
                .setExpirationDuration(60000)
                .build();
    }

    protected GeofencingRequest listviewGeofenceRequest(String name, double lat, double lon){

        return new GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                            .addGeofence(listviewGeofence(name, lat,lon))
                                    .build();


    }



    private PendingIntent mgeoFencePendingIntent;


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mgeoFencePendingIntent != null) {
            return mgeoFencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
//        intent.putExtra("valueKey", Geofence_Message);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(definedGeos.this, "Client Connected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onResult(@NonNull Status status) {
        if(status.isSuccess()){
            Toast.makeText(definedGeos.this, "Success, geofence added", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(definedGeos.this, "Failure", Toast.LENGTH_SHORT).show();
        }

    }
}


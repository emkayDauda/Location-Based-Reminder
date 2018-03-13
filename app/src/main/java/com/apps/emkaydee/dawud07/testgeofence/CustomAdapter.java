package com.apps.emkaydee.dawud07.testgeofence;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dawud07 on 20-Aug-16.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    String[] Locations = {};
    double[][] latlon = {};
    Context c;
    LayoutInflater inflater;



    public CustomAdapter(Context context,String[] Locations, double[][]latlon) {
        super(context, R.layout.geo_model, Locations);
        this.latlon = latlon;
        this.Locations = Locations;
        this.c = context;
    }

    public class ViewHolder{
        TextView Location,Latitude,Longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.geo_model, null);
        }
        final ViewHolder hold = new ViewHolder();

        hold.Location = (TextView) convertView.findViewById(R.id.location);
        hold.Latitude = (TextView) convertView.findViewById(R.id.lattext);
        hold.Longitude = (TextView) convertView.findViewById(R.id.lontext);



        hold.Location.setText(Locations[position]);
        hold.Latitude.setText(String.valueOf(latlon[position][0]));
        hold.Longitude.setText(String.valueOf(latlon[position][1]));

        return convertView;

    }
}

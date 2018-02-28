package com.jayjung.hikerswatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    String infoString = "";
    TextView info;
    Geocoder geocoder;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);

        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                infoString = "";
                infoString += "Latitude: "+location.getLatitude()+"\n\n";
                infoString += "Longitude: "+location.getLongitude()+"\n\n";
                infoString += "Accuracy: "+location.getAccuracy()+"\n\n";
                infoString += "Altitude: "+location.getAltitude()+"\n\n";
                infoString += "Address: \n";

                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listAddresses.get(0).getSubThoroughfare() != null)
                        infoString += listAddresses.get(0).getSubThoroughfare() + "\n";
                    if (listAddresses.get(0).getThoroughfare() != null)
                        infoString += listAddresses.get(0).getThoroughfare() + "\n";
                    if (listAddresses.get(0).getAdminArea() != null)
                        infoString += listAddresses.get(0).getAdminArea() + "\n";
                    if (listAddresses.get(0).getCountryName() != null)
                        infoString += listAddresses.get(0).getCountryName() + "\n";

                    info.setText(infoString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    infoString += "Not Found";
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Location lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            infoString += "Latitude: "+lastknownLocation.getLatitude()+"\n\n";
            infoString += "Longitude: "+lastknownLocation.getLongitude()+"\n\n";
            infoString += "Accuracy: "+lastknownLocation.getAccuracy()+"\n\n";
            infoString += "Altitude: "+lastknownLocation.getAltitude()+"\n\n";
            infoString += "Address: \n";

            List<Address> listAddresses = null;
            try { // geocode -> reverse geocoding은 주소 없는 경우 에러 많이 남!
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                listAddresses = geocoder.getFromLocation(lastknownLocation.getLatitude(), lastknownLocation.getLongitude(), 1);
                if (listAddresses.get(0).getSubThoroughfare() != null)
                    infoString += listAddresses.get(0).getSubThoroughfare() + "\n";
                if (listAddresses.get(0).getThoroughfare() != null)
                    infoString += listAddresses.get(0).getThoroughfare() + "\n";
                if (listAddresses.get(0).getAdminArea() != null)
                    infoString += listAddresses.get(0).getAdminArea() + "\n";
                if (listAddresses.get(0).getCountryName() != null)
                    infoString += listAddresses.get(0).getCountryName() + "\n";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                infoString += "Not Found";
            }




            info.setText(infoString);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
}

package com.example.admin.gpsapplication;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOCATION_UPDATE_MIN_TIME = 0;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        requestLocationUpdates();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged");
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e(TAG, "onStatusChanged");
        showProvider(s);
        switch (i) {
            case LocationProvider.OUT_OF_SERVICE:
                String outText = s + "圏外になっています。";
                showMessage(outText);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                String temText = "一時的に" + s + "が利用できません。";
                showMessage(temText);
                break;
            case LocationProvider.AVAILABLE:
                if (s.equals(LocationManager.NETWORK_PROVIDER)) {
                    String aveText = s + "利用可能になりました。";
                    showMessage(aveText);
                    requestLocationUpdates();
                }
                break;
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e(TAG, "onProviderEnabled");
        String message = s + "が有効になりました。";
        showMessage(message);
        showProvider(s);
        if (s.equals(LocationManager.NETWORK_PROVIDER)) {
            requestLocationUpdates();
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e(TAG, "onProviderDisabled");
        showProvider(s);
        if (s.equals(LocationManager.NETWORK_PROVIDER)) {
            String message = s + "が無効になりました。";
            showMessage(message);
        }
    }

    private void requestLocationUpdates() {
        Log.e(TAG, "requestLocationUpdates");
        showProvider(LocationManager.NETWORK_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        showNetworkEnabled(isNetworkEnabled);
        if (isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE,
                    this);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                showLocation(location);
            }
        } else {
            String message = "Networkが無効になっています。";
            showMessage(message);
        }
    }

    private void showLocation(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        long time = location.getTime();
        Date date = new Date(time);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        String dateText = dateFormat.format(date);
        TextView longitudeTextView = (TextView) findViewById(R.id.TextView_longitude);
        longitudeTextView.setText("Longitude : " + String.valueOf(longitude));
        TextView latitudeTextView = (TextView) findViewById(R.id.TextView_latitude);
        latitudeTextView.setText("Latitude : " + String.valueOf(latitude));
        TextView timeTextView = (TextView) findViewById(R.id.TextView_time);
        timeTextView.setText("取得時間 : " + dateText);
    }

    private void showMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.TextView_message);
        textView.setText(message);
    }

    private void showProvider(String provider) {
        TextView textView = (TextView) findViewById(R.id.TextView_provider);
        textView.setText("Provider : " + provider);
    }

    private void showNetworkEnabled(boolean isNetworkEnabled) {
        TextView textView = (TextView) findViewById(R.id.TextView_network);
        textView.setText("NetworkEnabled : " + String.valueOf(isNetworkEnabled));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.admin.gpsapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.admin.gpsapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

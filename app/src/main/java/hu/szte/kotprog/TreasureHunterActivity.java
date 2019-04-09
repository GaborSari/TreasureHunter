package hu.szte.kotprog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TreasureHunterActivity extends AppCompatActivity {


    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.locationManager =  (LocationManager) getSystemService(LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                2000,
                10, this.locationListener);

        setContentView(R.layout.activity_treasure_hunter);
        boolean enabled = this.locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {

        }
        else{

        }

        String EXTRA_REPLY = String.valueOf(R.string.EXTRA_REPLY);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.NOGPS));
        setResult(RESULT_OK, replyIntent);
        finish();
    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.i("test",String.valueOf(location.getLatitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


}

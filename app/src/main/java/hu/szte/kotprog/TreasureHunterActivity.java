package hu.szte.kotprog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TreasureHunterActivity extends AppCompatActivity implements LocationListener {


    private LocationManager locationManager;
    private Location treasure = new Location("treasure");
    private Location currentLocation = new Location("currentLocation");
    private double startDistance;

    private RadioButton debug;
    private boolean loaded = false;

    private Spinner metricsSpinner;

    private TextView distanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000,
                3, this);
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 3, this);

        setContentView(R.layout.activity_treasure_hunter);
        boolean enabled = this.locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        }
        this.metricsSpinner =  (Spinner) findViewById(R.id.metricsSpinner);
        this.debug =  (RadioButton) findViewById(R.id.debug);
        this.distanceTextView = (TextView) findViewById(R.id.distanceTextView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();      // notify gc to be called asap
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String EXTRA_REPLY = String.valueOf(R.string.EXTRA_REPLY);

            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.NOGPS));
            setResult(RESULT_OK, replyIntent);
            finish();
        }

    }


    @Override
    public void onLocationChanged(final Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String Text = latitude + " " + longitude;
        if(this.debug.isChecked())
        Toast.makeText(this, Text, Toast.LENGTH_SHORT).show();

        this.currentLocation.setLongitude(location.getLongitude());
        this.currentLocation.setLatitude(location.getLatitude());


        if (!this.loaded) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            this.loaded = true;
        }

        if (this.treasure.getLongitude() != 0 && this.treasure.getLatitude() != 0) {

            double distance = this.currentLocation.distanceTo(this.treasure);

            Log.i("distance", String.valueOf(distance));


            int bottom = android.graphics.Color.argb(255, 0, 0, 0);
            int top;
            if (distance < 5) {
                bottom = android.graphics.Color.argb(255, 255, 0, 0);
                top = android.graphics.Color.argb(255, 255, 0, 0);
                Toast.makeText(this, "You find the treasure!", Toast.LENGTH_LONG).show();
                //this.rndTreasure.setEnabled(true);
            } else {
                top = (int) ((distance / startDistance) * 255);

                if (top > 255)
                    top = 255;
                top = android.graphics.Color.argb(255, 255, top, top);
            }


            View indicator = (View) findViewById(R.id.indicator);
            GradientDrawable drawable = new GradientDrawable(
                    GradientDrawable.Orientation.BOTTOM_TOP, new int[]{bottom, top
            });
            indicator.setBackground(drawable);

            int sd = (int) (this.startDistance);
            int cd = (int) (distance);
            if(this.debug.isChecked())
            this.distanceTextView.setText( "Current distance:" + String.valueOf(cd) + "(" + String.valueOf(sd) + ")");


        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        String EXTRA_REPLY = String.valueOf(R.string.EXTRA_REPLY);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.NOGPS));
        setResult(RESULT_OK, replyIntent);
        finish();
    }


    public void rndTreasure_Click(View view) {
        double multipler = 1;
        String mastext = metricsSpinner.getSelectedItem().toString();
        if(mastext.equals("km")) multipler=1000;
        if(mastext.equals("cm")) multipler=0.01;
        String rad = ((EditText) findViewById(R.id.radius)).getText().toString();

        if(rad.isEmpty()) return;
        double rndRadius = Float.valueOf(rad);
        double radiusInPoint = (multipler*rndRadius) / 111000f;
        double angle = Math.random() * Math.PI * 2;

        this.treasure.setLatitude(this.currentLocation.getLatitude() + Math.cos(angle) * radiusInPoint);
        this.treasure.setLongitude(this.currentLocation.getLongitude() + Math.sin(angle) * radiusInPoint);

        this.startDistance = this.currentLocation.distanceTo(this.treasure);

        //this.rndTreasure = (Button) findViewById(R.id.rndTreasure);
        //this.rndTreasure.setEnabled(false);
        this.onLocationChanged(this.currentLocation);

    }


    public void specTreasure_Click(View view) {

        double longitude = Double.valueOf(((EditText) findViewById(R.id.longitude)).getText().toString());
        double latitude = Double.valueOf(((EditText) findViewById(R.id.latitude)).getText().toString());

        this.treasure.setLatitude(latitude);
        this.treasure.setLongitude(longitude);

        this.startDistance = this.currentLocation.distanceTo(this.treasure);
        this.onLocationChanged(this.currentLocation);

    }



}

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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TreasureHunterActivity extends AppCompatActivity implements LocationListener {


    private LocationManager locationManager;
    private MPoint treasure = new MPoint();
    private MPoint currentLocation = new MPoint();
    private double startDistance;

    private TextView distanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.locationManager =  (LocationManager) getSystemService(LOCATION_SERVICE);
        this.locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,
                1000,
                1, this);

        setContentView(R.layout.activity_treasure_hunter);
        boolean enabled = this.locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        }

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

        if (this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {

        }
        else{
            String EXTRA_REPLY = String.valueOf(R.string.EXTRA_REPLY);

            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, getResources().getString(R.string.NOGPS));
            setResult(RESULT_OK, replyIntent);
            finish();
        }

    }


        @Override
        public void onLocationChanged(final Location location) {

            Log.i("onLocationChanged","onLocationChanged");

           this.currentLocation.setLongitude(location.getLongitude());
           this.currentLocation.setLatitude(location.getLatitude());

        Log.i("treasure unset",this.treasure.unset()? "true" : "false");
           if(!this.treasure.unset()){



               double distance = this.calculateDistanceBetweenPoints(this.currentLocation.getLatitude(),this.currentLocation.getLongitude(),this.treasure.getLatitude(),this.treasure.getLongitude());

               Log.i("distance",String.valueOf(distance*1000));

               int bottom = 0;
               int top = 0;
               if(distance < (0.00001 * 5) ){
                   bottom = android.graphics.Color.argb(255,255, 30, 0);
                   top = android.graphics.Color.argb(255,150, 0, 0);
                   Toast.makeText(this, "You find the treasure!", Toast.LENGTH_LONG).show();
               }

               if(distance < (0.00001 * 25) ){
                   bottom = android.graphics.Color.argb(255,227, 83, 48);
                   top = android.graphics.Color.argb(255,192, 56, 49);
               }

               if(distance < (0.00001 * 50)){
                   bottom = android.graphics.Color.argb(255,227, 109, 81);
                   top = android.graphics.Color.argb(255,227, 124, 94);
               }

               if(distance < (0.00001 * 100) )
               {

                   bottom = android.graphics.Color.argb(255,227, 158, 138);
                   top = android.graphics.Color.argb(255,227, 146, 126);
               }



               if(distance >= (0.00001 * 100) )
               {
                   bottom = android.graphics.Color.argb(255,156, 196, 182);
                   top = android.graphics.Color.argb(255,198, 196, 182);
               }


               View indicator = (View) findViewById(R.id.indicator);
               GradientDrawable drawable = new GradientDrawable(
                       GradientDrawable.Orientation.BOTTOM_TOP, new int[] { bottom, top
               });
               indicator.setBackground(drawable);
                int d = (int)(this.startDistance*100000000);
                int c = (int) (distance * 100000000);
               this.distanceTextView.setText(String.valueOf(d) + " / " +String.valueOf(c));



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


    public void rndTreasure_Click(View view){
        Log.i("currentLocation.unset",this.currentLocation.unset()? " true":"false");
        if(this.currentLocation.unset())  {
            Toast.makeText(this, "No GPS information yet!", Toast.LENGTH_SHORT).show(); return;
        }


        String rad = ((EditText) findViewById(R.id.radius)).getText().toString();
        double rndRadius = Float.valueOf(rad);


        double radiusInPoint = 0.00001 * rndRadius;
        double angle = Math.random()*Math.PI*2;

        this.treasure.setLatitude(this.currentLocation.getLatitude()  + Math.cos(angle)*radiusInPoint);
        this.treasure.setLongitude(this.currentLocation.getLongitude()  + Math.sin(angle)*radiusInPoint);


        this.startDistance = this.calculateDistanceBetweenPoints(this.currentLocation.getLatitude(),this.currentLocation.getLongitude(),this.treasure.getLatitude(),this.treasure.getLongitude());


    }


    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

}

package hu.szte.kotprog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int START_TREASURE = 1;
    public static final int PERMISSION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, this.PERMISSION);
        }

    }

    public void start(View view) {
        Intent intent = new Intent(this, TreasureHunterActivity.class);
        startActivityForResult(intent, START_TREASURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (START_TREASURE):
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this, data.getStringExtra(String.valueOf(R.string.EXTRA_REPLY)), Toast.LENGTH_LONG).show();
                }
                break;
            case (PERMISSION): {
                Toast.makeText(MainActivity.this, String.valueOf(resultCode), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case (PERMISSION): {
                if (grantResults[0] == -1) {
                    Toast.makeText(MainActivity.this, R.string.NO_PERMISSION, Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        }
    }

}

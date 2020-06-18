package com.ridesharedj.ridesharedj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button spotify_login_button = (Button) findViewById(R.id.Login_button);
        spotify_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpotifyLogin();
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
                //do nothing
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
            }
        }
    }

    private void openSongEditor() {
        Intent intent = new Intent(this, SongEditor.class);
        startActivity(intent);
    }

    public void openSpotifyLogin() {
        Intent intent = new Intent(this, Spotify_login.class);
        startActivity(intent);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thankyou for permitting", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Well i can't do anything unless you permit", Toast.LENGTH_LONG).show();
                }
        }
    }
}


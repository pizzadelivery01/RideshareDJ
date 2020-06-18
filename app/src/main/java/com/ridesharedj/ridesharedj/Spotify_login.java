package com.ridesharedj.ridesharedj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.util.Objects;

public class Spotify_login extends AppCompatActivity {
    private static final String CLIENT_ID = "ac025b4e630a4f6383c8e333570e29b2";
    private static final String REDIRECT_URI = "com.ridesharedj.ridesharedj://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private,user-modify-playback-state,user-read-playback-state,user-read-playback-position,playlist-modify-public,playlist-modify-private";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);


        authenticateSpotify();


    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }
    @SuppressLint("ApplySharedPref")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    String auth = response.getAccessToken();
                    Log.d("token", auth);
                    SharedPreferences.Editor editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.commit();
                    Intent intent1 = new Intent(this, SongEditor.class);
                    startActivity(intent1);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.d("error", "error case handled");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.d("default", "default case handled");
            }
        }
    }

}
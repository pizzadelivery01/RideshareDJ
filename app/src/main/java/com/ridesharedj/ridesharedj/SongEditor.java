package com.ridesharedj.ridesharedj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ridesharedj.ridesharedj.models.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SongEditor<StableArrayAdapter> extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_editor);
        registerReceiver(broadcastReceiver, new IntentFilter("SMS_INTENT"));

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            String msg = bundle.getString("smsText");
            Log.d("newmessage", "" + msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

            SharedPreferences preferences = context.getSharedPreferences("SPOTIFY",0);
            String auth = preferences.getString("token", "DEFAULT");
            final String token = "Bearer " + auth;

            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                   Request original = chain.request();
                   Request request;

                   request = original.newBuilder()
                           .header("Authorization", token)
                           .header("Accept", "application/json")
                           .header("Content-Type", "application/json")
                           .method(original.method(), original.body())
                           .build();
                   Log.d("TokenActual", token);
                   return chain.proceed(request);

                }
            });

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://api.spotify.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpBuilder.build());

            Retrofit retrofit = builder.build();
            spotifyapi client = retrofit.create(spotifyapi.class);
            Call<Song> call = client.searchtrack(msg,"track");

            call.enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, Response<Song> response) {
                    Log.d("call request", call.request().toString());
                    Log.d("call request header", call.request().headers().toString());
                    Log.d("Response raw header", response.headers().toString());
                    Log.d("Response raw", String.valueOf(response.raw().body()));
                    Log.d("Response code", String.valueOf(response.code()));

                    Song responseBody =  response.body();
                    String responsebodystring =  responseBody.toString();
                    Log.d("Response body", responsebodystring);
                    if (response.isSuccessful()) {
                        Log.d("Success body response", response.body().toString());
                        Song songs = response.body();
                        Toast.makeText(context, "api search worked succesfully", Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<Song> call, Throwable t) {
                    Toast.makeText(context, "api search failed", Toast.LENGTH_LONG).show();
                }
            });

        }
    };

    }



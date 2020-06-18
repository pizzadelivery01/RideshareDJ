package com.ridesharedj.ridesharedj;

import com.ridesharedj.ridesharedj.models.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface spotifyapi {

    @GET("/v1/search")

    Call<Song> searchtrack(
            @Query("q") String q ,
            @Query("type") String track);
}


package com.example.andreea.bookhunt.retrofitUtils;

import com.example.andreea.bookhunt.retrofitUtils.model.GoodreadsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodreadsAPI {
    @GET("title.xml")
    Call<GoodreadsResponse> getGoodreadsResponse(@Query("key") String key,
                                                 @Query("title") String title,
                                                 @Query("author") String author);
}

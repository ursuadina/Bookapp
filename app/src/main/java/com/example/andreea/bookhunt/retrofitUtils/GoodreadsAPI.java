package com.example.andreea.bookhunt.retrofitUtils;

import com.example.andreea.bookhunt.retrofitUtils.modelAuthor.AuthorResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GoodreadsAPI {
    @GET("book/title.xml")
    Call<GoodreadsResponse> getGoodreadsResponse(@Query("key") String key,
                                                 @Query("title") String title,
                                                 @Query("author") String author);

    @GET("api/author_url/{name}")
    Call<AuthorResponse> getAuthorResponse(@Path("name") String name,
                                           @Query("key") String key);
}

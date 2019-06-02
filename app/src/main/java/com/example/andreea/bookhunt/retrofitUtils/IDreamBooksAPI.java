package com.example.andreea.bookhunt.retrofitUtils;

import com.example.andreea.bookhunt.retrofitUtils.modelGoodReads.GoodreadsResponse;
import com.example.andreea.bookhunt.retrofitUtils.modelIDreamBooks.IDreamBooksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IDreamBooksAPI {
    @GET("reviews.xml")
    Call<IDreamBooksResponse> getIDreamBooksResponse(@Query("q") String titleAuthor,
                                                     @Query("key") String key);
}

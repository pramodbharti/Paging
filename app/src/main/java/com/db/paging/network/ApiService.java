package com.db.paging.network;

import com.db.paging.model.GithubResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    //https://api.github.com/search/repositories?q=android&page=1&per_page=10

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/search/repositories")
    Call<GithubResponse> getSearchResults(
            @Query("q") String searchQuery,
            @Query("page") int page,
            @Query("per_page") int per_page
    );
}

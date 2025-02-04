package com.foxstoncold.githubviewer.data

import com.foxstoncold.githubviewer.sl
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class GHApi(private val retrofit: Retrofit) {
    init {
        sl.en()
    }
    private val service: GHApiService = retrofit.create(GHApiService::class.java)

    //region request functions

    fun searchUsers(query: String) = flyCatching(
        { service.searchUsers(query) },
        { it.items }
    )

    fun searchRepos(query: String) = flyCatching(
        { service.searchRepos(query) },
        { it.items }
    )

    //endregion


}

interface GHApiService{
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") query: String
    ): Response<SearchResponseWrapper<UserData>>

    @GET("/search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String
    ): Response<SearchResponseWrapper<RepoData>>
}
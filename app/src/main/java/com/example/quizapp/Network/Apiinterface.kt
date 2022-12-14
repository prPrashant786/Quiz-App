package com.example.quizapp.Network


import com.example.quizapp.Model.ListItem
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface Apiinterface {

    @GET("/api/v1/questions?")
    fun getquestion(
        @Query("apiKey") appid : String?,
        @Query("category") cat : String?,
        @Query("difficulty") diff : String?,
        @Query("limit") lim : Int,
    ) : Call<ArrayList<ListItem>>

}
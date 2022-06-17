package com.jarvizu.listit.api

import com.jarvizu.listit.data.CommentObject
import com.jarvizu.listit.data.ListObject
import com.jarvizu.listit.data.RedditResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    // Set limit for pagination
    @GET("/r/all/new.json?limit=300")
    suspend fun getList(): ListObject

    @GET("{post}/.json")
    suspend fun getComments(@Path("post")post : String): CommentObject
}
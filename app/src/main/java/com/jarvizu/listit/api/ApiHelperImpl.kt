package com.jarvizu.listit.api

import com.jarvizu.listit.data.CommentObject
import com.jarvizu.listit.data.ListObject
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
):ApiHelper{
    override suspend fun getList(): ListObject = apiService.getList()
    override suspend fun getComments(post : String): CommentObject = apiService.getComments(post)
}
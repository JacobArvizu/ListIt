package com.jarvizu.listit.data.local

import com.jarvizu.listit.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
){
    suspend fun getList() = apiHelper.getList()
    suspend fun getComments(post : String) = apiHelper.getComments(post)

}
package com.jarvizu.listit.api

import com.jarvizu.listit.data.CommentObject
import com.jarvizu.listit.data.ListObject

interface ApiHelper {
    suspend fun getList(): ListObject
    suspend fun getComments(post: String): CommentObject
}
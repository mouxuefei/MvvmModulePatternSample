package com.scheartmed.lib_im.data.api

import com.mou.network.bean.BaseBean
import com.scheartmed.lib_im.data.model.ChatBean
import retrofit2.http.GET

interface ChatService {

    /**
     * 判断是否上线
     */
    @GET("article/list/1/json")
    suspend fun getArticle(): BaseBean<ChatBean>
}
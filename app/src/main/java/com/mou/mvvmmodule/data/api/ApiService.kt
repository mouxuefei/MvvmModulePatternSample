package com.mou.mvvmmodule.data.api

import com.fortunes.commonsdk.api.bean.BaseBean
import com.mou.mvvmmodule.data.model.ArticleBean
import retrofit2.http.GET

interface ApiService {

    /**
     * 判断是否上线
     */
    @GET("article/list/1/json")
    suspend fun getArticle(): BaseBean<ArticleBean>
}
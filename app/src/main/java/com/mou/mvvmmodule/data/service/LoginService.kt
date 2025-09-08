package com.mou.mvvmmodule.data.service

import com.mou.mvvmmodule.data.model.ArticleBean
import com.core.network.bean.BaseBean
import retrofit2.http.GET

interface LoginService {

    /**
     * 判断是否上线
     */
    @GET("article/list/1/json")
    suspend fun getArticle(): BaseBean<ArticleBean>
}
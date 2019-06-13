package com.mou.mine.mvvm.model.repository

import com.fortunes.commonsdk.network.bean.BaseBean
import com.mou.mine.mvvm.model.data.MineBean
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 */

interface ApiService {
    @GET("project/list/{pageNum}/json")
    fun getProjectList(@Path("pageNum") pageNum: Int, @Query("cid") cid: Int):Single<BaseBean<MineBean>>
}

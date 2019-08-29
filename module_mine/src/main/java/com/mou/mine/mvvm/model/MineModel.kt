package com.mou.mine.mvvm.model

import com.mou.basemvvm.base.BaseModel
import com.mou.mine.http.MineApiManager

/**
 * @FileName: MineModel.java
 * @author: villa_mou
 * @date: 08-11:12
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class MineModel:BaseModel() {
    fun getProjectList(pageNum:Int,cid:Int)=MineApiManager.apiService.getProjectList(pageNum,cid)
}
package com.fortunes.commonsdk.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @auther:MR-Cheng
 * @date: 2019/1/2
 * @description:  统一查询个人信息 common/userInfo/queryUserInfo
 * @parameter:
 */
@Parcelize
data class UserInfoBean(
        @SerializedName("mobile") val mobile: String = "",
        @SerializedName("name") val name: String = ""
) : Parcelable


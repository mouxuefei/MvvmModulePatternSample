package com.core.commonsdk.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SpUtil {

    private const val SP_NAME = "AppSharedPreferences"
    private lateinit var sp: SharedPreferences
    val gson = Gson()

    /**
     * 在 Application.onCreate 初始化
     */
    fun init(context: Context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String?) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sp.getString(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sp.getInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defaultValue)
    }

    fun putFloat(key: String, value: Float) {
        sp.edit().putFloat(key, value).apply()
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sp.getFloat(key, defaultValue)
    }

    fun putLong(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sp.getLong(key, defaultValue)
    }

    fun remove(key: String) {
        sp.edit().remove(key).apply()
    }

    fun clear() {
        sp.edit().clear().apply()
    }

    /**
     * 存对象
     */
    fun <T> putObject(key: String, value: T) {
        val json = gson.toJson(value)
        putString(key, json)
    }

    /**
     * 取对象
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val json = getString(key, null)
        return if (json.isNullOrEmpty()) null else gson.fromJson(json, clazz)
    }

    /**
     * 存集合
     */
    fun <T> putList(key: String, list: List<T>) {
        val json = gson.toJson(list)
        putString(key, json)
    }

    /**
     * 取集合
     */
    inline fun <reified T> getList(key: String): List<T>? {
        val json = getString(key, null) ?: return null
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }
}
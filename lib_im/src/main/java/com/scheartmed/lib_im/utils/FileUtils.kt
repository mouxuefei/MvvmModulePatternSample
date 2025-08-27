package com.scheartmed.lib_im.utils

import com.mou.basemvvm.BaseApplication
import java.io.File




/**
 * @FileName: FileUtils.java
 * @author: villa_mou
 * @date: 08-09:56
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
object FileUtils {
    private const val VOICE_DIR_SUFFIX = "/voice/"
    private const val IMAGE_DIR_SUFFIX = "/image/"

    private fun defaultAppDir(): String {
        return BaseApplication.instance().filesDir.absolutePath
    }

    fun getImageCachePath(): String {
        val file = File(defaultAppDir() + IMAGE_DIR_SUFFIX)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    fun getVoiceCachePath(): String {
        val file = File(defaultAppDir() + VOICE_DIR_SUFFIX)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

}
package com.scheartmed.lib_im.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.mou.basemvvm.BaseApplication
import java.io.File
import java.io.FileOutputStream


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
    private const val VIDEO_DIR_SUFFIX = "/video/"
    private const val FILE_DIR_SUFFIX = "/file/"

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

    fun getVideoCachePath(): String {
        val file = File(defaultAppDir() + VIDEO_DIR_SUFFIX)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    fun getFileCachePath(): String {
        val file = File(defaultAppDir() + FILE_DIR_SUFFIX)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath
    }

    fun copyVideoToCache(context: Context, uri: Uri): File? {
        return try {
            val fileName = queryDisplayName(context, uri) ?: "${System.currentTimeMillis()}.mp4"
            val file = File(getVideoCachePath(), fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                return cursor.getString(index)
            }
        }
        return null
    }

    fun createVideoThumb(context: Context, file: File): File {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)
        val bitmap = retriever.getFrameAtTime(0)
        retriever.release()

        val thumbFile = File(getVideoCachePath(), "${System.currentTimeMillis()}_thumb.jpg")
        FileOutputStream(thumbFile).use { fos ->
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        }
        return thumbFile
    }


    fun getExtensionName(filePath: String): String {
        val file = File(filePath)
        return getExtensionName(file)
    }

    private fun getExtensionName(file: File): String {
        val fileName = file.getName()
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }
}
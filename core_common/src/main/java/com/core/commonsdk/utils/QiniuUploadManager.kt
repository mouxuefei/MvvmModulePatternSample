package com.core.commonsdk.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * val file = File("/sdcard/photo.jpg")
 * val token = "服务端下发的token"
 * val domain = "http://yourdomain.com"
 *
 * // 上传图片，压缩开启
 * QiniuUploadManager.upload(context, file, token, domain, compressImage = true, object : QiniuUploadManager.UploadCallback {
 *     override fun onProgress(percent: Double) { println("上传进度: $percent") }
 *     override fun onSuccess(fileUrl: String) { println("上传成功: $fileUrl") }
 *     override fun onFailure(error: String) { println("上传失败: $error") }
 * })
 *
 * // 上传大文件（自动断点续传）
 * val videoFile = File("/sdcard/large_video.mp4")
 * QiniuUploadManager.upload(context, videoFile, token, domain, callback = object : QiniuUploadManager.UploadCallback {
 *     override fun onProgress(percent: Double) { println("上传进度: $percent") }
 *     override fun onSuccess(fileUrl: String) { println("上传成功: $fileUrl") }
 *     override fun onFailure(error: String) { println("上传失败: $error") }
 * })
 */
object QiniuUploadManager {

    private const val chunkSize = 10 * 1024 * 1024 // 分片大小 512 KB

    interface UploadCallback {
        fun onProgress(percent: Double)
        fun onSuccess(fileUrl: String)
        fun onFailure(error: String)
    }

    // 生成唯一上传 key
    private fun generateUploadKey(originalName: String): String {
        val date = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date())
        val uuid = UUID.randomUUID().toString().replace("-", "")
        val ext = originalName.substringAfterLast('.', "")
        return if (ext.isNotEmpty()) "$date/$uuid.$ext" else "$date/$uuid"
    }

    // 自动识别文件类型并上传，可选择图片压缩
    fun upload(
        context: Context,
        file: File,
        uploadToken: String,
        domain: String,
        compressImage: Boolean = true,
        callback: UploadCallback
    ) {
        val mimeType = getMimeType(file)
        when {
            mimeType.startsWith("image") -> uploadImage(
                file,
                uploadToken,
                domain,
                compressImage,
                callback
            )

            mimeType.startsWith("video") || file.length() > chunkSize -> uploadLargeFile(
                file,
                uploadToken,
                domain,
                callback,
                context
            )

            else -> uploadFile(file.absolutePath, uploadToken, domain, callback)
        }
    }

    private fun getMimeType(file: File): String {
        val name = file.name.lowercase(Locale.getDefault())
        return when {
            name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(
                ".bmp"
            ) -> "image"

            name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".avi") -> "video"
            else -> "file"
        }
    }

    private fun uploadFile(
        filePath: String,
        uploadToken: String,
        domain: String,
        callback: UploadCallback
    ) {
        val key = generateUploadKey(File(filePath).name)
        val config = Configuration.Builder()
            .putThreshold(chunkSize)
            .useConcurrentResumeUpload(true)   // 开启分片上传
            .resumeUploadVersion(Configuration.RESUME_UPLOAD_VERSION_V2) // 使用分片 V2
//            .chunkSize(chunkSize)
            .connectTimeout(10)
            .responseTimeout(60)
            .buildV2()

        val manager = UploadManager(config)
        manager.put(filePath, key, uploadToken, { _, info, response ->
            handleResponse(info, response, domain, callback)
        }, UploadOptions(null, null, false, { _, percent -> callback.onProgress(percent) }, null))
    }

    private fun uploadImage(
        file: File,
        uploadToken: String,
        domain: String,
        compressImage: Boolean,
        callback: UploadCallback
    ) {
        val key = generateUploadKey(file.name)
        val data: ByteArray = if (compressImage) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            baos.toByteArray()
        } else {
            file.readBytes()
        }

        val config = Configuration.Builder()
            .putThreshold(chunkSize)
            .useConcurrentResumeUpload(true)   // 开启分片上传
            .connectTimeout(10)
            .resumeUploadVersion(Configuration.RESUME_UPLOAD_VERSION_V2) // 使用分片 V2
            .responseTimeout(60)
            .build()

        val manager = UploadManager(config)
        manager.put(data, key, uploadToken, { _, info, response ->
            handleResponse(info, response, domain, callback)
        }, UploadOptions(null, null, false, { _, percent -> callback.onProgress(percent) }, null))
    }

    private fun uploadLargeFile(
        file: File,
        uploadToken: String,
        domain: String,
        callback: UploadCallback,
        context: Context
    ) {
        val recorder = FileRecorder(FileUtils.getFileCachePath())
        val key = generateUploadKey(file.name)

        val config = Configuration.Builder()
            .putThreshold(chunkSize)
            .useConcurrentResumeUpload(true)   // 开启分片上传
            .connectTimeout(10)
            .resumeUploadVersion(Configuration.RESUME_UPLOAD_VERSION_V2) // 使用分片 V2
            .responseTimeout(60)
            .recorder(recorder, null)
            .buildV2()

        val manager = UploadManager(config)
        manager.put(file.absolutePath, key, uploadToken, { _, info, response ->
            handleResponse(info, response, domain, callback)
        }, UploadOptions(null, null, false, { _, percent -> callback.onProgress(percent) }, null))
    }

    private fun handleResponse(
        info: ResponseInfo,
        response: JSONObject?,
        domain: String,
        callback: UploadCallback
    ) {
        if (info.isOK && response != null) {
            val key = response.getString("key")
            val fileUrl = "$domain/$key"
            callback.onSuccess(fileUrl)
        } else {
            callback.onFailure(info.error)
        }
    }
}
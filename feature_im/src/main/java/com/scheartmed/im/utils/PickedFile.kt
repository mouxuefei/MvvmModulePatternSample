package com.scheartmed.im.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.database.Cursor
import android.provider.MediaStore
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.core.commonsdk.utils.FileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


data class PickedFile(
    val uri: Uri,
    val name: String?,
    val size: Long,
    var file: File? = null,
)

class FilePickerAndSender(caller: ActivityResultCaller, private val context: Context) {

    private var callback: ((PickedFile) -> Unit)? = null

    private val pickFileLauncher: ActivityResultLauncher<Array<String>> =
        caller.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                val picked = uriToPickedFile(it)
                CoroutineScope(Dispatchers.Main).launch { // 主线程
                    val result = withContext(Dispatchers.IO) { // 切换到 IO
                        getFileFromUri(context, picked.uri)
                    }
                    picked.file = result
                    callback?.invoke(picked)
                }
            }
        }

    fun pickOfficeOrPdfFile(callback: (PickedFile) -> Unit) {
        this.callback = callback
        val mimeTypes = arrayOf(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        )
        pickFileLauncher.launch(mimeTypes)
    }

    private fun uriToPickedFile(uri: Uri): PickedFile {
        var name: String? = null
        var size: Long = 0

        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                if (nameIndex != -1) name = it.getString(nameIndex)
                if (sizeIndex != -1) size = it.getLong(sizeIndex)
            }
        }

        return PickedFile(uri, name, size)
    }

    /**
     * 获取文件路径，如果无法获取返回null
     * targetSdk 34 适配，兼容各类 content:// uri
     */
    private fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            if ("file" == uri.scheme) {
                return File(uri.path ?: return null)
            }
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = queryFileName(context, uri) ?: "temp_file"
            val cacheFile = File(FileUtils.getFileCachePath(), fileName)
            copyInputStreamToFile(inputStream, cacheFile)
            cacheFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun queryFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = it.getString(index)
                }
            }
        }
        return name
    }

}
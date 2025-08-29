package com.scheartmed.lib_im.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.database.Cursor
import android.widget.Toast
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


data class PickedFile(
    val uri: Uri,
    val name: String?,
    val size: Long
)

class FilePickerAndSender(caller: ActivityResultCaller, private val context: Context) {

    private var callback: ((PickedFile) -> Unit)? = null

    private val pickFileLauncher: ActivityResultLauncher<Array<String>> =
        caller.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                val picked = uriToPickedFile(it)
                callback?.invoke(picked)
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

//    /**
//     * 发送文件消息
//     * sessionId: 对方账号或群ID
//     * sessionType: 单聊 or 群聊
//     */
//    fun sendFileMessage(
//        picked: PickedFile,
//        sessionId: String,
//        sessionType: V2NIMSessionType = V2NIMSessionType.P2P
//    ) {
//        val attachment = V2NIMFileAttachment(context, picked.uri)
//        attachment.displayName = picked.name ?: "file"
//
//        val message = V2NIMMessage.createFileMessage(sessionId, sessionType, attachment)
//
//        NIMClient.getService(V2NIMMessageService::class.java)
//            .sendMessage(message)
//            .observe { result ->
//                if (result.isSuccess()) {
//                    Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context, "发送失败: ${result.error}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
}
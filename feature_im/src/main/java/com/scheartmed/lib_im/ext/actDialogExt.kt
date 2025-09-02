package com.scheartmed.lib_im.ext

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.core.commonsdk.view.dialog.TipDialogEntity
import com.core.commonsdk.view.dialog.TipDialogType
import com.core.commonsdk.view.dialog.showTipDialog
import com.scheartmed.lib_im.R

/**
 * 重新发送
 */
fun AppCompatActivity.retrySendMessageDialog(listener: View.OnClickListener? = null) {
    val entity = TipDialogEntity(
        fragmentManager = supportFragmentManager,
        title = "发送失败",
        desc = "是否重发该消息？",
        type = TipDialogType.Info,
        buttonRightTitle = "重试",
        buttonRightColor = ContextCompat.getColor(this, R.color.purple_700),
        buttonRightClickListener = listener,
        buttonLeftTitle = "取消",
        buttonLeftColor = ContextCompat.getColor(this, R.color.purple_700),
    )
    showTipDialog(entity)
}

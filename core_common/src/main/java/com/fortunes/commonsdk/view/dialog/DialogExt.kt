package com.fortunes.commonsdk.view.dialog

import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import com.fortunes.commonsdk.R

/**
 * @FileName: DialogExt.java
 * @author: villa_mou
 * @date: 10-09:27
 * @version V1.0 <描述当前版本功能>
 * @desc
 */

enum class TipDialogType {
    Error, Info, Success
}

class TipDialogEntity(
    val fragmentManager: FragmentManager,
    val title: String? = null,
    val desc: String? = null,
    val type: TipDialogType? = null,
    val buttonLeftTitle: String? = null,
    val buttonRightTitle: String? = null,
    val buttonLeftColor: Int? = null,
    val buttonRightColor: Int? = null,
    val buttonLeftClickListener: View.OnClickListener? = null,
    val buttonRightClickListener: View.OnClickListener? = null,
)


fun showTipDialog(entity: TipDialogEntity) {
    mDialog(entity.fragmentManager) {
        layoutId = R.layout.dialog_tip
        margin = 40
        outCancel = true
        viewClick = object : DialogListener() {
            override fun convertView(view: View, dialog: NiceDialog) {
                entity.title?.let {
                    val tv = view.getView<TextView>(R.id.dialog_tip_title)
                    tv.text = it
                }
                entity.desc?.let {
                    val tv = view.getView<TextView>(R.id.dialog_tip_desc)
                    tv.text = it
                }
                val right = view.getView<LinearLayout>(R.id.button_right)
                val line = view.getView<View>(R.id.dialog_tip_line)
                line.visibility = if (entity.buttonRightTitle == null) View.GONE else View.VISIBLE
                right.visibility = if (entity.buttonRightTitle == null) View.GONE else View.VISIBLE
                entity.buttonRightTitle?.let {
                    val tv = view.getView<TextView>(R.id.button_right_title)
                    tv.text = it
                    entity.buttonRightColor?.let { color ->
                        tv.setTextColor(color)
                    }
                }
                entity.buttonLeftTitle?.let {
                    val tv = view.getView<TextView>(R.id.button_Left_title)
                    tv.text = it
                    entity.buttonLeftColor?.let { color ->
                        tv.setTextColor(color)
                    }
                }
                view.getView<LinearLayout>(R.id.button_left).setOnClickListener { v ->
                    entity.buttonLeftClickListener?.onClick(v)
                    dialog.dismiss()
                }
                view.getView<LinearLayout>(R.id.button_right).setOnClickListener { v ->
                    entity.buttonRightClickListener?.onClick(v)
                    dialog.dismiss()
                }
                val icon = view.getView<ImageView>(R.id.dialog_tip_icon)

            }
        }
    }
}





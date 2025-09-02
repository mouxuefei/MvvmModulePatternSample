package com.fortunes.commonsdk.binds

import android.app.Dialog
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import java.util.*

/***
 * You may think you know what the following code does.
 * But you dont. Trust me.
 * Fiddle with it, and youll spend many a sleepless
 * night cursing the moment you thought youd be clever
 * enough to "optimize" the code below.
 * Now close this file and go play with something else.
 */
/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/10/27.


 */

/**
 * 简化时间选择dialog的build代码
 */
fun TimePickerBuilder.buildBottom(showDay: Boolean = false,year:Int = 2008,month:Int = 0 ,day:Int = 1): TimePickerView {
    val startDate = Calendar.getInstance()
    val endDate = Calendar.getInstance()
    //设置选择起始日期
    startDate.set(year, month, day)

    //设置选择结束日期
    endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DATE))
    val timePickerView = this
            //设置是否显示年/月/日/时/分/秒
            .setType(booleanArrayOf(true, true, showDay, false, false, false))
            //是否显示为对话框样式
            .isDialog(true)
            //设置两横线之间的间隔倍数
            .setLineSpacingMultiplier(2.0f)
            //起始终止年月日设定
            .setRangDate(startDate, endDate)
            .build()
    return timePickerView.setShowBottom()
}

/**
 * 时间选择dialog从底部弹出
 */
fun TimePickerView.setShowBottom(): TimePickerView {
    val mDialog: Dialog = this.dialog
    val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM)
    params.leftMargin = 0
    params.rightMargin = 0
    this.dialogContainerLayout.layoutParams = params
    val dialogWindow = mDialog.window
    dialogWindow?.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
    dialogWindow?.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
    return this
}


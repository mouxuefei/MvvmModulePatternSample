package com.mou.basemvvm.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.mou.basemvvm.widget.dialog.CommonDialog
import com.mou.easymvvm.R

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
 * Created by mou on 2018/12/22.
 */
@SuppressLint("InflateParams")
class LoadDialog private constructor(private val context: Context) {
    private val mCommonDialog by lazy {
        CommonDialog.Builder(context)
                .setContentView(contentView)
                .setCancelableOnTouchOutside(false)
                .create()
    }

    private val contentView by lazy {
        LayoutInflater.from(context).inflate(R.layout.public_dialog_load, null)
    }

    private val titleTv by lazy {
        contentView.findViewById<TextView>(R.id.dialog_tv)
    }

    fun show() {
        if (mCommonDialog.isShowing) return
        mCommonDialog.show()
    }

    fun setMessage(message: String) {
        titleTv.text = message
    }

    fun dismiss() {
        if (!mCommonDialog.isShowing) return
        mCommonDialog.dismiss()
    }

    companion object {
        fun create(context: Context): LoadDialog {
            return LoadDialog(context)
        }
    }
}
package com.fortunes.commonsdk.view.dialog

import android.os.Parcel
import android.os.Parcelable
import android.view.View

/**
 * @FileName: DialogListner.java
 * @author: villa_mou
 * @date: 10-17:12
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
abstract class DialogListener : Parcelable {

    abstract fun convertView(view: View, dialog: NiceDialog)

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    constructor() {}

    protected constructor(`in`: Parcel) {}

    companion object {
        val CREATOR: Parcelable.Creator<DialogListener> = object : Parcelable.Creator<DialogListener> {
            override fun createFromParcel(source: Parcel): DialogListener {
                return object : DialogListener(source) {
                    override fun convertView(view: View, dialog: NiceDialog) {

                    }
                }
            }

            override fun newArray(size: Int): Array<DialogListener?> {
                return arrayOfNulls(size)
            }
        }
    }
}

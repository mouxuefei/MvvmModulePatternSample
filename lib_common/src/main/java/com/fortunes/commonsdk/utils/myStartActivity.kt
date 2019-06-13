package com.fortunes.commonsdk.utils

import android.app.Activity
import android.content.Intent

inline fun <reified A : Activity> Activity.myStartActivity(vararg args: Pair<String, Any>, requestCode: Int = -1) {
    val intent = Intent(this, A::class.java)
    for (arg in args) {
        if (arg.second is Boolean) {
            intent.putExtra(arg.first, arg.second as Boolean)
        } else if (arg.second is Int) {
            intent.putExtra(arg.first, arg.second as Int)
        } else {
            intent.putExtra(arg.first, arg.second.toString())
        }

    }
    if (requestCode > 0) {
        this.startActivityForResult(intent, requestCode)
    } else {
        this.startActivity(intent)
    }
}
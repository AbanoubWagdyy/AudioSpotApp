package com.audiospotapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.TextView
import com.audiospotapplication.R

object DialogUtils {

    private var dialog: AlertDialog? = null

    @Synchronized
    fun showProgressDialog(
        context: Context?,
        message: String?
    ) {
        if (dialog == null || !dialog!!.isShowing) {
            val builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setView(R.layout.layout_loading_dialog)
            dialog = builder.create()
        }
        dialog!!.show()

        val tvLoadingMessage = dialog!!.findViewById<TextView>(R.id.loading)
        tvLoadingMessage.text = message
    }

    @get:Synchronized
    private val isProgressShowing: Boolean
        get() = if (dialog == null) false else dialog!!.isShowing

    @Synchronized
    fun dismissProgressDialog() {
        if (isProgressShowing) dialog!!.dismiss()
    }
}
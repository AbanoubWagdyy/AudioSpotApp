package com.audiospotapplication

import android.content.Context
import com.example.jean.jcplayer.JcPlayerManager

import java.util.Date

class CallReceiver : PhonecallReceiver() {

    private var jcPlayerManager: JcPlayerManager? = null

    override fun onIncomingCallReceived(ctx: Context, number: String, start: Date) {
        pauseAudio(ctx)
    }

    override fun onIncomingCallAnswered(ctx: Context, number: String, start: Date) {
        pauseAudio(ctx)
    }

    override fun onIncomingCallEnded(ctx: Context, number: String, start: Date, end: Date) {
        continueAudio(ctx)
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String, start: Date) {
        pauseAudio(ctx)
    }

    override fun onOutgoingCallEnded(ctx: Context, number: String, start: Date, end: Date) {
        jcPlayerManager = JcPlayerManager.getInstance(ctx).get()!!
        if (jcPlayerManager!!.isPaused()) {
            jcPlayerManager!!.continueAudio()
        }
    }

    override fun onMissedCall(ctx: Context, number: String, start: Date) {

    }

    private fun pauseAudio(ctx: Context) {
        jcPlayerManager = JcPlayerManager.getInstance(ctx).get()!!
        if (jcPlayerManager!!.isPlaying()) {
            jcPlayerManager!!.pauseAudio()
        }
    }

    private fun continueAudio(ctx: Context) {
        jcPlayerManager = JcPlayerManager.getInstance(ctx).get()!!
        if (jcPlayerManager!!.isPaused()) {
            jcPlayerManager!!.continueAudio()
        }
    }
}
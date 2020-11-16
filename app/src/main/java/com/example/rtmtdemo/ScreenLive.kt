package com.example.rtmtdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 浙江集商优选电子商务有限公司
 * @author zenglw
 * @date   2020/11/16 11:39 PM
 */
class ScreenLive : Runnable {
    private lateinit var mLiveUrl: String
    private lateinit var manager: MediaProjectionManager
    private var mediaProjection:MediaProjection? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startLive(url: String, context: Activity) {
        mLiveUrl = url
        manager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val createScreenCaptureIntent = manager.createScreenCaptureIntent()
        context.startActivityForResult(createScreenCaptureIntent, 100)
    }

    fun stopLive() {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data ?: return
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            mediaProjection = manager.getMediaProjection(resultCode, data)
            LiveExecutors.instance?.execute(this)
        }
    }

    override fun run() {

        if (!connect(mLiveUrl)) {
            return
        }
        val videoCodec = VideoCodec()

    }

    external fun connect(url: String): Boolean
}
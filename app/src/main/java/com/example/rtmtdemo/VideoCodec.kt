package com.example.rtmtdemo

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

/**
 * 浙江集商优选电子商务有限公司
 * @author zenglw
 * @date   2020/11/17 12:20 AM
 */
class VideoCodec : Thread() {

    private var isLiving = false

    private lateinit var mediaCodec: MediaCodec
    private var timestramp: Long = 0
    private lateinit var mediaProjection: MediaProjection
    private lateinit var virtualDisplay: VirtualDisplay

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startLive(mediaProjection: MediaProjection) {
        this.mediaProjection = mediaProjection

        mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        val videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 360, 640)
        with(videoFormat) {
            setInteger(MediaFormat.KEY_BIT_RATE, 80_000)
            setInteger(MediaFormat.KEY_FRAME_RATE, 15)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2)
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
        }

        mediaCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        val surface = mediaCodec.createInputSurface()

        virtualDisplay = mediaProjection.createVirtualDisplay(
                "abc",
                360, 640, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null
        )

        start()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun run() {
        val mediaInfo = MediaCodec.BufferInfo()
        timestramp = System.currentTimeMillis()

        while (isLiving) {
            if (System.currentTimeMillis() - timestramp > 2000) {
                val bundle = Bundle()
                bundle.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0)
                mediaCodec.setParameters(bundle)
                timestramp = System.currentTimeMillis()
            } else {
                timestramp = System.currentTimeMillis()
            }

            val index = mediaCodec.dequeueOutputBuffer(mediaInfo, 10)
            val outputBuffer = mediaCodec.getOutputBuffer(index)
            val byteArray = ByteArray(mediaInfo.size)
            outputBuffer?.get(byteArray)
            mediaCodec.releaseOutputBuffer(index, false)


        }

        isLiving = false

        mediaCodec.stop()
        mediaCodec.release()

        virtualDisplay.release()
        mediaProjection.stop()
    }
}
package com.example.rtmtdemo

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

/**
 * 浙江集商优选电子商务有限公司
 * @author zenglw
 * @date   2020/11/17 12:20 AM
 */
class VideoCodec {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startLive(mediaProjection: MediaProjection) {

        val mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        val videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, 360, 640)
        with(videoFormat) {

            setInteger(MediaFormat.KEY_BIT_RATE, 80_000)
            setInteger(MediaFormat.KEY_FRAME_RATE, 15)
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2)
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            SimpleDateFormat("yyyy").format(Date())
        }


        mediaCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        val surface = mediaCodec.createInputSurface()


        mediaProjection.createVirtualDisplay(
                "abc",
                360, 640, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface,null

        )
    }
}
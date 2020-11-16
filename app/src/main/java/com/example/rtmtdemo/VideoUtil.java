package com.example.rtmtdemo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 浙江集商优选电子商务有限公司
 *
 * @author zenglw
 * @date 2020/11/12 9:20 PM
 */

class VideoUtil {
    /**
     * 获取手机中所有视频的信息
     */
    public static void getAllVideoInfos(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] proj = {MediaStore.Video.Thumbnails._ID
                        , MediaStore.Video.Thumbnails.DATA
                        , MediaStore.Video.Media.DURATION
                        , MediaStore.Video.Media.SIZE
                        , MediaStore.Video.Media.DISPLAY_NAME
                        , MediaStore.Video.Media.DATE_MODIFIED};
                Cursor mCursor = context.getContentResolver().query(mImageUri,
                        proj,
                        MediaStore.Video.Media.MIME_TYPE + "=?",
                        new String[]{"video/mp4"},
                        MediaStore.Video.Media.DATE_MODIFIED + " desc");
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        // 获取视频的信息
                        int videoId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        int duration = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        if (size < 0) {
                            //某些设备获取size<0，直接计算
                            Log.e("lily", "this video size < 0 " + path);
                            size = new File(path).length();
                        }
                        String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long modifyTime = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));//暂未用到

                        //提前生成缩略图，再获取：http://stackoverflow.com/questions/27903264/how-to-get-the-video-thumbnail-path-and-not-the-bitmap
                        MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), videoId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
                        String[] projection = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.DATA};
                        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
                                , projection
                                , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
                                , new String[]{videoId + ""}
                                , null);
                        String thumbPath = "";
                        while (cursor.moveToNext()) {
                            thumbPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                        }
                        cursor.close();
                        // 获取该视频的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();
                        Log.e("VideoUtil", "run: " + path);
                    }
                    mCursor.close();
                }


            }
        }).start();

    }
}

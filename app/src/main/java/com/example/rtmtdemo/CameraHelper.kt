package com.example.rtmtdemo

/**
 * 浙江集商优选电子商务有限公司
 * @author zenglw
 * @date   2020/11/12 11:17 PM
 */

class CameraHelper {

    external fun native_init( )

    companion object {
       init {
           System.loadLibrary("native-lib")
       }
    }

}
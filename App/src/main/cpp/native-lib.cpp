#include <jni.h>
#include <string>
#include <x264.h>
#include <librtmp/rtmp.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_rtmtdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    x264_image_t  *t = new x264_image_t ;
    RTMP_Alloc();

    return env->NewStringUTF(hello.c_str());
}

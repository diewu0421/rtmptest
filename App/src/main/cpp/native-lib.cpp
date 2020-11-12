#include <jni.h>
#include <string>
#include <x264.h>
#include <librtmp/rtmp.h>
#include <macro.h>
#include <safe_queue.h>

SafeQueue<RTMPPacket *> packets;
class Person{

};

void releasePackets(RTMPPacket *& person) {
    DELETE(person)
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_rtmtdemo_CameraHelper_native_1init(JNIEnv *env, jobject thiz) {

    packets.setReleaseCallback(releasePackets);
}
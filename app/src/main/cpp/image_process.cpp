#include <jni.h>

extern "C"
JNIEXPORT jintArray JNICALL
Java_net_jahanco_jahandaru_App_imageBlur(
        JNIEnv* env,
        jobject /* this */) {
    int p1 []={49,72,119,115,115};
    int p2 []={69,57,49,76,98,120,54,109,102,88,72,57,69,54,49,105,104,119,86,108,52,79,53};
    int p3 []={100,78,100,48};
    jint fill[32];
    for (int i = 0; i < 32; i++) {
        if(i<=4){
           fill[i]= p1[i];
        }
        else if(i>4 && i<=27){

            fill[i]= p2[i-5];

        }
        else if(i>27){

            fill[i]= p3[i-28];
        }
    }
    jintArray result;
    result = env->NewIntArray(32);
    env->SetIntArrayRegion(result, 0, 32, fill);
    return result;

}
extern "C"
JNIEXPORT jintArray JNICALL
Java_net_jahanco_jahandaru_App_imageResize(
        JNIEnv* env,
        jobject /* this */) {
    int p1 []={52,113,49,72,87,79,48,66,72,84,52,120};
    int p2 []={86,76,51,88};
    int p3 []={78,80,69,57,109,84,86,56,101,89,83,48,71,78,85,67};
    jint fill[32];
    for (int i = 0; i < 32; i++) {
        if(i<=11){
            fill[i]= p1[i];
        }
        else if(i>11 && i<=15){

            fill[i]= p2[i-12];

        }
        else if(i>15){

            fill[i]= p3[i-16];
        }
    }
    jintArray result;
    result = env->NewIntArray(32);
    env->SetIntArrayRegion(result, 0, 32, fill);
    return result;

}
extern "C"
JNIEXPORT jintArray JNICALL
Java_net_jahanco_jahandaru_App_imageScale(
        JNIEnv* env,
        jobject /* this */) {
    int p1 []={52,113,49,72,87,79,48,66,72,84,52,120};
    int p2 []={86,76,51,88};
    int p3 []={78,80,69,57,109,84,86,56,101,89,83,48,71,78,85,67};
    jint fill[32];
    for (int i = 0; i < 32; i++) {
        if(i<=11){
            fill[i]= p1[i];
        }
        else if(i>11 && i<=15){

            fill[i]= p2[i-12];

        }
        else if(i>15){

            fill[i]= p3[i-16];
        }
    }
    jintArray result;
    result = env->NewIntArray(32);
    env->SetIntArrayRegion(result, 0, 32, fill);
    return result;

}

#include "com_sharegogo_video_lib_LibInterface.h"
#include <android/log.h>
#include <stdio.h>
#define TAG "libsharegogo"

JNIEXPORT void JNICALL Java_com_sharegogo_video_lib_LibInterface_init
  (JNIEnv *env, jobject interface, jobject context)
{
	jbyte* realAppName = NULL;
	jclass contextCls = (*env)->GetObjectClass(env,context);

	jmethodID getPackageName = (*env)->GetMethodID(env,contextCls,"getPackageName", "()Ljava/lang/String;");
	
	//注意第二个参数是jobject不是jclass
	jstring packageName= (jstring)(*env)->CallObjectMethod(env,context,getPackageName);
			
	realAppName = (*env)->GetStringUTFChars(env, packageName, NULL);
	
	__android_log_write(ANDROID_LOG_ERROR,TAG,"Java_com_sharegogo_video_lib_LibInterface_init");
	__android_log_write(ANDROID_LOG_ERROR,TAG,realAppName);
	
	(*env)->ReleaseStringUTFChars(env, packageName, realAppName);
}
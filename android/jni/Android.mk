LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= libsharegogo.c
LOCAL_MODULE := sharegogo
LOCAL_LDLIBS := -lm -llog

include $(BUILD_SHARED_LIBRARY)


LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
        test.c

LOCAL_LDLIBS    := -llog

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE := libtest_jni

include $(BUILD_SHARED_LIBRARY)

##############################################
include $(CLEAR_VARS)

#TARGET_PLATFORM := android-3
#TARGET_PLATFORM := android-15
LOCAL_MODULE    := nt96650_serial_port
LOCAL_SRC_FILES := SerialPort.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
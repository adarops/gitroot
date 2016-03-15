LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
        test.c

LOCAL_LDLIBS    := -llog

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE := libtest_jni

include $(BUILD_SHARED_LIBRARY)
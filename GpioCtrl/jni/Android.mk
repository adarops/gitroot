LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
        gpio_ctrl.c

LOCAL_LDLIBS    := -llog

LOCAL_MODULE_TAGS := optional

LOCAL_MODULE := libgpio_ctrl_jni

include $(BUILD_SHARED_LIBRARY)
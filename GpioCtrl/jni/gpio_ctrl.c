#include <android/log.h>
#include <errno.h>
#include <fcntl.h>
#include <jni.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <termios.h>
#include <unistd.h>

static const char *TAG = "zyx";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define DEVICE_PATH		"/sys/class/misc/car_gpio_ctrl/lcm_switch"
#define POWER_PATH		"/sys/class/misc/car_gpio_ctrl/nt96655_power"
#define RESET_PATH		"/sys/class/misc/car_gpio_ctrl/nt96655_reset"

/*=====================================================================================================================*/

JNIEXPORT void JNICALL Java_com_android_io_GpioCtrl_lcmSwitch(JNIEnv *env, jobject thiz, jint mode)
{
	int fd;
	char val = '1';

	if (mode)
	val = '1';
	else
	val = '0';

	LOGD("lcmSwitch %c", val);
	fd = open(DEVICE_PATH, O_RDWR);
	if (fd < 0) {
		LOGE("open device err, %s\n", strerror(errno));
		return;
	}

	write(fd, &val, sizeof(val));

	close(fd);
	return;
}

JNIEXPORT void JNICALL Java_com_android_io_GpioCtrl_nt655PowerOn(JNIEnv *env, jobject thiz)
{
	int fd;
	char val = '1';

	LOGD("nt655PowerOn");
	fd = open(POWER_PATH, O_WRONLY);
	if (fd < 0) {
		LOGE("open device err, %s\n", strerror(errno));
		return;
	}

	write(fd, &val, sizeof(val));

	close(fd);
	return;
}

JNIEXPORT void JNICALL Java_com_android_io_GpioCtrl_nt655PowerOff(JNIEnv *env, jobject thiz)
{
	int fd;
	char val = '0';

	LOGD("nt655PowerOff");
	fd = open(POWER_PATH, O_WRONLY);
	if (fd < 0) {
		LOGE("open device err, %s\n", strerror(errno));
		return;
	}

	write(fd, &val, sizeof(val));

	close(fd);
	return;
}

JNIEXPORT void JNICALL Java_com_android_io_GpioCtrl_nt655Reset(JNIEnv *env, jobject thiz)
{
	int fd;
	char val = '1';

	LOGD("nt655Reset");
	fd = open(RESET_PATH, O_WRONLY);
	if (fd < 0) {
		LOGE("open device err, %s\n", strerror(errno));
		return;
	}

	write(fd, &val, sizeof(val));

	close(fd);
	return;
}

JNIEXPORT void JNICALL Java_com_android_io_GpioCtrl_setBacklightOff(JNIEnv *env, jobject thiz)
{
	int fd1;
	int fd2;
	char buf[8];
	int ret = 0;
	char bl_off[3] = "0\n";

	memset(buf, 0, sizeof(buf));

	fd1 = open("/sys/class/leds/lcd-backlight/brightness", O_RDWR);
	if (fd1 < 0) {
		LOGE("brightness open failure %s", strerror(errno));
	    return;
	}
	fd2 = open("/sys/class/leds/lcd-backlight/bl_switch", O_RDWR);
	if (fd2 < 0) {
		LOGE("bl_switch open failure %s", strerror(errno));
		close(fd1);
	    return;
	}


	if (read(fd1, buf, sizeof(buf)) < 0) {
		LOGE("wake_level open failure %s", strerror(errno));
		ret = -1;
		goto exit_dl;
	}
	LOGD("read bl_level: %s", buf);

	if (write(fd2, bl_off, sizeof(buf)) < 0) {
		LOGE("write bl_switch failure %s", strerror(errno));
		ret = -1;
		goto exit_dl;
	}

	if (write(fd1, buf, 4) < 0) {
		LOGE("write brightness failture %s", strerror(errno));
		ret = -1;
		goto exit_dl;
	}

exit_dl:
	close(fd1);
	close(fd2);
	return;
}

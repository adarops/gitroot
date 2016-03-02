package com.ilincar.radar;

import com.mediatek.engineermode.io.EmGpio;

public class RadarUtil {
	/************** service √¸¡Ó *********/
	public final static int CMD_STOP_SERVICE = 0x01;
	public final static int CMD_SYSTEM_EXIT = 0x02;
	public final static int CMD_FINISH = 0x03;

	public final static int CMD_SEND_RADAR_DATA = 0x04;
	public final static int CMD_RECEIVE_RADAR_DATA = 0x05;
			
	public final static String SERIALPORT_ACTIVITY_RECEIVE = "android.intent.action.radar.serialport.activity.rx";
	public final static String SERIALPORT_SERVICE_RECEIVE = "android.intent.action.radar.serialport.service.rx";

	public static void gpio_crtl(int gpio_number, boolean on) {
		EmGpio.gpioInit();
		if (on) {
			EmGpio.setGpioOutput(gpio_number);
			EmGpio.setGpioDataHigh(gpio_number);
		} else {
			EmGpio.setGpioOutput(gpio_number);
			EmGpio.setGpioDataLow(gpio_number);
		}
	}
}

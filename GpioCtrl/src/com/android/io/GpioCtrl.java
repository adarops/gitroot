package com.android.io;

import android.util.Log;


public class GpioCtrl {
	static {
		System.loadLibrary("gpio_ctrl_jni");
	}

	public void lcmSwitchTo35() {
		Log.i("zyx", "lcm switch to 35");
		lcmSwitch(1);
	}

	public void lcmSwitchTo655() {
		Log.i("zyx", "lcm switch to 35");
		lcmSwitch(0);
	}

	private native void lcmSwitch(int val);
	public native void nt655PowerOn();
	public native void nt655PowerOff();
	public native void nt655Reset();
	public native void setBacklightOff();
}
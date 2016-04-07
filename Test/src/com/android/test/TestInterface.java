package com.android.test;

import android.util.Log;


public class TestInterface {
	static {
		System.loadLibrary("test_jni");
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
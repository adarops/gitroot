package com.android.test;

public class TestInterface {
	static {
		System.loadLibrary("test_jni");
	}

	//切换成系统
	public void lcmSwitchTo35() {
		lcmSwitch(1);
	}

	//模块
	public void lcmSwitchTo655() {
		lcmSwitch(0);
	}

	private native void lcmSwitch(int val);
	public native void nt655PowerOn();
	public native void nt655PowerOff();
	public native void nt655Reset();
	public native void setBacklightOff();
}
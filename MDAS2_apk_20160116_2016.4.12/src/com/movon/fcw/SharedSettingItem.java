package com.movon.fcw;

import java.io.Serializable;

public class SharedSettingItem implements Serializable{

	private int		mDispWidth;
	private int		mDispHeight;
	
	private boolean	mKilometer;
	private int		mVanishLine;
	private int		mHoodLine;
	private int		mVehicleWidth;
	private int		mCameraHeight;
	private int		mWheelLength;
	private int		mCameraCenter;
	private int		mBumperLength;
	private int		mSensitivityLeft;
	private int		mSensitivityRight;
	private float	mFCWSensitivity;
	private boolean mNeedflush;
	
	public SharedSettingItem(){
		mDispWidth = 0;
		mDispHeight = 0;
		
		mKilometer = false;
		mVanishLine = 0;
		mHoodLine = 0;
		mVehicleWidth = 0;
		mCameraHeight = 0;
		mWheelLength = 0;
		mCameraCenter = 0;
		mBumperLength = 0;
		mSensitivityLeft = 0;
		mSensitivityRight = 0;
		mFCWSensitivity = 0;
		mNeedflush = false;
	}
	
	int		getDispWidth()			{ return mDispWidth; }
	int		getDispHeight()			{ return mDispHeight; }
	
	boolean getSpeedType()			{ return mKilometer; }
	int		getVanishLine()			{ return mVanishLine; }
	int		getHoodLine()			{ return mHoodLine; }
	int 	getVehicleWidth()		{ return mVehicleWidth; }
	int 	getCameraHeight()		{ return mCameraHeight; }
	int 	getWheelLength()		{ return mWheelLength; }
	int 	getCameraCenter()		{ return mCameraCenter; }
	int 	getBumperLength()		{ return mBumperLength; }
	int 	getSensitivityLeft()	{ return mSensitivityLeft; }
	int 	getSensitivityRight()	{ return mSensitivityRight; }
	float 	getFCWSensitivity()		{ return mFCWSensitivity; }
	boolean getNeedflush()          { return mNeedflush;}
	
	void setDispWidth( int dispWidth )				{ mDispWidth = dispWidth; }
	void setDispHeight( int dispHeight )			{ mDispHeight = dispHeight; }
	void setSpeedType( boolean type )				{ mKilometer = type; }
	void setVanishLine( int vanishLine )			{ mVanishLine = vanishLine; }
	void setHoodLine( int hoodLine )				{ mHoodLine = hoodLine; }
	void setVehicleWidth( int vehicleWidth )		{ mVehicleWidth = vehicleWidth; }
	void setCameraHeight( int cameraHeight )		{ mCameraHeight = cameraHeight; }
	void setWheelLength( int wheelLength )			{ mWheelLength = wheelLength; }
	void setCameraCenter( int cameraCenter )		{ mCameraCenter = cameraCenter; }
	void setBumperLength( int bumperLength )		{ mBumperLength = bumperLength; }
	void setSensitivityLeft( int sensitivityLeft )	{ mSensitivityLeft = sensitivityLeft; }
	void setSensitivityRight( int sensitivityRight ){ mSensitivityRight = sensitivityRight; }
	void setFCWSensitivity( float FCWSensitivity )	{ mFCWSensitivity = FCWSensitivity; }
	void setNeedflush(boolean enable)               { mNeedflush = enable;}
}
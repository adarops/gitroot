/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ilincar.radar;

import java.util.ArrayList;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class RadarSound {

	private static final String TAG = "RadarSound";

	public final int SOUND_RADAR_START = 0;
	public final int SOUND_RADAR_SECOND = 1;
	public final int SOUND_RADAR_ERROR = 2;
	public final int SOUND_RADAR_RFDIDI = 3;
	public final int SOUND_RADAR_LASER = 4;
	public final int SOUND_RADAR_K = 5;
	public final int SOUND_RADAR_KA = 6;
	public final int SOUND_RADAR_KU = 7;
	public final int SOUND_RADAR_X = 8;

	public final int SOUND_ITEM_MAX = 9;

	private final int[] PlayTime = { 8000, 3000, 3000, 2500, 4000, 4000, 4000,
			4000, 4000 , 2500, 2500};

	private Context mContext;
	private static RadarSound instance;
	private AudioManager mAudioManager;

	private SoundPool mSoundPool;
	private int mCurrentSound;
	private ArrayList<Integer> soundBuffer = new ArrayList<Integer>();

	private int sound[];

	private Object mSoundSemaphore = new Object();

	private RadarSoundThread mRadarSoundThread;

	private boolean isRunning = false;

	public RadarSound(Context context) {
		super();
		mContext = context;
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);

		mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
		sound = new int[SOUND_ITEM_MAX];
		sound[0] = mSoundPool.load(context, R.raw.start, 0);
		sound[1] = mSoundPool.load(context, R.raw.second, 0);
		sound[2] = mSoundPool.load(context, R.raw.error, 0);
		sound[3] = mSoundPool.load(context, R.raw.didi, 0);
		sound[4] = mSoundPool.load(context, R.raw.laser, 0);
		sound[5] = mSoundPool.load(context, R.raw.k, 0);
		sound[6] = mSoundPool.load(context, R.raw.ka, 0);
		sound[7] = mSoundPool.load(context, R.raw.ku, 0);
		sound[8] = mSoundPool.load(context, R.raw.x, 0);

		isRunning = true;
		mRadarSoundThread = new RadarSoundThread();
		mRadarSoundThread.start();

	}

	public static RadarSound getInstance(Context context) {
		if (instance == null) {
			synchronized (RadarSound.class) {
				if (instance == null)
					instance = new RadarSound(context);
			}
		}
		return instance;
	}

	private class RadarSoundThread extends Thread {
		@Override
		public void run() {
			while (isRunning) {
				int i = 0xff;
				int sleeptime;
				Log.v(TAG, "Running");
				if (soundBuffer != null && soundBuffer.size() != 0) {
					synchronized (mSoundSemaphore) {
						i = soundBuffer.get(0);
						soundBuffer.remove(0);
//						int num=soundBuffer.size();
//						Log.v(TAG, Integer.toHexString(num));
					}
					
					if (i < SOUND_ITEM_MAX) {
						// mSoundPool.setVolume(sound[i], 10, 10);
						int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );   // 15
						int current = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );  // 1
						
						float vol=(float)current;
						vol/=max;
						   
						mCurrentSound = i;
						mSoundPool.play(sound[i], vol, vol, 0, 0, 1);
					}
				}

				if (i < SOUND_ITEM_MAX) {
					sleeptime = PlayTime[i];
				} else
					sleeptime = 1000;

				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void play(final int index) {
		synchronized (mSoundSemaphore) {
			if(soundBuffer.size()<3)
				soundBuffer.add(index);
		}
	}

	public void close() {
		if (mSoundPool != null) {
			mSoundPool.stop(sound[mCurrentSound]); // 不能马上停止
			mSoundPool.release();
			mSoundPool = null;
		}
		isRunning = false;
		instance = null;
	}
}

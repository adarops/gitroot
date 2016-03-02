package com.movon.fcw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;

import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.Display;
import android.view.View;
import android.view.ViewManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.ViewGroup.LayoutParams; 

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Math;
import android.media.AudioManager;
import android.media.SoundPool;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.graphics.YuvImage;

public class SplashScreen extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new MovonView(this));
		initialize();
	}

	private void initialize() {
		Handler handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 2000);
	}
}

class MovonView extends View {

	private Bitmap image;
	private Bitmap scaledImage;

	public MovonView(Context context) {
		super(context);
		image = BitmapFactory.decodeResource(context.getResources(), R.raw.intro_bg);
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		scaledImage = Bitmap.createScaledBitmap(image, display.getWidth(), display.getHeight(), true);		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(scaledImage, 0, 0, null);
		invalidate();
	}
}

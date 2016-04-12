package com.movon.fcw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SettingPage1 extends Fragment implements OnClickListener{
	
	DrawLine mDrawLine;
	
	Button btnNext, btnPrev, btnMinus, btnPlus;
	EditText etVanishLine;
	
	int mVanishLine;
	int mDispWidth;
	int mDispHeight;
	
	private SharedPreferences mPref;
    
    RelativeLayout layout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mPref = getActivity().getSharedPreferences( "calib", getActivity().MODE_PRIVATE );
		
		mVanishLine = ((Settings)getActivity()).mParam.getVanishLine();
		mDispWidth = ((Settings)getActivity()).mParam.getDispWidth();
		mDispHeight = ((Settings)getActivity()).mParam.getDispHeight();
		
		mDrawLine = new DrawLine(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = (RelativeLayout)inflater.inflate(
							R.layout.my_pager1, container, false);
		
		btnNext = (Button)layout.findViewById(R.id.btn_next1);
		btnNext.setOnClickListener(this);
		btnPrev = (Button)layout.findViewById(R.id.btn_prev1);
		btnPrev.setOnClickListener(this);
		
		btnMinus = (Button)layout.findViewById(R.id.btn_vanish_minus);
		btnMinus.setOnClickListener(this);
		btnPlus = (Button)layout.findViewById(R.id.btn_vanish_plus);
		btnPlus.setOnClickListener(this);
		
		etVanishLine = (EditText)layout.findViewById(R.id.et_vanish_line); 
		etVanishLine.setText(""+mVanishLine);
		etVanishLine.setInputType(0);

		layout.addView(mDrawLine);
	
		return layout;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		layout.removeView(mDrawLine);
		super.onDestroyView();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch( v.getId() ){
		case R.id.btn_next1:				
			((Settings)getActivity()).getViewPager().setCurrentItem(1);	break;
		case R.id.btn_prev1:
			RestoreParam();
			((Settings)getActivity()).finish();	break;
		case R.id.btn_vanish_plus:				
			if( mVanishLine < (mDispHeight/2+5) ){
				etVanishLine.setText(""+(mVanishLine+5));
				mVanishLine = mVanishLine+5;
				((Settings)getActivity()).mParam.setVanishLine(mVanishLine);
				mDrawLine.invalidate();
			}	break;
		case R.id.btn_vanish_minus:
			if( mVanishLine >= 5 ){
				etVanishLine.setText(""+(mVanishLine-5));
				mVanishLine = mVanishLine - 5;
				((Settings)getActivity()).mParam.setVanishLine(mVanishLine);
				mDrawLine.invalidate();
			}	break;
		}
	}

	private void RestoreParam(){
		  
		((Settings)getActivity()).
		mParam.setSpeedType( mPref.getBoolean( "SpeedType", false) );
		((Settings)getActivity()).
		mParam.setVanishLine( mPref.getInt( "VanishLine", 0 ) );
		((Settings)getActivity()).
		mParam.setHoodLine( mPref.getInt( "HoodLine", 0 ) );
		((Settings)getActivity()).
		mParam.setVehicleWidth( mPref.getInt( "VehicleWidth", 200 ) );
		((Settings)getActivity()).
		mParam.setCameraHeight( mPref.getInt( "CameraHeight", 150 ) );
		((Settings)getActivity()).
		mParam.setWheelLength( mPref.getInt( "WheelLength", 100 ) );
		((Settings)getActivity()).
		mParam.setCameraCenter( mPref.getInt( "CameraCenter", 0 ) );
		((Settings)getActivity()).
		mParam.setBumperLength( mPref.getInt( "BumperLength", 150 ) );
		((Settings)getActivity()).
		mParam.setSensitivityLeft( mPref.getInt( "SensitivityLeft", 0 ) );
		((Settings)getActivity()).
		mParam.setSensitivityRight( mPref.getInt( "SensitivityRight", 0 ) );
		((Settings)getActivity()).
		mParam.setFCWSensitivity( mPref.getFloat( "FCWSensitivity", 1.2f ) );
	}
	
	private class DrawLine extends View{

		Paint paint;
		public DrawLine( Context context ){
			super(context);
			paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setStrokeWidth(3);
			paint.setAntiAlias(true);
			paint.setColor(Color.RED);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
		
			canvas.drawRect(mDispWidth/5,mVanishLine,
							mDispWidth*4/5,mVanishLine+4,paint);			
		}		
	}
}
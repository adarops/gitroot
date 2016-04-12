package com.movon.fcw;

import android.content.Context;
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

public class SettingPage2 extends Fragment implements OnClickListener{
	
	DrawLine mDrawLine;
	
	Button btnNext, btnPrev, btnMinus, btnPlus;
	EditText etHoodLine;
	RelativeLayout layout;
	
	int mHoodLine;
	int mDispWidth;
	int mDispHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mHoodLine = ((Settings)getActivity()).mParam.getHoodLine();
		mDispWidth = ((Settings)getActivity()).mParam.getDispWidth();
		mDispHeight = ((Settings)getActivity()).mParam.getDispHeight();
		
		mDrawLine = new DrawLine(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = (RelativeLayout)inflater.inflate(
							R.layout.my_pager2, container, false);
		
		btnNext = (Button)layout.findViewById(R.id.btn_next2);
		btnNext.setOnClickListener(this);
		btnPrev = (Button)layout.findViewById(R.id.btn_prev2);
		btnPrev.setOnClickListener(this);
		
		btnMinus = (Button)layout.findViewById(R.id.btn_hood_minus);
		btnMinus.setOnClickListener(this);
		btnPlus = (Button)layout.findViewById(R.id.btn_hood_plus);
		btnPlus.setOnClickListener(this);
		
		etHoodLine = (EditText)layout.findViewById(R.id.et_hood_line); 
		etHoodLine.setText(""+mHoodLine);
		etHoodLine.setInputType(0);
		
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
		case R.id.btn_next2: 
			((Settings)getActivity()).getViewPager().setCurrentItem(2);
			break;
		case R.id.btn_prev2:
			((Settings)getActivity()).getViewPager().setCurrentItem(0);
			break;
		case R.id.btn_hood_plus:				
			if( mHoodLine < (mDispHeight-5) ){
				etHoodLine.setText(""+(mHoodLine+5));
				mHoodLine = mHoodLine+5;
				((Settings)getActivity()).mParam.setHoodLine(mHoodLine);
				mDrawLine.invalidate();				
			}
			break;
		case R.id.btn_hood_minus:
			if( mHoodLine > (mDispHeight/2+150) ){
				etHoodLine.setText(""+(mHoodLine-5));
				mHoodLine = mHoodLine - 5;
				((Settings)getActivity()).mParam.setHoodLine(mHoodLine);
				mDrawLine.invalidate();
			}
			break;
		}
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
			canvas.drawRect(mDispWidth/5,mHoodLine,
							mDispWidth*4/5,mHoodLine+4,paint);			
		}		
	}
}
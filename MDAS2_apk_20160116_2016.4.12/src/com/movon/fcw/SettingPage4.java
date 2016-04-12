package com.movon.fcw;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class SettingPage4 extends Fragment implements OnClickListener{
	
	private	Button btnNext;
	private	Button btnPrev;
	
	private RadioGroup mRgLine1;
	private RadioGroup mRgLine2;
	
	private Button btnLdwLeftMinus;
	private Button btnLdwLeftPlus;
	private Button btnLdwRightMinus;
	private Button btnLdwRightPlus;

	private EditText etLDWLeftSensi;
	private EditText etLDWRightSensi;

	private int		mSensitivityLeft;
	private int		mSensitivityRight;
	private float 	mFCWSensitivity;
	
	private SharedPreferences mPref;
    private SharedPreferences.Editor mPrefEditor;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mPref = getActivity().getSharedPreferences( 
				"calib", Context.MODE_PRIVATE );
		mPrefEditor = mPref.edit();
		
		mSensitivityLeft = ((Settings)getActivity()).mParam.getSensitivityLeft();
		mSensitivityRight = ((Settings)getActivity()).mParam.getSensitivityRight();
		mFCWSensitivity = ((Settings)getActivity()).mParam.getFCWSensitivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			
		RelativeLayout layout = (RelativeLayout)inflater.inflate(
							R.layout.my_pager4, container, false);
		btnNext = (Button)layout.findViewById(R.id.btn_next4);
		btnNext.setOnClickListener(this);
		btnPrev = (Button)layout.findViewById(R.id.btn_prev4);
		btnPrev.setOnClickListener(this);
		
		btnLdwLeftMinus = (Button)layout.findViewById(R.id.btn_ldw_left_minus);
		btnLdwLeftMinus.setOnClickListener(this);
		btnLdwLeftPlus = (Button)layout.findViewById(R.id.btn_ldw_left_plus);
		btnLdwLeftPlus.setOnClickListener(this);
		etLDWLeftSensi = (EditText)layout.findViewById(R.id.et_ldw_left_sensi); 
		etLDWLeftSensi.setText(""+mSensitivityLeft);
		etLDWLeftSensi.setInputType(0);
		
		btnLdwRightMinus = (Button)layout.findViewById(R.id.btn_ldw_right_minus);
		btnLdwRightMinus.setOnClickListener(this);
		btnLdwRightPlus = (Button)layout.findViewById(R.id.btn_ldw_right_plus);
		btnLdwRightPlus.setOnClickListener(this);
		etLDWRightSensi = (EditText)layout.findViewById(R.id.et_ldw_right_sensi); 
		etLDWRightSensi.setText(""+mSensitivityRight);
		etLDWRightSensi.setInputType(0);
				
		mRgLine1 = (RadioGroup)layout.findViewById(R.id.rg_line1);
		mRgLine1.clearCheck();
		mRgLine1.setOnCheckedChangeListener(listener1);
		mRgLine2 = (RadioGroup)layout.findViewById(R.id.rg_line2);
		mRgLine2.clearCheck();
		mRgLine2.setOnCheckedChangeListener(listener2);
		
		DefaultRadioButton();
		
		return layout;
	}
	
	private OnCheckedChangeListener listener1 = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if(checkedId != -1){
				mRgLine2.setOnCheckedChangeListener(null);
				mRgLine2.clearCheck();
				mRgLine2.setOnCheckedChangeListener(listener2);
			}			
		}
	};
	private OnCheckedChangeListener listener2 = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if(checkedId != -1){
				mRgLine1.setOnCheckedChangeListener(null);
				mRgLine1.clearCheck();
				mRgLine1.setOnCheckedChangeListener(listener1);
			}
		}
	};

	private void DefaultRadioButton(){
			 if(mFCWSensitivity == 0.6f){mRgLine1.check(R.id.radio_button1);}
		else if(mFCWSensitivity == 0.9f){mRgLine1.check(R.id.radio_button2);}
		else if(mFCWSensitivity == 1.2f){mRgLine1.check(R.id.radio_button3);}
		else if(mFCWSensitivity == 1.5f){mRgLine2.check(R.id.radio_button4);}
		else if(mFCWSensitivity == 2.0f){mRgLine2.check(R.id.radio_button5);}
		else if(mFCWSensitivity == 3.0f){mRgLine2.check(R.id.radio_button6);}
	}
	
	private void setAllParameter(){
		
		String selectedResult = "";
		if( mRgLine1.getCheckedRadioButtonId() > 0 ){
			View radioButton = mRgLine1.findViewById(mRgLine1.getCheckedRadioButtonId());
			int radioId = mRgLine1.indexOfChild(radioButton);
			RadioButton btn = (RadioButton)mRgLine1.getChildAt(radioId);
			selectedResult = (String)btn.getText();
		}else if( mRgLine2.getCheckedRadioButtonId() > 0 ){
			View radioButton = mRgLine2.findViewById(mRgLine2.getCheckedRadioButtonId());
			int radioId = mRgLine2.indexOfChild(radioButton);
			RadioButton btn = (RadioButton)mRgLine2.getChildAt(radioId);
			selectedResult = (String)btn.getText();
		}
		
		mFCWSensitivity = Float.parseFloat(selectedResult);
		
		((Settings)getActivity()).mParam.setFCWSensitivity(mFCWSensitivity);
		((Settings)getActivity()).mParam.setSensitivityLeft(mSensitivityLeft);
		((Settings)getActivity()).mParam.setSensitivityRight(mSensitivityRight);
		
		mPrefEditor.putBoolean(	"SpeedType",
				((Settings)getActivity()).mParam.getSpeedType() );  
		mPrefEditor.putInt( "VanishLine",
				((Settings)getActivity()).mParam.getVanishLine() );
		mPrefEditor.putInt( "HoodLine",
				((Settings)getActivity()).mParam.getHoodLine() );
		mPrefEditor.putInt( "VehicleWidth",
				((Settings)getActivity()).mParam.getVehicleWidth() );
		mPrefEditor.putInt( "CameraHeight",
				((Settings)getActivity()).mParam.getCameraHeight() );
		mPrefEditor.putInt( "WheelLength",
				((Settings)getActivity()).mParam.getWheelLength() );
		mPrefEditor.putInt( "CameraCenter",
				((Settings)getActivity()).mParam.getCameraCenter() );
		mPrefEditor.putInt( "BumperLength",
				((Settings)getActivity()).mParam.getBumperLength() );
		mPrefEditor.putInt( "SensitivityLeft",
				((Settings)getActivity()).mParam.getSensitivityLeft() );
		mPrefEditor.putInt( "SensitivityRight",
				((Settings)getActivity()).mParam.getSensitivityRight() );
		mPrefEditor.putFloat( "FCWSensitivity",
				((Settings)getActivity()).mParam.getFCWSensitivity() );
		
		mPrefEditor.putBoolean(	"needflush",
				true );  
		mPrefEditor.commit();
	}
	
	@Override
	public void onClick(View v) {
	// TODO Auto-generated method stub
	
		switch( v.getId() ){
			case R.id.btn_next4:
			setAllParameter();
			((Settings)getActivity()).finish();
			break;
			case R.id.btn_prev4:
			((Settings)getActivity()).getViewPager().setCurrentItem(2);
			break;
			case R.id.btn_ldw_left_minus:	
				if( mSensitivityLeft > -10 ){
					etLDWLeftSensi.setText(""+(mSensitivityLeft-1));
					mSensitivityLeft = mSensitivityLeft-1;
				}break;
			case R.id.btn_ldw_left_plus:
				if( mSensitivityLeft < 10 ){
					etLDWLeftSensi.setText(""+(mSensitivityLeft+1));
					mSensitivityLeft = mSensitivityLeft+1;
				}break;
			case R.id.btn_ldw_right_minus:	
				if( mSensitivityRight > -10 ){
					etLDWRightSensi.setText(""+(mSensitivityRight-1));
					mSensitivityRight = mSensitivityRight-1;
				}break;
			case R.id.btn_ldw_right_plus:
				if( mSensitivityRight < 10 ){
					etLDWRightSensi.setText(""+(mSensitivityRight+1));
					mSensitivityRight = mSensitivityRight+1;
				}break;
		}
	
	}
}
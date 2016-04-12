package com.movon.fcw;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SettingPage3 extends ListFragment implements OnClickListener 
{
	ArrayList<MyItem> mArrayList;
	ArrayList<EditText> mArrayET;
	MyAdapter mAdapter;

	boolean mSpeedType;
	int mVehicleWidth;
	int mWheelLength;
	int mBumperLength;
	int mCameraHeight;
	int mCameraCenter;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mSpeedType = ((Settings)getActivity()).mParam.getSpeedType();
		mVehicleWidth = ((Settings)getActivity()).mParam.getVehicleWidth();
		mWheelLength = ((Settings)getActivity()).mParam.getWheelLength();
		mBumperLength = ((Settings)getActivity()).mParam.getBumperLength();
		mCameraHeight = ((Settings)getActivity()).mParam.getCameraHeight();
		mCameraCenter = ((Settings)getActivity()).mParam.getCameraCenter();		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
			
		return inflater.inflate(R.layout.my_pager3, null, false);
	}
	
	OnEditorActionListener editorActionListener = new OnEditorActionListener() {	
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			
			if(actionId == EditorInfo.IME_ACTION_DONE){
				int data, id;
				int min = 0, max = 0;

				try{
					data	= Integer.parseInt(v.getText().toString());
					id		= Integer.parseInt(v.getTag().toString());
				}catch( NumberFormatException ne){
					return true;
				}

		    	switch(id){
		    	case 1:
		    		min = ConstClass.VEHICLE_WIDTH_MIN;
		    		max = ConstClass.VEHICLE_WIDTH_MAX;	break;
		    	case 2:
		    		min = ConstClass.WHEEL_LENGTH_MIN;
		    		max = ConstClass.WHEEL_LENGTH_MAX;	break;
		    	case 3:
		    		min = ConstClass.BUMPER_LENGTH_MIN;
		    		max = ConstClass.BUMPER_LENGTH_MAX;	break;
		    	case 4:
		    		min = ConstClass.CAMERA_HEIGHT_MIN;
		    		max = ConstClass.CAMERA_HEIGHT_MAX;	break;
		    	case 5:	
		    		min = ConstClass.CAMERA_CENTER_MIN;
		    		max = ConstClass.CAMERA_CENTER_MAX;	break;
		    	}

		    	if( (data < min) || (data > max) ){
				Toast.makeText(getActivity(), 
					"Please enter the correct value.\n"
					+"("+min+" ~ "+max+")",
					Toast.LENGTH_SHORT).show();
				return true;
		    	}
			}
			return false;
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mArrayList = new ArrayList<MyItem>();		
		
		mArrayList.add(new MyItem("Speed Type"));
		mArrayList.add(new MyItem("Vehicle Width"));
		mArrayList.add(new MyItem("Wheel Length"));
		mArrayList.add(new MyItem("Bumper Length"));
		mArrayList.add(new MyItem("Camera Height"));
		mArrayList.add(new MyItem("Camera Center"));
	
		
		mAdapter = new MyAdapter(getActivity(), mArrayList);
		setListAdapter(mAdapter);
		
		Button btnNext = (Button)getActivity().findViewById(R.id.btn_next3);
		btnNext.setOnClickListener(this);
		Button btnPrev = (Button)getActivity().findViewById(R.id.btn_prev3);
		btnPrev.setOnClickListener(this);
	}
	
	private void setAllParameters(){
		
		mSpeedType    = (mArrayET.get(ConstClass.SPEED_TYPE).getText().toString() == "Km/h")? true : false;
		mVehicleWidth = Integer.parseInt(mArrayET.get(ConstClass.VEHICLE_WIDTH)
												.getText().toString());
		mWheelLength = Integer.parseInt(mArrayET.get(ConstClass.WHEEL_LENGTH)
												.getText().toString());
		mBumperLength = Integer.parseInt(mArrayET.get(ConstClass.BUMPER_LENGTH)
												.getText().toString());
		mCameraHeight = Integer.parseInt(mArrayET.get(ConstClass.CAMERA_HEIGHT)
												.getText().toString());
		mCameraCenter = Integer.parseInt(mArrayET.get(ConstClass.CAMERA_CENTER)
												.getText().toString());
		
		((Settings)getActivity()).mParam.setSpeedType(mSpeedType);
		((Settings)getActivity()).mParam.setVehicleWidth(mVehicleWidth);
		((Settings)getActivity()).mParam.setWheelLength(mWheelLength);
		((Settings)getActivity()).mParam.setBumperLength(mBumperLength);
		((Settings)getActivity()).mParam.setCameraHeight(mCameraHeight);
		((Settings)getActivity()).mParam.setCameraCenter(mCameraCenter);	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch( v.getId() ){
		case R.id.btn_next3:
			//Toast.makeText(getActivity(), "next button is pressed",Toast.LENGTH_SHORT).show();
			setAllParameters();
			((Settings)getActivity()).getViewPager().setCurrentItem(3);
			break;
		case R.id.btn_prev3:
			((Settings)getActivity()).getViewPager().setCurrentItem(1);
			break;
		}
	}	
		
	class MyAdapter extends BaseAdapter{
		
		Context mContext = null;
		ViewHolder viewHolder = null;
		ArrayList<MyItem> items = null;
		
		public MyAdapter(Context context, ArrayList<MyItem>item){
			mContext = context;
			this.items = item;
			mArrayET = new ArrayList<EditText>();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return items.indexOf(getItem(position));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v = convertView;
			InputFilter[] filterArr = new InputFilter[1];
			
			if( v == null ){
				
				viewHolder = new ViewHolder();
				LayoutInflater mInflater = 
						(LayoutInflater)mContext
						.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				v = mInflater.inflate( R.layout.setting_item, null );
				viewHolder.textView = (TextView)v.findViewById(R.id.tv_list);
				viewHolder.editText = (MyEditText)v.findViewById(R.id.et_list);
				viewHolder.btnMinus = (Button)v.findViewById(R.id.btn_minus_list);
				viewHolder.btnPlus = (Button)v.findViewById(R.id.btn_plus_list);
				
				v.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)v.getTag();
			}

			viewHolder.textView.setText(items.get(position).getString());

			
			
			switch(position){
			case ConstClass.SPEED_TYPE:
				if( mSpeedType )	viewHolder.editText.setText("Mp/h");
				else				viewHolder.editText.setText("Km/h");
				viewHolder.editText.setInputType(0);		break;
			case ConstClass.VEHICLE_WIDTH:
				viewHolder.editText.setText(""+mVehicleWidth);
				filterArr[0] = new InputFilter.LengthFilter(3);
				viewHolder.editText.setFilters(filterArr);		break;
			case ConstClass.WHEEL_LENGTH:
				viewHolder.editText.setText(""+mWheelLength);
				filterArr[0] = new InputFilter.LengthFilter(3);
				viewHolder.editText.setFilters(filterArr);		break;
			case ConstClass.BUMPER_LENGTH:
				viewHolder.editText.setText(""+mBumperLength);
				filterArr[0] = new InputFilter.LengthFilter(3);
				viewHolder.editText.setFilters(filterArr);		break;
			case ConstClass.CAMERA_HEIGHT:
				viewHolder.editText.setText(""+mCameraHeight);
				filterArr[0] = new InputFilter.LengthFilter(3);
				viewHolder.editText.setFilters(filterArr);		break;
			case ConstClass.CAMERA_CENTER:
				viewHolder.editText.setText(""+mCameraCenter);
				filterArr[0] = new InputFilter.LengthFilter(4);
				viewHolder.editText.setFilters(filterArr);
				viewHolder.editText.setInputType(
				InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
				break;
			}

			mArrayET.add(viewHolder.editText);
			viewHolder.editText.setTag(position);
			viewHolder.editText.setOnEditorActionListener(editorActionListener);
		
			viewHolder.btnMinus.setTag(position);
			viewHolder.btnMinus.setOnClickListener(buttonClickListener);
			
			viewHolder.btnPlus.setTag(position);
			viewHolder.btnPlus.setOnClickListener(buttonClickListener);
			
			return v;
		}
		
		private View.OnClickListener buttonClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int tag = Integer.parseInt(""+v.getTag());
				
				switch( v.getId() ){
				
				case R.id.btn_minus_list:
					switch(tag){
					case ConstClass.SPEED_TYPE:
						if( mSpeedType )	mArrayET.get(tag).setText("Km/h");
						else				mArrayET.get(tag).setText("Mp/h");
						mSpeedType = !mSpeedType;
						break;
					case ConstClass.VEHICLE_WIDTH:
						mArrayET.get(tag).setText(""+(mVehicleWidth-1));
						mVehicleWidth = mVehicleWidth-1;
						break;
					case ConstClass.WHEEL_LENGTH: 
						mArrayET.get(tag).setText(""+(mWheelLength-1));
						mWheelLength = mWheelLength-1;
						break;
					case ConstClass.BUMPER_LENGTH: 
						mArrayET.get(tag).setText(""+(mBumperLength-1));
						mBumperLength = mBumperLength-1;
						break;
					case ConstClass.CAMERA_HEIGHT: 
						mArrayET.get(tag).setText(""+(mCameraHeight-1));
						mCameraHeight = mCameraHeight-1;
						break;
					case ConstClass.CAMERA_CENTER: 
						mArrayET.get(tag).setText(""+(mCameraCenter-1));
						mCameraCenter = mCameraCenter-1;
						break;
					}break;
				case R.id.btn_plus_list:
					switch(tag){
					case ConstClass.SPEED_TYPE:
						if( mSpeedType )	mArrayET.get(tag).setText("Km/h");
						else				mArrayET.get(tag).setText("Mp/h");
						mSpeedType = !mSpeedType;
						break;
					case ConstClass.VEHICLE_WIDTH:
						mArrayET.get(tag).setText(""+(mVehicleWidth+1));
						mVehicleWidth = mVehicleWidth+1;
						break;
					case ConstClass.WHEEL_LENGTH: 
						mArrayET.get(tag).setText(""+(mWheelLength+1));
						mWheelLength = mWheelLength+1;
						break;
					case ConstClass.BUMPER_LENGTH: 
						mArrayET.get(tag).setText(""+(mBumperLength+1));
						mBumperLength = mBumperLength+1;
						break;
					case ConstClass.CAMERA_HEIGHT: 
						mArrayET.get(tag).setText(""+(mCameraHeight+1));
						mCameraHeight = mCameraHeight+1;
						break;
					case ConstClass.CAMERA_CENTER: 
						mArrayET.get(tag).setText(""+(mCameraCenter+1));
						mCameraCenter = mCameraCenter+1;
						break;
					}break;
				}
				setAllParameters();			 
			}
		};
		
		@Override
		protected void finalize() throws Throwable {
			// TODO Auto-generated method stub
			free();
			super.finalize();
		}
		private void free(){
			viewHolder = null;
		}
		
		class ViewHolder{
			public TextView textView = null;
			public MyEditText editText = null;
			public Button btnPlus = null;
			public Button btnMinus = null;					
		}
	}	
	
	class MyItem{
		String tx1;
		public MyItem(String tx1){
			
			this.tx1 = tx1;
		}
		public String getString(){
			return tx1;
		}		
	}
}



package com.movon.fcw;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

public class MyEditText extends EditText{
	
	public MyEditText(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	public MyEditText(Context context, AttributeSet attri) {
		// TODO Auto-generated constructor stub
		super(context, attri);
	}
	
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		
	    if (keyCode == KeyEvent.KEYCODE_BACK &&
	        event.getAction() == KeyEvent.ACTION_UP) {

			int data, id;
			int min = 0, max = 0;

			try{
				id = Integer.parseInt(getTag().toString());
			}catch(NumberFormatException ne){
				return true;
			}
			
			try{
				data = Integer.parseInt(getText().toString());
			}catch(NumberFormatException ne){
				
				Toast.makeText(getContext(),
					"Please enter the correct value.\n"
					+"("+min+" ~ "+max+")",
					Toast.LENGTH_SHORT).show();
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
	    		Toast.makeText(getContext(),
				"Please enter the correct value.\n"
				+"("+min+" ~ "+max+")",
				Toast.LENGTH_SHORT).show();
			return true;
	    	}    	
	    }
	    return super.dispatchKeyEvent(event);
	}
}

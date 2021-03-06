package com.driving.touch;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ThirdActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);

		final TypedArray buttons = getResources().obtainTypedArray(
				R.array.second_buttons);
		for (int i = 0; i < buttons.length(); i++) {
			setOnClickListener(null, buttons.getResourceId(i, 0));
		}
	}
	
	private void setOnClickListener(View root, int id) {
		final View target = root != null ? root.findViewById(id)
				: findViewById(id);
		target.setOnClickListener(onClickListener);
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int state = -1;
			switch (v.getId()) {
			case R.id.btn_up:
				Log.d("zoufeng", "R.id.btn_up");
				state = FirstActivity.mNtApi.keyUp();
				break;
			case R.id.btn_down:
				Log.d("zoufeng", "R.id.btn_down");
				state = FirstActivity.mNtApi.keyDown();
				break;
			case R.id.btn_ok:
				Log.d("zoufeng", "R.id.btn_ok");
				state = FirstActivity.mNtApi.keyOk();
				break;
			case R.id.btn_back_previous:
				Log.d("zoufeng", "R.id.btn_back_previous");
				state = FirstActivity.mNtApi.keyBack();
				finish();
				break;
			}
		}
	};
}

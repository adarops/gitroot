package com.ilincar.drivingrecorder2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ilincar.ntmode.NT96655Manager;
import com.ilincar.ntmode.NT96655Manager.NISettingStateCallBack;

@SuppressLint("NewApi")
public class SettingsActivity extends Activity {

	private static final int LIST_ITEM_CHANGE = 1001;

	private ListView car_model, car_item;
	private String[] models_name;
	private int[] carTypeCmd = { R.array.array_video_resolution,
			R.array.array_double_recording, R.array.array_loop_recorder_time,
			R.array.array_recorder_sound, R.array.array_motion_detection,
			R.array.array_g_sensor, R.array.array_exposure_compensation,
			R.array.array_time_watermark, R.array.array_wdr,
			R.array.array_light_balancing, R.array.array_tv_output,
			R.array.array_tv_format, R.array.array_usb_mode,
			R.array.array_sdcard_format, R.array.array_restore_factory };
	ListNameAdpter listNameAdpter;
	ItemNameAdpter itemNameAdpter;
	LinearLayout erase_data;
	TextView erase_data_text;
	Button erase_date_but;
	MyDialog mydialog;
	private ImageButton mImageBackBtn;
	private int[] mMenuItemsValue = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		//back_btn
		mImageBackBtn = (ImageButton) findViewById(R.id.img_back_btn);
		mImageBackBtn.setOnClickListener(onClickListener);
		
		NT96655Manager.getInstance().setNISettingStateCallBack(mSetCallBack);
		NT96655Manager.getInstance().doSyncSettingsMenu();
		car_model = (ListView) findViewById(R.id.car_model);
		car_item = (ListView) findViewById(R.id.car_item);
		erase_data = (LinearLayout) findViewById(R.id.erase_data);
		erase_data_text = (TextView) findViewById(R.id.erase_data_text);
		erase_date_but = (Button) findViewById(R.id.erase_date_but);
		car_model.setOverScrollMode(View.OVER_SCROLL_NEVER);
		car_item.setOverScrollMode(View.OVER_SCROLL_NEVER);
		models_name = getResources().getStringArray(R.array.models_name);
		listNameAdpter = new ListNameAdpter();
		car_model.setAdapter(listNameAdpter);
		car_model.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListNameAdpter adapter = (ListNameAdpter) parent.getAdapter();
				itemNameAdpter.additem(SettingsActivity.this.getResources()
						.getStringArray(carTypeCmd[position]));
				itemNameAdpter.setListPos(position);
				listNameAdpter.setButton(position);
				if (position == (models_name.length - 1)) {
					// 最后一项
					car_item.setVisibility(View.GONE);
					erase_data.setVisibility(View.VISIBLE);
					erase_data_text.setText(getResources().getString(
							R.string.backToStart_note));
					erase_date_but.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mydialog = new MyDialog(SettingsActivity.this,
									R.style.dialog,
									getString(R.string.backToStart),
									getString(R.string.backToStart_note),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											switch (v.getId()) {
											case R.id.determine:
												NT96655Manager.getInstance().restoreFactory();
												break;
											default:
												break;
											}
											mydialog.dismiss();
											mydialog = null;
										}
									});
							mydialog.show();
						}
					});
					erase_data.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
				} else if (position == (models_name.length - 2)) {
					car_item.setVisibility(View.GONE);
					erase_data.setVisibility(View.VISIBLE);
					erase_data_text.setText(getResources().getString(
							R.string.formatcardpro));
					erase_date_but.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mydialog = new MyDialog(SettingsActivity.this,
									R.style.dialog,
									getString(R.string.formatcard),
									getString(R.string.formatcardpro),
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											switch (v.getId()) {
											case R.id.determine:
												NT96655Manager.getInstance().formatSdcard();
												break;
											default:
												break;
											}
											mydialog.dismiss();
											mydialog = null;
										}
									});
							mydialog.show();
						}
					});
					erase_data.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});
				} else {
					car_item.setVisibility(View.VISIBLE);
					erase_data.setVisibility(View.GONE);
					// 设置默认选项
					if(mMenuItemsValue != null)
						itemNameAdpter.setCurrenValue(mMenuItemsValue[position]);
				}
			}
		});
		itemNameAdpter = new ItemNameAdpter();
		car_item.setAdapter(itemNameAdpter);
		car_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ItemNameAdpter adapter = (ItemNameAdpter) parent.getAdapter();
				//Toast.makeText(
				//		SettingsActivity.this,
				//		"you clicked ：" + adapter.getListPos() + ","
				//				+ "position = " + position + adapter.getItem(position), Toast.LENGTH_SHORT).show();
				NT96655Manager.getInstance().doSettingsPara(adapter.getListPos() + 1, position);
				itemNameAdpter.setCurrenValue(position);
			}
		});
	}

	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.img_back_btn:
				finish();
				break;
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case LIST_ITEM_CHANGE:
				//	car_item.
					itemNameAdpter.setCurrenValue(mMenuItemsValue[0]);
					break;
			}
		}
	};

	private NISettingStateCallBack mSetCallBack = new NISettingStateCallBack(){

		@Override
		public void onBackSettingData(int[] items) {
			// TODO Auto-generated method stub
			mMenuItemsValue = items;
			mHandler.sendEmptyMessage(LIST_ITEM_CHANGE);
		}
	};
	
	class ListNameAdpter extends BaseAdapter {
		private int post_butt = 0;

		@Override
		public int getCount() {
			return models_name.length;
		}

		@Override
		public Object getItem(int position) {
			return models_name[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Attribute attribute = null;
			if (convertView == null) {
				attribute = new Attribute();
				convertView = LinearLayout.inflate(SettingsActivity.this,
						R.layout.set_car_type_model_list, null);
				attribute.car_model_text = (TextView) convertView
						.findViewById(R.id.car_model_text);
				convertView.setTag(attribute);
			} else {
				attribute = (Attribute) convertView.getTag();
			}
			String str = (String) getItem(position);
			attribute.car_model_text.setText(str);
			if (post_butt == position) {
				attribute.car_model_text.setTextSize(21);
				attribute.car_model_text.setTextColor(SettingsActivity.this
						.getResources().getColor(R.color.colorLightBlue));
			} else {
				attribute.car_model_text.setTextSize(18);
				attribute.car_model_text.setTextColor(SettingsActivity.this
						.getResources().getColor(R.color.colorWhite));
			}
			return convertView;
		}

		class Attribute {
			TextView car_model_text;
		}

		public void setButton(int i) {
			post_butt = i;
			notifyDataSetChanged();
		}
	}

	class ItemNameAdpter extends BaseAdapter {
		String[] strs;
		// 开始进来的默认值
		private String car_name;
		private int list_pos;

		public ItemNameAdpter() {
			this.strs = SettingsActivity.this.getResources().getStringArray(
					carTypeCmd[0]);
		}

		@Override
		public int getCount() {
			return strs.length;
		}

		@Override
		public Object getItem(int position) {
			return strs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Attribute attribute;
			if (convertView == null) {
				convertView = LinearLayout.inflate(SettingsActivity.this,
						R.layout.setcarobditem, null);
				attribute = new Attribute();
				attribute.car_model_item_text = (TextView) convertView
						.findViewById(R.id.car_model_item_text);
				attribute.car_model_item_image = (ImageView) convertView
						.findViewById(R.id.car_model_item_image);
				convertView.setTag(attribute);
			} else {
				attribute = (Attribute) convertView.getTag();
			}
			String st = strs[position];
			attribute.car_model_item_text.setText(st);
			if (st.equals(car_name)) {
				attribute.car_model_item_image
						.setBackgroundResource(R.drawable.choice_sel);
			} else {
				attribute.car_model_item_image
						.setBackgroundResource(R.drawable.choice);
			}
			return convertView;
		}

		class Attribute {
			TextView car_model_item_text;
			ImageView car_model_item_image;
		}

		public void additem(String[] str) {
			strs = str;
			notifyDataSetChanged();
		}

		public void setCurrenValue(int i) {
			car_name = strs[i];
			notifyDataSetChanged();
		}

		public void setListPos(int i) {
			list_pos = i;
		}

		public int getListPos() {
			return list_pos;
		}
	}
}

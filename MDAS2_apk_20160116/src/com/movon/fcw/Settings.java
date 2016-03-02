package com.movon.fcw;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

public class Settings extends FragmentActivity{

	private ViewPager mPager;
	
	public SharedSettingItem mParam;

	@Override
	public void onCreate( Bundle savedInstanceState ){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
		

		Intent intent = getIntent();
		mParam = (SharedSettingItem)intent.getSerializableExtra("param");
		
		Point display_size = new Point();
		
		getWindowManager().getDefaultDisplay().getSize(display_size);
		
		setContentView(R.layout.settings);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new MyFPager(getSupportFragmentManager()));
	}
	public class MyFPager extends FragmentPagerAdapter{
		
		public MyFPager( FragmentManager fm){
			super(fm);			
		}
		
		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			switch(position){
			case 0: 	return new SettingPage1();
			case 1: 	return new SettingPage2();
			case 2: 	return new SettingPage3();
			case 3: 	return new SettingPage4();
			default:	return null;
			}
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}
	}
	
	public ViewPager getViewPager(){
		if(mPager == null){
			mPager = (ViewPager)findViewById(R.id.pager);
		}
		return mPager;
	}
}
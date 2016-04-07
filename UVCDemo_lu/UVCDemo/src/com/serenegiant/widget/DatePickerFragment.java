package com.serenegiant.widget;

import java.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

/**
 * 日期选择对话框
 * @see DatePickerFragment
 * */
@SuppressLint("ValidFragment") 
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

	private View v;
	private String result;
	
	public DatePickerFragment(Context context, View view) {
		this.v = view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int month,
			int day) {
		month++;
		String monthStr = month + "";
		String dayStr = day + "";
		if (month < 10) {
			monthStr = "0" + month;
		}
		if (day < 10) {
			dayStr = "0" + day;
		}
		((TextView) v).setText(year + "年" + monthStr + "月" + dayStr + "日");
		result = year + "-" + monthStr + "-" + dayStr;
		
	}

	public String getResult() {
		return result;
	}
	
	

}

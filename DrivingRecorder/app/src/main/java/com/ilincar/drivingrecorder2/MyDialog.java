package com.ilincar.drivingrecorder2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {
	private String title, text;
	private TextView title_dialog, context_dialog;
	private Button determine, cancel;
	private View.OnClickListener onClickListener;

	public MyDialog(Context context, int theme, String title, String text,
			View.OnClickListener onClickListener) {
		super(context, theme);
		this.title = title;
		this.text = text;
		this.onClickListener = onClickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_obdmodels_dialog);
		determine = (Button) findViewById(R.id.determine);
		determine.setOnClickListener(this.onClickListener);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this.onClickListener);
		title_dialog = (TextView) findViewById(R.id.title_dialog);
		title_dialog.setText(title);
		context_dialog = (TextView) findViewById(R.id.context_dialog);
		context_dialog.setText(text);
	}
}

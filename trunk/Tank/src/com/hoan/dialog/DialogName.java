package com.hoan.dialog;

import com.hoan.StaticDefine.ScreenStatic;
import com.hoan.tank.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DialogName extends Dialog {

	private EditText edittext_name;
	private Button mBtnOK;
	public DialogName(Context context,final Activity activity) {
		super(context);
		this.setTitle("Name:");
		this.setContentView(R.layout.dialog_name);
		this.setCanceledOnTouchOutside(false);
		edittext_name = (EditText)findViewById(R.id.get_name_player);
		mBtnOK = (Button)findViewById(R.id.get_name_ok);
	}

	public String getText(){
		return edittext_name.getEditableText().toString();
	}

	public void setOnOKClick(View.OnClickListener listener){
		mBtnOK.setOnClickListener(listener);
	}
}

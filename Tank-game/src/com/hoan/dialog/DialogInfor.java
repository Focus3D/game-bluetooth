package com.hoan.dialog;

import com.hoan.tank.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogInfor extends Dialog{

	public DialogInfor(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_thongtin);		
		
		Button button_thongtin = (Button)findViewById(R.id.button_thongtin);
		button_thongtin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				DialogInfor.this.dismiss();				
			}
		});
	}
}


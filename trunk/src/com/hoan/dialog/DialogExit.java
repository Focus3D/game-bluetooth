package com.hoan.dialog;

import com.hoan.tank.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class DialogExit extends Dialog{
	
	public boolean bool_thoat = false;

	public DialogExit(Context context,final Activity activity) {
		super(context);
		this.setTitle(Html.fromHtml("<b>EXIT</b>"));
		this.setContentView(R.layout.dialog_thoat);
		
		Button exit = (Button)findViewById(R.id.exit);
		exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				bool_thoat = true;
				if(activity != null)
					activity.finish();
				DialogExit.this.dismiss();
			}
		});
		
		Button no = (Button)findViewById(R.id.noexit);
		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bool_thoat = false;		
				DialogExit.this.dismiss();
			}
		});
	}
	
}

package com.hoan.MainGame;

import com.hoan.tank.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Win extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.win);
		
		Button ok = (Button)findViewById(R.id.winOK);
		ok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				
			}
		});
	}
	

}

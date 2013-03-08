package com.hoan.MainGame;

import com.hoan.Database.Database;
import com.hoan.Sound.Sound;
import com.hoan.StaticDefine.ControlerStatic;
import com.hoan.dialog.DialogExit;
import com.hoan.tank.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ChienThang extends Activity {
	int DIEM = 0;
	private Database db;


	public Sound nhac_nen_win;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chienthang);

		nhac_nen_win = new Sound(this, R.raw.nhac_nen_win);
		if(ControlerStatic.SOUND)
			nhac_nen_win.play();
		
		Bundle bun = getIntent().getExtras();

		if (bun != null) {
			DIEM = bun.getInt("diem");
		}

		db = new Database(this);

		Button b = (Button) findViewById(R.id.button_dongy);
		b.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				nhac_nen_win.stop();
				if (db.kt_luu(DIEM)) {
					Intent i = new Intent(ChienThang.this, Luudiem.class);
					i.putExtra("diem", DIEM);
					ChienThang.this.startActivity(i);
					ChienThang.this.finish();
				} else {
					Intent i = new Intent(ChienThang.this, TabActivity.class);
					i.putExtra("diem", DIEM);
					ChienThang.this.startActivity(i);
					ChienThang.this.finish();
				}
			}
		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			DialogExit dialogexit = new DialogExit(ChienThang.this,
					ChienThang.this);
			dialogexit.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}


package com.hoan.MainGame;

import com.hoan.Database.Database;
import com.hoan.Sound.Sound;
import com.hoan.StaticDefine.ControlerStatic;
import com.hoan.dialog.DialogExit;
import com.hoan.dialog.DialogXemDiem;
import com.hoan.tank.R;
import com.hoan.tank.TankActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Luudiem extends Activity implements OnClickListener {

	Button menu, choilai, luu, xem_diem_cao;
	TextView text_diem;
	EditText edit_ten;
	private Database db;
	
	Sound nhac_nen_gameover;
	
	
	int diem = 0;
	// ------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.luudiem);
		
		nhac_nen_gameover = new Sound(this, R.raw.nhac_nen_gameover);
		if(ControlerStatic.SOUND)
			nhac_nen_gameover.play();
		
		db = new Database(this);
		
		Bundle b = getIntent().getExtras();
		
		if(b != null){
			diem = b.getInt("diem");
		}

		choilai = (Button) findViewById(R.id.choilai);
		choilai.setOnClickListener(this);
		
		luu = (Button) findViewById(R.id.luu);
		luu.setOnClickListener(this);
		
		menu = (Button) findViewById(R.id.menu);
		menu.setOnClickListener(this);
		
		xem_diem_cao = (Button) findViewById(R.id.xem_diem_cao);
		xem_diem_cao.setOnClickListener(this);
		
		
		text_diem = (TextView) findViewById(R.id.diem);
		text_diem.setText(String.valueOf(diem));
		
		edit_ten = (EditText) findViewById(R.id.name);
	}

	// ------------------------------------------------------------------------------
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.choilai: {
				nhac_nen_gameover.stop();
				choilai();
				break;
			}
			case R.id.luu: {
				luu();
				break;
			}
			case R.id.menu: {
				Intent i = new Intent(this, TankActivity.class);
				nhac_nen_gameover.stop();
				this.startActivity(i);
				this.finish();
				break;
			}
			case R.id.xem_diem_cao: {
				xemdiemcao();
				break;
			}
		}
	}
	// ------------------------------------------------------------------------------
	private void luu() {
		String name = edit_ten.getText().toString();
		int diemchoi = Integer.parseInt(text_diem.getText().toString());
		// Nếu chưa nhập tên thì yêu cầu nhập tên
		if (name.length() == 0 || name == null) {
			Toast.makeText(this, "Bạn chưa nhập tên.", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			db.open();
			Cursor c = db.getAllRows();
			if (c.moveToPosition(9)) {
				String id = c.getString(c.getColumnIndex(Database.KEY_ROWID));
				int math = c.getInt(c.getColumnIndex(Database.KEY_DIEM));
						if (math < diemchoi) {
							db.updatePlayer(id, name, diemchoi);
							LinearLayout l = (LinearLayout)findViewById(R.id.linearLayout_diem);
							l.setVisibility(View.GONE);
							luu.setVisibility(View.GONE);
							xem_diem_cao.setVisibility(View.VISIBLE);
							xemdiemcao();
						}

			}
			c.close();
			db.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}

	public void xemdiemcao(){
		DialogXemDiem xemdiem = new DialogXemDiem(this);
		xemdiem.show();
	}
	// ------------------------------------------------------------------------------
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		this.finish();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	};

	// ------------------------------------------------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			DialogExit dialogexit = new DialogExit(this,this);
			nhac_nen_gameover.stop();
			dialogexit.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	// ------------------------------------------------------------------------------
	private void choilai() {
		Intent i = new Intent(this, MainGameSingleActivity.class);
		this.startActivity(i);
		this.finish();
	}
}



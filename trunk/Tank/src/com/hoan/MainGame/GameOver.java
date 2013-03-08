package com.hoan.MainGame;


import com.hoan.Sound.Sound;
import com.hoan.StaticDefine.ControlerStatic;
import com.hoan.dialog.DialogExit;
import com.hoan.tank.R;
import com.hoan.tank.TankActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameOver extends Activity{

	Sound nhac_nen_gameover;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gameover);
		
		
		nhac_nen_gameover = new Sound(this, R.raw.nhac_nen_gameover);
		if(ControlerStatic.SOUND)
			nhac_nen_gameover.play();
		
		Button button_gameover_choilai = (Button)findViewById(R.id.button_gameover_choilai);
		Button button_gameover_mainmenu = (Button)findViewById(R.id.button_gameover_mainmenu);
		Button button_gameover_thoat = (Button)findViewById(R.id.button_gameover_thoat);
		
		button_gameover_choilai.setOnClickListener(new View.OnClickListener() {			
			
			public void onClick(View arg0) {
				Intent i = new Intent(GameOver.this, MainGameSingleActivity.class);
				GameOver.this.startActivity(i);
				GameOver.this.finish();
			}
		});
		
		button_gameover_mainmenu.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				Intent i = new Intent(GameOver.this, TankActivity.class);
				GameOver.this.startActivity(i);
				GameOver.this.finish();
			}
		});

		button_gameover_thoat.setOnClickListener(new View.OnClickListener() {			
			
			public void onClick(View arg0) {
				DialogExit dialogexit = new DialogExit(GameOver.this,GameOver.this);
				nhac_nen_gameover.stop();
				dialogexit.show();
				
			}
		});
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			DialogExit dialogexit = new DialogExit(GameOver.this, GameOver.this);
			nhac_nen_gameover.stop();
			dialogexit.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}

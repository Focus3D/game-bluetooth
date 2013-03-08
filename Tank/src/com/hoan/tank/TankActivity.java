package com.hoan.tank;

import java.security.PublicKey;

import com.hoan.bluetooth.*;

import com.hoan.tank.R;
import com.hoan.Database.*;
import com.hoan.MainGame.MainGameSingleActivity;
import com.hoan.Sound.Sound;
import com.hoan.StaticDefine.ControlerStatic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import com.hoan.dialog.*;


public class TankActivity extends Activity {

	public Database db;
	private ImageButton exit;
	private ImageButton info;
	private ImageButton multi;
	private ImageButton single;
	
	Sound nhac_nen;
	int index = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tank);
        
        nhac_nen = new Sound(this, R.raw.nhac_nen_menu);
		if(ControlerStatic.SOUND)
			nhac_nen.play();
     // Tạo cơ sở dữ liệu để lưu điểm
     		csdlMau();
        //////////////////////////////////////////////////////////////////////
        single = (ImageButton)findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) {
				Intent i = new Intent(TankActivity.this, MainGameSingleActivity.class);
				TankActivity.this.startActivity(i);
		//		TankActivity.this.finish();
				
			}
		});
        ////////////////////////////////////////////////////////////////////////
        multi = (ImageButton)findViewById(R.id.multi);
        multi.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) {
				try {

					Intent i = new Intent(TankActivity.this, RemoteBluetooth.class);
					TankActivity.this.startActivity(i);
				} catch (Exception e) {
					Toast.makeText(TankActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
		//		TankActivity.this.finish();
				
			}
		});
        ////////////////////////////////////////////////////////////////////////
        info = (ImageButton)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				DialogInfor dialog_info = new DialogInfor(TankActivity.this);
				dialog_info.show();
			}
		});
        ////////////////////////////////////////////////////////////////////////
        exit = (ImageButton)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() 
        {
			
			@SuppressLint("ParserError")
			public void onClick(View v) 
			{
				DialogExit dialog_exit = new DialogExit(TankActivity.this, TankActivity.this);
				dialog_exit.show();
				
			}
		});
        //////////////////////////////////////////////////////////////////////////////
      
        final ImageView imageView_sound = (ImageView)findViewById(R.id.imageView_sound);
		imageView_sound.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(ControlerStatic.SOUND){//Đang mở. Yêu cầu tắt
					imageView_sound.setImageResource(R.drawable.sound_off);
					ControlerStatic.SOUND = false;
					if(nhac_nen.mPlaying)
						nhac_nen.stop();
				}else{//Đang tắt. Yêu cầu mở
					imageView_sound.setImageResource(R.drawable.sound_on);
					ControlerStatic.SOUND = true;
					if(!nhac_nen.mPlaying)
						nhac_nen.play();
				}
			}
		});
		  
        
    }
    
    
    protected void onDestroy() {
		if(nhac_nen != null)
			nhac_nen.release();
		super.onDestroy();
	}
    ///////////
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			DialogExit dialogexit = new DialogExit(TankActivity.this, TankActivity.this);
			dialogexit.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void csdlMau() {
		// Lần đầu khi bắt đầu chơi
		// Giả lập dữ liệu mẫu
		db = new Database(this);
		// Dữ liệu mẫu gồm 10 người
		try {
			db.open();
			Cursor c = db.getAllRows();
			if (!c.moveToFirst()) {
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
				db.isertRow("Player", 100);
			}
			c.close();
			db.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}

    
}

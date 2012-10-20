package com.hoan.tank;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.support.v4.app.NavUtils;
import com.hoan.dialog.*;

public class TankActivity extends Activity {

	private ImageButton exit;
	private ImageButton info;
	private ImageButton multi;
	private ImageButton single;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tank);
        
        //////////////////////////////////////////////////////////////////////
        single = (ImageButton)findViewById(R.id.single);
        single.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        ////////////////////////////////////////////////////////////////////////
        multi = (ImageButton)findViewById(R.id.multi);
        multi.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        ////////////////////////////////////////////////////////////////////////
        info = (ImageButton)findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
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
			@Override
			public void onClick(View v) 
			{
				DialogExit dialog_exit = new DialogExit(TankActivity.this, TankActivity.this);
				dialog_exit.show();
				
			}
		});
    }



    
}

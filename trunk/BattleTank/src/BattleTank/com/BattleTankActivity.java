package BattleTank.com;

import BattleTank.com.Dialog.Dialog_Info;
import BattleTank.com.MainGame.MainGameActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class BattleTankActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		final AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
		myDialog.setTitle("Exit Game");
		myDialog.setMessage("Bạn có muốn thoát không ?");

		myDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {
						BattleTankActivity.this.finish();
						dialog.cancel();

					}
				});

		

		myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface diablog, int arg1) {
				diablog.dismiss();

			}
		});
		
		final ImageButton imageButton_Start = (ImageButton) findViewById(R.id.imageButton_Start);
		imageButton_Start.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent i = new Intent(BattleTankActivity.this, MainGameActivity.class);
				BattleTankActivity.this.startActivity(i);
				BattleTankActivity.this.finish();
				
			}
		});
		
		

		final ImageButton imageButton_Info = (ImageButton) findViewById(R.id.imageButton_Info);
		imageButton_Info.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Dialog_Info dialog_info = new Dialog_Info(
						BattleTankActivity.this);
				dialog_info.show();
			}
		});
		
		
		final AlertDialog.Builder dialog_test = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		dialog_test.setView(inflater.inflate(R.layout.dialog_test, null))
				// Add action buttons
				.setPositiveButton("SignIn",new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int id) {
								// sign in the user ...
							}
						})
				.setNegativeButton("Canel",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							}
						});

		final ImageButton imageButton_Exit = (ImageButton) findViewById(R.id.imageButton_Exit);
		imageButton_Exit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				AlertDialog diablog_exit = dialog_test.create();
				dialog_test.show();

			}
		});
	}
}
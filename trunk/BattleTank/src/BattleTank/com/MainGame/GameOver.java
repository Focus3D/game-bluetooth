package BattleTank.com.MainGame;
import BattleTank.com.BattleTankActivity;
import BattleTank.com.R;
import BattleTank.com.Dialog.Dialog_exit;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class GameOver extends Activity{

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gameover);
		
		Button button_gameover_choilai = (Button)findViewById(R.id.button_gameover_choilai);
		Button button_gameover_mainmenu = (Button)findViewById(R.id.button_gameover_mainmenu);
		Button button_gameover_thoat = (Button)findViewById(R.id.button_gameover_thoat);
		
		button_gameover_choilai.setOnClickListener(new View.OnClickListener() {			
			
			public void onClick(View arg0) {
				Intent i = new Intent(GameOver.this, MainGameActivity.class);
				GameOver.this.startActivity(i);
				GameOver.this.finish();
			}
		});
		
		button_gameover_mainmenu.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				Intent i = new Intent(GameOver.this, BattleTankActivity.class);
				GameOver.this.startActivity(i);
				GameOver.this.finish();
			}
		});

		button_gameover_thoat.setOnClickListener(new View.OnClickListener() {			
			
			public void onClick(View arg0) {
				Dialog_exit dialogexit = new Dialog_exit(GameOver.this,GameOver.this);
				dialogexit.show();
			}
		});
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			Dialog_exit dialogexit = new Dialog_exit(GameOver.this, GameOver.this);
			dialogexit.show();
		}
		return super.onKeyDown(keyCode, event);
	}
}
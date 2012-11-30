package BattleTank.com.Dialog;

import BattleTank.com.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Dialog_Info extends Dialog {

	private TextView textview_Info;

	public Dialog_Info(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_info);
		Button bt_Exit = (Button) findViewById(R.id.bt_exitInfo);
		bt_Exit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				Dialog_Info.this.dismiss();

			}
		});

	}
}

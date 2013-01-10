package BattleTank.com.Dialog;

import BattleTank.com.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.Button;

public class Dialog_exit extends Dialog{

	public boolean bool_thoat = false;

	public Dialog_exit(Context context,final Activity activity) {
		super(context);
		this.setTitle(Html.fromHtml("<b>Thoát.</b>"));
		this.setContentView(R.layout.dialog_thoat);
		
		Button exit = (Button)findViewById(R.id.button_thoat_co);
		exit.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View arg0) {
				bool_thoat = true;
				if(activity != null)
					activity.finish();
				Dialog_exit.this.dismiss();
			}
		});
		
		Button no = (Button)findViewById(R.id.button_thoat_khong);
		no.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				bool_thoat = false;		
				Dialog_exit.this.dismiss();
			}
		});
	}
	
}

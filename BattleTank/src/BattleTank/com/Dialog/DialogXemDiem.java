package BattleTank.com.Dialog;

import BattleTank.com.R;
import BattleTank.com.Database.Database;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogXemDiem extends Dialog implements android.view.View.OnClickListener {
	private Database db;
	private TextView[] text_diem, text_name;
	public DialogXemDiem(Context context) {
		super(context);
		setContentView(R.layout.dialog_xemdiem);	
		this.setTitle(Html.fromHtml("<b>Danh sách điểm cao.</b>"));
		
		db = new Database(context);
		
		text_diem = new TextView[10];
		text_name = new TextView[10];
		
		//fin các text
		text_name[0] = (TextView)findViewById(R.id.name_1);
		text_name[1] = (TextView)findViewById(R.id.name_2);
		text_name[2] = (TextView)findViewById(R.id.name_3);
		text_name[3] = (TextView)findViewById(R.id.name_4);
		text_name[4] = (TextView)findViewById(R.id.name_5);
		text_name[5] = (TextView)findViewById(R.id.name_6);
		text_name[6] = (TextView)findViewById(R.id.name_7);
		text_name[7] = (TextView)findViewById(R.id.name_8);
		text_name[8] = (TextView)findViewById(R.id.name_9);
		text_name[9] = (TextView)findViewById(R.id.name_10);
		
		
		text_diem[0] = (TextView)findViewById(R.id.diem_1);
		text_diem[1] = (TextView)findViewById(R.id.diem_2);
		text_diem[2] = (TextView)findViewById(R.id.diem_3);
		text_diem[3] = (TextView)findViewById(R.id.diem_4);
		text_diem[4] = (TextView)findViewById(R.id.diem_5);
		text_diem[5] = (TextView)findViewById(R.id.diem_6);
		text_diem[6] = (TextView)findViewById(R.id.diem_7);
		text_diem[7] = (TextView)findViewById(R.id.diem_8);
		text_diem[8] = (TextView)findViewById(R.id.diem_9);
		text_diem[9] = (TextView)findViewById(R.id.diem_10);
		
		updateList();
		
		Button dong = (Button)findViewById(R.id.dong);
		Button xoa = (Button)findViewById(R.id.xoa);
		
		xoa.setOnClickListener(this);
		dong.setOnClickListener(this);
	}
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.dong:{
				this.dismiss();
				break;
			}
			case R.id.xoa:{
				db.open();
				db.deleteAllRows();
				db.close();
				csdlMau();
				updateList();
				break;
			}
			default: break;
		}		
	}
	//Phương thức cập nhật danh sách điểm
	public void updateList(){
		int i = 0;
		try {
			db.open();
			Cursor c = db.getAllRows();
			if (c.moveToFirst()) {
				do {
					String name = c.getString(c.getColumnIndex(Database.KEY_NAME));
					int math = c.getInt(c.getColumnIndex(Database.KEY_DIEM));
					text_name[i].setText(name);
					text_diem[i].setText(String.valueOf(math));
					if (i == 9)
						break;
					else
						i++;
				} while (c.moveToNext());
			}
			c.close();
			db.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
	}
	
	// --------------------------------------------------------------------------
	public void csdlMau() {
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

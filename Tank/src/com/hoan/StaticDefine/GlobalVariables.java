package com.hoan.StaticDefine;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalVariables {
	public static JSONObject mJSONExchange = null;
	public static boolean isServer = true;
	
	// Âm thanh
	public static boolean SOUND = true;
	// Loai thong diep 
	public static final int MOVE = 1;
	public static final int FIRE = 2;
	public static final int MAPS = 3;
	public static final int SERVER_CREATE = 4;
	public static final int JOIN_CLIENT = 5;
	public static final int START = 6;
	public static final int PAUSE = 7;
	
	// thong diep dieu khien	
	public static int MENU_STATUS = 0;
 
	
	public static boolean SHOOT_STATUS = false;
	public static String MY_NAME = "";
	public static String HOST_NAME = "";
	public static String FRIEND_NAME = "";
	public static int DIRECTION = 0;
	public static float POSSTIONX = 0;
	public static float POSSTIONY = 0;
	public static int MAP_INDEX =0;


}

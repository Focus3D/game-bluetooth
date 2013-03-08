package com.hoan.Communicate;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.hoan.StaticDefine.GlobalVariables;
import com.hoan.bluetooth.BluetoothCommandService;
import com.hoan.bluetooth.RemoteBluetooth;
public class Communicate {
	private JSONObject jSonExchange; 
	private Handler mHandler;
	private BluetoothCommandService mService;
	
	public Communicate(BluetoothCommandService cmd){
		jSonExchange = GlobalVariables.mJSONExchange;
		mHandler = new Handler();
	//	mService = new BluetoothCommandService(mHandler);
		mService = cmd;
	}
	
	public int getAction(){
		if(jSonExchange != null){
			try {
				JSONObject jSonOb = new JSONObject();
				jSonOb = jSonExchange.getJSONObject("Data");
				int action_type = jSonExchange.getInt("message");
				if(action_type == GlobalVariables.MOVE){
					// derection : 1- left 2-right 3-up 4-down
					GlobalVariables.DIRECTION = jSonOb.getInt("direction");
				}else if(action_type == GlobalVariables.FIRE){
					// fire type : 1-buf 2-rocket
			//		GlobalVariables.FIRE = jSonOb.getInt("FireType");
					// hostname khoi tao = "" khi hostname thay doi la host da duoc tao.
				}else if (action_type == GlobalVariables.SERVER_CREATE) {
					GlobalVariables.HOST_NAME = jSonOb.getString("ServerCreat"); 
				}else if (action_type == GlobalVariables.JOIN_CLIENT) {
					// khi bam join thi se gui ten nguoi choi sang ben host
					GlobalVariables.FRIEND_NAME = jSonOb.getString("joinServer");
				}else if (action_type == GlobalVariables.MAPS) {
					// chi so cua map duoc chon
					GlobalVariables.MAP_INDEX = jSonOb.getInt("MapIndex");
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	private void send(int code, JSONObject jSonObject){
		try {
			JSONObject object = new JSONObject();
			object.put("message", code);
			object.put("Data", jSonObject);
			// send JSON: object
			mService.sendJson(object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMove(int div){
		JSONObject obj = new JSONObject();
		try {
			obj.put("direction", div);
			send(GlobalVariables.MOVE, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sendFire(int fire_type){
		JSONObject obj = new JSONObject();
		try {
			obj.put("FireType", fire_type);
			send(GlobalVariables.FIRE, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sendJoinServer(String nameClient){
		JSONObject obj = new JSONObject();
		try {
			obj.put("JoinServer", nameClient);
			send(GlobalVariables.JOIN_CLIENT, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sendServerCreate(String nameServer){
		JSONObject obj = new JSONObject();
		try {
			obj.put("ServerCreate", nameServer);
			send(GlobalVariables.SERVER_CREATE, obj);
			System.out.println(nameServer);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMapIndex(int index){
		JSONObject obj = new JSONObject();
		try {
			obj.put("MapIndex",index);
			send(GlobalVariables.MAPS, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

package com.hoan.bluetooth;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.text.BreakIterator;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.hoan.Communicate.Communicate;
import com.hoan.MainGame.MainGameActivity;
import com.hoan.MainGame.MainGameSingleActivity;
import com.hoan.StaticDefine.GlobalVariables;
import com.hoan.StaticDefine.ScreenStatic;
import com.hoan.dialog.DialogExit;
import com.hoan.dialog.DialogName;
import com.hoan.tank.R;
import com.hoan.tank.TankActivity;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RemoteBluetooth extends Activity {
	private static CommandWacher  mWacher;
	// Layout view
	private TextView mTitle;
	
	// Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Key names received from the BluetoothCommandService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

	private final int []map ={R.drawable.map1 , R.drawable.map2};
	// Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for Bluetooth Command Service
    private BluetoothCommandService mCommandService = null;
    private Communicate mCommunicate;
    
    private ImageButton mConnectGame;
    private ImageButton mJoinGame;
    private ImageButton mStartGame;
    private ImageButton mSellectMap;
    private ImageButton mMap;
    private ImageButton mCreateGame;
    private TextView mTextviewLayer;
    private ListView mListViewLayer;
    public int mMapMax = 2;
    private int int_map = 0;
    
    private ArrayAdapter<String> arrayAdapter;
    private final ArrayList<String> arrayPlayer = new ArrayList<String>();
    
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        // Set up the window layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.connectgame);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        
        
        mConnectGame = (ImageButton)findViewById(R.id.connect_game);
        mConnectGame.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ParserError")
			public void onClick(View v) {
				
				Connect_bluetooth();
			}
		});
     // Set up the custom title
        mTitle = (TextView) findViewById(R.id.title_left_text);  
        mTitle.setText(R.string.app_name);
        mTitle = (TextView) findViewById(R.id.title_right_text);
        } catch (Exception e) {
			Toast.makeText(RemoteBluetooth.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();   
            return;
        }
        mJoinGame = (ImageButton)findViewById(R.id.join_game);
        mStartGame = (ImageButton)findViewById(R.id.start_game);
        mSellectMap= (ImageButton)findViewById(R.id.select_map_button);
        mCreateGame = (ImageButton)findViewById(R.id.create_game);
        mListViewLayer = (ListView)findViewById(R.id.listview_layer);
        mMap = (ImageButton)findViewById(R.id.select_map);
        mMap.setEnabled(false);
       
        
       
       arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayPlayer);
       mListViewLayer.setAdapter(arrayAdapter);
        mCreateGame.setOnClickListener(new View.OnClickListener() {
        	String nameServer = "";
			public void onClick(View v) {
				GlobalVariables.isServer = true;
				if(arrayPlayer.size() == 0 && mCommandService.getState() == BluetoothCommandService.STATE_CONNECTED){
					final DialogName dialogName = new DialogName(RemoteBluetooth.this, RemoteBluetooth.this);
					dialogName.setOnOKClick(new View.OnClickListener() {				
						public void onClick(View v) {
							nameServer = dialogName.getText();
							GlobalVariables.HOST_NAME = nameServer;
							GlobalVariables.MY_NAME = nameServer;
							arrayAdapter.add(nameServer);
							sendServerCreate(nameServer);
							arrayAdapter.notifyDataSetChanged();						
							dialogName.dismiss();
						}
					});
					dialogName.show();			
				} GlobalVariables.isServer = true;
			}
		});
        mJoinGame.setOnClickListener(new View.OnClickListener() {
        	String ClientName = "";
			public void onClick(View v) {
				if(GlobalVariables.HOST_NAME != "" && arrayPlayer.size() <= 2 && GlobalVariables.HOST_NAME != GlobalVariables.MY_NAME){
					final DialogName dialogName = new DialogName(RemoteBluetooth.this, RemoteBluetooth.this);
					dialogName.setOnOKClick(new View.OnClickListener() {
						public void onClick(View v) {
							ClientName = dialogName.getText();
							arrayAdapter.add(ClientName);
							sendJoinGame(ClientName);
							arrayAdapter.notifyDataSetChanged();
							dialogName.dismiss();
						}
					});
					dialogName.show();
					mStartGame.setEnabled(false);
					mSellectMap.setEnabled(false);	
				 GlobalVariables.isServer = false;
				}
			}
		});
        
        mSellectMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(GlobalVariables.HOST_NAME != null ){
					int_map++;
					if(int_map == mMapMax)
					{
						int_map =0;
					}
					mSellectMap.setImageResource(map[int_map]);
					sendMapIndex(int_map);
					GlobalVariables.MAP_INDEX = int_map;			
				}
			}
		});
        
        mStartGame.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
		//		if(GlobalVariables.isServer){
					arrayPlayer.clear();
					arrayAdapter.clear();
					arrayAdapter.notifyDataSetChanged();
					MainGameActivity.mCommandService = mCommandService;
					Intent i = new Intent(RemoteBluetooth.this, MainGameActivity.class);
					RemoteBluetooth.this.startActivity(i);	
					sendStartGame();
	//				RemoteBluetooth.this.finish();
		//		}
			}
		});
        
        
    }
	
	public static void setCommandWacher(CommandWacher wacher){
		mWacher = wacher;
	}
	private void sendStartGame(){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("message", GlobalVariables.START);
			JSONObject JsonO = null;
			jObject.put("Data", JsonO);
			mCommandService.write(jObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendMapIndex(int mapIndex){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("message", GlobalVariables.MAPS);
			JSONObject JsonO = new JSONObject();
			JsonO.put("MapIndex", mapIndex);
			jObject.put("Data", JsonO);
			mCommandService.write(jObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void sendServerCreate(String nameServer){
		JSONObject jObject = new  JSONObject();
		try {
			jObject.put("message", GlobalVariables.SERVER_CREATE);
			JSONObject ob = new JSONObject();
			ob.put("ServerCreate", nameServer);
			jObject.put("Data", ob);
			mCommandService.write(jObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void sendJoinGame(String nameClient){
		JSONObject jObject = new  JSONObject();
		try {
			jObject.put("message", GlobalVariables.JOIN_CLIENT);
			JSONObject jsonO = new JSONObject();
			jsonO.put("JoinServer", nameClient);
			jObject.put("Data", jsonO);
			mCommandService.write(jObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
    
	@Override
	protected void onStart() {
		super.onStart();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
		// otherwise set up the command service
		else {
			if (mCommandService==null)
				setupCommand();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (mCommandService != null) {
			if (mCommandService.getState() == BluetoothCommandService.STATE_NONE) {
				mCommandService.start();
			}
		}
	}

	private void setupCommand() {
		// Initialize the BluetoothChatService to perform bluetooth connections
        mCommandService = new BluetoothCommandService(mHandler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mCommandService != null)
			mCommandService.stop();
	}
	
	private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothCommandService.STATE_CONNECTED:
                    mTitle.setText(R.string.title_connected_to);
                    mTitle.append(mConnectedDeviceName);
                    break;
                case BluetoothCommandService.STATE_CONNECTING:
                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothCommandService.STATE_LISTEN:
                case BluetoothCommandService.STATE_NONE:
                    mTitle.setText(R.string.title_not_connected);
                    break;
                } 
                break;
            case MESSAGE_READ:
            	byte[] writeBuf = (byte[]) msg.obj;
                String writeMessage = new String(writeBuf);
                try {
					JSONObject jSon = new JSONObject(writeMessage);
					switch (jSon.getInt("message")) {
						case GlobalVariables.SERVER_CREATE:
							JSONObject jsonObjiect = jSon.getJSONObject("Data");
							String str = jsonObjiect.getString("ServerCreate");
							GlobalVariables.HOST_NAME = str;
							GlobalVariables.FRIEND_NAME = str;
							arrayAdapter.add(str);
							arrayAdapter.notifyDataSetChanged();
							break;
						case GlobalVariables.JOIN_CLIENT:
							JSONObject jsonObject = jSon.getJSONObject("Data");
							String str1 = jsonObject.getString("JoinServer");
							GlobalVariables.FRIEND_NAME = str1;
							arrayAdapter.add(str1);
							arrayAdapter.notifyDataSetChanged();
							break;
						case GlobalVariables.MAPS:
							JSONObject jsonObject1 = jSon.getJSONObject("Data");
							int map_index = jsonObject1.getInt("MapIndex");
							GlobalVariables.MAP_INDEX = map_index;
							mSellectMap.setImageResource(map[map_index]);
							break;
						case GlobalVariables.START:
							if(!GlobalVariables.isServer){
								MainGameActivity.mCommandService = mCommandService;
								arrayPlayer.clear();
								arrayAdapter.clear();
								arrayAdapter.notifyDataSetChanged();
								Intent i = new Intent(RemoteBluetooth.this, MainGameActivity.class);
								RemoteBluetooth.this.startActivity(i);	
							}
							break;
						case GlobalVariables.MOVE:
							JSONObject jsonObject2 = jSon.getJSONObject("Data");
							GlobalVariables.DIRECTION =jsonObject2.getInt("direction");
							GlobalVariables.POSSTIONX = (float)jsonObject2.getDouble("possitionX");
							GlobalVariables.POSSTIONY = (float)jsonObject2.getDouble("possitionY");
							break;
						case GlobalVariables.PAUSE:
							JSONObject jsonObject4 = jSon.getJSONObject("Data");
							if(mWacher != null){
								mWacher.onComand(GlobalVariables.PAUSE, jsonObject4);
							}
							break;
						case GlobalVariables.FIRE:
							GlobalVariables.SHOOT_STATUS = true;
							JSONObject jsonObject3 = jSon.getJSONObject("Data");
							GlobalVariables.DIRECTION = jsonObject3.getInt("direction");
							break;
						default:
							break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                mCommandService.connect(device);
                arrayPlayer.clear();
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                setupCommand();
            } else {
                // User did not enable Bluetooth or an error occured
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            return true;
        case R.id.discoverable:
            ensureDiscoverable();
            return true;
        }
        return false;
    }
	public void Connect_bluetooth()
	{
		Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	public static interface CommandWacher{
		public void onComand(int indexCommand, JSONObject jsonObject );
	}

}
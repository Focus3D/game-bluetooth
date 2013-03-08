package com.hoan.MainGame;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.support.v4.app.NavUtils;

import com.hoan.Bullet.Boom;
import com.hoan.Bullet.Bullet;
import com.hoan.Maps.*;
import com.hoan.Player.*;
import com.hoan.StaticDefine.*;
import com.hoan.bluetooth.BluetoothCommandService;
import com.hoan.bluetooth.RemoteBluetooth;
import com.hoan.dialog.DialogExit;
import com.hoan.tank.TankActivity;

public class MainGameActivity extends BaseGameActivity implements IOnMenuItemClickListener{
	
	private boolean fired = false;
	private boolean fireFinish = true;
	private boolean collision = false;
	private boolean collision1 = false;
	private long time = 0;
	
	private int bulletIndex = 0;
	private int bulletIndexFriend = 0;
	private int myDirection = 0;
	public static BluetoothCommandService mCommandService;
	private Player MyPlayer;
	private Player FriendPlayer;
	
	private TMXTiledMap mTMXTileMap;
	private TMXLayer vatcanTMXLayer;
	
	private Font mFont;
	private Font mFont1;
	private BitmapTextureAtlas mFontTexTure;
	private BitmapTextureAtlas mFontTexTure1;
	
	private ChangeableText myScoreText;
	private ChangeableText frienfScoreText;
	private ChangeableText vsText;
	private ChangeableText namePlayer;
	private ChangeableText nameFriendPlayer;
	
	private static final int CONTINUE = 0;
	private static final int NEWGAME = CONTINUE + 1;
	private static final int MAINMENU  = NEWGAME + 1;
	private static final int QUIT = MAINMENU + 1;
	private MenuScene mMenuScene;
	
	
	private Camera MyCamera;
	public Scene MyScene;
	
	private Scene mSceneLoading;
	
	private String mTenMap= "";
	private DigitalOnScreenControl digitalOnScreenControl;
	
	private Sound outchSound;
	private Sound shootSound;
	private Sound bomSound;
	// Fog
	private Fog myFog;
	
	// Boom
	private Boom[] myBoom;
	// control move
	private BitmapTextureAtlas mOnscreenControlTeture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	// Icon Sound
	private BitmapTextureAtlas soundTexture;
	private TextureRegion soundOnRegion;
	private TextureRegion sounOffRegion;
	private Sprite SoundOnSprite;
	private Sprite SoundOffSprite;
	// Icon Pause
	private BitmapTextureAtlas pauseTexTure;
	private TextureRegion pauseRegion;
	private Sprite PauseSprite;
	// Icon shoot
	private BitmapTextureAtlas shootTexture;
	private TextureRegion shootRegion;
	private Sprite ShootSprite; 
	// vat can
	private BitmapTextureAtlas vatCanTexture;
	private TextureRegion vatCanRegion;
	private Sprite vatCanSprite;
	//===================================================||||||||||||||||||==================================================================
	public Engine onLoadEngine() 
	{
		myFog = new Fog();
		MyPlayer = new Player();
		FriendPlayer = new Player();
		switch (GlobalVariables.MAP_INDEX) {
		case 0:
			mTenMap = "map01.tmx";
			break;
		case 1:
			mTenMap = "map02.tmx";
			break;
		case 2:
			mTenMap = "map03.tmx";
			break;
		case 3:
			mTenMap = "map04.tmx";
			break;
		case 4:
			mTenMap = "map05.tmx";
			break;

		default:
			break;
		}
		myBoom = new Boom[5];
		this.MyCamera = new Camera(0, 0, ScreenStatic.CAMERA_WIDTH, ScreenStatic.CAMERA_HEIGHT);
		Engine mEngine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, 
				new RatioResolutionPolicy(ScreenStatic.CAMERA_WIDTH, ScreenStatic.CAMERA_HEIGHT), this.MyCamera).setNeedsSound(true).setNeedsMusic(true));
		return mEngine;
	}

	
	private BitmapTextureAtlas loadingBitmapTextureAlas;
	private TextureRegion loadingTetureRegion;
	private Sprite loadingSprite;
	public void onLoadResources() 
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Loading/");
		this.loadingBitmapTextureAlas = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.loadingTetureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.loadingBitmapTextureAlas, this,"boom_loading.png", 0,0);
		mEngine.getTextureManager().loadTextures(this.loadingBitmapTextureAlas);
	}

	public Scene onLoadScene() 
	{
		mSceneLoading = new Scene();
		mSceneLoading.setBackgroundEnabled(false);
		loadingSprite = new Sprite(0, 0, loadingTetureRegion);
		mSceneLoading.attachChild(loadingSprite);
		
		
		return mSceneLoading;
	}

	public void onLoadComplete() 
	{
		mEngine.registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() {
			public void onTimePassed(TimerHandler pTimerHandler) 
			{
				mEngine.registerUpdateHandler(pTimerHandler);
				loadResources();
				loadScenes();
				
				RemoteBluetooth.setCommandWacher(new RemoteBluetooth.CommandWacher() {
					
					public void onComand(int indexCommand, JSONObject jsonObject) {
						try {
							GlobalVariables.MENU_STATUS = jsonObject.getInt("statusMenu");
							switch (GlobalVariables.MENU_STATUS) {
							case 1:
								MainGameActivity.this.MyScene.setChildScene(mMenuScene, false, true, false);
								GlobalVariables.MENU_STATUS = 0;
								break;
							case 2: // continue game
								MainGameActivity.this.MyScene.back();
								mMenuScene.reset();
								MainGameActivity.this.MyScene.setChildScene(digitalOnScreenControl);
								GlobalVariables.MENU_STATUS = 0;
								break;
							case 3:// to menu
								Intent ii = new Intent(MainGameActivity.this, RemoteBluetooth.class);
								MainGameActivity.this.startActivity(ii);
								MainGameActivity.this.finish();
								GlobalVariables.MENU_STATUS = 0;
								break;
							case 4: // quit game
								DialogExit dialogExit = new DialogExit(MainGameActivity.this, MainGameActivity.this);
								dialogExit.show();
								GlobalVariables.MENU_STATUS = 0;
								break;

							default:
								break;
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					}
				});
				mEngine.setScene(MyScene);
				
			}
		}));
		
	}
	
	
	public void loadResources()
	{
		myFog.onLoadResources(MainGameActivity.this.mEngine, MainGameActivity.this);
		for(int i = 0; i< myBoom.length; i++){
			myBoom[i] = new Boom();
			myBoom[i].onLoadResources(MainGameActivity.this.mEngine, MainGameActivity.this);
		}
		MyPlayer.onLoadResources(MainGameActivity.this.mEngine, MainGameActivity.this);
		FriendPlayer.onLoadResources(MainGameActivity.this.mEngine, MainGameActivity.this);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Control/");
		this.pauseTexTure = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.pauseRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.pauseTexTure, this, "pause.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.pauseTexTure);
		//
		mOnscreenControlTeture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnscreenControlTeture,this, "ControlBase.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnscreenControlTeture,this, "ControlKnob.png", 128,0);
		this.mEngine.getTextureManager().loadTextures(mOnscreenControlTeture);

		// Load Pause Icon
		this.pauseTexTure = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.pauseRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.pauseTexTure, this, "pause.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.pauseTexTure);
		
		//Load Sound Icon
		this.soundTexture = new BitmapTextureAtlas(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.soundOnRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundTexture, this, "sound_on.png", 0, 0);
		this.sounOffRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundTexture, this, "sound_off.png", 32, 0);
		this.mEngine.getTextureManager().loadTexture(soundTexture);
		// Load shoot icon
		this.shootTexture = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.shootRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shootTexture,this, "shoot.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(shootTexture);
		// vat can
		this.vatCanTexture = new BitmapTextureAtlas(16, 16, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.vatCanRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(vatCanTexture, this, "vatcan.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(vatCanTexture);
		// Load font
		FontFactory.setAssetBasePath("Fonts/");
		this.mFontTexTure = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(this.mFontTexTure, this, "CGFArchReactor.ttf", 34, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexTure);
		this.mEngine.getFontManager().loadFont(this.mFont);	
		this.mFontTexTure1 = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont1 = FontFactory.createFromAsset(this.mFontTexTure1, this, "CGFArchReactor.ttf", 34, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexTure1);
		this.mEngine.getFontManager().loadFont(this.mFont1);	
		//Load sound
		 SoundFactory.setAssetBasePath("Sound/");
		try {
			outchSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "outch.wav");
            bomSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "shoot.wav");
            shootSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "EXPLODE.wav");
		} catch (Exception e) {
			System.out.println("khong load duoc am thanh");
		}
	}
	public void loadScenes()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());
		MyScene = new Scene();
		MyScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		MyScene.setOnAreaTouchTraversalFrontToBack();
		MyScene.setOnSceneTouchListener(SceneTouchListener);
		MyScene.setTouchAreaBindingEnabled(true);
		MyScene.registerUpdateHandler(UpdateHandler);
		
		// LOAD MAP
		mTMXTileMap = Maps.getTMXTileMap(MyScene, this.mEngine, MainGameActivity.this,mTenMap);
		ArrayList<TMXLayer> mapLayers = mTMXTileMap.getTMXLayers();
		for(TMXLayer layer : mapLayers)
		{
			if(layer.getName().equals("vatcan"))
			{
				vatcanTMXLayer = layer;
				continue;
			}
			MyScene.attachChild(layer);
		}
		// OnLoadScene Bullet
				for (int i = 0; i < MyPlayer.bullet.length; i++) {
					MyPlayer.bullet[i].setTMXTiledMap(mTMXTileMap);
					MyPlayer.bullet[i].setTMXLayer(vatcanTMXLayer);
				}
				for (int i = 0; i < FriendPlayer.bullet.length; i++) {
					FriendPlayer.bullet[i].setTMXTiledMap(mTMXTileMap);
					FriendPlayer.bullet[i].setTMXLayer(vatcanTMXLayer);
				}
		if(GlobalVariables.isServer){
			MyPlayer.setPositionXY(50, 150);
		}else{
			MyPlayer.setPositionXY(400, 150);
		}
		MyPlayer.onLoadScene(MyScene);
		MyPlayer.setStatusPlayer(StatusPlayer.UN_MOVE_RIGHT);
		
		if(GlobalVariables.isServer){
			FriendPlayer.setPositionXY(400, 150);
		}else{
			FriendPlayer.setPositionXY(50, 150);
		}
		FriendPlayer.onLoadScene(MyScene);
		FriendPlayer.setStatusPlayer(StatusPlayer.MOVE_LEFT);
		
		digitalOnScreenControl = new DigitalOnScreenControl(0, ScreenStatic.CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), 
				this.MyCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 
				new IOnScreenControlListener() {
             public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
            	 if(pValueX == 1)
            		 myDirection = 1;
            	 else if (pValueX == -1) {
					myDirection = 2;
				}else if(pValueX == 0 && pValueY == 0) {
					myDirection = 0;
				} 
            	 if(pValueY == 1)
            		 myDirection = 3;
            	 else if (pValueY == -1) {
					myDirection = 4;
				}
        //    	 sendMove(myDirection);
             }
             
		 });
		digitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		digitalOnScreenControl.getControlBase().setAlpha(0.7f);
		digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		digitalOnScreenControl.getControlBase().setScale(0.8f);
		digitalOnScreenControl.getControlKnob().setScale(0.8f);
		digitalOnScreenControl.refreshControlKnobPosition();

        MyScene.setChildScene(digitalOnScreenControl);
        // Su kien nhan nut ban
        this.ShootSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 64, ScreenStatic.CAMERA_HEIGHT - 64, this.shootRegion){
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && fireFinish){
        			fireFinish = false;
        			if(GlobalVariables.SOUND)
        				shootSound.play();
        			ShootSprite.setAlpha(1.0f);
        			fired = true;
        			sendFire();
        			return fired;
        		}
        		ShootSprite.setAlpha(0.5f);
        		return fired = false;
        	}
        };
        this.ShootSprite.setAlpha(0.5f);
        this.MyScene.attachChild(ShootSprite);
        this.MyScene.registerTouchArea(ShootSprite);
        
        // Load Sound icon len scene
        this.SoundOnSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 64, 1, this.soundOnRegion){
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
        			if(GlobalVariables.SOUND) outchSound.play();
        			GlobalVariables.SOUND = !GlobalVariables.SOUND;
        			SoundOffSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 64, 1);
        			SoundOnSprite.setPosition(-100,-100);
        		}
        		return true;
        	}
        }; 
        SoundOnSprite.setAlpha(0.5f);
        MyScene.attachChild(SoundOnSprite);
        MyScene.registerTouchArea(SoundOnSprite);
        
        this.SoundOffSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 64, 1, this.sounOffRegion){
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
        			if(GlobalVariables.SOUND) outchSound.play();
        			GlobalVariables.SOUND = !GlobalVariables.SOUND;
        			SoundOnSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 64, 1);
        			SoundOffSprite.setPosition(-100, -100);
        		}
        		return true;
        	}
        };
        SoundOffSprite.setAlpha(0.5f);
        MyScene.attachChild(SoundOffSprite);
        MyScene.registerTouchArea(SoundOffSprite);
        
        if(GlobalVariables.SOUND){
        	SoundOnSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 64, 1);
        	SoundOffSprite.setPosition(-100,-100);
        } else {
			SoundOnSprite.setPosition(-100, -100);
			SoundOffSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 64, 1);
		}
        // Load pause icon len scene
        this.PauseSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 32, 1, this.pauseRegion){
        	@Override
        	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
        			if(GlobalVariables.SOUND)
        				outchSound.play();
        			PauseSprite.setAlpha(1.0f);
        			sendMenuStatus(1);
        			MainGameActivity.this.MyScene.setChildScene(MainGameActivity.this.mMenuScene, false, true, true);
        		}
        		return true;
        	}
        };
        this.PauseSprite.setAlpha(0.5f);
        this.MyScene.attachChild(PauseSprite);
        this.MyScene.registerTouchArea(PauseSprite);
        //////////////////////////////////////////
        for(int i = 0; i< myBoom.length; i++){
        	myBoom[i].onLoadScene(MyScene);
        }
        //////////////////////////////////////////
        this.vatCanSprite = new Sprite(-100, -100, this.vatCanRegion);
        vatCanSprite.setVisible(false);
        MyScene.attachChild(vatCanSprite);
        MyScene.registerTouchArea(vatCanSprite);
        ////////////////////////////////////////
        myFog.onLoadScene(MyScene);
        ////////////////////////////////////////
    //    namePlayer = new ChangeableText(StaticDefine.CAMERA_WIDTH/5, 0, this.mFont1,GlobalVariables.MY_NAME);
    //    MyScene.attachChild(namePlayer);
   //     namePlayer = new ChangeableText(2*StaticDefine.CAMERA_WIDTH/3, 0, this.mFont1,GlobalVariables.FRIEND_NAME);
    //    MyScene.attachChild(namePlayer);
        myScoreText = new ChangeableText((ScreenStatic.CAMERA_WIDTH/2)-40, 0, this.mFont,String.valueOf(MyPlayer.myScore),2);
        MyScene.attachChild(myScoreText);
        frienfScoreText = new ChangeableText((ScreenStatic.CAMERA_WIDTH/2)+ 28 , 0, this.mFont, String.valueOf(FriendPlayer.myScore), 2);
        MyScene.attachChild(frienfScoreText);
        vsText = new ChangeableText((ScreenStatic.CAMERA_WIDTH/2) - 5, 0,this.mFont1, "v");
        MyScene.attachChild(vsText);
        ///////////////////////////////////////
        this.mMenuScene = this.createMenuScene();
        
	}
	IUpdateHandler UpdateHandler = new IUpdateHandler() {
		
		public void reset() {
			// TODO Auto-generated method stub
			
		}
		
		public void onUpdate(float pSecondsElapsed) {
			if(time == 0){
				time = SystemClock.elapsedRealtime();
			}
			if(SystemClock.elapsedRealtime() -time > 300){
				sendMyPossition();
				time = 0;
			}
				
				//////////////////////////////////////////////////////////////////////
				MoveSprite(MyPlayer, myDirection);
				if(GlobalVariables.POSSTIONX != 0){
					FriendPlayer.setPositionXY(GlobalVariables.POSSTIONX, GlobalVariables.POSSTIONY);
					FriendPlayer.playAnimatedSprite.setPosition(GlobalVariables.POSSTIONX, GlobalVariables.POSSTIONY);
				}
				FriendPlayer.setStatusPlayer(GlobalVariables.DIRECTION);
			//	MoveSprite(FriendPlayer, GlobalVariables.DIRECTION);

				// Vien dan do doi phuong ban ra
				while(GlobalVariables.SHOOT_STATUS){
					FriendPlayer.bullet[bulletIndexFriend].setStatusBullet(FriendPlayer.getStatusPlayer());
					FriendPlayer.bullet[bulletIndexFriend].StatusMove(FriendPlayer);
					bulletIndexFriend ++ ;
					GlobalVariables.SHOOT_STATUS = false;

				}
				for(Bullet bullet : FriendPlayer.bullet){
					bullet.moveBullet();
					if(bulletIndexFriend == FriendPlayer.bullet.length){
						bulletIndexFriend = 0;
					}
				}
				for(Bullet bullet : FriendPlayer.bullet){
					if(checkCollision(bullet.bullet_Sprite, MyPlayer.playAnimatedSprite) && bullet.bullet_Sprite.isVisible()){
						if(MyPlayer.playAnimatedSprite.isVisible()){
							FriendPlayer.myScore = FriendPlayer.myScore + 1;
							if(GlobalVariables.isServer){
								frienfScoreText.setText(String.valueOf(FriendPlayer.myScore));
							}else myScoreText.setText(String.valueOf(FriendPlayer.myScore));
						}
						for (int j = 0; j < myBoom.length; j++) {
							if(!myBoom[j].begin_no) {
								myBoom[j].moveXY(MyPlayer.playAnimatedSprite.getX() - myBoom[j].no_AnimatedSprite.getBaseWidth() / 4, MyPlayer.playAnimatedSprite.getY() - myBoom[j].no_AnimatedSprite.getBaseHeight() / 4);
								break;
							}
						}
						MyPlayer.playAnimatedSprite.setVisible(false);
						MyPlayer.playAnimatedSprite.setPosition(-100, -100);
						
						bullet.bullet_Sprite.setVisible(false);
						bullet.bullet_Sprite.setPosition(-10, -10);
					}
					for(int i = 0; i< myBoom.length; i++){
						myBoom[i].delay_no(MyPlayer, FriendPlayer);
						if(myBoom[i].no_end){
							MyPlayer.playAnimatedSprite.setVisible(true);
						}
					}
					
				}
				
				
				
				// Vien dan cua minh ban ra
				while(fired && bulletIndex < MyPlayer.bullet.length){
					MyPlayer.bullet[bulletIndex].setStatusBullet(MyPlayer.getStatusPlayer());
					MyPlayer.bullet[bulletIndex].StatusMove(MyPlayer);
					bulletIndex++;
					fired = false;
				}
				for(Bullet bullet : MyPlayer.bullet){
					fireFinish = !bullet.moveBullet();
					if(fireFinish)
					System.out.println("MyPlayer.bullet.length");
					if(bulletIndex == MyPlayer.bullet.length){
						bulletIndex = 0;
					}
				}
				for(Bullet bullet : MyPlayer.bullet){
					if(checkCollision(bullet.bullet_Sprite, FriendPlayer.playAnimatedSprite) && bullet.bullet_Sprite.isVisible()){
						if(FriendPlayer.playAnimatedSprite.isVisible()){
							MyPlayer.myScore = MyPlayer.myScore + 1;
							if(GlobalVariables.isServer){
								myScoreText.setText(String.valueOf(MyPlayer.myScore));
							}else frienfScoreText.setText(String.valueOf(MyPlayer.myScore));
						}
						for (int j = 0; j < myBoom.length; j++) {
							if(!myBoom[j].begin_no) {
								myBoom[j].moveXY(FriendPlayer.playAnimatedSprite.getX() - myBoom[j].no_AnimatedSprite.getBaseWidth() / 4, FriendPlayer.playAnimatedSprite.getY() - myBoom[j].no_AnimatedSprite.getBaseHeight() / 4);
								break;
							}
						}
						FriendPlayer.playAnimatedSprite.setVisible(false);
						FriendPlayer.playAnimatedSprite.setPosition(-100, -100);
						
						bullet.bullet_Sprite.setVisible(false);
						bullet.bullet_Sprite.setPosition(-10, -10);
					}
					for(int i = 0; i< myBoom.length; i++){
						myBoom[i].delay_no(MyPlayer, FriendPlayer);
						if(myBoom[i].no_end){
							FriendPlayer.playAnimatedSprite.setVisible(true);
						}
					}
				}
				
				// Tao vung nhin thay va khong nhin thay
				myFog.Fog_move(MyPlayer.getPositionX() - 480 + MyPlayer.playAnimatedSprite.getWidth()/2, MyPlayer.getPositionY() - 320 + MyPlayer.playAnimatedSprite.getHeight());
				

			}
		
	};
	/////////////////////////////////////////////////////////////
	private boolean checkCollision(AnimatedSprite a, AnimatedSprite b){
		this.vatCanSprite.setPosition(b.getX() + 8, b.getY() + 8);
		if(a.collidesWith(vatCanSprite)){
			return true;
		}
		return false;
	}
	/////////////////////////////////////////////////////////////
	private void MoveSprite(Player player, int direction){
		 float pX = 0, pY = 0;
			if(direction == 1 && player.getStatusPlayer() != StatusPlayer.MOVE_RIGHT){
				player.setStatusPlayer(StatusPlayer.MOVE_RIGHT);
				pX = player.playAnimatedSprite.getWidth() + player.getPositionX();
				pY = player.getPositionY() + (player.playAnimatedSprite.getHeight() / 2);
				}
				
			else if(direction == 2  &&player.getStatusPlayer() != StatusPlayer.MOVE_LEFT){
				player.setStatusPlayer(StatusPlayer.MOVE_LEFT);
				pX = player.getPositionX();
				pY = player.getPositionY() + (MyPlayer.playAnimatedSprite.getHeight() / 2);
			}
			
			else if(direction == 3 && player.getStatusPlayer() != StatusPlayer.MOVE_DOWN){
				player.setStatusPlayer(StatusPlayer.MOVE_DOWN);					
				pX = player.getPositionX() + (player.playAnimatedSprite.getWidth()/2);
				pY = player.getPositionY() + player.playAnimatedSprite.getHeight();
			}
			
			else if(direction == 4 && player.getStatusPlayer() != StatusPlayer.MOVE_UP){
				player.setStatusPlayer(StatusPlayer.MOVE_UP);
				pX = player.getPositionX() + (player.playAnimatedSprite.getWidth()/2);
				pY = player.getPositionY();
			}
     	
			else{
				switch(player.getStatusPlayer()){
					case StatusPlayer.MOVE_RIGHT: player.setStatusPlayer(StatusPlayer.UN_MOVE_RIGHT); break;
					case StatusPlayer.MOVE_LEFT: player.setStatusPlayer(StatusPlayer.UN_MOVE_LEFT); break;
					case StatusPlayer.MOVE_UP: player.setStatusPlayer(StatusPlayer.UN_MOVE_UP); break;
					case StatusPlayer.MOVE_DOWN: player.setStatusPlayer(StatusPlayer.UN_MOVE_DOWN); break;
					default: break;
				} 
			}

			float x = 0;
			float y = 0;
			switch (direction) {
			case 1:
				x = 1;
				y = 0;
				break;
			case 2:
				x = -1;
				y = 0;
				break;
			case 3:
				x = 0;
				y = 1;
				break;
			case 4:
				x = 0;
				y = -1;
				break;
			case 0:
				x = 0;
				y = 0;
				break;
			default:
				break;
			}
     	if(x != 0 || y != 0){
         	TMXTile mTMXTile = vatcanTMXLayer.getTMXTileAt(pX + x * 4,pY + y * 4);
         	try{
					if(mTMXTile == null){
					}
					else{
						TMXProperties<TMXTileProperty> mTMXProperties= mTMXTile.getTMXTileProperties(mTMXTileMap);
				
						TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
						if(mTMXTileProperty.getName().equals("vatcan")){
							
						}
					}
				}catch(Exception e){
					player.moveRelativeXY(x * 4, y * 4);
				}
     	}
	}
	// Phuong thuc gui cac tin hieu dieu khien
	private void sendMyPossition(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("message", GlobalVariables.MOVE);
			JSONObject object = new JSONObject();
			object.put("possitionX", MyPlayer.getPositionX());
			object.put("possitionY", MyPlayer.getPositionY());
			object.put("direction", MyPlayer.getStatusPlayer());
			jsonObject.put("Data", object);
			mCommandService.write(jsonObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void sendMenuStatus(int index){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("message", GlobalVariables.PAUSE);
			JSONObject object = new JSONObject();
			object.put("statusMenu", index);
			jsonObject.put("Data", object);
			mCommandService.write(jsonObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void sendFire(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("message", GlobalVariables.FIRE);
			JSONObject object = new JSONObject();
			object.put("direction", MyPlayer.getStatusPlayer());
			jsonObject.put("Data", object);
			mCommandService.write(jsonObject.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
//	private void sendMove(int directionIndex){
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put("message", GlobalVariables.MOVE);
//			JSONObject jObject = new JSONObject();
//			jObject.put("direction", directionIndex);
//			jsonObject.put("Data", jObject);
//			mCommandService.write(jsonObject.toString().getBytes());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	} 
	//=======================================||MENU||====================================================
	protected MenuScene createMenuScene() {
        final MenuScene menuScene = new MenuScene(MainGameActivity.this.MyCamera);
       
        final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(CONTINUE, MainGameActivity.this.mFont, "RESUME"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(resetMenuItem);
        
        final IMenuItem MainMenuMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MAINMENU, MainGameActivity.this.mFont, "MENU"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        MainMenuMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(MainMenuMenuItem);

        final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(QUIT, this.mFont, "QUIT"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
        return menuScene;
	}
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case CONTINUE:
			if(GlobalVariables.SOUND)
				outchSound.play();
			MainGameActivity.this.MyScene.back();
			mMenuScene.reset();
			MainGameActivity.this.MyScene.setChildScene(digitalOnScreenControl);
			sendMenuStatus(2);
			return true;
		case MAINMENU:
			if(GlobalVariables.SOUND)
				outchSound.play();
			sendMenuStatus(3);
			Intent ii = new Intent(MainGameActivity.this, RemoteBluetooth.class);
			MainGameActivity.this.startActivity(ii);
			MainGameActivity.this.finish();
			return true;
		case QUIT:
			if(GlobalVariables.SOUND)
				outchSound.play();
			sendMenuStatus(4);
			DialogExit dialogExit = new DialogExit(MainGameActivity.this, MainGameActivity.this);
			dialogExit.show();
			return true;

		default:
			return false;
		}
	};
	protected void onDestroy() {		
		if(shootSound != null)
			shootSound.release();
		if(outchSound != null)
			outchSound.release();
		
		MainGameActivity.this.finish();
		System.out.println("MainGameActivity.this.finish");
		super.onDestroy();
	}
	///////////////////////////////////////////////////////////////////
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Khi báº¥m nÃºt back thÃ¬ hiá»‡n thá»‹ menu xem cÃ³ tiáº¿p tá»¥c chÆ¡i hay thoÃ¡t
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			Intent ii = new Intent(MainGameActivity.this, RemoteBluetooth.class);
			MainGameActivity.this.startActivity(ii);
			MainGameActivity.this.finish();
		}
		return false;
	}
	
	//=======================================|| IOnSceneTouchListener ||================================
	IOnSceneTouchListener SceneTouchListener = new IOnSceneTouchListener() {
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) 
		{
		if(pSceneTouchEvent.getAction()== TouchEvent.ACTION_DOWN)
			{
//				if(MyPlayer.getStatusPlayer()+1 > StatusPlayer.SUM)
//				{
//					MyPlayer.setStatusPlayer(1);
//				}
//				else {
//					MyPlayer.setStatusPlayer(MyPlayer.getStatusPlayer() + 1);
//				}
			}
			return false;
		}
	};
    
}

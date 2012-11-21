package com.hoan.MainGame;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
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
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import com.hoan.Maps.*;
import com.hoan.Player.*;
import com.hoan.StaticDefine.*;

public class MainGameActivity extends BaseGameActivity{

	private Player MyPlayer;
	private TMXTiledMap mTMXTileMap;
	private TMXLayer vatcanTMXLayer;
	
	private Camera MyCamera;
	private Scene MyScene;
	
	private Scene mSceneLoading;
	
	private String mTenMap= "";
	private int status_digitalOnScreenControl = -1;
	private DigitalOnScreenControl digitalOnScreenControl;
	
	private BitmapTextureAtlas mOnscreenControlTeture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	public Engine onLoadEngine() 
	{
		MyPlayer = new Player();
		mTenMap = "maps_1.tmx";
		this.MyCamera = new Camera(0, 0, StaticDefine.CAMERA_WIDTH, StaticDefine.CAMERA_HEIGHT);
		Engine mEngine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, 
				new RatioResolutionPolicy(StaticDefine.CAMERA_WIDTH, StaticDefine.CAMERA_HEIGHT), this.MyCamera));
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
				mEngine.setScene(MyScene);
				
			}
		}));
		
	}
	
	
	public void loadResources()
	{
		MyPlayer.onLoadResoures(MainGameActivity.this.mEngine, MainGameActivity.this);
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Control/");
		mOnscreenControlTeture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnscreenControlTeture,this, "ControlBase.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnscreenControlTeture,this, "ControlKnob.png", 128,0);
		this.mEngine.getTextureManager().loadTextures(mOnscreenControlTeture);
		
	}
	public void loadScenes()
	{
		this.mEngine.registerUpdateHandler(new FPSLogger());
		MyScene = new Scene();
		MyScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		MyScene.setOnAreaTouchTraversalFrontToBack();
		MyScene.setOnSceneTouchListener(SceneTouchListener);
		MyScene.setTouchAreaBindingEnabled(true);
		
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
		MyPlayer.setPositionXY(150, 150);
		MyPlayer.onLoadScene(MyScene);
		
		
		
		digitalOnScreenControl = new DigitalOnScreenControl(0, StaticDefine.CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(), 
				this.MyCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 
				new IOnScreenControlListener() {
			 float pX = 0, pY = 0;
             public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {

            	if(pValueX > 0 && MyPlayer.getStatusPlayer() != StatusPlayer.MOVE_RIGHT){
					MyPlayer.setStatusPlayer(StatusPlayer.MOVE_RIGHT);
					status_digitalOnScreenControl = StatusPlayer.MOVE_RIGHT;
					pX = MyPlayer.playAnimatedSprite.getWidth() + MyPlayer.getPositionX();
					pY = MyPlayer.getPositionY() + (MyPlayer.playAnimatedSprite.getHeight() / 2);
					}
					
				else if(pValueX < 0  &&MyPlayer.getStatusPlayer() != StatusPlayer.MOVE_LEFT){
					MyPlayer.setStatusPlayer(StatusPlayer.MOVE_LEFT);
					status_digitalOnScreenControl = StatusPlayer.MOVE_LEFT;
					pX = MyPlayer.getPositionX();
					pY = MyPlayer.getPositionY() + (MyPlayer.playAnimatedSprite.getHeight() / 2);
				}
				
				else if(pValueY > 0 && MyPlayer.getStatusPlayer() != StatusPlayer.MOVE_DOWN){
					MyPlayer.setStatusPlayer(StatusPlayer.MOVE_DOWN);
					status_digitalOnScreenControl = StatusPlayer.MOVE_DOWN;					
					pX = MyPlayer.getPositionX() + (MyPlayer.playAnimatedSprite.getWidth()/2);
					pY = MyPlayer.getPositionY() + MyPlayer.playAnimatedSprite.getHeight();
				}
				
				else if(pValueY < 0 && MyPlayer.getStatusPlayer() != StatusPlayer.MOVE_UP){
					MyPlayer.setStatusPlayer(StatusPlayer.MOVE_UP);
					status_digitalOnScreenControl = StatusPlayer.MOVE_UP;
					pX = MyPlayer.getPositionX() + (MyPlayer.playAnimatedSprite.getWidth()/2);
					pY = MyPlayer.getPositionY();
				}
            	
				else{
					switch(status_digitalOnScreenControl){
						case StatusPlayer.MOVE_RIGHT: MyPlayer.setStatusPlayer(StatusPlayer.UN_MOVE_RIGHT); break;
						case StatusPlayer.MOVE_LEFT: MyPlayer.setStatusPlayer(StatusPlayer.UN_MOVE_LEFT); break;
						case StatusPlayer.MOVE_UP: MyPlayer.setStatusPlayer(StatusPlayer.UN_MOVE_UP); break;
						case StatusPlayer.MOVE_DOWN: MyPlayer.setStatusPlayer(StatusPlayer.UN_MOVE_DOWN); break;
						default: break;
					}
				}
            	
            	if(pValueX != 0 || pValueY != 0){
	            	TMXTile mTMXTile = vatcanTMXLayer.getTMXTileAt(pX + pValueX * 7,pY + pValueY * 7);
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
						MyPlayer.moveRelativeXY(pValueX * 7, pValueY * 7);
					}
            	}
             }
             
		 });
		digitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		digitalOnScreenControl.getControlBase().setAlpha(0.7f);
		digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		digitalOnScreenControl.getControlBase().setScale(0.8f);
		digitalOnScreenControl.getControlKnob().setScale(0.8f);
		digitalOnScreenControl.refreshControlKnobPosition();

        MyScene.setChildScene(digitalOnScreenControl);
	}
	
	//=======================================|| IOnSceneTouchListener ||================================
	IOnSceneTouchListener SceneTouchListener = new IOnSceneTouchListener() {
		public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) 
		{
		if(pSceneTouchEvent.getAction()== TouchEvent.ACTION_DOWN)
			{
				if(MyPlayer.getStatusPlayer()+1 <StatusPlayer.SUM)
				{
					MyPlayer.setStatusPlayer(0);
				}
				else {
					MyPlayer.setStatusPlayer(MyPlayer.getStatusPlayer() + 1);
				}
			}
			return false;
		}
	};
    
}

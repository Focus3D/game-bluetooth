package com.hoan.Player;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

import com.hoan.Bullet.Bullet;
import com.hoan.InterfaceSprite.*;
public class Player implements InterfaceSprite 
{
	private int STATUS_PLAYER = StatusPlayer.UN_MOVE_DOWN;
	
	public AnimatedSprite playAnimatedSprite;
	public Bullet[] bullet = new Bullet[10];
	public int myScore = 0;
	
	private TiledTextureRegion playerTiledTextureRegion;
	private BitmapTextureAtlas playerBitmapTextureAlas;
	
	private float positionX =0;
	private float positionY = 0;
	public Player()
	{
		
	}

	public void onLoadResources(Engine mEngine, Context mContext) 
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Player/");
		this.playerBitmapTextureAlas = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.playerBitmapTextureAlas, mContext, "nhan_vat_chinh.png", 0, 0,5, 4);
		mEngine.getTextureManager().loadTextures(this.playerBitmapTextureAlas);
		for (int i = 0; i < bullet.length; i++) {
			bullet[i] = new Bullet();
			bullet[i].onLoadResources(mEngine, mContext);	
		}
		
	}

	public void onLoadScene(Scene mScene) 
	{
		playAnimatedSprite = new AnimatedSprite(this.positionX, this.positionY, this.playerTiledTextureRegion);
		showPlayerStatus();
		mScene.attachChild(playAnimatedSprite);
		
		for (int i = 0; i < bullet.length; i++) {
			bullet[i].onLoadScene(mScene);			
		} 
	}
	//////////////////////////////////
	public int getStatusPlayer()
	{
		return this.STATUS_PLAYER;
	}
	/////////////////////////////////
	public void setStatusPlayer(int status)
	{
		this.STATUS_PLAYER = status;
		showPlayerStatus();
	}
	//////////////////////////////////
	public float getPositionX()
	{
		return this.positionX;
	}
	//////////////////////////////////
	public float getPositionY()
	{
		return this.positionY;
	}
	///////////////////////////////////
	public void setPositionXY(float positionX, float positionY)
	{
		this.positionX = positionX;
		this.positionY = positionY;
	}
	/////////////////////////////////
	public void moveXY(float positionX, float positionY){
		this.positionX = positionX;
		this.positionY = positionY;
		movePlayer();
	}
	//////////////////////////////////
	
	public void moveRelativeXY(float moveRelativeX, float moveRelativeY)
	{
		this.positionX += moveRelativeX;
		this.positionY += moveRelativeY;
		movePlayer();
	}
	///////////////////////////////////
	private void movePlayer()
	{
		playAnimatedSprite.setPosition(this.positionX, this.positionY);
	}
	///////////////////////////////////
	public void showPlayerStatus()
	{
		
		switch (this.STATUS_PLAYER) {
		case StatusPlayer.MOVE_LEFT:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{16}, 1000);
			break;
		}
		case StatusPlayer.MOVE_RIGHT:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{6}, 1000);
			break;
		}
		case StatusPlayer.MOVE_UP:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{11}, 1000);
			break;
		}
		case StatusPlayer.MOVE_DOWN:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{1}, 1000);
			break;
		}
		case StatusPlayer.UN_MOVE_LEFT:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{16}, 1000);
			break;
		}
		case StatusPlayer.UN_MOVE_RIGHT:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{6}, 1000);
			break;
		}
		case StatusPlayer.UN_MOVE_UP:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{11}, 1000);
			break;
		}
		case StatusPlayer.UN_MOVE_DOWN:
		{
			playAnimatedSprite.animate(new long[]{1}, new int[]{1}, 1000);
			break;
		}

		default:
			break;
		}
	}

}

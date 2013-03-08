package com.hoan.Player;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.hoan.InterfaceSprite.InterfaceSprite;
import com.hoan.Bullet.*;
import android.content.Context;


public class PlayerSingle implements InterfaceSprite {

	public int PLAYER_STATUS = StatusPlayer.UN_MOVE_UP;

	public AnimatedSprite player;
	private BitmapTextureAtlas player_BitmapTextureAtlas;
	private TiledTextureRegion player_TiledTextureRegion;

	private float positionX = 0;
	private float positionY = 0;
	
	// Sá»‘ máº¡ng cá»§a player. Máº·c Ä‘á»‹nh ban Ä‘áº§u lÃ  cÃ³ 3 lÆ°á»£t chÆ¡i
	private int heart = 03;

	
	public BulletSingle[] MyBullet = new BulletSingle[10];
	

	public PlayerSingle() {
	}

	public void onLoadResources(Engine mEngine, Context mContext) {

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/player/");
		this.player_BitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.player_TiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.player_BitmapTextureAtlas, mContext,
						"tanks.png", 0, 0, 8, 8);
		mEngine.getTextureManager().loadTexture(this.player_BitmapTextureAtlas);
		
		// Thá»±c hiá»‡n load toÃ n bá»™ sá»‘ viÃªn Ä‘áº¡n vÃ o
		for (int i = 0; i < MyBullet.length; i++) {
			MyBullet[i] = new BulletSingle();
			MyBullet[i].onLoadResources(mEngine, mContext);			
		}
		
		
	}

	public void onLoadScene(Scene mScene) {
		this.player = new AnimatedSprite(positionX, positionY,
				this.player_TiledTextureRegion);
		showPlayerStatus();
		this.player.animate(new long[] { 100, 100, 100, 100, 100, 100, 100, 100 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 1000);
		mScene.attachChild(this.player);
		
		// Thá»±c hiá»‡n viá»‡c load toÃ n bá»™ sá»‘ viÃªn Ä‘áº¡n
		for (int i = 0; i < MyBullet.length; i++) {
			MyBullet[i].onLoadScene(mScene);			
		}
		
	}

	// ===============POSITION==============//
	/**
	 * PhÆ°Æ¡ng thá»©c cho ta Ä‘áº·t vá»‹ trÃ­ cá»§a player theo trá»¥c X.
	 * 
	 * @param positionX
	 */

	public void setPositionX(float positionX) {
		PlayerSingle.this.positionX = positionX;
	}

	/**
	 * Get vá»‹ trÃ­ x xá»§a player
	 * 
	 * @return
	 */
	public float getPositionX() {
		return PlayerSingle.this.positionX;
	}

	/**
	 * PhÆ°Æ¡ng thá»©c cho ta Ä‘áº¡t vá»‹ trÃ­ cá»§a player theo trá»¥c Y
	 * 
	 * @param positionY
	 */
	public void setPositionY(float positionY) {
		PlayerSingle.this.positionY = positionY;
	}

	/**
	 * Get vá»‹ trÃ­ y cá»§a player
	 * 
	 * @return
	 */
	public float getPositionY() {
		return PlayerSingle.this.positionY;
	}

	/**
	 * Ä�áº·t vá»‹ trÃ­ khá»Ÿi táº¡o cho player
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public void setPositionXY(float positionX, float positionY) {
		PlayerSingle.this.positionX = positionX;
		PlayerSingle.this.positionY = positionY;
	}

	// =======================================|| setStatus_Player
	// ||================================
	/**
	 * PhÆ°Æ¡ng thá»©c set tráº¡ng thÃ¡i cho Player
	 * 
	 * @param: Status_Player lÃ  tráº¡ng thÃ¡i báº¡n muá»‘n Ä‘áº·t. CÃ¡c tráº¡ng thÃ¡i báº¡n cÃ³
	 *         thá»ƒ láº¥y báº±ng cÃ¡ch gá»�i Status_Player. (cháº¥m) Ä‘á»ƒ tham chiáº¿u Ä‘áº¿n cÃ¡c
	 *         tráº¡ng thÃ¡i
	 */
	public void setStatus_Player(int Status_Player) {
		PlayerSingle.this.PLAYER_STATUS = Status_Player;
		// Náº¿u setStatus_Player thÃ¬ ta sáº½ cáº­p nháº­t xem player sáº½ hiá»‡n thá»‹
		// AnimatedSprite nÃ o.
		showPlayerStatus();
	}
	
	/*
	 * 
	 * 
	 * // =======================================|| getStatus_Player //
	 * ||================================
	 *//**
	 * PhÆ°Æ¡ng thá»©c get tráº¡ng thÃ¡i cho Player
	 */
	public int getStatus_Player() {
		return PlayerSingle.this.PLAYER_STATUS;
	}

	/*
	 * 
	 * // =======================================|| showPlayerStatus //
	 * ||================================
	 *//**
	 * PhÆ°Æ¡ng thá»©c xÃ¡c Ä‘á»‹nh xem Player Ä‘ang á»Ÿ tráº¡ng thÃ¡i nÃ o Ä‘á»ƒ hiá»‡n thá»‹
	 */
	public void showPlayerStatus() {

		switch (this.PLAYER_STATUS) {
		case StatusPlayer.MOVE_LEFT: {
			player.setRotation(180);
			break;
		}
		case StatusPlayer.MOVE_RIGHT: {
			player.setRotation(0);
			break;
		}
		case StatusPlayer.MOVE_UP: {
			player.setRotation(-90);
			break;
		}
		case StatusPlayer.MOVE_DOWN: {
			player.setRotation(90);

			break;
		}
		case StatusPlayer.UN_MOVE_LEFT: {
			player.setRotation(180);
			break;
		}
		case StatusPlayer.UN_MOVE_RIGHT: {
			player.setRotation(0);
			break;
		}
		case StatusPlayer.UN_MOVE_UP: {
			player.setRotation(-90);
			break;
		}
		case StatusPlayer.UN_MOVE_DOWN: {
			player.setRotation(90);
			break;
		}

		default:
			break;
		}
	}

	/*
	 * 
	 * 
	 * // =======================================|| movePlayer //
	 * ||================================
	 *//**
	 * Move player tá»›i vá»‹ trÃ­ X
	 * 
	 * @param moveX
	 */
	public void moveX(float moveX) {
		this.positionX = moveX;
		movePlayer();
	}

	/**
	 * Move player tá»›i vá»‹ trÃ­ Y
	 * 
	 * @param moveY
	 */
	public void moveY(float moveY) {
		this.positionY = moveY;
		movePlayer();
	}

	/**
	 * Move player tá»›i vá»‹ trÃ­ X,Y
	 * 
	 * @param moveX
	 * @param moveY
	 */
	public void moveXY(float moveX, float moveY) {
		this.positionX = moveX;
		this.positionY = moveY;
		movePlayer();
	}

	/**
	 * Move player táº¡i vá»‹ trÃ­ hiá»‡n táº¡i vÃ  cá»™ng thÃªm 1 giÃ¡ trá»‹ moveRelativeX
	 * 
	 * @param moveRelativeX
	 */
	public void moveRelativeX(float moveRelativeX) {
		this.positionX += moveRelativeX;
		movePlayer();
	}

	/**
	 * Move player táº¡i vá»‹ trÃ­ hiá»‡n táº¡i vÃ  cá»™ng thÃªm 1 giÃ¡ trá»‹ moveRelativeY
	 * 
	 * @param moveRelativeY
	 */
	public void moveRelativeY(float moveRelativeY) {
		this.positionY += moveRelativeY;
		movePlayer();
	}

	/**
	 * Move player táº¡i vá»‹ trÃ­ hiá»‡n táº¡i vÃ  cá»™ng thÃªm 1 giÃ¡ trá»‹ moveRelativeX,
	 * moveRelativeY
	 * 
	 * @param moveRelativeX
	 * @param moveRelativeY
	 */
	public void moveRelativeXY(float moveRelativeX, float moveRelativeY) {
		this.positionX += moveRelativeX;
		this.positionY += moveRelativeY;
		movePlayer();
	}

	/**
	 * Move player
	 */
	private void movePlayer() {
		player.setPosition(this.positionX, this.positionY);
	}

	public AnimatedSprite getAnimatedSprite() {
		return this.player;
	}
	
	// =======================================|| Heart ||================================
	/**
	 * Set lÆ°á»£ng mÃ¡u mÃ  ngÆ°á»�i chÆ¡i cÃ³
	 */
	public void setHeart(int heart) {
		this.heart = heart;
	}

	/**
	 * Get lÆ°á»£ng mÃ¡u mÃ  ngÆ°á»�i chÆ¡i cÃ³
	 * 
	 * @return
	 */
	public int getHeart() {
		return this.heart;
	}
	
	
}
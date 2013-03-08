package com.hoan.Bullet;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.hoan.InterfaceSprite.InterfaceSprite;

import android.content.Context;
import android.os.SystemClock;

public class No implements InterfaceSprite {

	// Cho biáº¿t Ä‘á»‘i tÆ°á»£ng nÃ y Ä‘Æ°á»£c hiá»‡n thá»‹ hay khÃ´ng Ä‘Æ°á»£c hiá»‡n thá»‹
	// Náº¿u visiable = true lÃ  Ä‘Æ°á»£c hiá»‡n thá»‹, ngÆ°á»£c láº¡i lÃ  áº©n
	private boolean visiable = true;

	// Khai bÃ¡o cÃ¡c biáº¿n load áº£nh
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	public AnimatedSprite no_AnimatedSprite;

	// Khai bÃ¡o cÃ¡c biáº¿n vá»‹ trÃ­, ban Ä‘áº§u vá»‹ trÃ­ lÃ  X =0 , Y = 0
	private float pX = -100;
	private float pY = -100;

	// Thá»�i gian xÃ¡c Ä‘á»‹nh báº¯t Ä‘áº§u Ä‘áº·t bom xuá»‘ng
	public long time = 0;

	// Thá»�i gian xÃ¡c Ä‘á»‹nh lÃºc ná»• xong
	long time_no = 0;

	// Khi vá»¥ ná»• káº¿t thÃºc thÃ¬ no_end = true. Ban Ä‘áº§u lÃ  flase vÃ¬ bom chÆ°a ná»•
	public boolean no_end = false;

	// Biáº¿n cho biáº¿t khi nÃ o thÃ¬ bom Ä‘Æ°á»£c kÃ­ch hoáº¡t ná»•. Khi bom Ä‘Æ°á»£c kÃ­ch hoáº¡t
	// ná»• thÃ¬ begin_no = true
	public boolean begin_no = false;

	public No() {
	}

	/**
	 * PhÆ°Æ¡ng thá»©c OnLoadResources
	 */
	public void onLoadResources(Engine mEngine, Context mContext) {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/Bullets/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, mContext, "bum.png",
						0, 0, 4, 4);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);

	}

	/**
	 * PhÆ°Æ¡ng thá»©c OnLoadScene
	 */
	public void onLoadScene(Scene mScene) {
		no_AnimatedSprite = new AnimatedSprite(pX, pY, mTiledTextureRegion);
		no_AnimatedSprite.setPosition(-100, -100);
		this.no_AnimatedSprite.animate(40);
		mScene.attachChild(no_AnimatedSprite);
	}

	/**
	 * Khá»Ÿi táº¡o láº¡i cÃ¡c biáº¿n vá»›i giÃ¡ trá»‹ máº·c Ä‘á»‹nh ban Ä‘áº§u
	 */
	public void init() {
		no_end = false;
		time = 0;
		time_no = 0;
		begin_no = false;
	}

	/**
	 * PhÆ°Æ¡ng thá»©c di chuyá»ƒn Ä‘áº¿n vá»‹ trÃ­ X,Y
	 */

	public void moveXY(float pX, float pY) {
		this.pX = pX;
		this.pY = pY;
		this.no_AnimatedSprite.setPosition(pX, pY);
		this.no_AnimatedSprite.setVisible(false);
		begin_no = true;
	}

	public void delay_no() {
		// if(no_end)//Khi bom Ä‘Ã£ ná»• rá»“i thÃ¬ khÃ´ng gá»�i ná»¯a
		// return;

		if (begin_no) {
			this.no_AnimatedSprite.setVisible(true);
			// begin_no = true;

			// Báº¯t Ä‘áº§u tÃ­nh thá»�i gian ná»•
			if (time_no == 0)
				time_no = SystemClock.elapsedRealtime();
			if (SystemClock.elapsedRealtime() - time_no > 500) {// Cho phÃ©p ná»•
																	// trong 1s
				// Háº¿t 1s ta cho áº©n toÃ n bá»™ vÃ  cho di chuyá»ƒn Ä‘áº¿n vá»‹ trá»‹
				// -100,-100
				this.no_AnimatedSprite.setPosition(-100, -100);
				this.no_AnimatedSprite.setVisible(false);
				init();
				 no_end = true;
			}
		}
	}
}

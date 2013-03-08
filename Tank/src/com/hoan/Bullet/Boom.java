package com.hoan.Bullet;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.hoan.InterfaceSprite.InterfaceSprite;
import com.hoan.Player.Player;
import com.hoan.StaticDefine.GlobalVariables;

import android.content.Context;
import android.os.SystemClock;

public class Boom implements InterfaceSprite{
	
	// Cho biết đối tượng này được hiện thị hay không được hiện thị
	// Nếu visiable = true là được hiện thị, ngược lại là ẩn
	public boolean visiable = true;
	
	// Khai báo các biến load ảnh
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	public AnimatedSprite no_AnimatedSprite;

	// Khai báo các biến vị trí, ban đầu vị trí là X =0 , Y = 0
	private float pX = -100;
	private float pY = -100;
	
	public long time = 0;
	long time_no = 0;
	public boolean no_end = false;
	public boolean begin_no = false;
	
//	public Boom(float pX, float pY){
//		this.pX = pX;
//		this.pY = pY;
//	}
	public Boom(){
		
	}
	
/**	
Phương thức OnLoadResources
*/
	public void onLoadResources(Engine mEngine, Context mContext) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/Player/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mContext, "bum.png", 0, 0, 4, 4);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		
	}
	
/**	
Phương thức OnLoadScene
*/
	public void onLoadScene(Scene mScene) {
		no_AnimatedSprite = new AnimatedSprite(pX, pY, mTiledTextureRegion);
		this.no_AnimatedSprite.animate(40);
		mScene.attachChild(no_AnimatedSprite);	
	}
	
	public void init() {
		no_end = false;
		time = 0;
		time_no = 0;
		begin_no = false;
	}
	
/**	
Phương thức di chuyển đến vị trí X,Y
*/
	
	public void moveXY(float pX, float pY){
		this.pX = pX;
		this.pY = pY;
		this.no_AnimatedSprite.setPosition(pX, pY);			
		this.no_AnimatedSprite.setVisible(false);
		begin_no = true;
	}
	
	
	public void delay_no(Player a, Player b) {
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
				if(GlobalVariables.isServer){
					a.moveXY(50, 150);
					a.playAnimatedSprite.setVisible(true);
					b.moveXY(400,150);
					b.playAnimatedSprite.setVisible(true);
				}else{
					a.moveXY(400,150);
					a.playAnimatedSprite.setVisible(true);
					b.moveXY(50,150);
					b.playAnimatedSprite.setVisible(true);
				}
				init();
				// no_end = true;
			}
		}
	}
}

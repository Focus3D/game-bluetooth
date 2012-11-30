package BattleTank.com.Bullets;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.widget.Button;
import BattleTank.com.InterFaceSprite.InterFaceSprite;

public class Bullet implements InterFaceSprite {

	private BitmapTextureAtlas bulletBitmapTextureAtlas;
	private TextureRegion bulletTextureRegion;
	public Sprite bullet_Sprite;

	private float pX = -100;
	private float pY = -100;

	public Bullet() {
	}

	public void onLoadResources(Engine mEngine, Context mContext) {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/Bullets/");
		this.bulletBitmapTextureAtlas = new BitmapTextureAtlas(32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.bulletBitmapTextureAtlas, mContext,
						"Bullet.png", 0, 0);
		mEngine.getTextureManager().loadTexture(this.bulletBitmapTextureAtlas);
	}

	public void onLoadScene(Scene mScene) {
		this.bullet_Sprite = new Sprite(pX, pY, this.bulletTextureRegion);
		mScene.attachChild(bullet_Sprite);
	}

	public void setPositionX(float pX) {
		Bullet.this.pX = pX;
	}

	public float getPositionX() {
		return Bullet.this.pX;
	}

	public void setPositionY(float pY) {
		Bullet.this.pY = pY;
	}

	public float getPositionY() {
		return Bullet.this.pY;
	}

	public void setPositionXY(float pX, float pY) {
		Bullet.this.pX = pX;
		Bullet.this.pY = pY;
	}

	public void moveX(float moveX) {
		this.pX = moveX;
		moveBullet();
	}

	public void moveY(float moveY) {
		this.pY = moveY;
		moveBullet();
	}

	public void moveXY(float moveX, float moveY) {
		this.pX = moveX;
		this.pY = moveY;
		moveBullet();
	}

	public void moveRelativeX(float moveRelativeX) {
		this.pX += moveRelativeX;
		moveBullet();
	}

	public void moveRelativeY(float moveRelativeY) {
		this.pY += moveRelativeY;
		moveBullet();
	}

	public void moveRelaviteXY(float moveRelativeX, float moveRelativeY) {
		this.pX += moveRelativeX;
		this.pY += moveRelativeY;
		moveBullet();
	}

	private void moveBullet() {
		this.bullet_Sprite.setPosition(this.pX, this.pY);
	}
	
	private Sprite getSprite(){
		return bullet_Sprite;
	}
}

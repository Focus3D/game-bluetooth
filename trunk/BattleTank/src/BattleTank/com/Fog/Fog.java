package BattleTank.com.Fog;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import BattleTank.com.InterFaceSprite.InterFaceSprite;

public class Fog implements InterFaceSprite {

	public float pX = 0, pY = 0;
	public float alpha = 0.5f;
	private BitmapTextureAtlas fog_BitmapTextureAtlas;
	private TextureRegion fog_TextureOptions;
	private Sprite fog_Sprite;
	private String FOG_PATH = "Images/background/fog.png";

	public Fog() {
	}

	public void onLoadResources(Engine mEngine, Context mContext) {
		fog_BitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		fog_TextureOptions = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.fog_BitmapTextureAtlas, mContext,
						FOG_PATH, 0, 0);
		mEngine.getTextureManager().loadTexture(fog_BitmapTextureAtlas);
	}

	public void onLoadScene(Scene mScene) {
		this.fog_Sprite = new Sprite(pX, pY, fog_TextureOptions);
		mScene.attachChild(fog_Sprite);
		this.fog_Sprite.setAlpha(alpha);
	}
	
	public void setPositionX (float pX) {
		Fog.this.pX = pX;
	}
	
	public float getPoSitionX() {
		return Fog.this.pX;
	}
	
	public void setPositionY (float pY) {
		Fog.this.pY = pY;
	}
	
	public float getPoSitionY() {
		return Fog.this.pY;
	}
	
	public void setPisitionXY(float pX, float pY) {
		Fog.this.pX = pX;
		Fog.this.pY = pY;
	}
	
	/**
	Khởi tạo các phương thức di chuyển cho màn sương
	*/
	public void Fog_move(float pX, float pY){
		Fog.this.fog_Sprite.setPosition(pX, pY);
	}

}

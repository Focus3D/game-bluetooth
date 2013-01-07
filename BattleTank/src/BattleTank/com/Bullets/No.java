package BattleTank.com.Bullets;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasFactory;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.os.SystemClock;
import BattleTank.com.InterFaceSprite.InterFaceSprite;

public class No implements InterFaceSprite {
	
	// Cho biết đối tượng này được hiện thị hay không được hiện thị
	// Nếu visiable = true là được hiện thị, ngược lại là ẩn
	private boolean visiable = true;
	
	// Khai báo các biến load ảnh
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	public AnimatedSprite no_AnimatedSprite;

	// Khai báo các biến vị trí, ban đầu vị trí là X =0 , Y = 0
	private float pX = -100;
	private float pY = -100;
	
	public No() {
	}
	
/**	
Phương thức OnLoadResources
*/
	public void onLoadResources(Engine mEngine, Context mContext) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("Images/Bullets/");
		mBitmapTextureAtlas = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, mContext, "bum.png", 0, 0, 4, 4);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		
	}
	
/**	
Phương thức OnLoadScene
*/
	public void onLoadScene(Scene mScene) {
		no_AnimatedSprite = new AnimatedSprite(pX, pY, mTiledTextureRegion);
		no_AnimatedSprite.setPosition(-100, -100);
		this.no_AnimatedSprite.animate(60);
		mScene.attachChild(no_AnimatedSprite);		
	}
	
/**	
Phương thức di chuyển đến vị trí X,Y
*/
	
	public void moveXY(float pX, float pY){
		this.pX = pX;
		this.pY = pY;
		this.no_AnimatedSprite.setPosition(pX, pY);			
		this.no_AnimatedSprite.setVisible(false);
	}
}

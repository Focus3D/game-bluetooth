package TileMapDemo.com.Player;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

public class Player_test {

	private BitmapTextureAtlas player_bitBitmapTextureAtlas;
	private TiledTextureRegion player_TextureRegion;

	public AnimatedSprite player_AnimatedSprite;

	public void onLoadResources(Engine mEngine, Context mContext) {

		this.player_bitBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		System.out.print("test");
		this.player_TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(player_bitBitmapTextureAtlas, mContext,
						"tanks.png", 0, 0, 8, 8);

		/*
		 * player_bitBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,
		 * TextureOptions.BILINEAR_PREMULTIPLYALPHA); player_TextureRegion =
		 * BitmapTextureAtlasTextureRegionFactory
		 * .createTiledFromAsset(player_bitBitmapTextureAtlas, mContext,
		 * "tanks.png", 0, 0, 8, 8);
		 */

		mEngine.getTextureManager().loadTexture(player_bitBitmapTextureAtlas);
	}

	public void onLoadScene(Scene mScene) {
		player_AnimatedSprite = new AnimatedSprite(33, 132,
				player_TextureRegion);
		mScene.attachChild(player_AnimatedSprite);
	}

}

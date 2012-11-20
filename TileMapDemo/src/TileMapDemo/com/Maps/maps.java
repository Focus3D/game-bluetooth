package TileMapDemo.com.Maps;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.Debug;

import android.content.Context;

public class maps {

	/*
	 * Load maps với tên được chuyền vào. Return TMXTiledMap
	 */
	public static TMXTiledMap getTMXTiledMap(Scene MyScene, Engine mEngine,
			Context mContext, String maps_name) {
		TMXTiledMap mTMXTiledMap;
		try {
			final TMXLoader tmxLoader = new TMXLoader(mContext,
					mEngine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					new ITMXTilePropertiesListener() {
						public void onTMXTileWithPropertiesCreated(
								TMXTiledMap pTMXTiledMap,
								TMXLayer pTMXLayer,
								TMXTile pTMXTile,
								TMXProperties<TMXTileProperty> pTMXTileProperties) {
						}
					});
			mTMXTiledMap = tmxLoader
					.loadFromAsset(mContext, "tmx/" + maps_name);// Load từ thư
																	// mục
																	// Assets/tmx/tên
																	// map
			return mTMXTiledMap;
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
		return null;
	}
}

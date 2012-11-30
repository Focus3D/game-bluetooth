package BattleTank.com.Maps;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.Debug;

import android.content.Context;

public class Maps {

	/*
	 * load map với tên được truyền vào, return TMXTiledMap
	 */

	public static TMXTiledMap getTmxTiledMap(Scene MyScene, Engine mEngine,
			Context mContext, String maps_name) {

		TMXTiledMap mTMXTiledMap;
		try {
			final TMXLoader tmxloader = new TMXLoader(mContext,
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

			// Đường dẫn đến map
			mTMXTiledMap = tmxloader.loadFromAsset(mContext, "Maps/"
					+ maps_name);
			return mTMXTiledMap;
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
		return null;

	}
}

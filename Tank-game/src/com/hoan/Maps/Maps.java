package com.hoan.Maps;

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

public class Maps {
	public static TMXTiledMap getTMXTileMap(Scene mScene, Engine myEngine, Context mContext, String map_name)
	{
		TMXTiledMap mTMXTileMap;
		try{
			final TMXLoader mTMXLoader = new TMXLoader(mContext, myEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					new ITMXTilePropertiesListener() {
						public void onTMXTileWithPropertiesCreated(TMXTiledMap pTMXTiledMap,
								TMXLayer pTMXLayer, TMXTile pTMXTile,
								TMXProperties<TMXTileProperty> pTMXTileProperties) {
							// TODO Auto-generated method stub
							
						}
					});
			mTMXTileMap = mTMXLoader.loadFromAsset(mContext, "tmx/" + map_name);
		return mTMXTileMap;
		}catch(TMXLoadException ex)
		{
			Debug.e(ex);
		}
		return null;
	}


}

package TileMapDemo.com;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import TileMapDemo.com.Maps.maps;
import android.content.Context;
import android.graphics.YuvImage;
import android.os.Bundle;

public class TileMapDemoActivity extends BaseGameActivity {

	private Camera myCamera;
	private Scene myScene;

	private int WIDTH = 480;
	private int HEIGHT = 320;

	// Maps
	private TMXTiledMap tmxTiledMap;
	private TMXLayer VatCanTmxLayer;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTiledTextureRegion;
	private AnimatedSprite tank;
	
	
	private Player player;

	public Engine onLoadEngine() {
		myCamera = new Camera(0, 0, WIDTH, HEIGHT);
		Engine engine = new Engine(new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(WIDTH,
						HEIGHT), this.myCamera));

		return engine;
	}

	public void onLoadResources() {

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlas, this, "tanks.png",
						0, 0, 8, 8);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		
		
		this.player.onLoadResources(this.mEngine, this);

	}

	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		myScene = new Scene();

		this.tank = new AnimatedSprite(0, 164, mTiledTextureRegion);
		this.tank.animate(
				new long[] { 100, 100, 100, 100, 100, 100, 100, 100 },
				new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 1000);

		// myScene.setBackground(new ColorBackground(0.09804f, 0.6274f,
		// 0.8784f));

		// Load map

		tmxTiledMap = new maps().getTMXTiledMap(myScene, mEngine, this,
				"map22.tmx");
		ArrayList<TMXLayer> mapLayers = tmxTiledMap.getTMXLayers();
		for (TMXLayer layer : mapLayers) {
			if (layer.getName().equals("vatcan")) {

				VatCanTmxLayer = layer;
				// VatCanTmxLayer1 = layer;// N?u là v?t c?n th? không cho hi?n
				// th?
				// System.out.println("v?t c?n VatCanTmxLayer\n" + "X:" +
				// VatCanTmxLayer.getX() + "\nY" + VatCanTmxLayer.getY());

				continue;
			}
			myScene.attachChild(layer);
			myScene.attachChild(tank);
			player.onLoadScene(myScene);

			myScene.registerUpdateHandler(new IUpdateHandler() {

				public void reset() {
					// TODO Auto-generated method stub

				}

				// ==================================================================================

				public void onUpdate(float pSecondsElapsed) {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
					tank.setRotation(180);
					if (moveXY(tank, tank.getX() + tank.getWidth(), tank.getY()))
						tank.setPosition(tank.getX() + 5, tank.getY());

					else
						tank.setRotation(0);

				}
			});
		}
		return myScene;
	}

	public boolean collidesWith(float pX, float pY) {

		TMXTile mTMXTile = VatCanTmxLayer.getTMXTileAt(pX, pY);
		try {
			if (mTMXTile == null) {
				System.out.println("mTMXTile = khong co vat can");
			} else {
				TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile
						.getTMXTileProperties(tmxTiledMap);
				TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
				if (mTMXTileProperty.getName().equals("vatcan")) {
					System.out.println("Co vat can");
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean moveXY(AnimatedSprite sprite, float pX, float pY) {
		if (!collidesWith(pX, pY))
			return true;
		return false;
	}

	// Phương th?c set TMXTiledMap
	public void setTMXTiledMap(TMXTiledMap tmxTiledMap) {
		this.tmxTiledMap = tmxTiledMap;
	}

	// ==================================================================================
	// Phương th?c set TMXLayer
	public void setTMXLayer(TMXLayer VatCanTMXLayer) {
		this.VatCanTmxLayer = VatCanTMXLayer;
	}

	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}
	
	
	public class Player {

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

}

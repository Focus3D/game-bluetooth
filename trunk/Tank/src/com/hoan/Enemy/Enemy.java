package com.hoan.Enemy;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.hoan.Bullet.BulletSingle;
import com.hoan.InterfaceSprite.InterfaceSprite;
import com.hoan.Tools.Tools;

import android.content.Context;

public class Enemy implements InterfaceSprite {

	// Xác định mức tối đa quân địch mỗi map
	public int max_enemy = 10;
	
	// Tạo 1 Array chứ toàn bộ số kẻ địch
	public Enemy_src[] enemy_src;
	// Khai báo các biến load hình ảnh
	private BitmapTextureAtlas enemy_BitmapTextureAtlas;
	private TiledTextureRegion[] enemy_TiledTextureRegion;

	// Khai báo các biến làm việc với map
	private TMXTiledMap mTmxTiledMap;
	private TMXLayer VatCanTmxLayer;
	
	//
	public BulletSingle[] enemy_bullet;

	//
	private Scene mScene;

	// ==================================================================================
	/**
	 * Phương thức khởi dụng không tham số
	 */
	public Enemy(int max_enemy) {
		this.max_enemy = max_enemy;
		enemy_src = new Enemy_src[this.max_enemy];
		enemy_TiledTextureRegion = new TiledTextureRegion[this.max_enemy];
		enemy_bullet = new BulletSingle[this.max_enemy];
				
	}

	// ==================================================================================
	/**
	 * Phương thức onLoadResources
	 */

	public void onLoadResources(Engine mEngine, Context mContext) {
		this.enemy_BitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/player/");

		for (int i = 0; i < this.max_enemy; i++) {
			this.enemy_TiledTextureRegion[i] = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(enemy_BitmapTextureAtlas, mContext,
							"tanks.png", 0, 0, 8, 8);
		mEngine.getTextureManager().loadTexture(enemy_BitmapTextureAtlas);
		}
		for (int j = 0; j < this.max_enemy; j++) {
			this.enemy_bullet[j] = new BulletSingle();
			this.enemy_bullet[j].onLoadResources(mEngine, mContext);		
		}
	}

	// ==================================================================================
	/**
	 * Phương thức onLoadScene
	 */

	public void onLoadScene(Scene mScene) {
		this.mScene = mScene;
		for (int i = 0; i < this.max_enemy; i++) {
			enemy_src[i] = new Enemy_src(this, mScene, -100, -100,
					this.enemy_TiledTextureRegion[i]);		
		this.enemy_bullet[i].onLoadScene(mScene);	
		}
	}

	// ==================================================================================
	/**
	 * Khởi tạo vị trí ngẫu nhiên
	 */
	public void reset() {
		for (int i = 0; i < this.max_enemy; i++) {
			while (true) {
				int x = Tools.getRandomIndex(192, 416);
				int y = Tools.getRandomIndex(64, 256);
				if (!this.collidesWith(x, y) && !this.collidesWith(x + 32, y) && !this.collidesWith(x - 32, y) && !this.collidesWith(x, y + 32) && !this.collidesWith(x, y - 32)) {
					enemy_src[i] = new Enemy_src(this, this.mScene, x, y,this.enemy_TiledTextureRegion[i]);
					break;
				}
			}
		}
	}

	// ==================================================================================
	/**
	 * Phương thức set TMXTiledMap
	 */
	public void setTMXTiledMap(TMXTiledMap mTMXTiledMap) {
		this.mTmxTiledMap = mTMXTiledMap;
	}

	// ==================================================================================
	/**
	 * Phương thức set TMXLayer
	 */
	public void setTMXLayer(TMXLayer VatCanTMXLayer) {
		this.VatCanTmxLayer = VatCanTMXLayer;
	}

	// ==================================================================================
	/**
	 * Phương thức kiểm tra xem tại vị trí pX, pY có phải thuộc vào vùng không
	 * được di chuyển. Nếu thuộc vùng không được di chuyển thì return true. Nếu
	 * không thuộc thì return false. (Đối với cả player và quái vật đều được di
	 * chuyển nếu điều return false)
	 */
	public boolean collidesWith(float pX, float pY) {
		TMXTile mTMXTile = VatCanTmxLayer.getTMXTileAt(pX, pY);
		try {
			if (mTMXTile == null) {
				System.out.println("mTMXTile = null");
			} else {
				TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile
						.getTMXTileProperties(mTmxTiledMap);
				TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
				if (mTMXTileProperty.getName().equals("vatcan")) {
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ==================================================================================
	// Khi quái vật bị chết thì chờ trong thời gian là 5s sẽ được hiện thị lại.
	public void reset(Enemy_src enemy_src) {
		enemy_src.enemy_src_AnimatedSprite.setVisible(true);
		enemy_src.enemy_src_AnimatedSprite.setPosition(416, 128);
	}
}


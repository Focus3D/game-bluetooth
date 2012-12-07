package BattleTank.com.Bullets;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.content.Context;
import android.graphics.YuvImage;
import android.os.SystemClock;
import android.widget.Button;
import BattleTank.com.InterFaceSprite.InterFaceSprite;
import BattleTank.com.Player.Player;

// Lớp khởi tạo đối tượng viên đạn
public class Bullet implements InterFaceSprite {

	// Khai báo biến Player để lấy status
	public Player player;

	// Khai báo các biến trạng thái của viên đạn
	public int STATUS_BULLET = Status_Bullet.UN_MOVE_UP;

	// Khai báo các biến load hình ảnh viện đạn
	private BitmapTextureAtlas bulletBitmapTextureAtlas;
	private TextureRegion bulletTextureRegion;
	public Sprite bullet_Sprite;

	// Khai báo giá vị trí viên đạn ban đầu nằm ở ngoài màn hình
	private float pX = -100;
	private float pY = -100;

	// Khai báo biến speed - tốc độ di chuyển của viên đạn
	private int speed = 5;

	// Khai báo biến Nổ
	private No no;
	long time_no = 0;
	
	// khai báo biến TileMap
	private TMXTiledMap mTMXTiledMap;
	private TMXLayer VatCanTmxLayer;

	// Contructor khởi tạo không tham số
	public Bullet() {
	}

	public void onLoadResources(Engine mEngine, Context mContext) {
		// Load nổ vào 
		no = new No(pX, pY);
		no.onLoadResources(mEngine, mContext);
		// Load viên đạn vào
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
		no.onLoadScene(mScene);
		no.no_AnimatedSprite.setVisible(false);
		
	}

	/**
	 * Thiết lập trang thái cho viên đạn
	 */
	public void setStatusBullet(int statusbullet) {
		Bullet.this.STATUS_BULLET = statusbullet;
	}

	public int getStatusBullet() {
		return Bullet.this.STATUS_BULLET;
	}

	/**
	 * StatusMove
	 */
	public void StatusMove() {
		switch (this.STATUS_BULLET) {
		case Status_Bullet.MOVE_LEFT:
			this.bullet_Sprite.setRotation(180);
			break;
		case Status_Bullet.MOVE_RIGHT:
			this.bullet_Sprite.setRotation(0);
			break;
		case Status_Bullet.MOVE_UP:
			this.bullet_Sprite.setRotation(-90);
			break;
		case Status_Bullet.MOVE_DOWN:
			this.bullet_Sprite.setRotation(90);
			break;
		case Status_Bullet.UN_MOVE_LEFT:
			this.bullet_Sprite.setRotation(180);
			break;
		case Status_Bullet.UN_MOVE_RIGHT:
			this.bullet_Sprite.setRotation(0);
			break;
		case Status_Bullet.UN_MOVE_UP:
			this.bullet_Sprite.setRotation(-90);
			break;
		case Status_Bullet.UN_MOVE_DOWN:
			this.bullet_Sprite.setRotation(90);
			break;
		default:
			break;
		}

		System.out.println("bullet_Sprite.getRotation() = "
				+ bullet_Sprite.getRotation());
	}

	// -------------Các phương thức SetPosiotn và
	// GetPosition--------------------

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

	// Khởi tạo các phương thức di chuyển

	public void moveBullet() {
		// Trái
		this.bullet_Sprite.setVisible(true);
		if (!collidesWith(this.bullet_Sprite.getX(), this.bullet_Sprite.getY()) && !collidesWith(
				this.bullet_Sprite.getX(), this.bullet_Sprite.getY()
						+ this.bullet_Sprite.getHeight())
				&& this.bullet_Sprite.getRotation() == 180)
			moveRelaviteXY(-speed, 0);
		// Phải
		else if (!collidesWith(
				this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth(),
				this.bullet_Sprite.getY() - 2) && !collidesWith(
						this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth(),
						this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight() + 2)
				&& this.bullet_Sprite.getRotation() == 0)
			moveRelaviteXY(speed, 0);

		// Lên
		else if (!collidesWith(
				this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth() - 2,
				this.bullet_Sprite.getY()) && !collidesWith(
						this.bullet_Sprite.getX() + 2,
						this.bullet_Sprite.getY())
				&& this.bullet_Sprite.getRotation() == -90)
			moveRelaviteXY(0, -speed);
		// Xuống
		else if (!collidesWith(
				this.bullet_Sprite.getX() + 2,
				this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight())
				&& !collidesWith(
						this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth() - 2,
						this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight())
						&& this.bullet_Sprite.getRotation() == 90)
			moveRelaviteXY(0, speed);
		else {
			Delay_No();
		}
	}
	
	public void Delay_No(){
		no.moveXY(this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth() / 2, this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight() / 2);
		no.no_AnimatedSprite.setVisible(true);
		time_no = 0;
		time_no = SystemClock.elapsedRealtime();
		if(SystemClock.elapsedRealtime() - time_no > 1000) {
			System.out.println("SystemClock.elapsedRealtime() - time_no" + (SystemClock.elapsedRealtime() - time_no));
			
			no.moveXY(-100, -100);
			no.no_AnimatedSprite.setVisible(false);
		}
		this.bullet_Sprite.setVisible(false);
		this.bullet_Sprite.setPosition(-100, -100);
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
		this.bullet_Sprite.setPosition(this.pX, this.pY);
	}

	// Di chuyển đển vị trí mới
	public void moveRelativeX(float pX) {
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX() + pX,
				this.bullet_Sprite.getY());
	}

	public void moveRelativeY(float pY) {
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX(),
				this.bullet_Sprite.getY() + pY);
	}

	public void moveRelaviteXY(float pX, float pY) {
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX() + pX,
				this.bullet_Sprite.getY() + pY);
	}

	/**
	 * Phương thức set TMXTiledMap
	 */
	public void setTMXTiledMap(TMXTiledMap mTMXTiledMap) {
		this.mTMXTiledMap = mTMXTiledMap;
	}

	// ==================================================================================
	/**
	 * Phương thức set TMXLayer
	 */
	public void setTMXLayer(TMXLayer VatCanTMXLayer) {
		this.VatCanTmxLayer = VatCanTMXLayer;
	}

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
				//System.out.println("mTMXTile = null");
			} else {
				TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile
						.getTMXTileProperties(mTMXTiledMap);
				TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
				if (mTMXTileProperty.getName().equals("vatcan")) {
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private Sprite getSprite() {
		return this.bullet_Sprite;
	}
}
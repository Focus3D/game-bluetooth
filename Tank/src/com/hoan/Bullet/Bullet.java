package com.hoan.Bullet;

import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.hoan.InterfaceSprite.InterfaceSprite;
import com.hoan.Player.Player;
import com.hoan.Player.StatusPlayer;
import com.hoan.StaticDefine.GlobalVariables;
import com.hoan.Bullet.Boom;
import android.content.Context;
import android.os.SystemClock;

public class Bullet implements InterfaceSprite{

	// Khai báo biến Player để lấy status
	public Player player;

	// Khai báo các biến trạng thái của viên đạn
	public int STATUS_BULLET = StatusPlayer.UN_MOVE_UP;

	// Am thanh khi no
	private org.anddev.andengine.audio.sound.Sound soundBoom;
	// Khai báo các biến load hình ảnh viện đạn
	private BitmapTextureAtlas bulletBitmapTextureAtlas;
	private TiledTextureRegion bulletTextureRegion;
	public AnimatedSprite bullet_Sprite;
	public String BULLET_PATH = "gfx/Player/";

	// Khai báo giá vị trí viên đạn ban đầu nằm ở ngoài màn hình
	private float pX = -100;
	private float pY = -100;
	// cac bien dieu khien vu no
	public boolean no_end= false;
	private long time =0;
	private long time_no = 0;
	private boolean bool_sound_no = false;

	// Khai báo biến speed - tốc độ di chuyển của viên đạn
	private int speed = 10;
	
	// khai báo biến TileMap
	private TMXTiledMap mTMXTiledMap;
	private TMXLayer VatCanTmxLayer;

	// Contructor khởi tạo không tham số
	public Bullet() {
	}
	public void init(){
		no_end = false;
		time = 0;
		time_no = 0;
	}
	
//	public void delayNo(){
//		if(no_end)//Khi bom đã nổ rồi thì không gọi nữa
//			return;
//		//Khi mà bom được đặt xuống thì ta bắt đầu tính thời gian
//		if(time == 0)
//			time = SystemClock.elapsedRealtime();
//		
//	//	if(SystemClock.elapsedRealtime() - time >= 0){//Chờ đủ 3s thì ta cho phép nổ
//			//Đối với các đối tượng không được nổ thì không được hiện thị
//			boom.no_AnimatedSprite.setVisible(true);
//			
//			//Bắt đầu nổ.
//					if(!bool_sound_no && GlobalVariables.SOUND){
//						soundBoom.play();
//						bool_sound_no = true;
//					}
//			//Bắt đầu tính thời gian nổ
//			if(time_no == 0)
//				time_no = SystemClock.elapsedRealtime();
//			System.out.println(SystemClock.elapsedRealtime());
//			System.out.println(time_no);
//			System.out.println(SystemClock.elapsedRealtime() - time_no);
//			if(SystemClock.elapsedRealtime() - time_no > 1000){//Cho phép nổ trong 1s
//				System.out.println(SystemClock.elapsedRealtime() - time_no);
//				//Hết 1s ta cho ẩn toàn bộ và cho di chuyển đến vị trị -100,-100
//				boom.no_AnimatedSprite.setVisible(false);
//					moveNewXY(-100, -100);
//					no_end = true;
//				}
//	//		}
//	}
//	public void moveNewXY(float pX, float pY){
//		init();//khởi tạo lại các biến mạc định		
//		this.pX = pX;
//		this.pY = pY;
//		moveXY(pX, pY);//Khi gọi move thì bom sẽ được hiện thị
//		boom.moveXY(pX, pY);
//		bool_sound_no = false;
//	}

	public void onLoadResources(Engine mEngine, Context mContext) {
		// Load viên đạn vào
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(BULLET_PATH);
		this.bulletBitmapTextureAtlas = new BitmapTextureAtlas(128, 16,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.bulletTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bulletBitmapTextureAtlas, mContext,"dan.png", 0, 0, 4, 1);
	
//		boom = new Boom(pX, pY);
//		boom.onLoadResources(mEngine, mContext);
		SoundFactory.setAssetBasePath("Sound/");
		try {
			soundBoom = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(),mContext, "shoot.wav");
		} catch (Exception e) {
		}
		mEngine.getTextureManager().loadTexture(this.bulletBitmapTextureAtlas);
	}

	public void onLoadScene(Scene mScene) {
		this.bullet_Sprite = new AnimatedSprite(pX, pY,this.bulletTextureRegion);
		mScene.attachChild(bullet_Sprite);
//		boom.onLoadScene(mScene);
//		boom.no_AnimatedSprite.setVisible(false);
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
	public void StatusMove(Player player) {
		switch (this.STATUS_BULLET) {
		case StatusPlayer.MOVE_LEFT:
			this.bullet_Sprite.setRotation(180);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX() - player.playAnimatedSprite.getWidth() / 2,player.getPositionY() + player.playAnimatedSprite.getHeight() / 4);
			break;
		case StatusPlayer.MOVE_RIGHT:
			this.bullet_Sprite.setRotation(0);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX() + player.playAnimatedSprite.getWidth() / 2,player.getPositionY() + player.playAnimatedSprite.getHeight() / 4);
			break;
		case StatusPlayer.MOVE_UP:
			this.bullet_Sprite.setRotation(-90);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX(),player.getPositionY() - player.playAnimatedSprite.getHeight()/ 2);
			break;
		case StatusPlayer.MOVE_DOWN:
			this.bullet_Sprite.setRotation(90);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX(),player.getPositionY() + player.playAnimatedSprite.getHeight());
			break;
		case StatusPlayer.UN_MOVE_LEFT:
			this.bullet_Sprite.setRotation(180);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX() - player.playAnimatedSprite.getWidth() / 2,player.getPositionY() + player.playAnimatedSprite.getHeight() / 4);
			break;
		case StatusPlayer.UN_MOVE_RIGHT:
			this.bullet_Sprite.setRotation(0);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX() + player.playAnimatedSprite.getWidth() / 2,player.getPositionY() + player.playAnimatedSprite.getHeight() / 4);
			break;
		case StatusPlayer.UN_MOVE_UP:
			this.bullet_Sprite.setRotation(-90);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX(),player.getPositionY() - player.playAnimatedSprite.getHeight()/ 2);
			break;
		case StatusPlayer.UN_MOVE_DOWN:
			this.bullet_Sprite.setRotation(90);
			this.bullet_Sprite.animate(100);
			moveXY(player.getPositionX(),player.getPositionY() + player.playAnimatedSprite.getHeight());
			break;
		default:
			break;
		}
	}

	// -------------Các phương thức SetPosiotn và GetPosition--------------------

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

	public boolean moveBullet() {
		// Trái
		this.bullet_Sprite.setVisible(true);
		if (!collidesWith(this.bullet_Sprite.getX(), this.bullet_Sprite.getY()) && !collidesWith(this.bullet_Sprite.getX(),this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight()) && this.bullet_Sprite.getRotation() == 180){
			moveRelaviteXY(-speed, 0);
			return true;
		}
			
		
		// Phải
		else if (!collidesWith(this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth(),this.bullet_Sprite.getY() - 2) && !collidesWith(this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth(),this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight() + 2) && this.bullet_Sprite.getRotation() == 0){
			moveRelaviteXY(speed, 0);
			return true;
		}
			

		// Lên
		else if (!collidesWith(this.bullet_Sprite.getX() + this.bullet_Sprite.getWidth() - 2,this.bullet_Sprite.getY()) && !collidesWith(this.bullet_Sprite.getX() + 2,this.bullet_Sprite.getY()) && this.bullet_Sprite.getRotation() == -90){
			moveRelaviteXY(0, -speed);
			return true;
		}		
			
		
		// Xuống
		else if (!collidesWith(this.bullet_Sprite.getX() + 2,this.bullet_Sprite.getY() + this.bullet_Sprite.getHeight()) && !collidesWith(this.bullet_Sprite.getX()+ this.bullet_Sprite.getWidth() - 2,this.bullet_Sprite.getY()+ this.bullet_Sprite.getHeight())&& this.bullet_Sprite.getRotation() == 90){
			moveRelaviteXY(0, speed);
			return true;
		}
			
		
		else {
			this.bullet_Sprite.setVisible(false);
			this.bullet_Sprite.setPosition(-100, -100);
			return false;
		}		
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
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX() + pX,this.bullet_Sprite.getY());
	}

	public void moveRelativeY(float pY) {
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX(),this.bullet_Sprite.getY() + pY);
	}

	public void moveRelaviteXY(float pX, float pY) {
		this.bullet_Sprite.setPosition(this.bullet_Sprite.getX() + pX,this.bullet_Sprite.getY() + pY);
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
			} else {
				TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile.getTMXTileProperties(mTMXTiledMap);
				TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
				if (mTMXTileProperty.getName().equals("vatcan")) {
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private AnimatedSprite getSprite() {
		return this.bullet_Sprite;
	}

}

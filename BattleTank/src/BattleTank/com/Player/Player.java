package BattleTank.com.Player;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import BattleTank.com.Bullets.Bullet;
import BattleTank.com.InterFaceSprite.InterFaceSprite;

public class Player implements InterFaceSprite {

	public int PLAYER_STATUS = Status_Player.UN_MOVE_UP;

	public AnimatedSprite player;
	private BitmapTextureAtlas player_BitmapTextureAtlas;
	private TiledTextureRegion player_TiledTextureRegion;

	private float positionX = 0;
	private float positionY = 0;
	
	public Bullet[] MyBullet = new Bullet[10];
	
//	public ArrayList<Bullet> MyBullet = new ArrayList<Bullet>();

	public Player() {
	}

	public void onLoadResources(Engine mEngine, Context mContext) {

		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/player/");
		this.player_BitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.player_TiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.player_BitmapTextureAtlas, mContext,
						"tanks.png", 0, 0, 8, 8);
		mEngine.getTextureManager().loadTexture(this.player_BitmapTextureAtlas);
		
		// Thực hiện load toàn bộ số viên đạn vào
		for (int i = 0; i < MyBullet.length; i++) {
			MyBullet[i] = new Bullet();
			MyBullet[i].onLoadResources(mEngine, mContext);			
		}
		
		
	}

	public void onLoadScene(Scene mScene) {
		this.player = new AnimatedSprite(positionX, positionY,
				this.player_TiledTextureRegion);
		showPlayerStatus();
		this.player.animate(new long[] { 100, 100, 100, 100, 100, 100, 100, 100 }, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, 1000);
		mScene.attachChild(this.player);
		
		// Thực hiện việc load toàn bộ số viên đạn
		for (int i = 0; i < MyBullet.length; i++) {
			MyBullet[i].onLoadScene(mScene);			
		}
		
	}

	// ===============POSITION==============//
	/**
	 * Phương thức cho ta đặt vị trí của player theo trục X.
	 * 
	 * @param positionX
	 */

	public void setPositionX(float positionX) {
		Player.this.positionX = positionX;
	}

	/**
	 * Get vị trí x xủa player
	 * 
	 * @return
	 */
	public float getPositionX() {
		return Player.this.positionX;
	}

	/**
	 * Phương thức cho ta đạt vị trí của player theo trục Y
	 * 
	 * @param positionY
	 */
	public void setPositionY(float positionY) {
		Player.this.positionY = positionY;
	}

	/**
	 * Get vị trí y của player
	 * 
	 * @return
	 */
	public float getPositionY() {
		return Player.this.positionY;
	}

	/**
	 * Đặt vị trí khởi tạo cho player
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public void setPositionXY(float positionX, float positionY) {
		Player.this.positionX = positionX;
		Player.this.positionY = positionY;
	}

	// =======================================|| setStatus_Player
	// ||================================
	/**
	 * Phương thức set trạng thái cho Player
	 * 
	 * @param: Status_Player là trạng thái bạn muốn đặt. Các trạng thái bạn có
	 *         thể lấy bằng cách gọi Status_Player. (chấm) để tham chiếu đến các
	 *         trạng thái
	 */
	public void setStatus_Player(int Status_Player) {
		Player.this.PLAYER_STATUS = Status_Player;
		// Nếu setStatus_Player thì ta sẽ cập nhật xem player sẽ hiện thị
		// AnimatedSprite nào.
		showPlayerStatus();
	}
	
	/*
	 * 
	 * 
	 * // =======================================|| getStatus_Player //
	 * ||================================
	 *//**
	 * Phương thức get trạng thái cho Player
	 */
	public int getStatus_Player() {
		return Player.this.PLAYER_STATUS;
	}

	/*
	 * 
	 * // =======================================|| showPlayerStatus //
	 * ||================================
	 *//**
	 * Phương thức xác định xem Player đang ở trạng thái nào để hiện thị
	 */
	public void showPlayerStatus() {

		switch (this.PLAYER_STATUS) {
		case Status_Player.MOVE_LEFT: {
			player.setRotation(180);
			break;
		}
		case Status_Player.MOVE_RIGHT: {
			player.setRotation(0);
			break;
		}
		case Status_Player.MOVE_UP: {
			player.setRotation(-90);
			break;
		}
		case Status_Player.MOVE_DOWN: {
			player.setRotation(90);

			break;
		}
		case Status_Player.UN_MOVE_LEFT: {
			player.setRotation(180);
			break;
		}
		case Status_Player.UN_MOVE_RIGHT: {
			player.setRotation(0);
			break;
		}
		case Status_Player.UN_MOVE_UP: {
			player.setRotation(-90);
			break;
		}
		case Status_Player.UN_MOVE_DOWN: {
			player.setRotation(90);
			break;
		}

		default:
			break;
		}
	}

	/*
	 * 
	 * 
	 * // =======================================|| movePlayer //
	 * ||================================
	 *//**
	 * Move player tới vị trí X
	 * 
	 * @param moveX
	 */
	public void moveX(float moveX) {
		this.positionX = moveX;
		movePlayer();
	}

	/**
	 * Move player tới vị trí Y
	 * 
	 * @param moveY
	 */
	public void moveY(float moveY) {
		this.positionY = moveY;
		movePlayer();
	}

	/**
	 * Move player tới vị trí X,Y
	 * 
	 * @param moveX
	 * @param moveY
	 */
	public void moveXY(float moveX, float moveY) {
		this.positionX = moveX;
		this.positionY = moveY;
		movePlayer();
	}

	/**
	 * Move player tại vị trí hiện tại và cộng thêm 1 giá trị moveRelativeX
	 * 
	 * @param moveRelativeX
	 */
	public void moveRelativeX(float moveRelativeX) {
		this.positionX += moveRelativeX;
		movePlayer();
	}

	/**
	 * Move player tại vị trí hiện tại và cộng thêm 1 giá trị moveRelativeY
	 * 
	 * @param moveRelativeY
	 */
	public void moveRelativeY(float moveRelativeY) {
		this.positionY += moveRelativeY;
		movePlayer();
	}

	/**
	 * Move player tại vị trí hiện tại và cộng thêm 1 giá trị moveRelativeX,
	 * moveRelativeY
	 * 
	 * @param moveRelativeX
	 * @param moveRelativeY
	 */
	public void moveRelativeXY(float moveRelativeX, float moveRelativeY) {
		this.positionX += moveRelativeX;
		this.positionY += moveRelativeY;
		movePlayer();
	}

	/**
	 * Move player
	 */
	private void movePlayer() {
		player.setPosition(this.positionX, this.positionY);
	}

	public AnimatedSprite getAnimatedSprite() {
		return this.player;
	}
	
}

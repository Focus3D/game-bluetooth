package BattleTank.com.Enemy;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.os.SystemClock;
import BattleTank.com.Bullets.Status_Bullet;
import BattleTank.com.InterFaceSprite.InterFaceSprite;
import BattleTank.com.Player.Status_Player;
import BattleTank.com.Tools.Tools;

;

public class Enemy_src {
	// Biến tạo hiệu ứng
	public AnimatedSprite enemy_src_AnimatedSprite;

	private Scene mScene;

	// Xác định tốc độ di chuyển của các quái vật
	public int speed = 1;

	// Khai báo đối tượng quái vật
	private Enemy enemy;

	// Xác định xem quái vật này là loại nào. Hiện tại có 7 loại
	private int loai = 0;

	// =============================================================================
	/**
	 * Phương thức khởi dựng có tham số.
	 * 
	 * @param quaivat
	 * @param mScene
	 * @param pX
	 * @param pY
	 * @param Quaivat_TiledTextureRegion
	 */
	public Enemy_src(Enemy enemy, Scene mScene, float pX, float pY,
			TiledTextureRegion Quaivat_TiledTextureRegion) {
		this.enemy = enemy;
		this.mScene = mScene;
		loai = Tools.getRandomIndex(0, 6);
		enemy_src_AnimatedSprite = new AnimatedSprite(pX, pY,
				Quaivat_TiledTextureRegion);
		Enemy_Status(0);
		mScene.attachChild(enemy_src_AnimatedSprite);
		if (loai == 0)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 8, 15, true);
		else if (loai == 1)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 16, 23, true);
		else if (loai == 2)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 24, 31, true);
		else if (loai == 3)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 32, 39, true);
		else if (loai == 4)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 40, 47, true);
		else if (loai == 5)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 48, 55, true);
		else if (loai == 6)
			enemy_src_AnimatedSprite.animate(new long[] { 100, 100, 100, 100,
					100, 100, 100, 100 }, 56, 63, true);
	}

	// =============================================================================
	/**
	 * Phương thức statusMoveUp
	 */
	public void Enemy_Status(int huong) {

		switch (huong) {
		// LEFT
		case 0: {
			enemy_src_AnimatedSprite.setRotation(180);
			break;
		}
		// RIGHT
		case 1: {
			enemy_src_AnimatedSprite.setRotation(0);
			break;
		}
		// UP
		case 2: {
			enemy_src_AnimatedSprite.setRotation(-90);
			break;
		}
		// DOWN
		case 3: {
			enemy_src_AnimatedSprite.setRotation(90);
			break;
		}
		default:
			break;
		}
	}

	// =============================================================================
	/**
	 * Di chuyển quái vật tới vị trí pX, pY nếu mà không có va chạm với vật cản
	 */
	public void moveXY(float pX, float pY) {
		if (!enemy.collidesWith(pX, pY))
			enemy_src_AnimatedSprite.setPosition(pX, pY);
	}

	// =============================================================================
	public void moveRelativeXY(float pX, float pY) {
		enemy_src_AnimatedSprite.setPosition(enemy_src_AnimatedSprite.getX()
				+ pX, enemy_src_AnimatedSprite.getY() + pY);
	}

	// =============================================================================

	/**
	 * Di chuyển 1 cách ngẫu nhiên trên màn hình
	 */
	long time = SystemClock.elapsedRealtime();
	int huong = 0;

	public void moveRandom() {
		// Cứ sau 3s thì đối tượng quái vật tự xác định lại đường đi
		if (SystemClock.elapsedRealtime() - time > 2000) {
			huong = Tools.getRandomIndex(0, 3);
			time = SystemClock.elapsedRealtime();
			Enemy_Status(huong);
		}
		
		Enemy_Move(huong, loai);
	}
	
	public void Enemy_Move(int huong,int loai) {
		if(loai == 0 || loai == 1 )
			speed = 1;
		else if(loai ==2 || loai ==3)
			speed = 2;
		else if(loai == 4 || loai == 5)
			speed = 3;
		else speed = 4;
		
		if (huong == 0) {// Trái
			if (!enemy.collidesWith(enemy_src_AnimatedSprite.getX(),
					enemy_src_AnimatedSprite.getY() + 10) && !enemy.collidesWith(
					enemy_src_AnimatedSprite.getX(),
					enemy_src_AnimatedSprite.getY()
							+ enemy_src_AnimatedSprite.getHeight() - 10))
				moveRelativeXY(-speed, 0);
		} else if (huong == 1) {// Phải
			if (!enemy.collidesWith(enemy_src_AnimatedSprite.getX()
					+ enemy_src_AnimatedSprite.getWidth(),
					enemy_src_AnimatedSprite.getY() + 10) && !enemy.collidesWith(
					enemy_src_AnimatedSprite.getX()
							+ enemy_src_AnimatedSprite.getWidth(),
					enemy_src_AnimatedSprite.getY()
							+ enemy_src_AnimatedSprite.getHeight() - 10))
				moveRelativeXY(speed, 0);
		} else if (huong == 2) {// Lên
			if (!enemy.collidesWith(enemy_src_AnimatedSprite.getX() + 10,
					enemy_src_AnimatedSprite.getY()) && !enemy.collidesWith(
					enemy_src_AnimatedSprite.getX()
							+ enemy_src_AnimatedSprite.getWidth() - 10,
					enemy_src_AnimatedSprite.getY()))
				moveRelativeXY(0, -speed);
		} else if (huong == 3) {// Xuống
			if (!enemy.collidesWith(enemy_src_AnimatedSprite.getX() + 10,
					enemy_src_AnimatedSprite.getY()
							+ enemy_src_AnimatedSprite.getHeight()) && !enemy
					.collidesWith(enemy_src_AnimatedSprite.getX()
							+ enemy_src_AnimatedSprite.getWidth() - 10,
							enemy_src_AnimatedSprite.getY()
									+ enemy_src_AnimatedSprite.getHeight()))
				moveRelativeXY(0, speed);
		}
	}

	// =============================================================================
	/**
	 * Xóa bỏ quái vật
	 */
	public void deleteEnemy_src() {
		this.mScene.detachChild(enemy_src_AnimatedSprite);
	}

	// ============================================================================
	long time_reset_begin = 0;
	public boolean bool_reset = false;

	public void reset() {
		if (bool_reset) {
			if (!this.enemy_src_AnimatedSprite.isVisible()
					&& time_reset_begin == 0)
				time_reset_begin = SystemClock.elapsedRealtime();

			if (time_reset_begin != 0
					&& SystemClock.elapsedRealtime() - time_reset_begin > 5000) {
				// Khởi tạo lại quái vật tại vị trí ban đầu và cho hiện thị
				this.enemy_src_AnimatedSprite.setPosition(416, 128);
				this.enemy_src_AnimatedSprite.setVisible(true);
				time_reset_begin = 0;
				bool_reset = false;
			}
		}
	}

}

package BattleTank.com.MainGame;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXProperties;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTileProperty;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import BallteTank.com.BaseAndengine.MultiTouch;
import BallteTank.com.BaseAndengine.MultiTouchController;
import BattleTank.com.Bullets.Bullet;
import BattleTank.com.Bullets.No;
import BattleTank.com.Bullets.Status_Bullet;
import BattleTank.com.ClassStatic.ControlerStatic;
import BattleTank.com.ClassStatic.ScreenStatic;
import BattleTank.com.Database.Database;
import BattleTank.com.Dialog.Dialog_exit;
import BattleTank.com.Enemy.Enemy;
import BattleTank.com.Fog.Fog;
import BattleTank.com.Maps.Maps;
import BattleTank.com.Player.Player;
import BattleTank.com.Player.Status_Player;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainGameActivity extends BaseGameActivity implements
		IOnSceneTouchListener {

	private Scene myScene;
	private Camera MyCamera;

	/**
	 * Các biến phụ
	 */
	private int i = 0, j = 0;
	private long dem = 0;
	private long time = 0;
	private boolean shoot = false;
	boolean temp = false;
	// ứng với mỗi level ta load maps theo level
	private int LEVEL = 1;

	private Database db = new Database(this);
	// Điểm của người chơi.
	private int DIEM = 0;
	private int diem1 = 0;
	private ChangeableText TextDiem;

	private ChangeableText TextLevel;

	private BitmapTextureAtlas TiepTucBitmapTextureAtlas;
	private TextureRegion TiepTucTextureRegion;
	private Sprite TiepTucSprite;

	private BitmapTextureAtlas ABBitmapTextureAtlas;
	private TextureRegion ATextureRegion;
	private TextureRegion BTextureRegion;
	private Sprite A, B;

	// Khai báo biến Fog- màn sương
	private Fog myFog;
	/**
	 * Các biến của người chơi
	 */
	// Khai báo biến player
	private Player MyPlayer;
	// Máu của người chơi, mặc định ban đầu là 3 mạng
	private int HEART = 3;
	// Hiển thị của máu trên màn hình
	private BitmapTextureAtlas HeartBitmapTextureAtlas;
	private TextureRegion HeartTextureRegion;
	private Sprite HeartSprite;
	// Text hiện thị số mạng của người chơi, mặc định ban đầu là 3 mạng
	private ChangeableText TextHeart;
	// Font hiện thị số
	private Font mFont;
	private BitmapTextureAtlas mFontTexture;

	private ArrayList<Bullet> bulletsArrayList = new ArrayList<Bullet>();
	private boolean bullet_boolean = false;

	// Khai báo biến enemy
	private Enemy MyEnemy;
	// Số quân địch xuất hiện trên màn hình
	private int MAX_SO_ENEMY = 2;
	// Là số quái vật mà người chơi phải tiêu diệt
	private int SO_ENEMY_CAN_TIEU_DIET = 10;
	// Khi tiêu diệt đủ số quái thì sẽ được tăng level
	private int DEM_SO_QUAI_BI_TIEU_DIET = 0;

	// Khai báo biến nổ
	private No myNo;

	// ....
	private boolean OVERGAME = false;
	private boolean WIN = false;
	private boolean NEXT_LEVEL = false;

	// Maps
	private TMXTiledMap mTmxTiledMap;
	private TMXLayer vatcanTmxLayer;
	private String TEN_MAPS = "map22.tmx";

	// Khai báo phần điều khiển nhân vật chính
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private int status_digitalOnScreenControl = -1;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;

	private BitmapTextureAtlas shootBitmapTextureAtlas;
	private TextureRegion shootTextureRegion;
	private Sprite shoot_Sprite;

	// =======================================|| onLoadEngine
	// ||================================
	public Engine onLoadEngine() {

		Bundle b = getIntent().getExtras();
		if (b != null) {
			LEVEL = b.getInt("LEVEL");
			MAX_SO_ENEMY = b.getInt("MAX_SO_ENEMY");
			SO_ENEMY_CAN_TIEU_DIET = b.getInt("SO_ENEMY_CAN_TIEU_DIET");
			TEN_MAPS = b.getString("TEN_MAPS");
			DIEM = b.getInt("DIEM");

			HEART = b.getInt("HEART");
		} else {
			// Nếu không có giá trị được truyền theo thì ta coi như level là 1
			LEVEL = 1;

			// ENEMY
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 5;

			// Maps
			TEN_MAPS = "map22.tmx";
		}
		DEM_SO_QUAI_BI_TIEU_DIET = 0;

		// Khởi tạo biến màn sương
		myFog = new Fog();

		// Khởi tạo biến Player
		MyPlayer = new Player();
		MyPlayer.setHeart(HEART);

		// Khởi tạo biến Enemy
		MyEnemy = new Enemy(MAX_SO_ENEMY);

		// Khai báo biến nổ
		myNo = new No();

		MyCamera = new Camera(0, 0, ScreenStatic.CAMERA_WIDTH,
				ScreenStatic.CAMERA_HEIGHT);
		Engine engine = new Engine(new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						ScreenStatic.CAMERA_WIDTH, ScreenStatic.CAMERA_HEIGHT),
				this.MyCamera));
		// Kiểm tra xem thiết bị có hỗ trộ cảm ứng đa điểm hay không.
		try {
			if (MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
				if (MultiTouch.isSupportedDistinct(this)) { // Thiết bị có hỗ
															// trợ.
				} else {
				}
			} else {
			}
		} catch (Exception e) {
		}

		return engine;
	}

	// =======================================|| HIỆN THỊ PHẦN LOADING
	// ||========================

	public void onLoadResources() {

		myFog.onLoadResources(MainGameActivity.this.mEngine,
				MainGameActivity.this);

		MyPlayer.onLoadResources(MainGameActivity.this.mEngine,
				MainGameActivity.this);

		MyEnemy.onLoadResources(MainGameActivity.this.mEngine,
				MainGameActivity.this);

		myNo.onLoadResources(MainGameActivity.this.mEngine,
				MainGameActivity.this);

		// Load hình ảnh phần điều khiển
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/Control/");
		mOnScreenControlTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"base.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"knob.png", 128, 0);
		mEngine.getTextureManager().loadTexture(mOnScreenControlTexture);

		// Load hình ảnh Icon Shoot
		shootBitmapTextureAtlas = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		shootTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(shootBitmapTextureAtlas, this, "shoot.png", 0,
						0);
		mEngine.getTextureManager().loadTexture(shootBitmapTextureAtlas);

		// Load Heart
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/Control/");
		this.HeartBitmapTextureAtlas = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.HeartTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.HeartBitmapTextureAtlas, this,
						"tank_heart.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(
				this.HeartBitmapTextureAtlas);

		// ...... Tiếp tục
		this.TiepTucBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.TiepTucTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.TiepTucBitmapTextureAtlas, this,
						"tieptuc.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(
				this.TiepTucBitmapTextureAtlas);

		// .......
		this.ABBitmapTextureAtlas = new BitmapTextureAtlas(32, 32,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.ATextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.ABBitmapTextureAtlas, this, "A.png", 0, 0);
		this.BTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.ABBitmapTextureAtlas, this, "B.png", 16,
						0);
		this.mEngine.getTextureManager()
				.loadTextures(this.ABBitmapTextureAtlas);

		// Load font
		this.mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("Fonts/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this,
				"CGFArchReactor.ttf", 30, true, Color.BLUE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

	}

	public Scene onLoadScene() {
		myScene = new Scene();

		// Load maps
		mTmxTiledMap = Maps.getTmxTiledMap(myScene, mEngine, this, TEN_MAPS);
		ArrayList<TMXLayer> mapLayers = mTmxTiledMap.getTMXLayers();
		for (TMXLayer layer : mapLayers) {
			if (layer.getName().equals("vatcan")) {
				vatcanTmxLayer = layer;// Nếu là vật cản thì không cho hiện thị
				System.out.println("vật cản");
				continue;
			}
			myScene.attachChild(layer);
		}

		// Load phần player vào Scene
		MyPlayer.setPositionXY(100, 132);
		MyPlayer.onLoadScene(myScene);

		// load phần Enemy vào Scene
		MyEnemy.onLoadScene(myScene);
		MyEnemy.setTMXTiledMap(mTmxTiledMap);
		MyEnemy.setTMXLayer(vatcanTmxLayer);

		// Thực hiện việc load ngẫu nhiên kẻ thù lên màn hình
		MyEnemy.reset();

		// OnLoadScene Bullet
		for (int i = 0; i < MyPlayer.MyBullet.length; i++) {
			MyPlayer.MyBullet[i].setTMXTiledMap(mTmxTiledMap);
			MyPlayer.MyBullet[i].setTMXLayer(vatcanTmxLayer);
		}

		// Load và xử lý phần điều khiển
		mDigitalOnScreenControl = new DigitalOnScreenControl(0,
				ScreenStatic.CAMERA_HEIGHT
						- this.mOnScreenControlBaseTextureRegion.getHeight(),
				this.MyCamera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				new IOnScreenControlListener() {

					float pX = 0, pY = 0;

					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {
						if (pValueX > 0
								&& MyPlayer.getStatus_Player() != Status_Player.MOVE_RIGHT) {
							MyPlayer.setStatus_Player(Status_Player.MOVE_RIGHT);
							status_digitalOnScreenControl = Status_Player.MOVE_RIGHT;
							pX = MyPlayer.player.getWidth()
									+ MyPlayer.getPositionX();
							pY = MyPlayer.getPositionY()
									+ (MyPlayer.player.getHeight() / 2);
						}

						else if (pValueX < 0
								&& MyPlayer.getStatus_Player() != Status_Player.MOVE_LEFT) {
							MyPlayer.setStatus_Player(Status_Player.MOVE_LEFT);
							status_digitalOnScreenControl = Status_Player.MOVE_LEFT;
							pX = MyPlayer.getPositionX();
							pY = MyPlayer.getPositionY()
									+ (MyPlayer.player.getHeight() / 2);
						}

						else if (pValueY > 0
								&& MyPlayer.getStatus_Player() != Status_Player.MOVE_DOWN) {
							MyPlayer.setStatus_Player(Status_Player.MOVE_DOWN);
							status_digitalOnScreenControl = Status_Player.MOVE_DOWN;
							pX = MyPlayer.getPositionX()
									+ (MyPlayer.player.getWidth() / 2);
							pY = MyPlayer.getPositionY()
									+ MyPlayer.player.getHeight();
						}

						else if (pValueY < 0
								&& MyPlayer.getStatus_Player() != Status_Player.MOVE_UP) {
							MyPlayer.setStatus_Player(Status_Player.MOVE_UP);
							status_digitalOnScreenControl = Status_Player.MOVE_UP;
							pX = MyPlayer.getPositionX()
									+ (MyPlayer.player.getWidth() / 2);
							pY = MyPlayer.getPositionY();
						}

						else {
							switch (status_digitalOnScreenControl) {
							case Status_Player.MOVE_RIGHT:
								MyPlayer.setStatus_Player(Status_Player.UN_MOVE_RIGHT);
								break;
							case Status_Player.MOVE_LEFT:
								MyPlayer.setStatus_Player(Status_Player.UN_MOVE_LEFT);
								break;
							case Status_Player.MOVE_UP:
								MyPlayer.setStatus_Player(Status_Player.UN_MOVE_UP);
								break;
							case Status_Player.MOVE_DOWN:
								MyPlayer.setStatus_Player(Status_Player.UN_MOVE_DOWN);
								break;
							default:
								break;
							}
						}

						if (pValueX != 0 || pValueY != 0) {
							TMXTile mTMXTile = vatcanTmxLayer.getTMXTileAt(pX
									+ pValueX * 7, pY + pValueY * 7);
							System.out.println("pValueX=" + pValueX
									+ ";pValueY:=" + pValueY);
							try {
								if (mTMXTile == null) {
								} else {
									TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile
											.getTMXTileProperties(mTmxTiledMap);

									TMXTileProperty mTMXTileProperty = mTMXProperties
											.get(0);
									if (mTMXTileProperty.getName().equals(
											"vatcan")) {

									}
								}
							} catch (Exception e) {
								MyPlayer.moveRelativeXY(pValueX * 7,
										pValueY * 7);
							}
						}

					}
				});

		mDigitalOnScreenControl.getControlBase().setBlendFunction(
				GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mDigitalOnScreenControl.getControlBase().setAlpha(0.7f);
		mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		mDigitalOnScreenControl.getControlBase().setScale(0.8f);
		mDigitalOnScreenControl.getControlKnob().setScale(0.8f);
		mDigitalOnScreenControl.refreshControlKnobPosition();

		myScene.setChildScene(mDigitalOnScreenControl);

		// --------------------Hết Phần Điều Khiển Nhân Vật---------------------

		// -------------------- Phần di chuyển của Viên Đạn Bullet
		// Load phần Bullet vào Scene

		// Xử lý sự kiện khi chạm vào vùng bắn viên đạn SHOOT
		this.shoot_Sprite = new Sprite(ScreenStatic.CAMERA_WIDTH
				- this.shootTextureRegion.getWidth(),
				ScreenStatic.CAMERA_HEIGHT
						- this.shootTextureRegion.getHeight(),
				this.shootTextureRegion) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					shoot_Sprite.setAlpha(1.0f);
					bullet_boolean = true;
					return bullet_boolean;
				}
				shoot_Sprite.setAlpha(0.5f);
				return bullet_boolean = false;

			}
		};

		// setAlpha để chỉnh độ rõ hay mờ của hình ảnh
		this.shoot_Sprite.setAlpha(0.5f);
		myScene.attachChild(shoot_Sprite);
		myScene.registerTouchArea(shoot_Sprite);

		// Load HEART lên màn hình
		// this.HeartSprite = new Sprite(100, 0, this.HeartTextureRegion);
		this.HeartSprite = new Sprite(ScreenStatic.CAMERA_WIDTH
				- this.HeartTextureRegion.getWidth(), 0,
				this.HeartTextureRegion);
		HeartSprite.setAlpha(0.8f);
		myScene.attachChild(HeartSprite);
		myScene.registerTouchArea(HeartSprite);
		// Hiện thị tối đa 2 ký tự
		TextHeart = new ChangeableText(this.HeartSprite.getX() + 23, 15,
				this.mFont, String.valueOf(MyPlayer.getHeart()), 2);
		TextHeart.setAlpha(0.9f);
		myScene.attachChild(TextHeart);

		// ..... ĐIỂM CỦA NGƯỜI CHƠI
		// Hiện thị tối đa 10 ký tự
		TextDiem = new ChangeableText(0, 10, this.mFont, String.valueOf(DIEM),
				10);
		myScene.attachChild(TextDiem);

		// FOG - MÀN SƯƠNG
		myFog.onLoadScene(myScene);

		// No - hiệu ứng nổ
		myNo.onLoadScene(myScene);

		// TIẾP TỤC CHƠI

		this.TiepTucSprite = new Sprite(ScreenStatic.CAMERA_WIDTH / 2
				- this.TiepTucTextureRegion.getWidth() / 2,
				ScreenStatic.CAMERA_HEIGHT / 2
						- this.TiepTucTextureRegion.getHeight() / 2,
				this.TiepTucTextureRegion) {

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN
						&& TiepTucSprite.isVisible()) {

					// pHẦN ÂM THANH
					// if(ControlerStatic.SOUND)
					// outch.play();
					// Chiến thắng
					if (LEVEL == ControlerStatic.TONG_SO_LEVEL) {
						Intent i = new Intent(MainGameActivity.this,
								ChienThang.class);
						i.putExtra("diem", DIEM);
						MainGameActivity.this.startActivity(i);
						MainGameActivity.this.finish();
					} else
						nextLevel();
				}
				return true;
			}
		};
		TiepTucSprite.setVisible(false);
		myScene.attachChild(TiepTucSprite);
		myScene.registerTouchArea(TiepTucSprite);

		this.A = new Sprite(-100, -100, this.ATextureRegion);
		A.setVisible(false);
		myScene.attachChild(A);
		myScene.registerTouchArea(A);

		this.B = new Sprite(-100, -100, this.ATextureRegion);
		B.setVisible(false);
		myScene.attachChild(B);
		myScene.registerTouchArea(B);

		/**
		 * Vòng lặp game UPDATEHANDLER
		 */

		myScene.registerUpdateHandler(new IUpdateHandler() {

			public void reset() {
				// TODO Auto-generated method stub

			}

			public void onUpdate(float pSecondsElapsed) {

				// Nếu chưa kết thúc vòng chơi thì thực hiện
				if (!OVERGAME) {
					// Nếu chưa thắng hết vòng chơi thì thực hiện
					if (!WIN) {
						// Thiết lập vòng lặp, sau 20ms sẽ thay đổi 1 lần
						try {
							Thread.sleep(20);
							for (int i = 0; i < MyEnemy.max_enemy; i++) {
								if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
										.isVisible())
									MyEnemy.enemy_src[i].moveRandom();
								// System.out.println("DEM_SO_QUAI_BI_TIEU_DIET = "
								// + DEM_SO_QUAI_BI_TIEU_DIET);
								// Nếu quái vật nào mà ở trạng thái ẩn thì ta
								// cho
								// hiện thị lên.
								// Với điều kiện là số quái vật cần phải tiêu
								// diệt
								// lơn hơn so với tổng số quái vật có
								if (SO_ENEMY_CAN_TIEU_DIET
										- DEM_SO_QUAI_BI_TIEU_DIET >= MAX_SO_ENEMY) {
									if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
											.isVisible())
										// System.out.println("DEM_SO_QUAI_BI_TIEU_DIET = "
										// + DEM_SO_QUAI_BI_TIEU_DIET);
										MyEnemy.enemy_src[i].bool_reset = true;
								}

								if ((SO_ENEMY_CAN_TIEU_DIET * 50) - diem1 >= (MAX_SO_ENEMY * 50)) {
									if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
											.isVisible())
										MyEnemy.enemy_src[i].bool_reset = true;
								}

								MyEnemy.enemy_src[i].reset();

								// Kiểm tra va chạm giữa player với Enemy
								if (vaCham(
										MyEnemy.enemy_src[i].enemy_src_AnimatedSprite,
										MyPlayer.player)) {

									// Nếu có va chạm thì di chuyển player về vị
									// trí
									// ban
									// đầu
									MyPlayer.moveXY(100, 100);
									MyPlayer.setHeart(MyPlayer.getHeart() - 1);

									// Cập nhật và hiện thị số mạng còn lại
									TextHeart.setText(String.valueOf(MyPlayer
											.getHeart()));
								}
							}

							try {
								while (bullet_boolean
										&& i < MyPlayer.MyBullet.length) {
									MyPlayer.MyBullet[i]
											.setStatusBullet(MyPlayer
													.getStatus_Player());
									MyPlayer.MyBullet[i].StatusMove();
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_UP
											|| MyPlayer.MyBullet[i]
													.getStatusBullet() == Status_Bullet.UN_MOVE_UP)
										MyPlayer.MyBullet[i].moveXY(
												MyPlayer.player.getX(),
												MyPlayer.player.getY()
														- MyPlayer.player
																.getHeight()
														/ 2);
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_DOWN
											|| MyPlayer.MyBullet[i]
													.getStatusBullet() == Status_Bullet.UN_MOVE_DOWN)
										MyPlayer.MyBullet[i].moveXY(
												MyPlayer.player.getX(),
												MyPlayer.player.getY()
														+ MyPlayer.player
																.getHeight());
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_LEFT
											|| MyPlayer.MyBullet[i]
													.getStatusBullet() == Status_Bullet.UN_MOVE_LEFT)
										MyPlayer.MyBullet[i].moveXY(
												MyPlayer.player.getX()
														- MyPlayer.player
																.getWidth() / 2,
												MyPlayer.player.getY()
														+ MyPlayer.player
																.getHeight()
														/ 4);
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_RIGHT
											|| MyPlayer.MyBullet[i]
													.getStatusBullet() == Status_Bullet.UN_MOVE_RIGHT)
										MyPlayer.MyBullet[i].moveXY(
												MyPlayer.player.getX()
														+ MyPlayer.player
																.getWidth() / 2,
												MyPlayer.player.getY()
														+ MyPlayer.player
																.getHeight()
														/ 4);
									i++;
									bullet_boolean = false;
								}

								for (Bullet mybullet : MyPlayer.MyBullet) {
									mybullet.moveBullet();
								}

								if (i == MyPlayer.MyBullet.length)
									i = 0;

								// /Khi vien đạn đang trong trạng di chuyển thì
								// kiểm
								// tra
								// xem có enemy nào
								// va chạm với viên đạn không. Nếu Enemy va chạm
								// với
								// viên đạn thì Enemy đó sẽ bị biến mất
								for (Bullet mybullet : MyPlayer.MyBullet) {

									for (int i = 0; i < MyEnemy.max_enemy; i++) {

										System.out.println("A : " + A.getX());
										if (vaCham(
												mybullet.bullet_Sprite,
												MyEnemy.enemy_src[i].enemy_src_AnimatedSprite)) {

											System.out.println("Có va chạm !");
											if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
													.isVisible()) {
												// Mỗi 1 quân địch bị chết sẽ
												// cộng thêm 50 điểm vào số điểm
												if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
														.isVisible()) {
													DIEM = DIEM + 500;
													diem1 = diem1 + 500;
													DEM_SO_QUAI_BI_TIEU_DIET++;

													if (SO_ENEMY_CAN_TIEU_DIET == DEM_SO_QUAI_BI_TIEU_DIET) {
														// Đã tiêu diệt xong
														// quái vật/
														// Hiện thông báo vượt
														// qua level và chuyển
														// sang level tiếp theo
														System.out
																.println("Thành công. Đã tiêu diệt xong.");

														WIN = true;
													}

													if (SO_ENEMY_CAN_TIEU_DIET
															- DEM_SO_QUAI_BI_TIEU_DIET >= MAX_SO_ENEMY) {
														if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
																.isVisible())
															MyEnemy.enemy_src[i].bool_reset = true;
													}

													if ((SO_ENEMY_CAN_TIEU_DIET * 50)
															- diem1 >= (MAX_SO_ENEMY * 50)) {
														if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
																.isVisible())
															MyEnemy.enemy_src[i].bool_reset = true;
													}
												}

											}

											// Enemy bị ẩn đi và di chuyển về vị
											// trí
											// -100, -100
											// myNo.moveXY(MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.getX(),
											// MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.getY());
											MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
													.setVisible(false);
											MyEnemy.enemy_src[i].enemy_src_AnimatedSprite
													.setPosition(-10, -10);

											// Hiện thị điểm
											// Nếu nhiều hơn 10 ký tự thì hiện
											// thị
											TextDiem.setText(
													String.valueOf(DIEM), true);

											mybullet.bullet_Sprite
													.setVisible(false);
											mybullet.bullet_Sprite.setPosition(
													-100, -100);

										}
									}
								}

							} catch (Exception e) {
								// TODO: handle exception
							}

							// Màn sương di theo xe tank
							myFog.Fog_move(MyPlayer.getPositionX() - 480
									+ MyPlayer.player.getWidth() / 2,
									MyPlayer.getPositionY() - 320
											+ MyPlayer.player.getHeight() / 2);

							if (MyPlayer.getHeart() <= 0) {
								// Game over
								System.out.println("GameOver");
								// OVERGAME
								OVERGAME = true;
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
						// =============KHI NGƯỜI CHƠI DÀNH CHIẾN
						// THẮNG===============

					} else {
						// Vượt qua level
						if (!NEXT_LEVEL) {
							TiepTucSprite.setVisible(true);
							float pX = TiepTucSprite.getX() + 1;
							if (pX > 180)
								pX = ScreenStatic.CAMERA_WIDTH / 2
										- TiepTucSprite.getWidth() / 2;
							TiepTucSprite.setPosition(pX, TiepTucSprite.getY());
						}

					}
				}
				// OVERGAME
				else {
					// Kiểm tra xem số điểm của người chơi có cao hơn số điểm
					// của những người chơi khác không
					// Nếu cao hơn ta chuyển qua phần lưu tên người chơi.
					//if (db.kt_luu(DIEM)) {
						Intent i = new Intent(MainGameActivity.this,
								Luudiem.class);
						i.putExtra("diem", DIEM);
						MainGameActivity.this.startActivity(i);
						MainGameActivity.this.finish();
					//}
					// Nếu không được lưu tên ta quay lại menu chính để người
					// chơi có thể chọn play để chơi lại từ đầu.
					//else {
						// Chuyển sang activity Gameover
						//Intent i = new Intent(MainGameActivity.this,
							//	GameOver.class);
						//MainGameActivity.this.startActivity(i);
						//MainGameActivity.this.finish();
					//}
				}
			}
		});

		return myScene;
	}

	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	// =================================================|| onKeyDown
	// ||=========================================

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Khi bấm nút back thì hiện thị menu xem có tiếp tục chơi hay thoát
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			Dialog_exit dialogexit = new Dialog_exit(MainGameActivity.this,
					MainGameActivity.this);
			dialogexit.show();
		}
		return false;
	}

	// =================================================|| nextLevel
	// ||=========================================
	private void nextLevel() {
		Intent intent_BomActivity = new Intent(MainGameActivity.this,
				MainGameActivity.class);

		if (LEVEL + 1 < ControlerStatic.TONG_SO_LEVEL)
			LEVEL++;
		dinhNghiaCacBienThayDoi();

		HEART = MyPlayer.getHeart();

		// Mỗi lần tăng level người chơi được thưởng thêm 3 lượt chơi
		intent_BomActivity.putExtra("HEART", HEART + 3);
		intent_BomActivity.putExtra("DIEM", DIEM);
		intent_BomActivity.putExtra("LEVEL", LEVEL);
		intent_BomActivity.putExtra("MAX_SO_ENEMY", MAX_SO_ENEMY);
		intent_BomActivity.putExtra("SO_ENEMY_CAN_TIEU_DIET",
				SO_ENEMY_CAN_TIEU_DIET);
		intent_BomActivity.putExtra("TEN_MAPS", TEN_MAPS + ".tmx");

		MainGameActivity.this.startActivity(intent_BomActivity);
		MainGameActivity.this.finish();
	}

	// =================================================||
	// dinhNghiaCacBienThayDoi ||=========================================
	// Định nghĩa các giá trị thay đổi theo level
	public void dinhNghiaCacBienThayDoi() {
		switch (LEVEL) {
		case 1:
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 5;
			TEN_MAPS = "maps_" + LEVEL;
			break;
		case 2:
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 10;
			TEN_MAPS = "maps_" + LEVEL;
			break;
		case 3:
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 15;
			TEN_MAPS = "maps_" + LEVEL;
			break;
		case 4:
			MAX_SO_ENEMY = 10;
			SO_ENEMY_CAN_TIEU_DIET = 20;
			TEN_MAPS = "maps_" + LEVEL;
			break;
		case 5:
			MAX_SO_ENEMY = 10;
			SO_ENEMY_CAN_TIEU_DIET = 25;
			TEN_MAPS = "maps_" + LEVEL;
			break;
		}
	}

	// =================================================|| vaCham
	// ||=========================================
	public boolean vaCham(AnimatedSprite a, AnimatedSprite b) {
		// A.setPosition(a.getX(), a.getY());
		B.setPosition(b.getX() + 8, b.getY() + 8);
		if (a.collidesWith(B))
			return true;
		return false;
	}

	// =================================================|| checkIsVisiable
	// ||=========================================

}

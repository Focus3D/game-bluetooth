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
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.R.integer;
import android.os.SystemClock;

import BattleTank.com.Bullets.Bullet;
import BattleTank.com.ClassStatic.ScreenStatic;
import BattleTank.com.Fog.Fog;
import BattleTank.com.Maps.Maps;
import BattleTank.com.Player.Player;
import BattleTank.com.Player.Status_Player;

public class MainGameActivity extends BaseGameActivity implements
		IOnSceneTouchListener {

	private Scene myScene;
	private Camera MyCamera;

	private int i = 0;

	//Khai báo biến Fog- màn sương
	private Fog myFog;
	
	// Khai báo biến player
	private Player MyPlayer;

	// Khai báo biến bullet
	private int max_Bullet = 100;

	private ArrayList<Bullet> bulletsArrayList = new ArrayList<Bullet>();
	private boolean bullet_boolean = false;

	//
	// private ArrayList<Bullets> ArrayBullets = new ArrayList<Bullets>();

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

	public Engine onLoadEngine() {

		myFog = new Fog();
		
		MyPlayer = new Player(max_Bullet);
		

		MyCamera = new Camera(0, 0, ScreenStatic.CAMERA_WIDTH,
				ScreenStatic.CAMERA_HEIGHT);
		Engine engine = new Engine(new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						ScreenStatic.CAMERA_WIDTH, ScreenStatic.CAMERA_HEIGHT),
				this.MyCamera));

		return engine;
	}

	public void onLoadResources() {
		
		myFog.onLoadResources(MainGameActivity.this.mEngine, MainGameActivity.this);
		
		
		MyPlayer.onLoadResources(MainGameActivity.this.mEngine,
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
				// for (Bullet myBullet : MyPlayer.MyBullet) {
				// myBullet.setStatusBullet(MyPlayer.getStatus_Player());
				// myBullet.StatusMove();
				// System.out.println("MyPlayer.MyBullet = "
				// + MyPlayer.MyBullet);
				// System.out.println("myBullet.getStatusBullet() = "
				// + myBullet.getStatusBullet());
				// myBullet.moveXY(
				// MyPlayer.player.getX() + MyPlayer.player.getWidth()
				// / 2, MyPlayer.player.getY()
				// + MyPlayer.player.getHeight() / 2);
				//
				// bulletsArrayList.add(myBullet);
				//
				// break;
				//
				// }
				if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					bullet_boolean = true;
					return bullet_boolean;
				}
				return false;

			}
		};

		// setAlpha để chỉnh độ rõ hay mờ của hình ảnh
		this.shoot_Sprite.setAlpha(0.5f);
		myScene.attachChild(shoot_Sprite);
		myScene.registerTouchArea(shoot_Sprite);
		
// FOG - MÀN SƯƠNG	
		myFog.onLoadScene(myScene);
		
		
		
		
		
		
		

		myScene.registerUpdateHandler(new IUpdateHandler() {

			public void reset() {
				// TODO Auto-generated method stub

			}

			public void onUpdate(float pSecondsElapsed) {
				try {
					Thread.sleep(20);

					while (bullet_boolean && i < MyPlayer.MyBullet.length) {

						
							MyPlayer.MyBullet[i].setStatusBullet(MyPlayer
									.getStatus_Player());
							MyPlayer.MyBullet[i].StatusMove();
							System.out.println("i = " + i);
							MyPlayer.MyBullet[i].moveXY(
									MyPlayer.player.getX()
											,
									MyPlayer.player.getY()
											);
							i++;
							bullet_boolean = false;

					}
					for (Bullet mybullet : MyPlayer.MyBullet) {
						mybullet.moveBullet();
					}
					if(i == 100)
						i = 0;

					// }
					//

					// if (bullet_boolean) {
					//
					// for (i = 0; i < MyPlayer.MyBullet.length; ) {
					// MyPlayer.MyBullet[i].setStatusBullet(MyPlayer
					// .getStatus_Player());
					// MyPlayer.MyBullet[i].StatusMove();
					// MyPlayer.MyBullet[i].moveXY(
					// MyPlayer.player.getX()
					// + MyPlayer.player.getWidth() / 2,
					// MyPlayer.player.getY()
					// + MyPlayer.player.getHeight() / 2);
					// break;
					//
					// }
					// i++;
					// bullet_boolean = false;
					
					// Màn sương đi theo xe tank
					
					myFog.Fog_move(MyPlayer.getPositionX() - 480 + MyPlayer.player.getWidth()/2, MyPlayer.getPositionY() - 320 + MyPlayer.player.getHeight()/2);
				} catch (Exception e) {
					// TODO: handle exception
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
}

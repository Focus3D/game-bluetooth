package com.hoan.MainGame;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
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
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
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
import org.anddev.andengine.util.Debug;

import com.hoan.Bullet.BulletSingle;
import com.hoan.Bullet.No;
import com.hoan.Bullet.Status_Bullet;
import com.hoan.Database.Database;
import com.hoan.Enemy.Enemy;
import com.hoan.Maps.Maps;
import com.hoan.Player.Fog;
import com.hoan.Player.PlayerSingle;
import com.hoan.Player.StatusPlayer;
import com.hoan.StaticDefine.ControlerStatic;
import com.hoan.StaticDefine.ScreenStatic;
import com.hoan.dialog.DialogExit;
import com.hoan.tank.TankActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainGameSingleActivity extends BaseGameActivity implements IOnSceneTouchListener, IOnMenuItemClickListener {

	private Scene myScene;
	private Camera MyCamera;
	protected MenuScene mMenuScene;
	
	/**
	 * CÃ¡c biáº¿n phá»¥
	 */
	private int i = 0;
	boolean temp = false;
	
	// á»©ng vá»›i má»—i level ta load maps theo level
	private int LEVEL = 1;
	
	// CÃ¡c biáº¿n Ã¢m thanh
    private Sound outch;
    private Sound sound_shoot;
    private Sound sound_no;
    private Sound nhac_nen;
    private Sound tank_run;
    boolean nhacnen = false;

    // Biáº¿n cÆ¡ sá»Ÿ dá»¯ liá»‡u dÃ¹ng vÃ o viá»‡c lÆ°u Ä‘iá»ƒm
	private Database db = new Database(this);
	
	
	// Icon SHOOT - Báº¯n
	private BitmapTextureAtlas shootBitmapTextureAtlas;
	private TextureRegion shootTextureRegion;
	private Sprite shoot_Sprite;
	
	// Icon SOUND 
    private BitmapTextureAtlas SoundBitmapTextureAtlas;
    private TextureRegion SoundOnTextureRegion;
    private TextureRegion SoundOffTextureRegion;
    private Sprite SoundOnSprite;
    private Sprite SoundOffSprite;
	
    //Icon PAUSE
    private BitmapTextureAtlas PauseBitmapTextureAtlas;
    private TextureRegion PauseTextureRegion;
    private Sprite PauseSprite;
	
    //Icon TIáº¾P Tá»¤C chÆ¡i
	private BitmapTextureAtlas TiepTucBitmapTextureAtlas;
	private TextureRegion TiepTucTextureRegion;
	private Sprite TiepTucSprite;

	//Icon HEART cá»§a ngÆ°á»�i chÆ¡i
	private BitmapTextureAtlas HeartBitmapTextureAtlas;
	private TextureRegion HeartTextureRegion;
	private Sprite HeartSprite;
	
	// CÃ¡c sprite phá»¥
	private BitmapTextureAtlas ABBitmapTextureAtlas;
	private TextureRegion ATextureRegion;
	private TextureRegion BTextureRegion;
	private Sprite A, B;

	// Biáº¿n xá»­ lÃ½ menu khi báº¥m PAUSE game
	protected static final int CONTINUE = 0;
	protected static final int NEWGAME = CONTINUE + 1;
	protected static final int MAINMENU = NEWGAME + 1;
	protected static final int MENU_QUIT = MAINMENU + 1;

	// Khai bÃ¡o biáº¿n Fog- mÃ n sÆ°Æ¡ng
	private Fog myFog;
	
	// Khai bÃ¡o biáº¿n No
	private No[] myNo;
	
	/** 
	 * CÃ¡c biáº¿n cá»§a ngÆ°á»�i chÆ¡i
	 */
	// Khai bÃ¡o biáº¿n player
	private PlayerSingle MyPlayer;
	
	// MÃ¡u cá»§a ngÆ°á»�i chÆ¡i, máº·c Ä‘á»‹nh ban Ä‘áº§u lÃ  3 máº¡ng
	private int HEART = 3;	
	
	// Text hiá»‡n thá»‹ sá»‘ máº¡ng cá»§a ngÆ°á»�i chÆ¡i, máº·c Ä‘á»‹nh ban Ä‘áº§u lÃ  3 máº¡ng
	private ChangeableText TextHeart;
	
	// Ä�iá»ƒm cá»§a ngÆ°á»�i chÆ¡i.
	private int DIEM = 0;
	private int diem1 = 0;
	
	// Hiá»ƒn thá»‹ Ä‘iá»ƒm cá»§a ngÆ°á»�i chÆ¡i
	private ChangeableText TextDiem;
	
	// Font hiá»‡n thá»‹ sá»‘
	private Font mFont;
	private BitmapTextureAtlas mFontTexture;

	// Biáº¿n dÃ¹ng Ä‘á»ƒ kiá»ƒm tra viá»‡c cÃ³ báº¯n hay khÃ´ng
	private boolean bullet_boolean = false;

 /**
  * CÃ¡c biáº¿n cá»§a enemy
  */
	
	// Khai bÃ¡o biáº¿n enemy
	private Enemy MyEnemy;
	
	// Sá»‘ quÃ¢n Ä‘á»‹ch xuáº¥t hiá»‡n trÃªn mÃ n hÃ¬nh
	private int MAX_SO_ENEMY = 5;
	
	// Sá»‘ quÃ¢n Ä‘á»‹ch mÃ  ngÆ°á»�i chÆ¡i pháº£i tiÃªu diá»‡t
	private int SO_ENEMY_CAN_TIEU_DIET = 10;
	
	// Khi tiÃªu diá»‡t Ä‘á»§ sá»‘ quÃ¡i thÃ¬ sáº½ Ä‘Æ°á»£c tÄƒng level
	private int DEM_SO_ENEMY_BI_TIEU_DIET = 0;
	
	
	
	/**
	 * CÃ¡c biáº¿n xá»­ lÃ½ cho vÃ²ng láº·p cá»§a game
	 */
	private boolean OVERGAME = false;
	private boolean WIN = false;
	private boolean NEXT_LEVEL = false;

	/**
	 * MAPS 
	 */
	private TMXTiledMap mTmxTiledMap;
	private TMXLayer vatcanTmxLayer;
	private String TEN_MAPS = "map01.tmx";

	/**
	 * Khai bÃ¡o pháº§n Ä‘iá»�u khiá»ƒn nhÃ¢n váº­t chÃ­nh
	 */
	private DigitalOnScreenControl mDigitalOnScreenControl;
	private int status_digitalOnScreenControl = -1;
	private BitmapTextureAtlas mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;


	// =======================================|| onLoadEngine ||================================
	public Engine onLoadEngine() {

		// Biáº¿n lÃ m viá»‡c cho LEVEL, thay Ä‘á»•i cÃ¡c giÃ¡ trá»‹ khi LEVEL thay Ä‘á»•i
		Bundle b = getIntent().getExtras();
		if (b != null) {
			LEVEL = b.getInt("LEVEL");
			MAX_SO_ENEMY = b.getInt("MAX_SO_ENEMY");
			SO_ENEMY_CAN_TIEU_DIET = b.getInt("SO_ENEMY_CAN_TIEU_DIET");
			TEN_MAPS = b.getString("TEN_MAPS");
			DIEM = b.getInt("DIEM");
			HEART = b.getInt("HEART");
		} else {
			// Náº¿u khÃ´ng cÃ³ giÃ¡ trá»‹ Ä‘Æ°á»£c truyá»�n theo thÃ¬ ta coi nhÆ° level lÃ  1
			LEVEL = 1;
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 10;
			TEN_MAPS = "map01.tmx";
		}
		DEM_SO_ENEMY_BI_TIEU_DIET = 0;

		// Khá»Ÿi táº¡o biáº¿n mÃ n sÆ°Æ¡ng
		myFog = new Fog();
		
		// Khá»Ÿi táº¡o biáº¿n NO
		myNo = new No[5];

		// Khá»Ÿi táº¡o biáº¿n Player
		MyPlayer = new PlayerSingle();
		MyPlayer.setHeart(HEART);

		// Khá»Ÿi táº¡o biáº¿n Enemy
		MyEnemy = new Enemy(MAX_SO_ENEMY);

		MyCamera = new Camera(0, 0, ScreenStatic.CAMERA_WIDTH,ScreenStatic.CAMERA_HEIGHT);
		Engine engine = new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(ScreenStatic.CAMERA_WIDTH, 
						ScreenStatic.CAMERA_HEIGHT), this.MyCamera).setNeedsSound(true).setNeedsMusic(true));
		
		return engine;
	}

	// =======================================|| HIá»†N THá»Š PHáº¦N LOADING ||========================

	public void onLoadResources() {

		myFog.onLoadResources(MainGameSingleActivity.this.mEngine, MainGameSingleActivity.this);
		for (int i = 0; i < myNo.length; i++) {
			myNo[i] = new No();
			myNo[i].onLoadResources(MainGameSingleActivity.this.mEngine, MainGameSingleActivity.this);
		}
		
		
		MyPlayer.onLoadResources(MainGameSingleActivity.this.mEngine, MainGameSingleActivity.this);
		MyEnemy.onLoadResources(MainGameSingleActivity.this.mEngine, MainGameSingleActivity.this);


		// Load hÃ¬nh áº£nh pháº§n Ä‘iá»�u khiá»ƒn
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("Images/Control/");
		mOnScreenControlTexture = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "base.png", 0, 0);
		mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "knob.png", 128, 0);
		mEngine.getTextureManager().loadTexture(mOnScreenControlTexture);

		// Load hÃ¬nh áº£nh Icon Shoot
		shootBitmapTextureAtlas = new BitmapTextureAtlas(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		shootTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shootBitmapTextureAtlas, this, "shoot.png", 0, 0);
		mEngine.getTextureManager().loadTexture(shootBitmapTextureAtlas);
		
		 //Load Pause
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("Images/Control/");
        this.PauseBitmapTextureAtlas = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.PauseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.PauseBitmapTextureAtlas, this, "pause.png", 0, 0);
        this.mEngine.getTextureManager().loadTextures(this.PauseBitmapTextureAtlas);
        
        //Load Sound
        this.SoundBitmapTextureAtlas = new BitmapTextureAtlas(64, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        this.SoundOnTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.SoundBitmapTextureAtlas, this, "sound_on.png", 0, 0);
        this.SoundOffTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.SoundBitmapTextureAtlas, this, "sound_off.png", 32, 0);
        this.mEngine.getTextureManager().loadTextures(this.SoundBitmapTextureAtlas);
        
        
		// Load Heart
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("Images/Control/");
		this.HeartBitmapTextureAtlas = new BitmapTextureAtlas(128, 128,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.HeartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.HeartBitmapTextureAtlas, this, "tank_heart.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.HeartBitmapTextureAtlas);

		// Load Tiáº¿p tá»¥c
		this.TiepTucBitmapTextureAtlas = new BitmapTextureAtlas(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.TiepTucTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.TiepTucBitmapTextureAtlas, this, "tieptuc.png", 0, 0);
		this.mEngine.getTextureManager().loadTextures(this.TiepTucBitmapTextureAtlas);

		// .......
		this.ABBitmapTextureAtlas = new BitmapTextureAtlas(32, 32,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.ATextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.ABBitmapTextureAtlas, this, "A.png", 0, 0);
		this.BTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.ABBitmapTextureAtlas, this, "B.png", 16,0);
		this.mEngine.getTextureManager().loadTextures(this.ABBitmapTextureAtlas);

		// Load font
		this.mFontTexture = new BitmapTextureAtlas(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("Fonts/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this,"CGFArchReactor.ttf", 30, true, Color.BLUE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);
		
		// Load Ã¢m thanh
		SoundFactory.setAssetBasePath("Sound/");
        try { 
                outch = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "outch.wav");
                sound_no = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "shoot.wav");
                sound_shoot = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "EXPLODE.wav");
                tank_run = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tank_run.wav");
                //nhac_nen = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "nhac_nen.mp3");
        } catch (final IOException e) {
                Debug.e(e);
        }
	}
	// =======================================|| PHáº¦N SCENE ||========================
	public Scene onLoadScene() {
		myScene = new Scene();

		// Load maps
		mTmxTiledMap = Maps.getTMXTileMap(myScene, mEngine, this, TEN_MAPS);
		ArrayList<TMXLayer> mapLayers = mTmxTiledMap.getTMXLayers();
		for (TMXLayer layer : mapLayers) {
			if (layer.getName().equals("vatcan")) {
				vatcanTmxLayer = layer;// Náº¿u lÃ  váº­t cáº£n thÃ¬ khÃ´ng cho hiá»‡n thá»‹
				System.out.println("váº­t cáº£n");
				continue;
			}
			myScene.attachChild(layer);
		}

		// Load pháº§n player vÃ o Scene
		MyPlayer.setPositionXY(64, 100);
		MyPlayer.onLoadScene(myScene);

		// load pháº§n Enemy vÃ o Scene
		MyEnemy.onLoadScene(myScene);
		MyEnemy.setTMXTiledMap(mTmxTiledMap);
		MyEnemy.setTMXLayer(vatcanTmxLayer);

		// Thá»±c hiá»‡n viá»‡c load ngáº«u nhiÃªn káº» thÃ¹ lÃªn mÃ n hÃ¬nh
		MyEnemy.reset();

		// OnLoadScene Bullet
		for (int i = 0; i < MyPlayer.MyBullet.length; i++) {
			MyPlayer.MyBullet[i].setTMXTiledMap(mTmxTiledMap);
			MyPlayer.MyBullet[i].setTMXLayer(vatcanTmxLayer);
		}

		// Load vÃ  xá»­ lÃ½ pháº§n Ä‘iá»�u khiá»ƒn
		mDigitalOnScreenControl = new DigitalOnScreenControl(0,ScreenStatic.CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight(),this.MyCamera, this.mOnScreenControlBaseTextureRegion,this.mOnScreenControlKnobTextureRegion, 0.1f,new IOnScreenControlListener() {
					float pX = 0, pY = 0;
					public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl,final float pValueX, final float pValueY) {
						if (pValueX > 0 && MyPlayer.getStatus_Player() != StatusPlayer.MOVE_RIGHT) {
							MyPlayer.setStatus_Player(StatusPlayer.MOVE_RIGHT);
							status_digitalOnScreenControl = StatusPlayer.MOVE_RIGHT;
							pX = MyPlayer.player.getWidth() + MyPlayer.getPositionX();
							pY = MyPlayer.getPositionY() + (MyPlayer.player.getHeight() / 2);
						}

						else if (pValueX < 0 && MyPlayer.getStatus_Player() != StatusPlayer.MOVE_LEFT) {
							MyPlayer.setStatus_Player(StatusPlayer.MOVE_LEFT);
							status_digitalOnScreenControl = StatusPlayer.MOVE_LEFT;
							pX = MyPlayer.getPositionX();
							pY = MyPlayer.getPositionY() + (MyPlayer.player.getHeight() / 2);
						}

						else if (pValueY > 0 && MyPlayer.getStatus_Player() != StatusPlayer.MOVE_DOWN) {
							MyPlayer.setStatus_Player(StatusPlayer.MOVE_DOWN);
							status_digitalOnScreenControl = StatusPlayer.MOVE_DOWN;
							pX = MyPlayer.getPositionX() + (MyPlayer.player.getWidth() / 2);
							pY = MyPlayer.getPositionY() + MyPlayer.player.getHeight();
						}

						else if (pValueY < 0 && MyPlayer.getStatus_Player() != StatusPlayer.MOVE_UP) {
							MyPlayer.setStatus_Player(StatusPlayer.MOVE_UP);
							status_digitalOnScreenControl = StatusPlayer.MOVE_UP;
							pX = MyPlayer.getPositionX() + (MyPlayer.player.getWidth() / 2);
							pY = MyPlayer.getPositionY();
						}

						else {
							switch (status_digitalOnScreenControl) {
							case StatusPlayer.MOVE_RIGHT:
								MyPlayer.setStatus_Player(StatusPlayer.UN_MOVE_RIGHT);
								break;
							case StatusPlayer.MOVE_LEFT:
								MyPlayer.setStatus_Player(StatusPlayer.UN_MOVE_LEFT);
								break;
							case StatusPlayer.MOVE_UP:
								MyPlayer.setStatus_Player(StatusPlayer.UN_MOVE_UP);
								break;
							case StatusPlayer.MOVE_DOWN:
								MyPlayer.setStatus_Player(StatusPlayer.UN_MOVE_DOWN);
								break;
							default:
								break;
							}
						}

						if (pValueX != 0 || pValueY != 0) {
							TMXTile mTMXTile = vatcanTmxLayer.getTMXTileAt(pX + pValueX * 7, pY + pValueY * 7);
							try {
								if (mTMXTile == null) {
								} else {
									TMXProperties<TMXTileProperty> mTMXProperties = mTMXTile.getTMXTileProperties(mTmxTiledMap);
									TMXTileProperty mTMXTileProperty = mTMXProperties.get(0);
									if (mTMXTileProperty.getName().equals("vatcan")) {
									}
								}
							} catch (Exception e) {								
								MyPlayer.moveRelativeXY(pValueX * 7,pValueY * 7);
							}
						}

					}
				});

		mDigitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mDigitalOnScreenControl.getControlBase().setAlpha(0.7f);
		mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		mDigitalOnScreenControl.getControlBase().setScale(0.8f);
		mDigitalOnScreenControl.getControlKnob().setScale(0.8f);
		mDigitalOnScreenControl.refreshControlKnobPosition();

		myScene.setChildScene(mDigitalOnScreenControl);

		// --------------------Háº¿t Pháº§n Ä�iá»�u Khiá»ƒn NhÃ¢n Váº­t---------------------

		// -------------------- Pháº§n di chuyá»ƒn cá»§a ViÃªn Ä�áº¡n Bullet---------------------
		// Load pháº§n Bullet vÃ o Scene

		// Xá»­ lÃ½ sá»± kiá»‡n khi cháº¡m vÃ o vÃ¹ng báº¯n viÃªn Ä‘áº¡n SHOOT
		this.shoot_Sprite = new Sprite(ScreenStatic.CAMERA_WIDTH
				- this.shootTextureRegion.getWidth(),ScreenStatic.CAMERA_HEIGHT - this.shootTextureRegion.getHeight(),this.shootTextureRegion) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					if(ControlerStatic.SOUND)
        				sound_shoot.play();
					shoot_Sprite.setAlpha(1.0f);
					bullet_boolean = true;
					return bullet_boolean;
				}
				shoot_Sprite.setAlpha(0.5f);
				return bullet_boolean = false;
			}
		};

		// setAlpha Ä‘á»ƒ chá»‰nh Ä‘á»™ rÃµ hay má»� cá»§a hÃ¬nh áº£nh
		this.shoot_Sprite.setAlpha(0.5f);
		myScene.attachChild(shoot_Sprite);
		myScene.registerTouchArea(shoot_Sprite);
		
		// Load PUASE lÃªn mÃ n hÃ¬nh
        this.PauseSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 100, 1, this.PauseTextureRegion)
        {        	
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
        			if(ControlerStatic.SOUND)
        				outch.play();
        			PauseSprite.setAlpha(1.0f);
        			//Hiá»‡n thá»‹ menu
	                MainGameSingleActivity.this.myScene.setChildScene(MainGameSingleActivity.this.mMenuScene, false, true, true);
        		}
        		return true;
            }
        };        
        this.PauseSprite.setAlpha(0.5f);
        myScene.attachChild(PauseSprite);
        myScene.registerTouchArea(PauseSprite);
        
        // Load SOUND-ON lÃªn mÃ n hÃ¬nh
        this.SoundOnSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 164, 1, this.SoundOnTextureRegion){
        	
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){ 
        			ControlerStatic.SOUND = !ControlerStatic.SOUND;
        			SoundOffSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 164, 1);
        			SoundOnSprite.setPosition(-100, -100);
        		}
        		return true;
            }
        };        
        this.SoundOnSprite.setAlpha(0.5f);
        myScene.attachChild(SoundOnSprite);
        myScene.registerTouchArea(SoundOnSprite);
        
     // Load SOUND-OFF lÃªn mÃ n hÃ¬nh
        this.SoundOffSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - 164, 1, this.SoundOffTextureRegion){
        	
            public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
        		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
        			ControlerStatic.SOUND = !ControlerStatic.SOUND;
        			SoundOnSprite.setPosition(ScreenStatic.CAMERA_WIDTH - 164, 1);
        			SoundOffSprite.setPosition(-100, -100);
        		}
        		return true;
            }
        };        
        this.SoundOffSprite.setAlpha(0.5f);
        myScene.attachChild(SoundOffSprite);
        myScene.registerTouchArea(SoundOffSprite);
        
        

		// Load HEART lÃªn mÃ n hÃ¬nh
		this.HeartSprite = new Sprite(ScreenStatic.CAMERA_WIDTH - this.HeartTextureRegion.getWidth(), 0,this.HeartTextureRegion);
		HeartSprite.setAlpha(0.8f);
		myScene.attachChild(HeartSprite);
		myScene.registerTouchArea(HeartSprite);
		// Hiá»‡n thá»‹ tá»‘i Ä‘a 2 kÃ½ tá»±
		TextHeart = new ChangeableText(this.HeartSprite.getX() + 23, 15,this.mFont, String.valueOf(MyPlayer.getHeart()), 2);
		TextHeart.setAlpha(0.9f);
		myScene.attachChild(TextHeart);

		// Ä�Iá»‚M Cá»¦A NGÆ¯á»œI CHÆ I - Hiá»‡n thá»‹ tá»‘i Ä‘a 10 kÃ½ tá»±
		TextDiem = new ChangeableText(0, 10, this.mFont, String.valueOf(DIEM),10);
		myScene.attachChild(TextDiem);

		// FOG - MÃ€N SÆ¯Æ NG
		myFog.onLoadScene(myScene);
		
		// NO - Load hiá»‡u á»©ng ná»• lÃªn Scene
		for (int i = 0; i < myNo.length; i++) {
			myNo[i].onLoadScene(myScene);
		}
		

		// TIáº¾P Tá»¤C CHÆ I
		this.TiepTucSprite = new Sprite(ScreenStatic.CAMERA_WIDTH / 2 - this.TiepTucTextureRegion.getWidth() / 2,ScreenStatic.CAMERA_HEIGHT / 2 - this.TiepTucTextureRegion.getHeight() / 2,this.TiepTucTextureRegion) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && TiepTucSprite.isVisible()) {
					// pHáº¦N Ã‚M THANH
					 if(ControlerStatic.SOUND)
					 outch.play();
					// Chiáº¿n tháº¯ng
					if (LEVEL == ControlerStatic.TONG_SO_LEVEL) {
						Intent i = new Intent(MainGameSingleActivity.this,ChienThang.class);
						i.putExtra("diem", DIEM);
						MainGameSingleActivity.this.startActivity(i);
						MainGameSingleActivity.this.finish();
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

		//Menu
        this.mMenuScene = this.createMenuScene();
		
		/**
		 * VÃ²ng láº·p game UPDATEHANDLER
		 */

		myScene.registerUpdateHandler(new IUpdateHandler() {
			public void reset() {}

			public void onUpdate(float pSecondsElapsed) {
				
				// Náº¿u chÆ°a káº¿t thÃºc vÃ²ng chÆ¡i thÃ¬ thá»±c hiá»‡n
				if (!OVERGAME) {
					// Náº¿u chÆ°a tháº¯ng háº¿t vÃ²ng chÆ¡i thÃ¬ thá»±c hiá»‡n
					if (!WIN) {
						// Thiáº¿t láº­p vÃ²ng láº·p, sau 20ms sáº½ thay Ä‘á»•i 1 láº§n
						try {
							Thread.sleep(20);
							for (int i = 0; i < MyEnemy.max_enemy; i++) {
								if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible())
									MyEnemy.enemy_src[i].moveRandom();
								// Náº¿u quÃ¡i váº­t nÃ o mÃ  á»Ÿ tráº¡ng thÃ¡i áº©n thÃ¬ ta cho hiá»‡n thá»‹ lÃªn.
								// Vá»›i Ä‘iá»�u kiá»‡n lÃ  sá»‘ quÃ¡i váº­t cáº§n pháº£i tiÃªu diá»‡t lá»›n hÆ¡n so vá»›i tá»•ng sá»‘ quÃ¡i váº­t cÃ³
								if (SO_ENEMY_CAN_TIEU_DIET - DEM_SO_ENEMY_BI_TIEU_DIET >= MAX_SO_ENEMY) {
									if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible())
										MyEnemy.enemy_src[i].bool_reset = true;
								}

								if ((SO_ENEMY_CAN_TIEU_DIET * 50) - diem1 >= (MAX_SO_ENEMY * 50)) {
									if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible())
										MyEnemy.enemy_src[i].bool_reset = true;
								}

								MyEnemy.enemy_src[i].reset();

								// Kiá»ƒm tra va cháº¡m giá»¯a player vá»›i Enemy
								if (vaCham(MyEnemy.enemy_src[i].enemy_src_AnimatedSprite,MyPlayer.player)) {
									
									for (int j = 0; j < myNo.length; j++) {
										if(!myNo[j].begin_no) {
											myNo[j].moveXY(MyPlayer.player.getX() - myNo[j].no_AnimatedSprite.getBaseWidth() / 4, MyPlayer.player.getY() - myNo[j].no_AnimatedSprite.getBaseHeight() / 4);
											break;
										}
									}								
									
									// Náº¿u cÃ³ va cháº¡m thÃ¬ di chuyá»ƒn player vá»� vá»‹ trÃ­ ban Ä‘áº§u
									MyPlayer.moveXY(64, 100);
									// Trá»« Ä‘i 1 máº¡ng cá»§a player
									MyPlayer.setHeart(MyPlayer.getHeart() - 1);
									// Cáº­p nháº­t vÃ  hiá»‡n thá»‹ sá»‘ máº¡ng cÃ²n láº¡i
									TextHeart.setText(String.valueOf(MyPlayer.getHeart()));
								}
							}

							try {
								while (bullet_boolean && i < MyPlayer.MyBullet.length) {
									MyPlayer.MyBullet[i].setStatusBullet(MyPlayer.getStatus_Player());
									MyPlayer.MyBullet[i].StatusMove();
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_UP || MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.UN_MOVE_UP)
										MyPlayer.MyBullet[i].moveXY(MyPlayer.player.getX(),MyPlayer.player.getY() - MyPlayer.player.getHeight()/ 2);
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_DOWN || MyPlayer.MyBullet[i] .getStatusBullet() == Status_Bullet.UN_MOVE_DOWN)
										MyPlayer.MyBullet[i].moveXY(MyPlayer.player.getX(),MyPlayer.player.getY() + MyPlayer.player.getHeight());
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_LEFT || MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.UN_MOVE_LEFT)
										MyPlayer.MyBullet[i].moveXY(MyPlayer.player.getX() - MyPlayer.player.getWidth() / 2,MyPlayer.player.getY() + MyPlayer.player.getHeight() / 4);
									if (MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.MOVE_RIGHT || MyPlayer.MyBullet[i].getStatusBullet() == Status_Bullet.UN_MOVE_RIGHT)
										MyPlayer.MyBullet[i].moveXY(MyPlayer.player.getX() + MyPlayer.player.getWidth() / 2,MyPlayer.player.getY() + MyPlayer.player.getHeight() / 4);
									i++;
									bullet_boolean = false;
								}

								for (BulletSingle mybullet : MyPlayer.MyBullet) {
									mybullet.moveBullet();
								}

								if (i == MyPlayer.MyBullet.length)
									i = 0;

								// Khi viÃªn Ä‘áº¡n Ä‘ang trong tráº¡ng di chuyá»ƒn thÃ¬ kiá»ƒm tra xem cÃ³ enemy nÃ o va cháº¡m vá»›i viÃªn Ä‘áº¡n khÃ´ng. 
								// Náº¿u Enemy va cháº¡m vá»›i viÃªn Ä‘áº¡n thÃ¬ Enemy Ä‘Ã³ sáº½ bá»‹ biáº¿n máº¥t
								for (BulletSingle mybullet : MyPlayer.MyBullet) {
									for (int i = 0; i < MyEnemy.max_enemy; i++) {
										if (vaCham(mybullet.bullet_Sprite,MyEnemy.enemy_src[i].enemy_src_AnimatedSprite)) {
											if(ControlerStatic.SOUND)
												sound_no.play();
											if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible()) {
												// Má»—i 1 quÃ¢n Ä‘á»‹ch bá»‹ cháº¿t sáº½ cá»™ng thÃªm 50 Ä‘iá»ƒm vÃ o sá»‘ Ä‘iá»ƒm
												if (MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible()) {
													DIEM = DIEM + 50;
													diem1 = diem1 + 50;
													DEM_SO_ENEMY_BI_TIEU_DIET++;

													if (SO_ENEMY_CAN_TIEU_DIET == DEM_SO_ENEMY_BI_TIEU_DIET) {
														// Ä�Ã£ tiÃªu diá»‡t xong quÃ¡i váº­t Hiá»‡n thÃ´ng bÃ¡o vÆ°á»£t qua level vÃ  chuyá»ƒn sang level tiáº¿p theo
														System.out.println("ThÃ nh cÃ´ng. Ä�Ã£ tiÃªu diá»‡t xong.");
														WIN = true;
													}

													if (SO_ENEMY_CAN_TIEU_DIET - DEM_SO_ENEMY_BI_TIEU_DIET >= MAX_SO_ENEMY) {
														if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible())
															MyEnemy.enemy_src[i].bool_reset = true;
													}

													if ((SO_ENEMY_CAN_TIEU_DIET * 50) - diem1 >= (MAX_SO_ENEMY * 50)) {
														if (!MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.isVisible())
															MyEnemy.enemy_src[i].bool_reset = true;
													}
												}
											}
											
											// Hiá»‡u á»©ng ná»•
											for (int j = 0; j < myNo.length; j++) {
												if(!myNo[j].begin_no) {
													myNo[j].moveXY(MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.getX() - myNo[j].no_AnimatedSprite.getBaseWidth() / 4, MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.getY() - myNo[j].no_AnimatedSprite.getBaseHeight() / 4);
													break;
												}
											}
											
											
											// Enemy bá»‹ áº©n Ä‘i vÃ  di chuyá»ƒn vá»� vá»‹ trÃ­ -10, -10
											MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.setVisible(false);
											MyEnemy.enemy_src[i].enemy_src_AnimatedSprite.setPosition(-10, -10);
											
											// Hiá»‡n thá»‹ Ä‘iá»ƒm
											// Náº¿u nhiá»�u hÆ¡n 10 kÃ½ tá»± thÃ¬ hiá»‡n thá»‹
											TextDiem.setText(String.valueOf(DIEM), true);
											mybullet.bullet_Sprite.setVisible(false);
											mybullet.bullet_Sprite.setPosition(-100, -100);
										}
										for (int j = 0; j < myNo.length; j++) {
											myNo[j].delay_no();
										}
										
									}
								}

							} catch (Exception e) {
								// TODO: handle exception
							}

							// MÃ n sÆ°Æ¡ng di theo xe tank
							myFog.Fog_move(MyPlayer.getPositionX() - 480 + MyPlayer.player.getWidth() / 2,MyPlayer.getPositionY() - 320 + MyPlayer.player.getHeight() / 2);
							if (MyPlayer.getHeart() <= 0) {
								// Game over
								System.out.println("GameOver");
								// OVERGAME
								OVERGAME = true;
							}

						} catch (Exception e) {
							// TODO: handle exception
						}
						// =============KHI NGÆ¯á»œI CHÆ I DÃ€NH CHIáº¾N THáº®NG===============

					} else {
						// VÆ°á»£t qua level
						if (!NEXT_LEVEL) {
							TiepTucSprite.setVisible(true);
							float pX = TiepTucSprite.getX() + 1;
							if (pX > 180)
								pX = ScreenStatic.CAMERA_WIDTH / 2 - TiepTucSprite.getWidth() / 2;
							TiepTucSprite.setPosition(pX, TiepTucSprite.getY());
						}
					}
				}
				// OVERGAME
				else {
					// Kiá»ƒm tra xem sá»‘ Ä‘iá»ƒm cá»§a ngÆ°á»�i chÆ¡i cÃ³ cao hÆ¡n sá»‘ Ä‘iá»ƒm cá»§a nhá»¯ng ngÆ°á»�i chÆ¡i khÃ¡c khÃ´ng
					// Náº¿u cao hÆ¡n ta chuyá»ƒn qua pháº§n lÆ°u tÃªn ngÆ°á»�i chÆ¡i.
					if (db.kt_luu(DIEM)) {
						Intent i = new Intent(MainGameSingleActivity.this,Luudiem.class);
						i.putExtra("diem", DIEM);
						MainGameSingleActivity.this.startActivity(i);
						MainGameSingleActivity.this.finish();
					}
					// Náº¿u khÃ´ng Ä‘Æ°á»£c lÆ°u tÃªn ta quay láº¡i menu chÃ­nh Ä‘á»ƒ ngÆ°á»�i
					// chÆ¡i cÃ³ thá»ƒ chá»�n play Ä‘á»ƒ chÆ¡i láº¡i tá»« Ä‘áº§u.
					else {
						// Chuyá»ƒn sang activity Gameover
						Intent i = new Intent(MainGameSingleActivity.this,GameOver.class);
						MainGameSingleActivity.this.startActivity(i);
						MainGameSingleActivity.this.finish();
					}
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

	//=================================================|| onMenuItemClicked ||=========================================
	
    public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem, final float pMenuItemLocalX, final float pMenuItemLocalY) {
            switch(pMenuItem.getID()) {
                    case CONTINUE:
	                    	if(ControlerStatic.SOUND)
	                    		outch.play();
                    		MainGameSingleActivity.this.myScene.back();                    	
                            this.mMenuScene.reset();
                            this.myScene.setChildScene(mDigitalOnScreenControl);
                            return true;
                    case NEWGAME:
	                    	if(ControlerStatic.SOUND)
	                    		outch.play();
	                    	Intent i = new Intent(MainGameSingleActivity.this, MainGameSingleActivity.class);
	                    	MainGameSingleActivity.this.startActivity(i);
	                    	MainGameSingleActivity.this.finish();
	                        return true;
                    case MAINMENU:
	                    	if(ControlerStatic.SOUND)
	                    		outch.play();
//	                    	Intent intent_BomActivity = new Intent(MainGameSingleActivity.this, TankActivity.class);
//	                    	MainGameSingleActivity.this.startActivity(intent_BomActivity);
//	                    	MainGameSingleActivity.this.finish();
	                    	MainGameSingleActivity.this.finish();
	                        return true;
                    case MENU_QUIT:
	                    	if(ControlerStatic.SOUND)
	                    		outch.play();
	                    	DialogExit dialogexit = new DialogExit(MainGameSingleActivity.this, MainGameSingleActivity.this);
	            			dialogexit.show();
                            return true;
                    default:
                            return false;
            }
    }
	
	
	
	// =================================================|| onKeyDown ||=========================================
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Khi báº¥m nÃºt back thÃ¬ hiá»‡n thá»‹ menu xem cÃ³ tiáº¿p tá»¥c chÆ¡i hay thoÃ¡t
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			DialogExit dialogexit = new DialogExit(MainGameSingleActivity.this,MainGameSingleActivity.this);
			dialogexit.show();
		}
		return false;
	}

	//=======================================|| MENU ||============================================
	protected MenuScene createMenuScene() {
        final MenuScene menuScene = new MenuScene(MainGameSingleActivity.this.MyCamera);
        final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(CONTINUE, MainGameSingleActivity.this.mFont, "CONTINUOU"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(resetMenuItem);
   
        final IMenuItem NewGameMenuItem = new ColorMenuItemDecorator(new TextMenuItem(NEWGAME, MainGameSingleActivity.this.mFont, "RESTART"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        NewGameMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(NewGameMenuItem);
        
        final IMenuItem MainMenuMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MAINMENU, MainGameSingleActivity.this.mFont, "MAIN MENU"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        MainMenuMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(MainMenuMenuItem);

        final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, this.mFont, "EXIT"), 1.0f,0.0f,0.0f, 255.0f,255.0f,255.0f);
        quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);

        menuScene.buildAnimations();
        menuScene.setBackgroundEnabled(false);
        menuScene.setOnMenuItemClickListener(this);
        return menuScene;
	}
	
	
	// =================================================|| nextLevel ||=========================================
	private void nextLevel() {
		Intent intent_BattleTankActivity = new Intent(MainGameSingleActivity.this,MainGameSingleActivity.class);
		if (LEVEL + 1 <= ControlerStatic.TONG_SO_LEVEL)
			LEVEL++;
		dinhNghiaCacBienThayDoi();
		HEART = MyPlayer.getHeart();

		// Má»—i láº§n tÄƒng level ngÆ°á»�i chÆ¡i Ä‘Æ°á»£c thÆ°á»Ÿng thÃªm 3 lÆ°á»£t chÆ¡i
		intent_BattleTankActivity.putExtra("HEART", HEART + 1);
		intent_BattleTankActivity.putExtra("DIEM", DIEM);
		intent_BattleTankActivity.putExtra("LEVEL", LEVEL);
		intent_BattleTankActivity.putExtra("MAX_SO_ENEMY", MAX_SO_ENEMY);
		intent_BattleTankActivity.putExtra("SO_ENEMY_CAN_TIEU_DIET",SO_ENEMY_CAN_TIEU_DIET);
		intent_BattleTankActivity.putExtra("TEN_MAPS", TEN_MAPS + ".tmx");

		MainGameSingleActivity.this.startActivity(intent_BattleTankActivity);
		MainGameSingleActivity.this.finish();
	}

	// =================================================|| dinhNghiaCacBienThayDoi ||=========================================
	// Ä�á»‹nh nghÄ©a cÃ¡c giÃ¡ trá»‹ thay Ä‘á»•i theo level
	public void dinhNghiaCacBienThayDoi() {
		System.out.println("level" + LEVEL);
		switch (LEVEL) {
		case 1:
			MAX_SO_ENEMY = 5;
			SO_ENEMY_CAN_TIEU_DIET = 10;
			TEN_MAPS = "map0" + LEVEL;
			break;
		case 2:
			MAX_SO_ENEMY = 7;
			SO_ENEMY_CAN_TIEU_DIET = 15;
			TEN_MAPS = "map0" + LEVEL;
			break;
		case 3:
			MAX_SO_ENEMY = 9;
			SO_ENEMY_CAN_TIEU_DIET = 18;
			TEN_MAPS = "map0" + LEVEL;
			break;
		case 4:
			MAX_SO_ENEMY = 11;
			SO_ENEMY_CAN_TIEU_DIET = 20;
			TEN_MAPS = "map0" + LEVEL;
			break;

		case 5:
			MAX_SO_ENEMY = 15;
			SO_ENEMY_CAN_TIEU_DIET = 25;
			TEN_MAPS = "map0" + LEVEL;
			break;
		}
	}

	// =================================================|| vaCham ||=========================================
	public boolean vaCham(AnimatedSprite a, AnimatedSprite b) {
		// A.setPosition(a.getX(), a.getY());
		B.setPosition(b.getX() + 8, b.getY() + 8);
		if (a.collidesWith(B))
			return true;
		return false;
	}


	// =================================================|| checkIsVisiable ||=========================================

}


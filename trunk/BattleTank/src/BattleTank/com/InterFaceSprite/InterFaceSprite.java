package BattleTank.com.InterFaceSprite;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;

import android.content.Context;

public interface InterFaceSprite {
	public void onLoadResources(Engine mEngine, Context mContext);
	public void onLoadScene(Scene mScene);

}

package com.hoan.InterfaceSprite;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;

import android.content.Context;

public interface InterfaceSprite {
	public void onLoadResources(Engine mEngine, Context mContext);
	public void onLoadScene(Scene mScene);

}

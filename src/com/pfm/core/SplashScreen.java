package com.pfm.core;

import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.opengl.texture.source.ITextureSource;
import org.anddev.andengine.ui.activity.BaseSplashActivity;

import com.pfm.menu.Intro;

import android.app.Activity;

public class SplashScreen extends BaseSplashActivity{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SPLASH_DURATION = 3;
	private static final float SPLASH_SCALE_FROM = 0.7f;
	
	@Override
	protected ScreenOrientation getScreenOrientation() {
		// TODO check real orientation ?
		return ScreenOrientation.LANDSCAPE;
	}

	@Override
	protected ITextureSource onGetSplashTextureSource() {
		return new AssetTextureSource(this, "gfx/logoherzyo.png");
	}

	@Override
	protected float getSplashDuration() {
		return SPLASH_DURATION;
	}

	@Override
    protected float getSplashScaleFrom() {
            return SPLASH_SCALE_FROM;
    }
	
	// Setting a background for splashScreen
    @Override
    public Scene onLoadScene() {
            final Scene splashScene = super.onLoadScene();
            splashScene.setBackground(new ColorBackground(1.f, 1.f, 1.f));
            return splashScene;
           
    }
    
	@Override
	protected Class<? extends Activity> getFollowUpActivity() {
		return Intro.class;
	}
}
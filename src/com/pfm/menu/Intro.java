package com.pfm.menu;


import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.pfm.core.BaseExample;
import com.pfm.cuento.R;
import com.pfm.libro.Page1;

public class Intro extends BaseExample {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private Texture mAutoParallaxBackgroundTexture;

	private TextureRegion mParallaxLayerBack;
	private TextureRegion mParallaxLayerMid;
	private Music mMusic;
	private Texture mBotonesTex;
	private TextureRegion mBtnDTexR;
	private Sprite nextSprite;
	private int centerRX;
	private int centerRY;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.setNeedsMusic(true);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		this.mAutoParallaxBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
		this.mParallaxLayerBack = TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/portadaintro.png", 0, 0);
		this.mParallaxLayerMid = TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/nubeintro.png", 0, 512);
		
		this.mBotonesTex = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtnDTexR = TextureRegionFactory.createFromAsset(this.mBotonesTex, this, "gfx/btnini.png", 0, 0);
		
		this.mEngine.getTextureManager().loadTextures(this.mAutoParallaxBackgroundTexture, this.mBotonesTex);
		
		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/musicaIntro.mid");
			this.mMusic.setLooping(true);

		} catch (IOException ioe) {
			// No se ha podido cargar el fichero. Realizar las operaciones
			// oportunas.
//			Debug.e(ioe);
		}
		
		settings = getSharedPreferences("puntos", MODE_PRIVATE);
		editor = settings.edit();
		editor.putString("puntosReutilizar",String.valueOf(0));
		editor.putString("puntosReciclaje",String.valueOf(0));
		editor.putString("puntosReducir",String.valueOf(0));
		editor.commit();
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, this.mParallaxLayerMid)));
		scene.setBackground(autoParallaxBackground);

		centerRX = (CAMERA_WIDTH - this.mBtnDTexR.getWidth()) / 2;
		centerRY = (CAMERA_HEIGHT - this.mBtnDTexR.getHeight()) / 2;

		nextSprite = new Sprite(centerRX, centerRY+50, this.mBtnDTexR){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					inicio();
				}
				return true;
			}
		};
		scene.getLastChild().attachChild(nextSprite);
		scene.registerTouchArea(nextSprite);
		mMusic.play();
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void inicio() {
		mMusic.stop();
		Log.v("#PFM#", "Entramos en  inicio()");
        Intent i = new Intent(getBaseContext(), Page1.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}

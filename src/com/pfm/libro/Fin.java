package com.pfm.libro;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.EntityBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pfm.cuento.R;
import com.pfm.menu.Indice;

public class Fin extends BaseGameActivity implements IAccelerometerListener {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;

	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private Texture backTex;
	private TextureRegion backTexR;
	private Texture tripleRTex;
	private TextureRegion tripleRTexR;
	private Scene scene;
	private int centerRX;
	private int centerRY;
	private Sprite spTripleR;
	private boolean playingSound = false;
	private MediaPlayer mMediaPlayer;
	private Texture mPlokFontTextureXXL;
	private Font mPlokFontXXL;
	private Text lblPuntos;
	private SharedPreferences settings;
	private Text lblPuntosReutilizar;
	private Text lblPuntosReciclar;
	private Text lblPuntosReducir;


	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT,new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),this.mCamera); 
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		engineOptions.setNeedsMusic(true);
	    engineOptions.setNeedsSound(true);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		Log.v("#PFM#", "Entramos en  onLoadResources()");
		
		// TEXTURAS
		backTex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondolibrofin.png", 0, 0);
		
		tripleRTex = new Texture(256, 256,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		tripleRTexR = TextureRegionFactory.createFromAsset(this.tripleRTex, this,"gfx/tripleRm.png", 0, 0);
		
		FontFactory.setAssetBasePath("font/");
		this.mPlokFontTextureXXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontXXL = FontFactory.createFromAsset(this.mPlokFontTextureXXL,this, "Plok.ttf", 30, true, Color.BLACK);

		
		this.mEngine.getTextureManager().loadTextures(backTex, tripleRTex, mPlokFontTextureXXL);
		this.mEngine.getFontManager().loadFonts(this.mPlokFontXXL);

		this.enableAccelerometerSensor(this);
		
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(this, R.raw.danisalvado);
		mMediaPlayer.start();
	}

	@Override
	public Scene onLoadScene() {
		Log.v("#PFM#", "Entramos en  onLoadScene()");

		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(2);
		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));
		
		centerRX = (CAMERA_WIDTH - this.tripleRTexR.getWidth()) / 2;
		centerRY = (CAMERA_HEIGHT - this.tripleRTexR.getHeight())* 2 / 3;

		spTripleR = new Sprite(centerRX, centerRY+80, tripleRTexR);
		spTripleR.registerEntityModifier(new LoopEntityModifier(new RotationModifier(6, 0, 360)));
		
		settings = getSharedPreferences("puntos", MODE_PRIVATE);
        String puntosReutilizar = settings.getString("puntosReutilizar", "Empty");
        String puntosReducir = settings.getString("puntosReducir", "Empty");
        String puntosReciclar = settings.getString("puntosReciclaje", "Empty");
		
		lblPuntos = new Text(centerRX-100, centerRY-150, this.mPlokFontXXL,"Puntos:");
		lblPuntosReutilizar = new Text(centerRX-100, centerRY-100, this.mPlokFontXXL,"Reutilizar: "+puntosReutilizar);
		lblPuntosReciclar = new Text(centerRX-100, centerRY-50, this.mPlokFontXXL,"Reciclar: "+puntosReciclar);
		lblPuntosReducir = new Text(centerRX-100, centerRY, this.mPlokFontXXL,"Reducir: "+puntosReducir);
		
		scene.getLastChild().attachChild(spTripleR);
		scene.getLastChild().attachChild(lblPuntos);
		scene.getLastChild().attachChild(lblPuntosReutilizar);
		scene.getLastChild().attachChild(lblPuntosReciclar);
		scene.getLastChild().attachChild(lblPuntosReducir);

		scene.registerTouchArea(spTripleR);		
		
		return scene;
	}
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	public void dormir(int miliSeg) {
		try {
			Thread.sleep(miliSeg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.indice:
			Intent settings = new Intent(getBaseContext(),Indice.class);
			startActivity(settings);
			mMediaPlayer.stop();
			finish();
			return false;
		case R.id.acercade:
			  Toast.makeText(Fin.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
			  return true;
		case R.id.salir:
			mMediaPlayer.stop();
			this.finish(); 
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//Liberamos memoria al finalizar
	 @Override
	 protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
        	mMediaPlayer.release();
        	mMediaPlayer = null;
        }
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

	public void setPlayingSound(boolean playingSound) {
		this.playingSound = playingSound;
	}

	public boolean isPlayingSound() {
		return playingSound;
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub
		
	}
}

package com.pfm.libro;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.EntityBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.TickerText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.pfm.cuento.R;
import com.pfm.juegos.JuegoReciclaje;
import com.pfm.menu.Indice;


public class Page6  extends BaseGameActivity implements IAccelerometerListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;
	private Camera mCamera;
	private Texture backTex;
	private TextureRegion backTexR;
	private Texture mLetraMeievalTextureM;
	private Font mLetraMeievalFontM;
	private Scene scene;
	private PhysicsWorld mPhysicsWorld;
	private Texture mBotonesTex;
	private TextureRegion mBtnDTexR;
	private int centerRX;
	private int centerRY;
	private boolean playingSound;
	private Sprite nextSprite;
	private Sprite previousSprite;
	private int tiempoCrono = 28;
	private MediaPlayer mMediaPlayer;


	// ===========================================================
	// Fields
	// ===========================================================



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
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondolibro6.png", 0, 0);
		
		FontFactory.setAssetBasePath("font/");
		this.mLetraMeievalTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.mLetraMeievalFontM = FontFactory.createFromAsset(this.mLetraMeievalTextureM,this, "medievalsharp.ttf", 20, true, Color.DKGRAY);
		
		this.mBotonesTex = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtnDTexR = TextureRegionFactory.createFromAsset(this.mBotonesTex, this, "gfx/btndere.png", 0, 0);
	    
		this.mEngine.getTextureManager().loadTextures(backTex, mLetraMeievalTextureM, mBotonesTex);		
		this.mEngine.getFontManager().loadFonts(this.mLetraMeievalFontM);
		
		this.enableAccelerometerSensor(this);
		
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer = MediaPlayer.create(this, R.raw.audiolibro6);
		// Carga de música y sonido.
//		SoundFactory.setAssetBasePath("mfx/");
//		try {
////			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/street.mid");
////			this.mMusic.setLooping(true);
//
//
//			this.mLecturaSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "audiolibro5.ogg");
//
//		} catch (IOException ioe) {
//			// No se ha podido cargar el fichero. Realizar las operaciones
//			// oportunas.
////			Debug.e(ioe);
//		}
	}

	@Override
	public Scene onLoadScene() {
		Log.v("#PFM#", "Entramos en  onLoadScene()");

		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(2);
		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));
		
		setCenterRX((CAMERA_WIDTH - this.mBtnDTexR.getWidth()) / 2);
		setCenterRY((CAMERA_HEIGHT - this.mBtnDTexR.getHeight())* 2 / 3);

		nextSprite = new Sprite(CAMERA_WIDTH - 20 - this.mBtnDTexR.getWidth(), CAMERA_HEIGHT - 20 - this.mBtnDTexR.getHeight(), this.mBtnDTexR){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					next();
				}
				return true;
			}
		};
//		nextSprite.setVisible(false);
		
		previousSprite = new Sprite(20, CAMERA_HEIGHT - 20 - this.mBtnDTexR.getHeight(), this.mBtnDTexR.clone()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					previous();
				}
				return true;
			};
		};
		previousSprite.getTextureRegion().setFlippedHorizontal(true);
//		previousSprite.setVisible(false);

		final Text text = new TickerText(10, 10, this.mLetraMeievalFontM, "El poder de la siguiente R es el reciclaje.\n"+
																	     "Para encontrarla debes pasar una prueba\n"+
																	     "pero antes tienes que saber las claves\n"+
																	     "de como utilizar su poder.\n"+

																	     "Reciclaje: Quiere decir utilizar los residuos\n"+
																	     "para elaborar nuevos productos.\n"+
																	     "Es necesario separar el material.\n"+																     
																	     "- En el contenedor amarillo debemos depositar \n"+
																	     "el plástico y los bricks\n"+
																	     "- En el verde redondeado demos depositar\n"+
																	     " el vidrio.\n"+
																	     "- En el azul debemos depositar el papel\n"+
																	     "y el cartón\n"+
																	     "- En el verde cuadrado debemos depositar\n"+
																	     "el resto de basura.\n"+
																	     "Con esto conseguiremos el ahorro de energía,\n"+
																	     "agua, materias primas, tiempo, dinero y\n"+
																	     "esfuerzo además del menor impacto en los\n"+ 
																	     "ecosistemas y sus recursos naturales"
																		  , HorizontalAlign.LEFT, 15);
		text.registerEntityModifier(
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new AlphaModifier(5, 0.0f, 1.0f),
								new ScaleModifier(10, 0.5f, 1.0f)
						)
				)
		);
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		scene.getLastChild().attachChild(text);

		scene.getLastChild().attachChild(nextSprite);
		scene.getLastChild().attachChild(previousSprite);

		scene.registerTouchArea(nextSprite);
		scene.registerTouchArea(previousSprite);		
				
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
//
//		final Shape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
//		final Shape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
//		final Shape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
//		final Shape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
//
//		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
//		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
//		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
//		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
//		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
//
//		scene.getFirstChild().attachChild(ground);
//		scene.getFirstChild().attachChild(roof);
//		scene.getFirstChild().attachChild(left);
//		scene.getFirstChild().attachChild(right);
//
//		scene.registerUpdateHandler(this.mPhysicsWorld);
				
		scene.registerUpdateHandler(new TimerHandler(1.0f, true,new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
//				Log.v("#PFM#", "Entramos en  onLoadScene() - registerUpdateHandler()");
				actualizarEstados();			
			}
		}));
		
		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerometerData.getX(), pAccelerometerData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}


	// ===========================================================
	// Methods
	// ===========================================================
	
	public void next() {
		Log.v("#PFM#", "Entramos en  next()");
//		mLecturaSound.stop();
		mMediaPlayer.stop();
		this.finish();
        startActivity(new Intent(Page6.this, JuegoReciclaje.class));
        // Supply a custom animation.  This one will just fade the new
        // activity on top.  Note that we need to also supply an animation
        // (here just doing nothing for the same amount of time) for the
        // old activity to prevent it from going away too soon.
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
	}

	public void previous() {
		Log.v("#PFM#", "Entramos en  previous()");
//		mLecturaSound.stop();
		mMediaPlayer.stop();
		this.finish();
		Intent i = new Intent(getBaseContext(), Page5.class);
	    startActivity(i);
        // Supply a custom animation.  This one will just fade the new
        // activity on top.  Note that we need to also supply an animation
        // (here just doing nothing for the same amount of time) for the
        // old activity to prevent it from going away too soon.
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        this.finish();
	}
	
	//Método que comprueba si el juego esta cargando, si esta en cuenta atras o si es el finla del juego actualizando los objetos
	public void actualizarEstados(){
		int seg = tiempoCrono -((int) Page6.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (!isPlayingSound()){
//			mLecturaSound.play();
			mMediaPlayer.start();

			setPlayingSound(true);
		}else{
			Log.v("#PFM#", "seg:"+seg);

//			if(seg<0&&!nextSprite.isVisible()){
//				nextSprite.setVisible(true);
//				previousSprite.setVisible(true);
//			}
		}
	}
	
	//Método que lanza un thread con los milisegundos indicados haciendo esperar el flujo de la aplicación
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
			mMediaPlayer.stop();
			Intent settings = new Intent(getBaseContext(),Indice.class);
			startActivity(settings);
			finish();
			return false;
		case R.id.acercade:
			  Toast.makeText(Page6.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
			  return true;
		case R.id.salir:
			mMediaPlayer.stop();
			this.finish(); 
		default:
			return super.onOptionsItemSelected(item);
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public void setPlayingSound(boolean playingSound) {
		this.playingSound = playingSound;
	}

	public boolean isPlayingSound() {
		return playingSound;
	}

	public void setCenterRX(int centerRX) {
		this.centerRX = centerRX;
	}

	public int getCenterRX() {
		return centerRX;
	}

	public void setCenterRY(int centerRY) {
		this.centerRY = centerRY;
	}

	public int getCenterRY() {
		return centerRY;
	}
}
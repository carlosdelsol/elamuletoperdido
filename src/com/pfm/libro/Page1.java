package com.pfm.libro;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.EntityBackground;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.text.TickerText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.pfm.cuento.R;
import com.pfm.menu.Indice;

public class Page1  extends BaseGameActivity implements IAccelerometerListener {
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
	private Texture mLetraMeievalTextureM;
	private Font mLetraMeievalFontM;
	private Scene scene;
	private PhysicsWorld mPhysicsWorld;
	private Sound mLecturaSound;
	private Texture mBotonesTex;
	private TextureRegion mBtnDTexR;
	private int centerRX;
	private int centerRY;
	private boolean playingSound;
	private Sprite nextSprite;
	private int tiempoCrono = 10;
	private Texture diosesRTex;
	private TextureRegion diosesRTexR;
	private Sprite spDiosesR;
	
	private TiledTextureRegion mBoxFaceTextureRegion;
	protected TextureRegion mCircleFaceTextureRegion;
	private Texture tripleRTex;
	private TextureRegion tripleRTexR;
	private Sprite spTripleR;
	private Texture mTextureBox;
	private Texture mTextureCircle;
	private Sound maza1Sound;
	private Sound maza2Sound;

	// ===========================================================
	// Constructors
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
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondolibro.png", 0, 0);
		
		FontFactory.setAssetBasePath("font/");
		this.mLetraMeievalTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.mLetraMeievalFontM = FontFactory.createFromAsset(this.mLetraMeievalTextureM,this, "medievalsharp.ttf", 30, true, Color.DKGRAY);
		
		this.mBotonesTex = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtnDTexR = TextureRegionFactory.createFromAsset(this.mBotonesTex, this, "gfx/btndere.png", 0, 0);
		
		diosesRTex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		diosesRTexR = TextureRegionFactory.createFromAsset(this.diosesRTex, this,"gfx/dioses.png", 0, 0);
		
		tripleRTex = new Texture(256, 256,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		tripleRTexR = TextureRegionFactory.createFromAsset(this.tripleRTex, this,"gfx/tripleRm.png", 0, 0);
		
		this.mTextureBox = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTextureBox, this, "gfx/face_box_tiled.png", 0, 0, 2, 1); // 64x32
		this.mTextureCircle = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCircleFaceTextureRegion = TextureRegionFactory.createFromAsset(this.mTextureCircle, this, "gfx/maza.png", 0, 0); // 64x32
		
		this.mEngine.getTextureManager().loadTextures(mTextureBox, mTextureCircle, backTex, mLetraMeievalTextureM, mBotonesTex, tripleRTex, diosesRTex);		
		this.mEngine.getFontManager().loadFonts(this.mLetraMeievalFontM);
		
		this.enableAccelerometerSensor(this);
		
		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
//			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/street.mid");
//			this.mMusic.setLooping(true);
			
			this.maza1Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "maza.ogg");
			maza1Sound.setVolume(0.2f);
			this.maza2Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "metalico.ogg");
			maza2Sound.setVolume(0.2f);
			this.mLecturaSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "audiolibro1.ogg");

		} catch (IOException ioe) {
			// No se ha podido cargar el fichero. Realizar las operaciones
			// oportunas.
//			Debug.e(ioe);
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.v("#PFM#", "Entramos en  onLoadScene()");

		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(2);
		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));
		
		centerRX = (CAMERA_WIDTH - this.mBtnDTexR.getWidth()) / 2;
		centerRY = (CAMERA_HEIGHT - this.mBtnDTexR.getHeight())* 2 / 3;

		Log.v("#PFM#", "CAMERA_HEIGHT:"+CAMERA_HEIGHT);
		Log.v("#PFM#", "CAMERA_WIDTH:"+CAMERA_WIDTH);
		Log.v("#PFM#", "diosesRTexR.getWidth():"+diosesRTexR.getWidth());
		Log.v("#PFM#", "diosesRTexR.getHeight():"+diosesRTexR.getHeight());

		spDiosesR = new Sprite(-30,CAMERA_HEIGHT - 80 - diosesRTexR.getHeight(),diosesRTexR);
		
		spDiosesR.setScaleY(1.8f);
//		spDiosesR.setScaleX(0.9f);
		
		centerRX = (CAMERA_WIDTH - this.tripleRTexR.getWidth()) / 2;
		centerRY = (CAMERA_HEIGHT - this.tripleRTexR.getHeight()) * 2 / 3;
		
		spTripleR = new Sprite(centerRX, centerRY+150, tripleRTexR);
		spTripleR.setScale(0.8f);
		spTripleR.registerEntityModifier(new LoopEntityModifier(new RotationModifier(6, 0, 360)));		
		
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
	
		final Text text = new TickerText(10, 10, this.mLetraMeievalFontM, "En el comienzo de los tiempos\n" +
																		  "un pequeño planeta llamado\n" +
																		  "Tierra nacio del fruto de la\n" +
																		  "madre naturaleza y el dios\n" +
																		  "del sol.\n\n" +
																		  "Para mantener su seguridad,\n" +
																		  "forjaron un amuleto que \n" +
																		  "entregaron a los primeros\n" +
																		  "hombres.", HorizontalAlign.LEFT, 15);
		text.registerEntityModifier(
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new AlphaModifier(5, 0.0f, 1.0f),
								new ScaleModifier(10, 0.5f, 1.0f)
						)
				)
		);
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		scene.getLastChild().attachChild(spDiosesR);
//		scene.registerTouchArea(spDiosesR);
		
		scene.getLastChild().attachChild(spTripleR);
		
		scene.getLastChild().attachChild(text);

		scene.getLastChild().attachChild(nextSprite);

		scene.registerTouchArea(nextSprite);	
				
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		final Shape right = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape roof = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape ground = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);
//		final Shape right = new Rectangle(CAMERA_WIDTH - spDiosesR.getX()/2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createCircleBody(this.mPhysicsWorld, spTripleR, BodyType.StaticBody, wallFixtureDef);

		scene.getFirstChild().attachChild(ground);
		scene.getFirstChild().attachChild(roof);
		scene.getFirstChild().attachChild(left);
		scene.getFirstChild().attachChild(right);

		scene.registerUpdateHandler(this.mPhysicsWorld);
		
		scene.registerUpdateHandler(new TimerHandler(1.0f, true,new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
//				Log.v("#PFM#", "Entramos en  onLoadScene() - registerUpdateHandler()");
				actualizarEstados();			
			}
		}));
		
		//Añadimos las físicas
		this.initJoints(scene);

		return scene;
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerometerData.getX()* -1, pAccelerometerData.getY());
//		this.mPhysicsWorld.setGravity(new Vector2(pAccelerometerData.getX() * -1 , pAccelerometerData.getY()));
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}


	// ===========================================================
	// Methods
	// ===========================================================
	
	private void initJoints(final Scene pScene) {
		final int centerY = CAMERA_HEIGHT / 2;

		final int spriteWidth = this.mBoxFaceTextureRegion.getTileWidth();
		final int spriteHeight = this.mBoxFaceTextureRegion.getTileHeight();
		
		final int spriteWidthBrazo = this.mCircleFaceTextureRegion.getWidth();
		final int spriteHeightBrazo = this.mCircleFaceTextureRegion.getHeight();

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);

		//MAZA 1
		//****************************************************************
//		final float anchorFaceX = centerX - spriteWidth;
		final float anchorFaceX = CAMERA_WIDTH - spriteWidth * 0.5f +100;
		final float anchorFaceY = centerY - spriteHeight + 200;
//			final float anchorFaceY = CAMERA_HEIGHT/3 - spriteHeight * 0.5f + 250;

		final float anchorFaceXBrazo = CAMERA_WIDTH - spriteWidthBrazo * 0.5f - 20;
		final float anchorFaceYBrazo = centerY + spriteHeightBrazo;

		final AnimatedSprite anchorFace = new AnimatedSprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion);
		final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace, BodyType.StaticBody, objectFixtureDef);

		final Sprite movingFace = new Sprite(anchorFaceXBrazo - 120, anchorFaceYBrazo - 120, this.mCircleFaceTextureRegion);
		final Body movingBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, movingFace, BodyType.DynamicBody, objectFixtureDef);

		anchorFace.setVisible(false);

		pScene.getLastChild().attachChild(anchorFace);
		pScene.getLastChild().attachChild(movingFace);

		final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace, movingBody, true, true));

		final DistanceJointDef distanceJointDef = new DistanceJointDef();
		distanceJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter(), movingBody.getWorldCenter());
		this.mPhysicsWorld.createJoint(distanceJointDef);

		//MAZA 2
		//****************************************************************
		final float anchorFaceX2 = -spriteWidth*0.5f - 100;
		final float anchorFaceY2 =  centerY - spriteHeight + 200;
		
		final float anchorFaceXBrazo2 = -spriteWidthBrazo * 0.5f + 20;
		final float anchorFaceYBrazo2 = centerY + spriteHeightBrazo;

		final AnimatedSprite anchorFace2 = new AnimatedSprite(anchorFaceX2, anchorFaceY2, this.mBoxFaceTextureRegion);
//		anchorFace2.setRotaation(180.0f);
		final Body anchorBody2 = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace2, BodyType.StaticBody, objectFixtureDef);

		final Sprite movingFace2 = new Sprite(anchorFaceXBrazo2 + 120, anchorFaceYBrazo2 - 120, this.mCircleFaceTextureRegion);
		final Body movingBody2 = PhysicsFactory.createCircleBody(this.mPhysicsWorld, movingFace2, BodyType.DynamicBody, objectFixtureDef);

		anchorFace2.setVisible(false);

		pScene.getLastChild().attachChild(anchorFace2);
		pScene.getLastChild().attachChild(movingFace2);

		final Line connectionLine2 = new Line(anchorFaceX2 + spriteWidth / 2, anchorFaceY2 + spriteHeight / 2, anchorFaceX2 + spriteWidth / 2, anchorFaceY2 + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine2);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace2, movingBody2, true, true));


		final DistanceJointDef distanceJointDef2 = new DistanceJointDef();
		distanceJointDef2.initialize(anchorBody2, movingBody2, anchorBody2.getWorldCenter(), movingBody2.getWorldCenter());
		this.mPhysicsWorld.createJoint(distanceJointDef2);

		mPhysicsWorld.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				if(contact.getFixtureB().getBody() == movingBody)
					maza1Sound.play();
				
				if(contact.getFixtureB().getBody() == movingBody2)
					maza2Sound.play();
			}

			@Override
			public void endContact(Contact contact) {
				maza1Sound.stop();			
			}

			});
	}
	
	public void next() {
		Log.v("#PFM#", "Entramos en  next()");
		mLecturaSound.stop();
		maza1Sound.stop();
		maza2Sound.stop();
        startActivity(new Intent(Page1.this, Page2.class));
        // Supply a custom animation.  This one will just fade the new
        // activity on top.  Note that we need to also supply an animation
        // (here just doing nothing for the same amount of time) for the
        // old activity to prevent it from going away too soon.
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
	}

	//Método que comprueba si el juego esta cargando, si esta en cuenta atras o si es el finla del juego actualizando los objetos
	public void actualizarEstados(){
		int seg = tiempoCrono -((int) Page1.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (!isPlayingSound()){
			mLecturaSound.play();
			setPlayingSound(true);
		}else{
			Log.v("#PFM#", "seg:"+seg);

//			if(seg<0&&!nextSprite.isVisible()){
//				nextSprite.setVisible(true);
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
			Intent settings = new Intent(getBaseContext(),Indice.class);
			startActivity(settings);
			finish();
			return false;
		case R.id.acercade:
			  Toast.makeText(Page1.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
			  return true;
		case R.id.salir:
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
	// Getter & Setter
	// ===========================================================
	public void setPlayingSound(boolean playingSound) {
		this.playingSound = playingSound;
	}

	public boolean isPlayingSound() {
		return playingSound;
	}
}
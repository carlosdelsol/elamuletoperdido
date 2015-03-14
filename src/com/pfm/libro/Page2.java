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
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
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
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.pfm.cuento.R;
import com.pfm.menu.Indice;


public class Page2  extends BaseGameActivity implements IAccelerometerListener {
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
	private Sound mLecturaSound;
	private Texture mBotonesTex;
	private TextureRegion mBtnDTexR;
	private int centerRX;
	private int centerRY;
	private boolean playingSound;
	private Sprite nextSprite;
	private Sprite previousSprite;
	private int tiempoCrono = 12;
	private Texture mTierraTex;
	private TextureRegion mTierraTexR;
	private Texture mTextureBox;
	private TiledTextureRegion mBoxFaceTextureRegion;
	private Vector2 p1;
	private Vector2 p2;
	private Vector2 d;
	private Body m_bodies[] = new Body[4];
	private Joint m_joints[] = new Joint[8];



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
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondolibro.png", 0, 0);
		
		FontFactory.setAssetBasePath("font/");
		this.mLetraMeievalTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);	
		this.mLetraMeievalFontM = FontFactory.createFromAsset(this.mLetraMeievalTextureM,this, "medievalsharp.ttf", 30, true, Color.DKGRAY);
		
		this.mBotonesTex = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtnDTexR = TextureRegionFactory.createFromAsset(this.mBotonesTex, this, "gfx/btndere.png", 0, 0);
	    
		this.mTierraTex = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTierraTexR = TextureRegionFactory.createFromAsset(this.mTierraTex, this, "gfx/tierrasonriendo.png", 0, 0);
		
		this.mTextureBox = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBoxFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTextureBox, this, "gfx/face_box_tiled.png", 0, 0, 2, 1); // 64x32	    
		
		this.mEngine.getTextureManager().loadTextures(backTex, mLetraMeievalTextureM, mBotonesTex, mTierraTex, mTextureBox);		
		this.mEngine.getFontManager().loadFonts(this.mLetraMeievalFontM);
		
		this.enableAccelerometerSensor(this);
		
		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
//			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/street.mid");
//			this.mMusic.setLooping(true);


			this.mLecturaSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "audiolibro2.ogg");

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

		nextSprite = new Sprite(CAMERA_WIDTH - 20 - this.mBtnDTexR.getWidth(), CAMERA_HEIGHT - 20 - this.mBtnDTexR.getHeight(), this.mBtnDTexR){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					next();
				}
				return true;
			}
		};
		
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

		final Text text = new TickerText(10, 10, this.mLetraMeievalFontM, "El amuleto conocido como “LA\n" +
																		  "TRIPLE R” fue guardado con \n" +
																		  "cuidado a lo largo de cada una\n" +
																		  "de las civilizaciones por unos \n" +
																		  "pocos privilegiados que lo\n" +
																		  "mantenían en secreto. \n\n" +
																		  "Tierra se sentía alegre, feliz \n" +
																		  "y fuerte gracias a su amuleto \n" +
																		  "y los hombres que la cuidaban.", HorizontalAlign.LEFT, 15);

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

		final Shape right = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2);
		final Shape left = new Rectangle(0, 0, CAMERA_WIDTH, 2);
		final Shape roof = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
		final Shape ground = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

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
	
	private void initJoints(final Scene pScene) {
         m_joints = new Joint[8];

		final int centerY = CAMERA_HEIGHT / 2;

		final int spriteWidth = this.mBoxFaceTextureRegion.getTileWidth();
		final int spriteHeight = this.mBoxFaceTextureRegion.getTileHeight();

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1.0f, 0.2f, 0.5f);

		centerRX = (CAMERA_WIDTH - this.mBtnDTexR.getWidth()) / 2;
		centerRY = (CAMERA_HEIGHT - this.mBtnDTexR.getHeight())* 2 / 3;
		
		final Sprite tierraFace = new Sprite(centerRX, centerRY + 50, this.mTierraTexR){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				return true;
				}
			};
		
		final Body tierra = PhysicsFactory.createCircleBody(this.mPhysicsWorld, tierraFace, BodyType.DynamicBody, objectFixtureDef);
//		movingFace.setScale(4.0f);
		pScene.getLastChild().attachChild(tierraFace);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(tierraFace, tierra, true, true));

		//****************************************************************
		//CONECTOR 1
		//****************************************************************
		final float anchorFaceX = CAMERA_WIDTH -100;
		final float anchorFaceY = centerY - spriteHeight + 200;
		
		final AnimatedSprite b1Face = new AnimatedSprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion);
		final Body b1 = PhysicsFactory.createBoxBody(this.mPhysicsWorld, b1Face, BodyType.StaticBody, objectFixtureDef);
//		b1Face.setVisible(false);
		pScene.getLastChild().attachChild(b1Face);
		
//        bd.position.set(-5.0f, 5.0f);
//        m_bodies[0] = m_world.createBody(bd);
//        m_bodies[0].createFixture(shape, 5.0f);

		final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine);		
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(b1Face, b1, true, true){
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				final Vector2 movingBodyWorldCenter = tierra.getWorldCenter();
				connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			}
		});
		//****************************************************************

		//****************************************************************
		//CONECTOR 2
		//****************************************************************
		final float anchorFaceX2 = 100;
		final float anchorFaceY2 =  centerY - spriteHeight + 200;

		final AnimatedSprite b2Face = new AnimatedSprite(anchorFaceX2, anchorFaceY2, this.mBoxFaceTextureRegion);
		final Body b2 = PhysicsFactory.createBoxBody(this.mPhysicsWorld, b2Face, BodyType.StaticBody, objectFixtureDef);
//		b2Face.setVisible(false);
		pScene.getLastChild().attachChild(b2Face);

		final Line connectionLine2 = new Line(anchorFaceX2 + spriteWidth / 2, anchorFaceY2 + spriteHeight / 2, anchorFaceX2 + spriteWidth / 2, anchorFaceY2 + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine2);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(b2Face, b2, true, true){
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				final Vector2 movingBodyWorldCenter2 = tierra.getWorldCenter();
				connectionLine2.setPosition(connectionLine2.getX1(), connectionLine2.getY1(), movingBodyWorldCenter2.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter2.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			}
		});
		//****************************************************************
		
		//****************************************************************
		//CONECTOR 3
		//****************************************************************
		final float anchorFaceX3 = CAMERA_WIDTH -100;
		final float anchorFaceY3 =  centerY - spriteHeight + 300;

		final AnimatedSprite b3Face = new AnimatedSprite(anchorFaceX3, anchorFaceY3, this.mBoxFaceTextureRegion);
		final Body b3 = PhysicsFactory.createBoxBody(this.mPhysicsWorld, b3Face, BodyType.DynamicBody, objectFixtureDef);
//		b3Face.setVisible(false);
		pScene.getLastChild().attachChild(b3Face);

		final Line connectionLine3 = new Line(anchorFaceX3 + spriteWidth / 2, anchorFaceY3 + spriteHeight / 2, anchorFaceX3 + spriteWidth / 2, anchorFaceY3 + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine3);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(b3Face, b3, true, true){
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				final Vector2 movingBodyWorldCenter3 = tierra.getWorldCenter();
				connectionLine3.setPosition(connectionLine3.getX1(), connectionLine3.getY1(), movingBodyWorldCenter3.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter3.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			}
		});
		//****************************************************************
		
		//****************************************************************
		//CONECTOR 4
		//****************************************************************
		final float anchorFaceX4 = 100;
		final float anchorFaceY4 =  centerY - spriteHeight + 300;

		final AnimatedSprite b4Face = new AnimatedSprite(anchorFaceX4, anchorFaceY4, this.mBoxFaceTextureRegion);
		final Body b4 = PhysicsFactory.createBoxBody(this.mPhysicsWorld, b4Face, BodyType.DynamicBody, objectFixtureDef);
//		b4Face.setVisible(false);
		pScene.getLastChild().attachChild(b4Face);

		final Line connectionLine4 = new Line(anchorFaceX4 + spriteWidth / 2, anchorFaceY4 + spriteHeight / 2, anchorFaceX4 + spriteWidth / 2, anchorFaceY4 + spriteHeight / 2);
		pScene.getFirstChild().attachChild(connectionLine4);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(b4Face, b4, true, true){
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				final Vector2 movingBodyWorldCenter4 = tierra.getWorldCenter();
				connectionLine4.setPosition(connectionLine4.getX1(), connectionLine4.getY1(), movingBodyWorldCenter4.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter4.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			}
		});
		//****************************************************************


		//****************************************************************
		//DISTANCE JOINT
		//****************************************************************
		final DistanceJointDef distanceJointDef = new DistanceJointDef();
		//The mass-spring-damper frequency in Hertz.
		distanceJointDef.frequencyHz = 4.0f;
		//AMORTIGUACIÓN: The damping ratio. 0 = no damping, 1 = critical damping.
		distanceJointDef.dampingRatio = 0.5f; 
        // La longitud de equilibrio entre los puntos de anclaje.
		distanceJointDef.length = 1.0f;
		
		distanceJointDef.bodyA = tierra;
		distanceJointDef.bodyB = b1;
		distanceJointDef.localAnchorA.set(-10.0f, 0.0f);
		distanceJointDef.localAnchorB.set(-0.5f, -0.5f);
//		distanceJointDef.localAnchorA.set(tierra.getWorldCenter());
//		distanceJointDef.localAnchorB.set(b1.getWorldCenter().add(-2.0f, -2.0f));
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[0] = mPhysicsWorld.createJoint(distanceJointDef);
        
		distanceJointDef.bodyA = tierra;
		distanceJointDef.bodyB = b2;
		distanceJointDef.localAnchorA.set(10.0f, 0.0f);
		distanceJointDef.localAnchorB.set(0.5f, -0.5f);
//		distanceJointDef.localAnchorA.set(tierra.getWorldCenter());
//		distanceJointDef.localAnchorB.set(b2.getWorldCenter().add(-2.0f, -2.0f));
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[1] = mPhysicsWorld.createJoint(distanceJointDef);
        
		distanceJointDef.bodyA = tierra;
		distanceJointDef.bodyB = b3;
		distanceJointDef.localAnchorA.set(10.0f, 20.0f);
		distanceJointDef.localAnchorB.set(0.5f, 0.5f);
//		distanceJointDef.localAnchorA.set(tierra.getWorldCenter());
//		distanceJointDef.localAnchorB.set(b3.getWorldCenter().add(-2.0f, -2.0f));
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[2] = mPhysicsWorld.createJoint(distanceJointDef);
        
		distanceJointDef.bodyA = tierra;
		distanceJointDef.bodyB = b4;
		distanceJointDef.localAnchorA.set(-10.0f, 20.0f);
		distanceJointDef.localAnchorB.set(-0.5f, 0.5f);
//		distanceJointDef.localAnchorA.set(tierra.getWorldCenter());
//		distanceJointDef.localAnchorB.set(b4.getWorldCenter().add(-2.0f, -2.0f));
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[3] = mPhysicsWorld.createJoint(distanceJointDef);
        
        //=================================================     
        distanceJointDef.bodyA = b1;
		distanceJointDef.bodyB = b2;
		distanceJointDef.localAnchorA.set(0.5f, 0.0f);
		distanceJointDef.localAnchorB.set(-0.5f, 0.0f);
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[4] = mPhysicsWorld.createJoint(distanceJointDef);
        
        distanceJointDef.bodyA = b2;
		distanceJointDef.bodyB = b3;
		distanceJointDef.localAnchorA.set(0.0f, 0.5f);
		distanceJointDef.localAnchorB.set(0.0f, -0.5f);
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[5] = mPhysicsWorld.createJoint(distanceJointDef);
        
        distanceJointDef.bodyA = b3;
		distanceJointDef.bodyB = b4;
		distanceJointDef.localAnchorA.set(-0.5f, 0.0f);
		distanceJointDef.localAnchorB.set(0.5f, 0.0f);
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[6] = mPhysicsWorld.createJoint(distanceJointDef);
        
        distanceJointDef.bodyA = b4;
		distanceJointDef.bodyB = b1;
		distanceJointDef.localAnchorA.set(0.0f, -0.5f);
		distanceJointDef.localAnchorB.set(0.0f, 0.5f);
        p1 = distanceJointDef.bodyA.getWorldPoint(distanceJointDef.localAnchorA);
        p2 = distanceJointDef.bodyB.getWorldPoint(distanceJointDef.localAnchorB);
        d = p2.sub(p1);
        distanceJointDef.length = d.len();
        m_joints[7] = mPhysicsWorld.createJoint(distanceJointDef);
        
		//****************************************************************

//		mPhysicsWorld.setContactListener(new ContactListener(){
//
//			@Override
//			public void beginContact(Contact contact) {
//				if(contact.getFixtureB().getBody() == movingBody)
//					maza1Sound.play();
//				
//				if(contact.getFixtureB().getBody() == movingBody2)
//					maza2Sound.play();
//			}
//
//			@Override
//			public void endContact(Contact contact) {
//				maza1Sound.stop();			
//			}
//
//			});
	}
	

	protected void saltoTierra() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadComplete() {

	}

	@Override
	public void onAccelerometerChanged(final AccelerometerData pAccelerometerData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerometerData.getX()* -1, pAccelerometerData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}


	// ===========================================================
	// Methods
	// ===========================================================
	
	public void next() {
		Log.v("#PFM#", "Entramos en  next()");
		mLecturaSound.stop();
        startActivity(new Intent(Page2.this, Page3.class));
        // Supply a custom animation.  This one will just fade the new
        // activity on top.  Note that we need to also supply an animation
        // (here just doing nothing for the same amount of time) for the
        // old activity to prevent it from going away too soon.
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
	}

	public void previous() {
		Log.v("#PFM#", "Entramos en  previous()");
		mLecturaSound.stop();

        // Request the next activity transition (here starting a new one).
		Intent i = new Intent(getBaseContext(), Page1.class);
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
		int seg = tiempoCrono -((int) Page2.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (!isPlayingSound()){
			mLecturaSound.play();
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
			Intent settings = new Intent(getBaseContext(),Indice.class);
			startActivity(settings);
			finish();
			return false;
		case R.id.acercade:
			  Toast.makeText(Page2.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
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
	// Inner and Anonymous Classes
	// ===========================================================
	public void setPlayingSound(boolean playingSound) {
		this.playingSound = playingSound;
	}

	public boolean isPlayingSound() {
		return playingSound;
	}
}
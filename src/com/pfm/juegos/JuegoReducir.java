package com.pfm.juegos;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.FadeInModifier;
import org.anddev.andengine.entity.modifier.FadeOutModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AccelerationInitializer;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.EntityBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pfm.core.ObjReducir;
import com.pfm.core.ObjReducir.tipoObj;
import com.pfm.cuento.R;
import com.pfm.menu.Indice;

//public class JuegoReducir extends Activity {
public class JuegoReducir extends BaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private String[] imgObjReducir = { "gfx/rluz.png","gfx/rgrifo.png","gfx/rtele.png","gfx/rcalefaccion.png"};
	private tipoObj[] tipoObjReducir = { tipoObj.LUZ, tipoObj.AGUA, tipoObj.ENERGIA, tipoObj.CALEFACCION};
	
	private static final float velocidadObjReducir = 300.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Sound mGiroSound;

	private Texture[] mObjReducirTexture;	
	private TiledTextureRegion[] ObjReducirTexRegion;

	private ObjReducir[] ObjReducir;
	private ArrayList<Integer> vObjReducir;

	private Scene scene;
	private int centerObjReducirX;
	private int centerObjReducirY;
	
	private CircleOutlineParticleEmitter particleEmitter;
	private ParticleSystem particleSystem;
	private Texture mParticulaTex;
	private TextureRegion mParticulaTexRegion;
	
	private ChangeableText tresDosUno;
	private ChangeableText puntos;
	private ChangeableText strCuentaPuntos;
	private ChangeableText crono;
	private Text lblPuntos;
	private Text lblTiempo;
	private Texture mPlokFontTextureM;
	private Texture mPlokFontTextureL;
	private Texture mPlokFontTextureXL;

	private Font mPlokFontL;
	private Font mPlokFontXL;
	private Font mPlokFontM;
	
	private int tiempoCrono = 60;
	private boolean finDelJuego = false;
	private int numTiempo = 0;
	private float centroPapelera;
	private Music mMusic;
	private Sound mExplosionSound;
	private Sound mErrorSound;
	private Sound mPocoTiempoSound;
	private Sound mcuentaAtras1Sound;
	private Sound mcuentaAtras2Sound;
	
	private boolean juegoCargado = true;
	private boolean cuentaAtras = true;
	private int intCA = 3;
	private int puntosFinal = 0;
	private boolean finOk;
	private Texture backTex;
	private TextureRegion backTexR;
	private Texture mBotonesTex;
	private TextureRegion mBtnDTexR;
	private Sprite nextSprite;
	private Texture cuadrante1Tex;
	private TextureRegion cuadrante1TexR;
	private Texture cuadrante2Tex;
	private TextureRegion cuadrante2TexR;
	private Sprite cuad1;
	private Sprite cuad2;
	private Texture mMedievalFontTextureM;
	private Texture mMedievalFontTextureL;
	private Texture mMedievalFontTextureXL;
	private Font mMedievalFontL;
	private Font mMedievalFontM;
	private Font mMedievalFontXL;
	private ChangeableText Pregunta;
	private ChangeableText Respuesta;
	private String[] preguntas;
	private String[] respuestas1;
	private String[] respuestas2;
	private String[] respuestas3;
	private String[] respuestas4;
	private ArrayList<String[]> arrRespuestas;
	private Texture mAguaTex;
	private TextureRegion mAguaTexRegion;
	private PointParticleEmitter aguaEmitter;
	private ParticleSystem aguaSystem;
	private Sound mcerrarAguaSound;
	private Music mabrirAguaSound;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onLoadComplete() {
		Log.v("#PFM#", "Entramos en  onLoadComplete()");
		// TODO Auto-generated method stub
	}

	@Override
	public Engine onLoadEngine() {
		Log.v("#PFM#", "Entramos en  onLoadEngine()");
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE,new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),this.mCamera); 
		engineOptions.setNeedsMusic(true);
	    engineOptions.setNeedsSound(true);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		Log.v("#PFM#", "Entramos en  onLoadResources()");
		
		// TEXTURAS
		backTex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondoRayas3.png", 0, 0);
		
		cuadrante1Tex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		cuadrante1TexR = TextureRegionFactory.createFromAsset(this.cuadrante1Tex, this,"gfx/cuadrante1.png", 0, 0);
		
		cuadrante2Tex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		cuadrante2TexR = TextureRegionFactory.createFromAsset(this.cuadrante2Tex, this,"gfx/cuadrante2.png", 0, 0);
		
		this.mParticulaTex = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticulaTexRegion = TextureRegionFactory.createFromAsset(this.mParticulaTex, this, "gfx/particle_star.png", 0, 0);
		
		this.mAguaTex = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mAguaTexRegion = TextureRegionFactory.createFromAsset(this.mAguaTex, this, "gfx/particle_water.png", 0, 0);

		this.mBotonesTex = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBtnDTexR = TextureRegionFactory.createFromAsset(this.mBotonesTex, this, "gfx/btndere.png", 0, 0);
		
		// FUENTES
		FontFactory.setAssetBasePath("font/");
		this.mPlokFontTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mPlokFontM = FontFactory.createFromAsset(this.mPlokFontTextureM,this, "Plok.ttf", 20, true, Color.WHITE);
		this.mPlokFontL = FontFactory.createFromAsset(this.mPlokFontTextureL,this, "Plok.ttf", 30, true, Color.BLUE);
		this.mPlokFontXL = FontFactory.createFromAsset(this.mPlokFontTextureXL,this, "Plok.ttf", 40, true, Color.YELLOW);
		
		this.mMedievalFontTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMedievalFontTextureL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMedievalFontTextureXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mMedievalFontM = FontFactory.createFromAsset(this.mMedievalFontTextureM,this, "medievalsharp.ttf", 22, true, Color.WHITE);
		this.mMedievalFontL = FontFactory.createFromAsset(this.mMedievalFontTextureL,this, "medievalsharp.ttf", 38, true, Color.BLUE);
		this.mMedievalFontXL = FontFactory.createFromAsset(this.mMedievalFontTextureXL,this, "medievalsharp.ttf", 60, true, Color.YELLOW);
		
		this.mObjReducirTexture = new Texture[imgObjReducir.length];
		for (int i = 0; i < mObjReducirTexture.length; i++) {
			mObjReducirTexture[i] = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}
		ObjReducirTexRegion = new TiledTextureRegion[imgObjReducir.length];
		for (int i = 0; i < ObjReducirTexRegion.length; i++) {
			ObjReducirTexRegion[i] = TextureRegionFactory.createTiledFromAsset(mObjReducirTexture[i], this, imgObjReducir[i],0, 0, 3, 1);
		}
		
		ObjReducir = new ObjReducir[imgObjReducir.length];
		vObjReducir =new ArrayList<Integer>();
	    
		//Cargamos las texturas de los objetos reducción
		for (int i = 0; i < mObjReducirTexture.length; i++) {
			this.mEngine.getTextureManager().loadTextures(mObjReducirTexture[i]);
		}
	    
		this.mEngine.getTextureManager().loadTextures(backTex, cuadrante1Tex, cuadrante2Tex, 
														this.mPlokFontTextureM, this.mPlokFontTextureL, this.mPlokFontTextureXL, 
														this.mMedievalFontTextureM, this.mMedievalFontTextureL, this.mMedievalFontTextureXL, 
														this.mAguaTex, this.mParticulaTex, this.mBotonesTex);		
		this.mEngine.getFontManager().loadFonts(this.mPlokFontM,this.mPlokFontL, this.mPlokFontXL,
												this.mMedievalFontM,this.mMedievalFontL,this.mMedievalFontXL);

		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/bopon.mid");
			this.mMusic.setLooping(true);
			this.mabrirAguaSound =  MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/abriragua.ogg");
			this.mabrirAguaSound.setLooping(true);
			
			this.mGiroSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "giro.ogg");

			this.mExplosionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "celdasbien.ogg");
			this.mErrorSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "errorceldas.ogg");
			this.mPocoTiempoSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "pocotiempo.ogg");
			this.mcuentaAtras1Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "golpegrave.ogg");
			this.mcuentaAtras2Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "retroExplosion.ogg");

			this.mcerrarAguaSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "cerraragua.ogg");
			
		} catch (IOException ioe) {
			// No se ha podido cargar el fichero. Realizar las operaciones
			// oportunas.
//				Debug.e(ioe);
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.v("#PFM#", "Entramos en  onLoadScene()");

		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(1);
		
		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));		

		centerObjReducirX = (CAMERA_WIDTH - this.ObjReducirTexRegion[0].getWidth()) / 2;
		centerObjReducirY = (CAMERA_HEIGHT - this.ObjReducirTexRegion[0].getHeight()) / 2;
		
		crearObjetos();
		
		nextSprite = new Sprite(CAMERA_WIDTH - 100 - this.mBtnDTexR.getWidth(), (CAMERA_HEIGHT - this.mBtnDTexR.getHeight())/2, this.mBtnDTexR){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					next();
				}
				return true;
			}
		};
//		nextSprite.getTextureRegion().setFlippedHorizontal(true);

		cuad1 = new Sprite(150, 180, cuadrante1TexR);
		cuad1.setScale(1.7f);
		cuad2 = new Sprite(180, 50, cuadrante2TexR);
		cuad2.setScale(1.7f);
		
		scene.setTouchAreaBindingEnabled(true);
		
		/* The actual collision-checking. */
		scene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {			
				if (!vObjReducir.isEmpty()) {
//					for (int i = 0; i < papelera.length; i++) {
//						if (papelera[i].collidesWith(ObjReducir[vObjReducir.get(0)])) {
//							papelera[i].setAlpha(1.0f);
//						} else {
//							papelera[i].setAlpha(0.8f);
//						}
//					}
				}			
			}
		});
		
		scene.registerUpdateHandler(new TimerHandler(1.0f, true,new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
//					Log.v("#PFM#", "Entramos en  onLoadScene() - registerUpdateHandler()");
				actualizarEstados();			
			}
		}));
		
		scene.getLastChild().attachChild(nextSprite);
		scene.registerTouchArea(nextSprite);
		
		scene.getLastChild().attachChild(cuad1);
		scene.getLastChild().attachChild(cuad2);
		
		return scene;
	}
	// ===========================================================
	// Methods
	// ===========================================================		
	public void next() {
		Log.v("#PFM#", "Entramos en  next()");
		//SI ES CORRECTO ELIMINA EL OBJETO REDUCCIÓN Y LO QUITA DE LA PILA SI NO LO DEJA Y DA ERROR
		comprobarRespuesta(vObjReducir.get(0), ObjReducir[vObjReducir.get(0)].getCurrentTileIndex());
	}
	
	//Método que comprueba si el juego esta cargando, si esta en cuenta atras o si es el finla del juego actualizando los objetos
	public void actualizarEstados(){
		int seg = tiempoCrono - ((int) JuegoReducir.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (isJuegoCargado()){
			mMusic.play();
			setJuegoCargado(false);
		}
		
		if (isCuentaAtras()){
			mostrarCuentaAtras();
		} else {
			if(tresDosUno.isVisible()){
				tresDosUno.setVisible(false);
			}
			if (isFinDelJuego()) {
				if(seg == getNumTiempo()-2){
					crearObjeto3D();
				}
			} else {
				crono.setText(String.valueOf(seg));
				if (seg > -1) {
					setNumTiempo(seg);
					if (seg == 9) {
						crono.setColor(1, 0, 0);
						mPocoTiempoSound.play();
					} else if (seg < 10) {
						crono.registerEntityModifier(new FadeInModifier(0.5f));
					}
				} else{
					finalizarJuego(false);
				}
			}
		}
	}
	
	//Método que crea todo los objetos que aparecerán en pantalla
	public void crearObjetos(){
		
		Resources res = getResources();
		preguntas = res.getStringArray(R.array.RED_PREG);
		respuestas1 = res.getStringArray(R.array.RED_RESP1);
		respuestas2 = res.getStringArray(R.array.RED_RESP2);
		respuestas3 = res.getStringArray(R.array.RED_RESP3);
		respuestas4 = res.getStringArray(R.array.RED_RESP4);
		
		ArrayList<IEntity> arrObjetos = new ArrayList<IEntity>();
		arrRespuestas = new ArrayList<String[]>();
		arrRespuestas.add(respuestas1);
		arrRespuestas.add(respuestas2);
		arrRespuestas.add(respuestas3);
		arrRespuestas.add(respuestas4);
				
		particleEmitter = new CircleOutlineParticleEmitter(0.0f, 0.0f, 50);
		particleSystem = new ParticleSystem(particleEmitter, 100, 200, 350, this.mParticulaTexRegion);	
		
		aguaEmitter = new PointParticleEmitter(217.0f, 250.0f);
		aguaSystem = new ParticleSystem(aguaEmitter, 60, 60, 800, this.mAguaTexRegion);	
		aguaSystem.setVisible(false);

		tresDosUno = new ChangeableText(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, this.mPlokFontL, "3", 8);
//			tresDosUno.setVisible(false);
		
		lblTiempo = new Text(CAMERA_WIDTH/3, CAMERA_HEIGHT-100, this.mPlokFontM,getString(R.string.tiempo));		
		crono = new ChangeableText(lblTiempo.getX()+140, CAMERA_HEIGHT-110,this.mPlokFontXL, "100", 3);
		
		lblPuntos = new Text(CAMERA_WIDTH/3, CAMERA_HEIGHT-50, this.mPlokFontM,getString(R.string.puntos));
		puntos = new ChangeableText(lblPuntos.getX()+140, CAMERA_HEIGHT-60, this.mPlokFontXL,String.valueOf(numPuntos),6);
		cuad2 = new Sprite(180, 50, cuadrante2TexR);

		Pregunta = new ChangeableText(40, 65, this.mMedievalFontM, preguntas[0],100);
		Respuesta = new ChangeableText(300, 250, this.mMedievalFontL, arrRespuestas.get(0)[0],100);
		Pregunta.setVisible(false);
		Respuesta.setVisible(false);

		strCuentaPuntos = new ChangeableText(puntos.getX()+120, CAMERA_HEIGHT-60, this.mPlokFontM,"", 4);
		strCuentaPuntos.setVisible(false);
		strCuentaPuntos.setColor(0.8f, 0, 0.8f);
		
		arrObjetos.add(particleSystem);
		arrObjetos.add(aguaSystem);
		arrObjetos.add(tresDosUno);
		arrObjetos.add(lblTiempo);
		arrObjetos.add(crono);
		arrObjetos.add(lblPuntos);
		arrObjetos.add(puntos);
		arrObjetos.add(strCuentaPuntos);
		arrObjetos.add(Pregunta);
		arrObjetos.add(Respuesta);

		
		for (int i=0; i<arrObjetos.size();i++){
			scene.getLastChild().attachChild(arrObjetos.get(i));
		}
		
		setAnimacionParticula();
		setAnimacionAgua();
		
		crearObjReduccion();
	}
	
	//Método donde creamos las papeleras y los objetos de la basura
	public void crearObjReduccion(){	
		for (int i = 0; i < ObjReducir.length; i++) {
			vObjReducir.add(i);			
			ObjReducir[i] = new ObjReducir((this.mObjReducirTexture[i].getWidth()+260)+(i*60),CAMERA_HEIGHT-100, this.ObjReducirTexRegion[i], i){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {

//					Log.v("#PFM#", "ACTION_UP:" + this.getIdObj()+ ", isActiva():" + isActiva());
					if (this.isActiva()) {

						switch (pSceneTouchEvent.getAction()) {
						case TouchEvent.ACTION_DOWN:
							if (!this.isMuerta() && !tresDosUno.isVisible()) {								
								nextTile();
								respuestaCara(this.getIdObj(),this.getCurrentTileIndex());
								mGiroSound.play();
								
								if(vObjReducir.get(0)==1 && this.getCurrentTileIndex()!=0){
									abrirAgua();
								}
								if(vObjReducir.get(0)==1 && this.getCurrentTileIndex()==0){
									cerrarAgua();
								}
							}
						}
					}
					
					return true;
				}
				
			};			
//			celdas[i].setScale(1.2f);{
			
			ObjReducir[i].setTipo(tipoObjReducir[i]);
			ObjReducir[i].setScale(0.5f);

			scene.getLastChild().attachChild(ObjReducir[i]);
			scene.registerTouchArea(ObjReducir[i]);
		
		}
	}
	
	public void preguntaSiguiente(int posObj) {
//		Log.v("#PFM#", "preguntaSiguiente posObj: "+posObj);
		Pregunta.registerEntityModifier(new FadeInModifier(0.5f));
		Pregunta.setText(preguntas[posObj]);
		Pregunta.registerEntityModifier(new FadeOutModifier(0.5f));
	}

	public void respuestaCara(int posObj, int posTile) {
//		Log.v("#PFM#", "respuestaCara  posObj: "+posObj+", posTile:"+posTile);
		Respuesta.registerEntityModifier(new FadeInModifier(0.5f));
		Respuesta.setText(arrRespuestas.get(posObj)[posTile]);
		Respuesta.registerEntityModifier(new FadeOutModifier(0.5f));
	}

	public void comprobarRespuesta(int posObj, int posTile){
		Log.v("#PFM#", "comprobarRadioButtons posObj: "+posObj+", posTile:"+posTile);

		switch (posObj) {
		case 0:
			if(posTile==1){
				aciertoRespuesta();
			}else{
				falloRespuesta();
			}
			break;
		case 1:
			if(posTile==0){
				aciertoRespuesta();
			}else{
				cerrarAgua();
				falloRespuesta();
			}
			break;
		case 2:
			if(posTile==2){
				aciertoRespuesta();
			}else{
				falloRespuesta();
			}
			break;
		case 3:
			if(posTile==1){
				aciertoRespuesta();
			}else{
				falloRespuesta();
			}
			break;
		default:
			break;
		}
	}
	
	public void aciertoRespuesta(){
			mExplosionSound.play();
			lanzarParticulas(ObjReducir[vObjReducir.get(0)].getX(),ObjReducir[vObjReducir.get(0)].getY());
			eliminarElementoLista();
//			ObjReducir[vObjReducir.get(0)].matar();
			actualizarPuntos(true);
			if(vObjReducir.isEmpty()){
				finalizarJuego(true);
			}else{
//				ObjReducir[vObjReducir.get(0)].revivir();
//				ObjReducir[vObjReducir.get(0)].setActiva(true);
				recolocarMiniObjReducir();
				ObjReducir[vObjReducir.get(0)].setActiva(true);

				preguntaSiguiente(vObjReducir.get(0));
				respuestaCara(vObjReducir.get(0), ObjReducir[vObjReducir.get(0)].getCurrentTileIndex());
			}
	}
	
	public void falloRespuesta(){
		ObjReducir[vObjReducir.get(0)].setScale(1.5f);
		mErrorSound.play();
		if (vObjReducir.size() > 1) {
			ponerElementoFinLista(ObjReducir[vObjReducir.get(0)].getIdObj());
			recolocarMiniObjReducir();
			preguntaSiguiente(vObjReducir.get(0));
			respuestaCara(vObjReducir.get(0), ObjReducir[vObjReducir.get(0)].getCurrentTileIndex());

		} else {
			moverObjReducirCentro();
		}
		actualizarPuntos(false);
	}
	
	//Método que actualiza la cuenta atras
	public void mostrarCuentaAtras(){
		if(intCA!=0){
			tresDosUno.setText(String.valueOf(intCA));
			mcuentaAtras1Sound.play();
			tresDosUno.registerEntityModifier(new SequenceEntityModifier(new FadeInModifier(0.5f),new ScaleModifier(0.2f, 2, 0.5f), new ScaleModifier(0.3f, 0.5f,1.2f)));
			int ica = intCA - 1;
			dormir(100);
			setIntCA(ica);
		}else{
			tresDosUno.setText("¡A jugar!");
			tresDosUno.setPosition(tresDosUno.getX()-125, tresDosUno.getY());
			mcuentaAtras2Sound.play();
			tresDosUno.registerEntityModifier(new SequenceEntityModifier(new FadeInModifier(0.5f),new ScaleModifier(0.2f, 2, 0.5f), new ScaleModifier(0.3f, 0.5f,1.5f)));
			dormir(100);
			setCuentaAtras(false);
			
			ObjReducir[vObjReducir.get(0)].setActiva(true);
			Pregunta.setVisible(true);
			Respuesta.setVisible(true);

			moverObjReducirCentro();
//				lanzarParticulas(tresDosUno.getX(),tresDosUno.getY());
		}
	}

	//Método que actualiza los puntos añadiendo si es un acierto y restando si es un error
	public void actualizarPuntos(boolean acierto) {
		int p = getNumPuntos();
		int pcp = 0;
		
		if(p >= 100){
			strCuentaPuntos.setPosition(puntos.getX()+120,strCuentaPuntos.getY());
		}
		else{
			strCuentaPuntos.setPosition(puntos.getX()+80,strCuentaPuntos.getY());
		}
		
		if (acierto) {
			pcp = 100;
			p = getNumPuntos() + pcp;
		} else {
			if (getNumPuntos() >= 20) {
				pcp = -20;
				p = getNumPuntos() + pcp;
			}
		}

		setNumPuntos(p);
		puntos.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.2f, 1, 0.8f), new ScaleModifier(0.2f, 0.8f,1)));
		if(pcp != 0){			
			strCuentaPuntos.setText(String.valueOf(pcp));
			strCuentaPuntos.setVisible(true);
			strCuentaPuntos.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.2f, 1, 0.8f), new ScaleModifier(0.2f, 0.8f,1)));
		}
		dormir(300);
		puntos.setText(String.valueOf(p));
		strCuentaPuntos.setVisible(false);
	}
	
	//Método que se encarga de lanzar partículas en la posición indicada
	public void lanzarParticulas(float posX, float posY){
		particleSystem.setParticlesSpawnEnabled(true);
		centroPapelera = (ObjReducir[0].getBaseHeight() / 2)-30;
		particleEmitter.setCenter(posX + centroPapelera, posY + centroPapelera);
		dormir(100);
		particleSystem.setParticlesSpawnEnabled(false);
	}
	
	//Método que se encarga de abrir agua
	public void abrirAgua(){
		aguaSystem.setParticlesSpawnEnabled(true);
//		centroPapelera = (ObjReducir[0].getBaseHeight() / 2)-30;
//		aguaEmitter.setCenter(posX + centroPapelera+65, posY + centroPapelera+40);
//		dormir(100);
//		aguaSystem.setParticlesSpawnEnabled(false);
		aguaSystem.setVisible(true);
		mcerrarAguaSound.stop();
		mabrirAguaSound.play();
	}
	
	//Método que se encarga de cerrar el agua
	public void cerrarAgua(){
		mcerrarAguaSound.play();
		mabrirAguaSound.pause();
		aguaSystem.setParticlesSpawnEnabled(false);	
		aguaSystem.setVisible(false);
	}
	
	//Método que activa el sistema de particulas con los paŕametros que le indicamos para su animación
	public void setAnimacionParticula()
	{
		particleSystem.addParticleInitializer(new ColorInitializer(0, 1, 0));
		particleSystem.addParticleInitializer(new AlphaInitializer(0));
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		particleSystem.addParticleInitializer(new VelocityInitializer(-40, 40, -40, 40));
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
		particleSystem.addParticleModifier(new ColorModifier(0.5f, 0.5f, 0, 0.5f, 0, 0, 0, 3));
		particleSystem.addParticleModifier(new ColorModifier(0.5f, 0, 1, 1, 0, 1, 4, 6));
		particleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 1));
		particleSystem.addParticleModifier(new AlphaModifier(1, 0, 5, 6));
		particleSystem.addParticleModifier(new ExpireModifier(2.5f));	
		particleSystem.setParticlesSpawnEnabled(false);
	}
	
	//Método que activa el sistema de particulas con los paŕametros que le indicamos para su animación
	public void setAnimacionAgua()
	{
		aguaSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		aguaSystem.addParticleInitializer(new VelocityInitializer(-2, 2, 100, 300));
		aguaSystem.addParticleModifier(new ColorModifier(0, 0, 0, 0.2f, 0.5f, 1, 0, 0.25f));
		aguaSystem.addParticleModifier(new ExpireModifier(0.30f, 0.30f));
	}
	
	//Método que coloca el ultimo elemento en la cola de la pantalla y mueve todos hacía la izquierda
	//Finalmente llama al método para mover la ObjReducir al centro
	public void recolocarMiniObjReducir(){
		for (int i : vObjReducir){
			ObjReducir[i].moverMiniObj(ObjReducir[i].getX(), ObjReducir[i].getX()-50,CAMERA_HEIGHT-100,CAMERA_HEIGHT-100);
		}
		moverObjReducirCentro();
	}
	
	//Método que mueve el primer elemento al centro
	public void moverObjReducirCentro(){
//			Log.v("#PFM#", "Entramos en moverObjReducirCentro ObjReducir["+vObjReducir.get(0)+"].x: "+ObjReducir[vObjReducir.get(0)].getX()+", ObjReducir["+vObjReducir.get(0)+"].y: "+ObjReducir[vObjReducir.get(0)].getY()+", CX:"+centerObjReducirX+", CY:"+centerObjReducirY);
		cuad1.registerEntityModifier(new FadeInModifier(0.2f));
		ObjReducir[vObjReducir.get(0)].moverMiniObjAlCentro(ObjReducir[vObjReducir.get(0)].getX(), centerObjReducirX/2, 
															ObjReducir[vObjReducir.get(0)].getY(), centerObjReducirY);
		if (vObjReducir.get(0) == 1) {
			if (vObjReducir.get(0) == 1 && ObjReducir[vObjReducir.get(0)].getCurrentTileIndex() != 0) {
				dormir(400);
				abrirAgua();
			}
		}
	}
	
	//Método que elimina un elemento de la lista de ObjReducirs pendientes
	public void eliminarElementoLista(){
		ObjReducir[vObjReducir.get(0)].matar();
		vObjReducir.remove(0);
	}
	
	//Método que coloca el elmento indicado al final de la lista
	public void ponerElementoFinLista(int idObjReducir){
		vObjReducir.remove(0);
		vObjReducir.add(idObjReducir);
		int ultimoElemento =vObjReducir.get(vObjReducir.size()-1);
		int penultimoElemento =vObjReducir.get(vObjReducir.size()-2);
		dormir(400);

		ObjReducir[ultimoElemento].aparecerMiniObjFinal(ObjReducir[penultimoElemento].getX()+50,ObjReducir[penultimoElemento].getY());
		ObjReducir[ultimoElemento].setScale(0.5f);
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
	
	public void finalizarJuego(boolean finOk){
		setFinDelJuego(true);
		setFinOk(finOk);
		actualizarPuntosFinal();
		finalizarEstados();
		finMusica();
//			crearObjeto3D(finOk);
	}
	
	//Método que se encarga de actualizar los puntos finales sumando el tiempo restante
	public void actualizarPuntosFinal() {
		int puntosFinal = getNumPuntos()+getNumTiempo();

		Log.v("#PFM#", "actualizarPuntosFinal: "+puntosFinal);
		setPuntosFinal(puntosFinal);
	}
	
	//Método que muestra el mensaje final dependiendo si se ha ganado o se ha perdido.
	//Oculta el texto, finaliza la música, mata las celdas y llama a actualizarPuntosFinal
	public void finalizarEstados(){
		crono.setVisible(false);
		lblPuntos.setVisible(false);
		lblTiempo.setVisible(false);
		puntos.setVisible(false);
		Pregunta.setVisible(false);
		Respuesta.setVisible(false);
		cuad1.setVisible(false);
		cuad2.setVisible(false);
		nextSprite.setVisible(false);
		
		for (int i = 0; i < ObjReducir.length; i++) {
			ObjReducir[i].rematar();
			ObjReducir[i].sacarCelda(velocidadObjReducir);
		}
//		for (int i = 0; i < papelera.length; i++) {
//			papelera[i].rematar();
//			papelera[i].sacarCelda(velocidadObjReducir);
//		}
	}
	
	//Método que para todo los sonidos y muestra el sonido correspondiente si se ha ganado o se ha perdido
	public void finMusica(){
		mMusic.stop();
		mExplosionSound.stop();
		mErrorSound.stop();
		mPocoTiempoSound.stop();
		mabrirAguaSound.stop();
		mcerrarAguaSound.stop();
	}
	
	//Liberamos memoria al finalizar
	 @Override
   protected void onDestroy() {
	        super.onDestroy();
	        if (mMusic != null) {
	        	mMusic.release();
	        	mMusic = null;
	        }
	        if (mExplosionSound != null) {
	        	mExplosionSound.release();
	        	mExplosionSound = null;
	        }
	        if (mErrorSound != null) {
	        	mErrorSound.release();
	        	mErrorSound = null;
	        }
	        if (mPocoTiempoSound != null) {
	        	mPocoTiempoSound.release();
	        	mPocoTiempoSound = null;
	        }
	        if (mabrirAguaSound != null) {
	        	mabrirAguaSound.release();
	        	mabrirAguaSound = null;
	        }
	    }
	
	//Método que muestra la R en 3D
	 public void crearObjeto3D(){
			Intent i = new Intent(getBaseContext(), PantallaResultado.class);
			i.putExtra("finOk", isFinOk());
			i.putExtra("puntosFinales", getPuntosFinal());
			i.putExtra("tipoJuego", 2);
		    startActivity(i);
		    finish();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.indice:
			Intent settings = new Intent(getBaseContext(),Indice.class);
			startActivity(settings);
			finish();
			return false;
		case R.id.acercade:
			  Toast.makeText(JuegoReducir.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
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
	
	//*********************************************************************************************
	//PROPIEDADES
	//*********************************************************************************************
	private int[][] posicionPapelera;
	public void setPosicionPapelera(int[][] posicionPapelera) {
		this.posicionPapelera = posicionPapelera;
	}
	public int[][] getPosicionPapelera() {
		return posicionPapelera;
	}
	
	private int numPuntos = 0;
	public void setNumPuntos(int numPuntos) {
		this.numPuntos = numPuntos;
	}
	public int getNumPuntos() {
		return numPuntos;
	}

	public void setJuegoCargado(boolean juegoCargado) {
		this.juegoCargado = juegoCargado;
	}

	public boolean isJuegoCargado() {
		return juegoCargado;
	}

	public void setCuentaAtras(boolean cuentaAtras) {
		this.cuentaAtras = cuentaAtras;
	}

	public boolean isCuentaAtras() {
		return cuentaAtras;
	}
	public void setIntCA(int intCA) {
		this.intCA = intCA;
	}

	public int getIntCA() {
		return intCA;
	}
	
	public void setFinDelJuego(boolean finDelJuego) {
		this.finDelJuego = finDelJuego;
	}

	public boolean isFinDelJuego() {
		return finDelJuego;
	}

	public void setNumTiempo(int numTiempo) {
		this.numTiempo = numTiempo;
	}

	public int getNumTiempo() {
		return numTiempo;
	}

	public void setPuntosFinal(int puntosFinal) {
		this.puntosFinal = puntosFinal;
	}

	public int getPuntosFinal() {
		return puntosFinal;
	}

	public void setFinOk(boolean finOk) {
		this.finOk = finOk;
	}

	public boolean isFinOk() {
		return finOk;
	}
	
	//*********************************************************************************************



}
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
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.FadeInModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AlphaInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.EntityBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pfm.core.CeldaAnimada;
import com.pfm.cuento.R;
import com.pfm.libro.Page1;
import com.pfm.menu.Indice;

public class JuegoReutilizar extends BaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;

	private TiledTextureRegion[] pila;
	private Texture[] pilaTex;

	private CeldaAnimada[] celdas;
	
	private Sound mExplosionSound;
	private Sound mGiroSound;
	private Sound mErrorSound;
	private Sound mPocoTiempoSound;
	private Music mMusic;

	private Texture backTex;
	private TextureRegion backTexR;

	private ChangeableText tresDosUno;
	private ChangeableText puntos;
	private ChangeableText strCuentaPuntos;
	private ChangeableText crono;
	private Text lblPuntos;
	private Text lblTiempo;

	private int[][] celdaPosicion = { { 275, 65 }, { 400, 65 }, { 525, 65 },{ 650, 65 }, 
									  { 275, 190 }, { 400, 190 }, { 525, 190 },{ 650, 190 }, 
									  { 275, 315 }, { 400, 315 }, { 525, 315 },{ 650, 315 } };

	private String[] imgCelda = { "gfx/celda_dorso_tiled1_1.png",
			"gfx/celda_dorso_tiled1_2.png", "gfx/celda_dorso_tiled2_1.png",
			"gfx/celda_dorso_tiled2_2.png", "gfx/celda_dorso_tiled3_1.png",
			"gfx/celda_dorso_tiled3_2.png", "gfx/celda_dorso_tiled4_1.png",
			"gfx/celda_dorso_tiled4_2.png", "gfx/celda_dorso_tiled5_1.png",
			"gfx/celda_dorso_tiled5_2.png", "gfx/celda_dorso_tiled6_1.png",
			"gfx/celda_dorso_tiled6_2.png" };

	private int[] valorCeldas = { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6 };
	private int idCeldaGirada = -1;

	private Font mPlokFontM;
	private Font mPlokFontL;
	private Font mPlokFontXL;
	private Font mPlokFontXXL;

	private Texture mPlokFontTextureM;
	private Texture mPlokFontTextureL;
	private Texture mPlokFontTextureXL;
	private Texture mPlokFontTextureXXL;


	private Texture mParticulaTex;
	private TextureRegion mParticulaTexRegion;
	
	private CircleOutlineParticleEmitter particleEmitter;
	private ParticleSystem particleSystem;
	
	private int tiempoCrono = 60;
	private boolean finDelJuego = false;
	private int numPuntos = 0;
	private int numTiempo = 0;
	private int celdasRestantes = valorCeldas.length;

	private static final float velocidadCelda = 300.0f;
	private float centroCelda;
	 
	private Scene scene;
	protected MenuScene mMenuScene;
	private boolean juegoCargado = true;
	private boolean cuentaAtras = true;
	private boolean finOk; 
	
	private int puntosFinal = 0; 
	private int intCA = 3;
	private Sound mcuentaAtras1Sound;
	private Sound mcuentaAtras2Sound;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter &amp; Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

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
		mezclarCeldas();
		// TEXTURAS
		backTex = new Texture(512, 512,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondoRayas.png", 0, 0);

		pilaTex = new Texture[celdaPosicion.length];
		for (int i = 0; i < pilaTex.length; i++) {
			pilaTex[i] = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}

		pila = new TiledTextureRegion[celdaPosicion.length];
		for (int i = 0; i < pilaTex.length; i++) {
			pila[i] = TextureRegionFactory.createTiledFromAsset(this.pilaTex[i], this, imgCelda[i], 0, 0, 2, 1);
		}

		celdas = new CeldaAnimada[celdaPosicion.length];

		// FUENTES
		FontFactory.setAssetBasePath("font/");
		this.mPlokFontTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureXXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mPlokFontM = FontFactory.createFromAsset(this.mPlokFontTextureM,this, "Plok.ttf", 30, true, Color.WHITE);
		this.mPlokFontL = FontFactory.createFromAsset(this.mPlokFontTextureL,this, "Plok.ttf", 40, true, Color.BLUE);
		this.mPlokFontXL = FontFactory.createFromAsset(this.mPlokFontTextureXL,this, "Plok.ttf", 50, true, Color.YELLOW);
		this.mPlokFontXXL = FontFactory.createFromAsset(this.mPlokFontTextureXXL,this, "Plok.ttf", 60, true, Color.DKGRAY);

		for (int i = 0; i < pilaTex.length; i++) {
			this.mEngine.getTextureManager().loadTextures(pilaTex[i]);
		}
		
		this.mParticulaTex = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticulaTexRegion = TextureRegionFactory.createFromAsset(this.mParticulaTex, this, "gfx/particle_point.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(backTex,mPlokFontTextureM, mPlokFontTextureL, mPlokFontTextureXL, mPlokFontTextureXXL, this.mParticulaTex);
		this.mEngine.getFontManager().loadFonts(this.mPlokFontM,this.mPlokFontL, this.mPlokFontXL,this.mPlokFontXXL);
		
		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/street.mid");
			this.mMusic.setLooping(true);

			this.mExplosionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "celdasbien.ogg");
			this.mGiroSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "giro.ogg");
			this.mErrorSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "errorceldas.ogg");
			this.mPocoTiempoSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "pocotiempo.ogg");
			this.mcuentaAtras1Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "golpegrave.ogg");
			this.mcuentaAtras2Sound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "retroExplosion.ogg");
		} catch (IOException ioe) {
			// No se ha podido cargar el fichero. Realizar las operaciones
			// oportunas.
//			Debug.e(ioe);
		}
	}

	@Override
	public Scene onLoadScene() {
		Log.v("#PFM#", "Entramos en  onLoadScene()");

		scene = new Scene(2);

		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));
		
		ArrayList<IEntity> objs =  crearObjetos();
		
		setAnimacionParticula();
		
		crearCeldas();
		
		// Cargamos la escena con las celdas y los objetos
		for (int i = 0; i < celdas.length; i++) {
			scene.getLastChild().attachChild(celdas[i]);
			scene.registerTouchArea(celdas[i]);
		}
		
		for (int i=0; i<objs.size();i++){
			scene.getLastChild().attachChild(objs.get(i));
		}
		
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
		Log.v("#PFM#", "Entramos en  onLoadComplete()");
	}
	
	//Método que crea todo los objetos que aparecerán en pantalla
	public ArrayList<IEntity> crearObjetos(){
		ArrayList<IEntity> arrObjetos = new ArrayList<IEntity>();
		
		particleEmitter = new CircleOutlineParticleEmitter(0.0f, 0.0f, 20);
		particleSystem = new ParticleSystem(particleEmitter, 60, 60, 360, this.mParticulaTexRegion);	

		tresDosUno = new ChangeableText(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, this.mPlokFontL, "3", 8);
//		tresDosUno.setVisible(false);
		
		lblTiempo = new Text(20, 80, this.mPlokFontM,getString(R.string.tiempo));
		crono = new ChangeableText(20, 120,this.mPlokFontXL, "", 3);

		lblPuntos = new Text(20, 260, this.mPlokFontM,getString(R.string.puntos));
		puntos = new ChangeableText(20, 300, this.mPlokFontXL, String.valueOf(numPuntos), 6);
		
		strCuentaPuntos = new ChangeableText(20, 360, this.mPlokFontM,"", 4);
		strCuentaPuntos.setVisible(false);
		strCuentaPuntos.setColor(0.8f, 0, 0.8f);
		
		arrObjetos.add(particleSystem);
		arrObjetos.add(tresDosUno);
		arrObjetos.add(lblTiempo);
		arrObjetos.add(crono);
		arrObjetos.add(lblPuntos);
		arrObjetos.add(puntos);
		arrObjetos.add(strCuentaPuntos);
		return arrObjetos;
	}

	//Método donde creamos las Celdas y las cargamos las imágenes en escena y registramos su area de clickado
	public void crearCeldas(){
		for (int i = 0; i < celdas.length; i++) {
			celdas[i] = new CeldaAnimada(celdaPosicion[i][0],celdaPosicion[i][1], this.pila[i], i) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX,
						final float pTouchAreaLocalY) {

					switch (pSceneTouchEvent.getAction()) {
					case TouchEvent.ACTION_DOWN:
						if (!isMuerta() && !tresDosUno.isVisible()) {
							if (isGirada()) {
								idCeldaGirada = -1;
								ocultarCara();
								mGiroSound.play();
							} else {
								mostrarCara();
								mGiroSound.play();
								buttonPushed(getIdCelda());
							}
						}
					}
					return true;
				}

				@Override
				protected void applyRotation(final GL10 pGL) {
					/*
					 * Disable culling so we can see the backside of this
					 * sprite.
					 */
					GLHelper.disableCulling(pGL);

					final float rotation = this.mRotation;

					if (rotation != 0) {
						final float rotationCenterX = this.mRotationCenterX;
						final float rotationCenterY = this.mRotationCenterY;

						pGL.glTranslatef(rotationCenterX, rotationCenterY, 0);
						/*
						 * Note we are applying rotation around the y-axis and
						 * not the z-axis anymore!
						 */
						pGL.glRotatef(rotation, 0, 1, 0);
						pGL.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
					}
				}

				@Override
				protected void drawVertices(final GL10 pGL, final Camera pCamera) {
					super.drawVertices(pGL, pCamera);

					/* Enable culling as 'normal' entities profit from culling. */
					GLHelper.enableCulling(pGL);
				}
			};
			celdas[i].setScale(1.2f);
			scene.getLastChild().attachChild(celdas[i]);
			scene.registerTouchArea(celdas[i]);
		}
	}
	
	//Método que comprueba si el juego esta cargando, si esta en cuenta atras o si es el finla del juego actualizando los objetos
	public void actualizarEstados(){
		int seg = tiempoCrono - ((int) JuegoReutilizar.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (isJuegoCargado()){
			mMusic.play();
			setJuegoCargado(false);
		}
		
		if (isCuentaAtras()){
			mostrarCuentaAtras();
		} else {
			if(tresDosUno.isVisible()){
				tresDosUno.setVisible(false);
				for (int i = 0; i < celdas.length; i++) {
					celdas[i].registerEntityModifier(new FadeInModifier(0.5f));	
				}
			}
			if (isFinDelJuego()) {
				for (int i = 0; i < celdas.length; i++) {
					if (celdas[i].getY() >= 500) {
						celdas[i].sacarCelda(2.0f);
					}
				}
				if(seg == getNumTiempo()-2){
					crearObjeto3D();
				}
//				Log.v("#PFM#", "Segundos: "+seg+", getNumTiempo: "+getNumTiempo()+", tiempoCrono-getnumtiempo: "+(tiempoCrono-getNumTiempo()));

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

	//Método que se activa al pulsar una celda
	public void buttonPushed(int buttPressed) {
		esperandoGiro(buttPressed);
		if (idCeldaGirada != -1) {
			if (buttPressed == getPareja(idCeldaGirada)) {
				lanzarParticulas(celdas[buttPressed].getX(),celdas[buttPressed].getY());
				lanzarParticulas(celdas[idCeldaGirada].getX(),celdas[idCeldaGirada].getY());
				celdas[buttPressed].matar();
				celdas[idCeldaGirada].matar();
				mExplosionSound.play();
				celdasRestantes = celdasRestantes-2;
				actualizarPuntos(true);
			} else {
				celdas[buttPressed].ocultarCara();
				celdas[idCeldaGirada].ocultarCara();
				mErrorSound.play();
				actualizarPuntos(false);
			}
			idCeldaGirada = -1;
		} else {
			idCeldaGirada = buttPressed;
		}
		
		if(celdasRestantes==0){
			finalizarJuego(true);
		}
	}
	
	//Método que mezcla las celdas en su array para que cada vez salgan de una manera aleatoria
	public void mezclarCeldas(){
		for ( int i = valorCeldas.length-1; i > 0; i-- ) {
		int rand = (int)(Math.random()*(i+1));
		int temp = valorCeldas[i];
		valorCeldas[i] = valorCeldas[rand];
		valorCeldas[rand] = temp;
		
		String tempSt = imgCelda[i];
		imgCelda[i] = imgCelda[rand];
		imgCelda[rand] = tempSt;
		}
	}

	//Método que recibe el id de una celda y le devuelve el id de su pareja
	public int getPareja(int idCelda) {
		int pareja = 0;
		for (int i = 0; i < valorCeldas.length; i++) {
			if (i != idCelda) {
				if (valorCeldas[i] == valorCeldas[idCelda]) {
					pareja = i;
				}
			}
		}
		return pareja;
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
//			lanzarParticulas(tresDosUno.getX(),tresDosUno.getY());
		}
	}

	//Método que actualiza los puntos añadiendo si es un acierto y restando si es un error
	public void actualizarPuntos(boolean acierto) {
		int p = getNumPuntos();
		int pcp = 0;
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
			strCuentaPuntos.setVisible(true);
			strCuentaPuntos.setText(String.valueOf(pcp));
			strCuentaPuntos.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.2f, 1, 0.8f), new ScaleModifier(0.2f, 0.8f,1)));
		}
		dormir(300);
		puntos.setText(String.valueOf(p));
		strCuentaPuntos.setVisible(false);
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
		
		for (int i = 0; i < celdas.length; i++) {
			celdas[i].matar();
			celdas[i].sacarCelda(velocidadCelda);
		}
	}
	
	//Método que se encarga de lanzar partículas en la posición indicada
	public void lanzarParticulas(float posX, float posY){
		particleSystem.setParticlesSpawnEnabled(true);
		centroCelda = (celdas[0].getBaseHeight() / 2)-10;
		particleEmitter.setCenter(posX + centroCelda, posY + centroCelda);
		dormir(100);
		particleSystem.setParticlesSpawnEnabled(false);
	}
	
	//Método que activa el sistema de particulas con los paŕametros que le indicamos para su animación
	public void setAnimacionParticula()
	{
		particleSystem.addParticleInitializer(new ColorInitializer(0, 1, 0));
		particleSystem.addParticleInitializer(new AlphaInitializer(0));
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		particleSystem.addParticleInitializer(new VelocityInitializer(-20, 20, -20, 20));
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f, 360.0f));
		particleSystem.addParticleModifier(new ColorModifier(0.5f, 0.5f, 0, 0.5f, 0, 0, 0, 3));
		particleSystem.addParticleModifier(new ColorModifier(0.5f, 0, 1, 1, 0, 1, 4, 6));
		particleSystem.addParticleModifier(new AlphaModifier(0, 1, 0, 1));
		particleSystem.addParticleModifier(new AlphaModifier(1, 0, 5, 6));
		particleSystem.addParticleModifier(new ExpireModifier(3.5f));	
		particleSystem.setParticlesSpawnEnabled(false);
	}
	
	public void finalizarJuego(boolean finOk){
		setFinDelJuego(true);
		setFinOk(finOk);
		actualizarPuntosFinal();
		finalizarEstados();
		finMusica();
//		crearObjeto3D(finOk);
	}
	
	//Método que para todo los sonidos y muestra el sonido correspondiente si se ha ganado o se ha perdido
	public void finMusica(){
		mMusic.stop();
		mExplosionSound.stop();
		mGiroSound.stop();
		mErrorSound.stop();
		mPocoTiempoSound.stop();
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
	        if (mGiroSound != null) {
	        	mGiroSound.release();
	        	mGiroSound = null;
	        }
	        if (mErrorSound != null) {
	        	mErrorSound.release();
	        	mErrorSound = null;
	        }
	        if (mPocoTiempoSound != null) {
	        	mPocoTiempoSound.release();
	        	mPocoTiempoSound = null;
	        }
	    }

	//Método que duerme la aplicación hasta que la celda gira
	public void esperandoGiro(int idCelda) {
		// Esperamos a que la ficha se gire
		while (!celdas[idCelda].isGirada()) {}
		dormir(200);
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
	
	//Método que muestra la R en 3D
	public void crearObjeto3D(){
		Intent i = new Intent(getBaseContext(), PantallaResultado.class);
		i.putExtra("finOk", isFinOk());
		i.putExtra("puntosFinales", getPuntosFinal());
		i.putExtra("tipoJuego", 0);
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
			  Toast.makeText(JuegoReutilizar.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
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
		public void setNumPuntos(int numPuntos) {
			this.numPuntos = numPuntos;
		}
	
		public int getNumPuntos() {
			return numPuntos;
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

		public void setFinOk(boolean finOk) {
			this.finOk = finOk;
		}

		public boolean isFinOk() {
			return finOk;
		}

		public void setPuntosFinal(int puntosFinal) {
			this.puntosFinal = puntosFinal;
		}

		public int getPuntosFinal() {
			return puntosFinal;
		}

	//*********************************************************************************************


}
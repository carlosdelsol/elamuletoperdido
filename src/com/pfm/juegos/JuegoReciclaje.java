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
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.builder.BlackPawnTextureBuilder;
import org.anddev.andengine.opengl.texture.builder.ITextureBuilder.TextureSourcePackingException;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pfm.core.ObjBasura;
import com.pfm.core.ObjBasura.tipoPapelera;
import com.pfm.cuento.R;
import com.pfm.libro.Page1;
import com.pfm.menu.Indice;

public class JuegoReciclaje extends BaseGameActivity{

	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private String[] imgBasura = { "gfx/bplastico.png","gfx/bpapel.png","gfx/bvidrio.png",
			   "gfx/bbrik.png","gfx/bcarton.png","gfx/borganico.png", 
			   "gfx/bplastico2.png","gfx/bvidrio2.png","gfx/borganico2.png",
			   "gfx/blata.png"};
	private tipoPapelera[] tipoBasura = { tipoPapelera.PLASTICO, tipoPapelera.PAPEL, tipoPapelera.VIDRIO, 
						  tipoPapelera.PLASTICO, tipoPapelera.PAPEL, tipoPapelera.ORGANICO,  
						  tipoPapelera.PLASTICO, tipoPapelera.VIDRIO, tipoPapelera.ORGANICO,
						  tipoPapelera.PLASTICO};
	private String[] imgPapelera = { "gfx/corganico.png","gfx/cpapel.png",
					 "gfx/cplastico.png","gfx/cvidrio.png"};
	
	private static final float velocidadBasura = 300.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BuildableTexture mBasuraTexture;
	private BuildableTexture mPapelerasTexture;
	
	private TextureRegion[] basuraTexRegion;
	private TextureRegion[] papeleraTexRegion;

	private ObjBasura[] papelera;
	private ObjBasura[] basura;
	private ArrayList<Integer> vBasura;

	private Scene scene;
	private int centerBasuraX;
	private int centerBasuraY;
	
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
		backTexR = TextureRegionFactory.createFromAsset(this.backTex, this,"gfx/fondoRayas2.png", 0, 0);
		
		this.mParticulaTex = new Texture(32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mParticulaTexRegion = TextureRegionFactory.createFromAsset(this.mParticulaTex, this, "gfx/particle_star.png", 0, 0);

		// FUENTES
		FontFactory.setAssetBasePath("font/");
		this.mPlokFontTextureM = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlokFontTextureXL = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.mPlokFontM = FontFactory.createFromAsset(this.mPlokFontTextureM,this, "Plok.ttf", 20, true, Color.WHITE);
		this.mPlokFontL = FontFactory.createFromAsset(this.mPlokFontTextureL,this, "Plok.ttf", 30, true, Color.BLUE);
		this.mPlokFontXL = FontFactory.createFromAsset(this.mPlokFontTextureXL,this, "Plok.ttf", 40, true, Color.YELLOW);
		
		this.mBasuraTexture = new BuildableTexture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		basuraTexRegion = new TextureRegion[imgBasura.length];
		for (int i = 0; i < basuraTexRegion.length; i++) {
			basuraTexRegion[i] = TextureRegionFactory.createFromAsset(mBasuraTexture, this, imgBasura[i]);
		}
		
	    this.mPapelerasTexture = new BuildableTexture(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    papeleraTexRegion = new TextureRegion[imgPapelera.length];
		for (int i = 0; i < papeleraTexRegion.length; i++) {
			papeleraTexRegion[i] = TextureRegionFactory.createFromAsset(mPapelerasTexture, this, imgPapelera[i]);
		}
		basura = new ObjBasura[imgBasura.length];
		papelera = new ObjBasura[imgPapelera.length];
		vBasura =new ArrayList<Integer>();
		
		int[][] pPosicion = { { 50, 50 }, 
							  { 50, CAMERA_HEIGHT - this.papeleraTexRegion[2].getHeight()-50 }, 
							  { CAMERA_WIDTH - this.papeleraTexRegion[1].getWidth()-50, 50 },
							  { CAMERA_WIDTH - this.papeleraTexRegion[3].getWidth()-50, CAMERA_HEIGHT - this.papeleraTexRegion[3].getHeight()-50 }};
		  
		  setPosicionPapelera(pPosicion);
	    
//		this.mAutoParallaxBackgroundTexture = new Texture(1024, 1024, TextureOptions.DEFAULT);
//		this.mParallaxLayerFront = TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_front.png", 0, 0);
//		this.mParallaxLayerBack = TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_back.png", 0, 188);
//		this.mParallaxLayerMid = TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/parallax_background_layer_mid.png", 0, 669);


	 // Le indicamos a la textura que gestione donde cargará cada imagen
	    try {
	      this.mPapelerasTexture.build(new BlackPawnTextureBuilder(0));
	      this.mBasuraTexture.build(new BlackPawnTextureBuilder(0));
	      this.mPapelerasTexture.build(new BlackPawnTextureBuilder(0));

	    } catch (final TextureSourcePackingException e) {
	      Debug.e(e);
	    }
	    
	    
	    
		this.mEngine.getTextureManager().loadTextures(backTex,this.mPlokFontTextureM, this.mPlokFontTextureL, this.mPlokFontTextureXL,
													  this.mBasuraTexture, this.mPapelerasTexture, this.mPapelerasTexture, this.mParticulaTex);		
		this.mEngine.getFontManager().loadFonts(this.mPlokFontM,this.mPlokFontL, this.mPlokFontXL);

		// Carga de música y sonido.
		SoundFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "mfx/bopon.mid");
			this.mMusic.setLooping(true);

			this.mExplosionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "celdasbien.ogg");
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

		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene(1);
		
		scene.setBackground(new EntityBackground(new Sprite(0, 0, CAMERA_WIDTH,CAMERA_HEIGHT, backTexR)));		

		centerBasuraX = (CAMERA_WIDTH - this.basuraTexRegion[0].getWidth()) / 2;
		centerBasuraY = (CAMERA_HEIGHT - this.basuraTexRegion[0].getHeight()) / 2;
		
		crearObjetos();
	    
		scene.setTouchAreaBindingEnabled(true);
		
		/* The actual collision-checking. */
		scene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() { }

			@Override
			public void onUpdate(final float pSecondsElapsed) {			
				if (!vBasura.isEmpty()) {
					for (int i = 0; i < papelera.length; i++) {
						if (papelera[i].collidesWith(basura[vBasura.get(0)])) {
							papelera[i].setAlpha(1.0f);
						} else {
							papelera[i].setAlpha(0.8f);
						}
					}
				}			
			}
		});
		
		scene.registerUpdateHandler(new TimerHandler(1.0f, true,new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
//				Log.v("#PFM#", "Entramos en  onLoadScene() - registerUpdateHandler()");
				actualizarEstados();			
			}
		}));

		return scene;
	}
	// ===========================================================
	// Methods
	// ===========================================================
	
	//Método que comprueba si el juego esta cargando, si esta en cuenta atras o si es el finla del juego actualizando los objetos
	public void actualizarEstados(){
		int seg = tiempoCrono - ((int) JuegoReciclaje.this.mEngine.getSecondsElapsedTotal()-5);
		
		if (isJuegoCargado()){
			mMusic.play();
			setJuegoCargado(false);
		}
		
		if (isCuentaAtras()){
			mostrarCuentaAtras();
		} else {
			if(tresDosUno.isVisible()){
				tresDosUno.setVisible(false);
				for (int i = 0; i < papelera.length; i++) {
					papelera[i].registerEntityModifier(new FadeInModifier(0.5f));	
				}
			}
			if (isFinDelJuego()) {
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
	
	//Método que crea todo los objetos que aparecerán en pantalla
	public void crearObjetos(){
		crearPapelerasBasura();
		
		ArrayList<IEntity> arrObjetos = new ArrayList<IEntity>();
		
		particleEmitter = new CircleOutlineParticleEmitter(0.0f, 0.0f, 50);
		particleSystem = new ParticleSystem(particleEmitter, 100, 200, 350, this.mParticulaTexRegion);	

		tresDosUno = new ChangeableText(CAMERA_WIDTH/2, CAMERA_HEIGHT/2, this.mPlokFontL, "3", 8);
//		tresDosUno.setVisible(false);
		
		lblTiempo = new Text(CAMERA_WIDTH/3, CAMERA_HEIGHT-100, this.mPlokFontM,getString(R.string.tiempo));		
		crono = new ChangeableText(lblTiempo.getX()+140, CAMERA_HEIGHT-110,this.mPlokFontXL, "100", 3);
		
		lblPuntos = new Text(CAMERA_WIDTH/3, CAMERA_HEIGHT-50, this.mPlokFontM,getString(R.string.puntos));
		puntos = new ChangeableText(lblPuntos.getX()+140, CAMERA_HEIGHT-60, this.mPlokFontXL,String.valueOf(numPuntos),6);
		
		strCuentaPuntos = new ChangeableText(puntos.getX()+120, CAMERA_HEIGHT-60, this.mPlokFontM,"", 4);
		strCuentaPuntos.setVisible(false);
		strCuentaPuntos.setColor(0.8f, 0, 0.8f);
		
		arrObjetos.add(particleSystem);
		arrObjetos.add(tresDosUno);
		arrObjetos.add(lblTiempo);
		arrObjetos.add(crono);
		arrObjetos.add(lblPuntos);
		arrObjetos.add(puntos);
		arrObjetos.add(strCuentaPuntos);
		
		for (int i=0; i<arrObjetos.size();i++){
			scene.getLastChild().attachChild(arrObjetos.get(i));
		}
		
		setAnimacionParticula();

	}

	//Método donde creamos las papeleras y los objetos de la basura
	public void crearPapelerasBasura(){
		 for (ObjBasura.tipoPapelera tipo : ObjBasura.tipoPapelera.values()) {	
			papelera[tipo.ordinal()] = new ObjBasura(posicionPapelera[tipo.ordinal()][0],posicionPapelera[tipo.ordinal()][1], this.papeleraTexRegion[tipo.ordinal()], tipo.ordinal());
			papelera[tipo.ordinal()].setTipo(tipo);
			papelera[tipo.ordinal()].setAlpha(0.8f);
			papelera[tipo.ordinal()].setScale(1.4f);
			scene.getLastChild().attachChild(papelera[tipo.ordinal()]);
			scene.registerTouchArea(papelera[tipo.ordinal()]);
		}
		
		for (int i = 0; i < basura.length; i++) {
			vBasura.add(i);

			basura[i] = new ObjBasura((this.papelera[0].getWidth()+120)+(i*40),50, this.basuraTexRegion[i], i){
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					switch(pSceneTouchEvent.getAction()) {
						case TouchEvent.ACTION_DOWN:
							if(this.isActiva()){
								this.setScale(1.8f);
								this.setSoltada(false);
								break;
							}
						case TouchEvent.ACTION_MOVE:
							if(this.isActiva()) {
								this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
							}
							break;
						case TouchEvent.ACTION_UP:
							Log.v("#PFM#", "ACTION_UP:"+this.getIdObj()+", isActiva():"+isActiva()+", isSoltada: "+isSoltada());
							boolean bEncima = false;
							if(this.isActiva()) {
								for (int i=0; i<papelera.length;i++){

									if(papelera[i].collidesWith(this)){
										if(this.getTipo().equals(papelera[i].getTipo())){
//											this.setVisible(false);
											mExplosionSound.play();
											lanzarParticulas(papelera[i].getX(),papelera[i].getY());
											eliminarElementoLista();
											this.matar();
											actualizarPuntos(true);
											if(vBasura.isEmpty()){
												finalizarJuego(true);
											}else{
												recolocarMiniBasura();
											}
										}
										bEncima=true;
									}
								}
								if (!this.isMuerta()) {
									this.setSoltada(true);
									this.setScale(1.5f);
									if(bEncima){
										mErrorSound.play();
										if(vBasura.size()>1){
											ponerElementoFinLista(this.getIdObj());
											recolocarMiniBasura();
										}else{
											moverBasuraCentro();
										}
										actualizarPuntos(false);
									}
								}
							}
							break;
					}
					return true;
				}
			};
			basura[i].setTipo(tipoBasura[i]);
			basura[i].setScale(0.5f);
//			Log.v("#PFM#", "basura.tipo():"+basura[i].getTipo().name()+", basura.id():"+basura[i].getIdObj());

			scene.getLastChild().attachChild(basura[i]);
			scene.registerTouchArea(basura[i]);			
		}
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
			
			basura[vBasura.get(0)].setActiva(true);
			moverBasuraCentro();
//			lanzarParticulas(tresDosUno.getX(),tresDosUno.getY());
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
		centroPapelera = (papelera[0].getBaseHeight() / 2)-30;
		particleEmitter.setCenter(posX + centroPapelera, posY + centroPapelera);
		dormir(100);
		particleSystem.setParticlesSpawnEnabled(false);
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
	
	//Método que coloca el ultimo elemento en la cola de la pantalla y mueve todos hacía la izquierda
	//Finalmente llama al método para mover la basura al centro
	public void recolocarMiniBasura(){
		for (int i : vBasura){
			basura[i].moverMiniBasura(basura[i].getX(), basura[i].getX()-50,50,50);
		}
		moverBasuraCentro();
	}
	
	//Método que mueve el primer elemento al centro
	public void moverBasuraCentro(){
//		Log.v("#PFM#", "Entramos en moverBasuraCentro basura["+vBasura.get(0)+"].x: "+basura[vBasura.get(0)].getX()+", basura["+vBasura.get(0)+"].y: "+basura[vBasura.get(0)].getY()+", CX:"+centerBasuraX+", CY:"+centerBasuraY);
		basura[vBasura.get(0)].moverMiniBasuraAlCentro(basura[vBasura.get(0)].getX(), centerBasuraX, basura[vBasura.get(0)].getY(), centerBasuraY);
	}
	
	//Método que elimina un elemento de la lista de basuras pendientes
	public void eliminarElementoLista(){
		basura[vBasura.get(0)].matar();
		vBasura.remove(0);
	}
	
	//Método que coloca el elmento indicado al final de la lista
	public void ponerElementoFinLista(int idBasura){
		vBasura.remove(0);
		vBasura.add(idBasura);
		int ultimoElemento =vBasura.get(vBasura.size()-1);
		int penultimoElemento =vBasura.get(vBasura.size()-2);
		
		basura[ultimoElemento].aparecerMiniBasuraFinal(basura[penultimoElemento].getX()+50,basura[penultimoElemento].getY());
		basura[ultimoElemento].setScale(0.5f);
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
//		crearObjeto3D(finOk);
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
		
		for (int i = 0; i < basura.length; i++) {
			basura[i].rematar();
			basura[i].sacarCelda(velocidadBasura);
		}
		for (int i = 0; i < papelera.length; i++) {
			papelera[i].rematar();
			papelera[i].sacarCelda(velocidadBasura);
		}
	}
	
	//Método que para todo los sonidos y muestra el sonido correspondiente si se ha ganado o se ha perdido
	public void finMusica(){
		mMusic.stop();
		mExplosionSound.stop();
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
	        if (mErrorSound != null) {
	        	mErrorSound.release();
	        	mErrorSound = null;
	        }
	        if (mPocoTiempoSound != null) {
	        	mPocoTiempoSound.release();
	        	mPocoTiempoSound = null;
	        }
	    }
	
	//Método que muestra la R en 3D
	public void crearObjeto3D(){
		Intent i = new Intent(getBaseContext(), PantallaResultado.class);
		i.putExtra("finOk", isFinOk());
		i.putExtra("puntosFinales", getPuntosFinal());
		i.putExtra("tipoJuego", 1);
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
			  Toast.makeText(JuegoReciclaje.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
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

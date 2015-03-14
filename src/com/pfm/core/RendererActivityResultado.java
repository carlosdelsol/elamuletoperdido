package com.pfm.core;

import min3d.Shared;
import min3d.core.Renderer;
import min3d.core.Scene;
import min3d.interfaces.ISceneController;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pfm.cuento.R;
import com.pfm.juegos.JuegoReciclaje;
import com.pfm.juegos.JuegoReducir;
import com.pfm.juegos.JuegoReutilizar;
import com.pfm.libro.Fin;
import com.pfm.libro.Page1;
import com.pfm.libro.Page6;
import com.pfm.libro.Page7;
import com.pfm.menu.Indice;

public class RendererActivityResultado extends Activity implements ISceneController
{
	public Scene scene;
	protected GLSurfaceView _glSurfaceView;
	
	protected Handler _initSceneHander;
	protected Handler _updateSceneHander;
	private boolean finOk;
	private int puntosFinales;
	private int tipoJuego;
    private MediaPlayer mMediaPlayer; 
    
    Puntos puntos = new Puntos();
	
	final Runnable _initSceneRunnable = new Runnable() 
	{
        public void run() {
            onInitScene();
        }
    };
	final Runnable _updateSceneRunnable = new Runnable() 
    {
        public void run() {
            onUpdateScene();
        }
    };
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//Recogemos los parámetros
		Bundle extras = getIntent().getExtras();
		if(extras !=null)
		{
			finOk = extras.getBoolean("finOk");
			puntosFinales = extras.getInt("puntosFinales");
			tipoJuego = extras.getInt("tipoJuego");
		}
		
		//Quitamos la barra de arriba
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		_initSceneHander = new Handler();
		_updateSceneHander = new Handler();
		
		//
		// These 4 lines are important.
		//
		Shared.context(this);
		scene = new Scene(this);
		Renderer r = new Renderer(scene);
		Shared.renderer(r);
		
		_glSurfaceView = new GLSurfaceView(this);

		// TESTING - MAKE GLSURFACEVIEW TRANSPARENT 
		// CAN'T FIGURE THIS OUT WTF:
//         _glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
//         _glSurfaceView.setEGLConfigChooser(8,8,8,8, 16, 0);	
		
		// Uncomment for logging:
		// _glSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR | GLSurfaceView.DEBUG_LOG_GL_CALLS);

		_glSurfaceView.setRenderer(r);
		_glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		onCreateSetContentView();
	}
	
	protected GLSurfaceView glSurfaceView()
	{
		return _glSurfaceView;
	}
	
	/**
	 * Separated out for easier overriding...
	 */
	protected void onCreateSetContentView()
	{
		Log.v("#PFM#", "Entramos en  onCreateSetContentView()");
//		setContentView(_glSurfaceView);
		setContentView(R.layout.resultado_layout);

		Typeface fontSniglet = Typeface.createFromAsset(getAssets(), "font/Sniglet.ttf");
		Typeface fontBangers = Typeface.createFromAsset(getAssets(), "font/Bangers.ttf");
		
		TextView txtResultado = (TextView) findViewById(R.id.textoResultado);
		TextView txtPuntos = (TextView) findViewById(R.id.textoPuntos);
		LinearLayout lnlResultado = (LinearLayout) findViewById(R.id.layoutTextoResultado);
		txtResultado.setTypeface(fontSniglet);
		txtPuntos.setTypeface(fontBangers);
//		txt.setTextSize(40.f);

		mMediaPlayer = new MediaPlayer();
		if(finOk){
			mMediaPlayer = MediaPlayer.create(this, R.raw.danimuybien);
			txtResultado.setText(getString(R.string.txtGanado));
			txtPuntos.setText(txtPuntos.getText() +" "+ String.valueOf(puntosFinales));
			
			// Add OpenGL surface
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.sceneHolder);
			ll.addView(_glSurfaceView);
		}else{
			mMediaPlayer = MediaPlayer.create(this, R.raw.daniintentalodenuevo);
			txtResultado.setText(getString(R.string.txtPerdido));
			txtPuntos.setVisibility(View.INVISIBLE);
			lnlResultado.getLayoutParams().height= 300;
		}
		mMediaPlayer.start();
		
		final Button btnIndice = (Button) findViewById(R.id.btnIndice);
		btnIndice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
			    Intent i = new Intent(getBaseContext(), Indice.class);
			    startActivity(i);
			    finish();
            }
        });
        
        final Button btnReiniciar = (Button) findViewById(R.id.btnReiniciar);
        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i;
            	switch(tipoJuego){
            		case 0:
            			i = new Intent(getBaseContext(), JuegoReutilizar.class);
            			break;
            		case 1:
            			i = new Intent(getBaseContext(), JuegoReciclaje.class);
            			break;
            		case 2:
            			i = new Intent(getBaseContext(), JuegoReducir.class);
            			break;
            		default:
            			i = new Intent(getBaseContext(), JuegoReutilizar.class);            			
            			break;
            	}	        	
			    startActivity(i);
			    finish();
		    }
        });
        
        final Button btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent i;
            	switch(tipoJuego){
            		case 0:
            			i = new Intent(getBaseContext(), Page6.class);
            			break;
            		case 1:
            			i = new Intent(getBaseContext(), Page7.class);
            			break;
            		case 2:
            			i = new Intent(getBaseContext(), Fin.class);
            			break;
            		default:
            			i = new Intent(getBaseContext(), JuegoReutilizar.class);            			
            			break;
            	}	        	
			    startActivity(i);
			    finish();
            }
        });
        
        switch(tipoJuego){
			case 0:
				settings = getSharedPreferences("puntos", MODE_PRIVATE);
				editor = settings.edit();
				editor.putString("puntosReutilizar",String.valueOf(puntosFinales) );
				editor.commit();
				break;
			case 1:
				settings = getSharedPreferences("puntos", MODE_PRIVATE);
				editor = settings.edit();
				editor.putString("puntosReciclaje",String.valueOf(puntosFinales) );
				editor.commit();
				break;
			case 2:
				settings = getSharedPreferences("puntos", MODE_PRIVATE);
				editor = settings.edit();
				editor.putString("puntosReducir",String.valueOf(puntosFinales) );
				editor.commit();
				break;
			default:            			
				break;
        }	        
//        prefsPrivate = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        String puntosReutilizar = settings.getString("puntosReutilizar", "Empty");
        String puntosReducir = settings.getString("puntosReducir", "Empty");
        String puntosReciclar = settings.getString("puntosReciclaje", "Empty");
        
    	Log.v("#PFM#", "puntos reutilizar: "+puntosReutilizar);
    	Log.v("#PFM#", "puntos reducir: "+puntosReducir);
    	Log.v("#PFM#", "puntos reciclaje: "+puntosReciclar);
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
			  Toast.makeText(RendererActivityResultado.this, getString(R.string.TituloAlumno) + "\n" + getString(R.string.nombreAlumno)+ "\n" + getString(R.string.universidadAlumno) , Toast.LENGTH_SHORT).show();
			  return true;
		case R.id.salir:
//			System.exit(0);
//			Intent intent = new Intent(getBaseContext(), JuegoReducir.class);
//			intent.fi
//			JuegoReducir.this.finish();
//			JuegoReciclar.this.finish();
//			JuegoReducir.this.finish();
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
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		_glSurfaceView.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		_glSurfaceView.onPause();
	}

	/**
	 * Instantiation of Object3D's, setting their properties, and adding Object3D's 
	 * to the scene should be done here. Or any point thereafter.
	 * 
	 * Note that this method is always called after GLCanvas is created, which occurs
	 * not only on Activity.onCreate(), but on Activity.onResume() as well.
	 * It is the user's responsibility to build the logic to restore state on-resume.
	 */
	public void initScene()
	{
	}

	/**
	 * All manipulation of scene and Object3D instance properties should go here.
	 * Gets called on every frame, right before drawing.   
	 */
	public void updateScene()
	{
	}
	
    /**
     * Called _after_ scene init (ie, after initScene).
     * Unlike initScene(), is thread-safe.
     */
    public void onInitScene()
    {
    }
    
    /**
     * Called _after_ scene init (ie, after initScene).
     * Unlike initScene(), is thread-safe.
     */
    public void onUpdateScene()
    {
    }
    
	public Handler getInitSceneHandler()
	{
		return _initSceneHander;
	}
	
	public Handler getUpdateSceneHandler()
	{
		return _updateSceneHander;
	}

    public Runnable getInitSceneRunnable()
    {
    	return _initSceneRunnable;
    }
	
    public Runnable getUpdateSceneRunnable()
    {
    	return _updateSceneRunnable;
    }
}

package com.pfm.juegos;

import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.HorizontalAlign;

import android.graphics.Color;
import android.graphics.Typeface;

import com.pfm.core.constructorSecuencias;
import com.pfm.core.constructorSecuencias.TipoSecuencia;

public class cuentaAtras extends Scene{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private Texture mFontTexture;
	private Font mFont;
	
	private Scene scene = new Scene(1);
	

	public cuentaAtras()
	{
		super(1);
		onLoadResources();
		
		scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		final int centerX = CAMERA_WIDTH / 2;
		final int centerY = CAMERA_HEIGHT / 2;
		
		//Cargamos las letras
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);

		final Text text3 = new Text(centerX+10, centerY+10, this.mFont, "3", HorizontalAlign.CENTER);
		final Text text2 = new Text(centerX-10, centerY-10, this.mFont, "2", HorizontalAlign.CENTER);
		final Text text1 = new Text(centerX+20, centerY+20, this.mFont, "1", HorizontalAlign.CENTER);
		final Text textJugar = new Text(100, 340, this.mFont, "¡A jugar!", HorizontalAlign.CENTER);
		
		constructorSecuencias csecuenciaAtras= new constructorSecuencias(TipoSecuencia.CUENTAATRAS);
		final LoopEntityModifier modificadorCuentaAtras = csecuenciaAtras.getLoopShapeModifier(0);

		text3.registerEntityModifier(modificadorCuentaAtras);
		text2.registerEntityModifier(modificadorCuentaAtras.clone());
		text1.registerEntityModifier(modificadorCuentaAtras.clone());
		textJugar.registerEntityModifier(modificadorCuentaAtras.clone());
		
		scene.getLastChild().attachChild(text3);
		scene.getLastChild().attachChild(text2);
		scene.getLastChild().attachChild(text1);
		scene.getLastChild().attachChild(textJugar);
		
//		this.getLastChild().attachChild(scene);
	}
	
	public void onLoadResources() {
		//Cargamos las letras
		this.mFontTexture = new Texture(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(this.mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32, true, Color.BLACK);
	}
	
//	private void setScene(Scene scene) {
//		this.scene = scene;
//	}

	public Scene getScene() {
		return scene;
	}
	
	public Font getFont() {
		return mFont;
	}
	
	public Texture getTexture() {
		return mFontTexture;
	}
}

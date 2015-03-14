package com.pfm.core;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.modifier.FadeInModifier;
import org.anddev.andengine.entity.modifier.FadeOutModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;

import android.util.Log;

import com.pfm.juegos.JuegoReutilizar;

public class CeldaAnimada extends AnimatedSprite {

//	private AnimatedSprite celda;
	private final PhysicsHandler mPhysicsHandler;

	private boolean girada = false;
	private boolean muerta = false;
	private int idCelda;

	public CeldaAnimada(final float pX, final float pY,
			final TiledTextureRegion pTextureRegion, int idCelda) {
		super(pX, pY, pTextureRegion);

		
		setIdCelda(idCelda);
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
	}

	public void mostrarCara() {
//		this.registerEntityModifier(new FadeInModifier(0.8f));//new FadeInModifier(0.5f)
		
		this.registerEntityModifier(new SequenceEntityModifier(
				new RotationModifier(0.4f, 0, 180)));//new FadeInModifier(0.5f)
		this.animate(new long[] { 200L, 200L }, 0, 1, 0,
				new IAnimationListener() {
			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				// TODO Auto-generated method stub
				setGirada(true); 
			}
		});
	}

	public void ocultarCara() {
		this.registerEntityModifier(new SequenceEntityModifier(
				new RotationModifier(0.4f, 180, 0)));
		this.animate(new long[] { 100L, 100L }, 1, 2, 0,
				new IAnimationListener() {
					@Override
					public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
						// TODO Auto-generated method stub
						stopAnimation(0);
						setGirada(false); 
					}
				});
	}

	public void sacarCelda(float velocity) {
		mPhysicsHandler.setVelocityY(velocity);
	}

	public void setGirada(boolean girada) {
		this.girada = girada;
	}

	public boolean isGirada() {
		return girada;
	}

	public void setIdCelda(int valor) {
		this.idCelda = valor;
	}

	public int getIdCelda() {
		return idCelda;
	}

	public void matar() {
		this.muerta = Boolean.TRUE;
		// setVelocityY(40.0f);
		this.registerEntityModifier(new FadeOutModifier(0.8f)); // Al finalizar
																	// el giro
																	// comienza
		this.setColor(0.2f, 0.2f, 0.2f);
	}
	
	public void revivir() {
		this.muerta = Boolean.FALSE;
	}

	public boolean isMuerta() {
		return muerta;
	}
}

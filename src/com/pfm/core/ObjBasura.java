package com.pfm.core;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.modifier.FadeInModifier;
import org.anddev.andengine.entity.modifier.FadeOutModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.view.animation.ScaleAnimation;

public class ObjBasura extends Sprite {

//	private AnimatedSprite celda;
	private final PhysicsHandler mPhysicsHandler;

	private boolean muerta = false;
	private int idObj;
	private boolean Soltada;
	private boolean activa = false;

	public enum tipoPapelera {
	 	// ===========================================================
	 	// Elements
	 	// ===========================================================
	 	ORGANICO,
	 	PAPEL,
	 	PLASTICO,
	 	VIDRIO; 
	 	}
	private tipoPapelera tipo;


	public ObjBasura(final float pX, final float pY,final TextureRegion pTextureRegion, int idCelda) {
		super(pX, pY, pTextureRegion);
		
		setIdObj(idCelda);
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
	}

	public void moverMiniBasura(float pFromx, float pTox,float pFromy, float pToy) {
		this.registerEntityModifier(new MoveModifier(0.4f, pFromx, pTox, pFromy, pToy));
	}
	
	public void moverMiniBasuraAlCentro(float pFromx, float pTox,float pFromy, float pToy) {
		this.setActiva(true);
		dormir(400);
		this.registerEntityModifier(new MoveModifier(0.2f, pFromx, pTox, pFromy, pToy));
		this.registerEntityModifier(new SequenceEntityModifier(new FadeInModifier(0.2f), 
															   new ScaleModifier(0.4f,this.getScaleX(), 1.5f)));
//		this.setScale(1.5f);
	}
	
	public void aparecerMiniBasuraFinal(float posX, float posY){
		this.setVisible(false);
		this.setPosition(posX, posY);
		this.registerEntityModifier(new FadeInModifier(0.8f));
		this.setVisible(true);
		this.setActiva(false);
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
	
	public void sacarCelda(float velocity) {
		mPhysicsHandler.setVelocityY(velocity);
	}

	public void setIdObj(int valor) {
		this.idObj = valor;
	}

	public int getIdObj() {
		return idObj;
	}

	public void matar() {
		this.muerta = Boolean.TRUE;
		this.activa = Boolean.FALSE;
		// setVelocityY(40.0f);
		this.registerEntityModifier(new FadeOutModifier(0.8f)); // Al finalizar
																	// el giro
																	// comienza
		this.setColor(0.2f, 0.2f, 0.2f);
		this.setVisible(false);
	}
	
	public void rematar() {
		this.muerta = Boolean.TRUE;
		this.activa = Boolean.FALSE;
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

	public void setTipo(tipoPapelera tipo) {
		this.tipo = tipo;
	}

	public tipoPapelera getTipo() {
		return tipo;
	}

	public void setSoltada(boolean soltada) {
		Soltada = soltada;
	}

	public boolean isSoltada() {
		return Soltada;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public boolean isActiva() {
		return activa;
	}
}

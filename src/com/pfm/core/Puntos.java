package com.pfm.core;

public class Puntos {
	String PuntosReutilizar;
	String PuntosReciclaje;
	String PuntosReducir;
	
//	public Puntos(String pPuntosReutilizar, String pPuntosReciclaje, String pPuntosReducir){
//		this.PuntosReutilizar = pPuntosReutilizar;
//		this.PuntosReciclaje = pPuntosReciclaje;
//		this.PuntosReducir = pPuntosReducir;
//	}
	
	public Puntos(){
		
	}
	
	public String getPuntosReutilizar(){
		return this.PuntosReutilizar;
	}
	
	public String getPuntosReciclaje(){
		return this.PuntosReciclaje;
	}
	
	public String getPuntosReducir(){
		return this.PuntosReducir;
	}
	
	public void setPuntosReutilizar(String pPuntosReutilizar){
		this.PuntosReutilizar = pPuntosReutilizar;
	}
	
	public void setPuntosReciclaje(String pPuntosReciclaje){
		this.PuntosReciclaje = pPuntosReciclaje;
	}
	
	public void setPuntosReducir(String pPuntosReducir){
		this.PuntosReducir = pPuntosReducir;
	}
	
}
package com.pfm.core;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.DelayModifier;
import org.anddev.andengine.entity.modifier.LoopEntityModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationByModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.anddev.andengine.entity.modifier.LoopEntityModifier.ILoopEntityModifierListener;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.LoopModifier;

public class constructorSecuencias {

	public enum TipoSecuencia {
		COMPLETA, CUENTAATRAS, JUGAR
	}

	private TipoSecuencia tipo;

	private void setTipo(TipoSecuencia tipo) {
		this.tipo = tipo;
	}

	private TipoSecuencia getTipo() {
		return tipo;
	}
	
	public constructorSecuencias(TipoSecuencia tipo) {
		this.setTipo(tipo);
	}

	private SequenceEntityModifier getSecuencia() {
		switch (getTipo()) {
			case COMPLETA: 
				return new SequenceEntityModifier(
						new RotationModifier(1, 0, 90),
						new AlphaModifier(2, 1, 0),
						new AlphaModifier(1, 0, 1),
						new ScaleModifier(2, 1, 0.5f),
						new DelayModifier(0.5f),
						new ParallelEntityModifier(
								new ScaleModifier(3, 0.5f, 5),
								new RotationByModifier(3, 90)
						),
						new ParallelEntityModifier(
								new ScaleModifier(3, 5, 1),
								new RotationModifier(3, 180, 0)
						)
				);
			case CUENTAATRAS:
				return new SequenceEntityModifier(
						new ParallelEntityModifier(
							new AlphaModifier(1, 0, 1),
							new ScaleModifier(1, 1, 2)),
						new ParallelEntityModifier(
								new AlphaModifier(1, 1, 0),
								new ScaleModifier(1, 2, 3)));
			case JUGAR:
				return new SequenceEntityModifier(
						new RotationModifier(1, 0, 90),
						new AlphaModifier(2, 1, 0),
						new AlphaModifier(1, 0, 1),
						new ScaleModifier(2, 1, 0.5f),
						new DelayModifier(0.5f),
						new ParallelEntityModifier(
								new ScaleModifier(3, 0.5f, 5),
								new RotationByModifier(3, 90)
						),
						new ParallelEntityModifier(
								new ScaleModifier(3, 5, 1),
								new RotationModifier(3, 180, 0)
						)
				);
			default:
				return null;
		}
	}
	
	public LoopEntityModifier getLoopShapeModifier(int repeticiones) {
		return new LoopEntityModifier(this.getIShapeModifierListener(), repeticiones,
									 this.getILoopShapeModifierListener(),
									 this.getSecuencia() );
	}	
	
	private IEntityModifierListener getIShapeModifierListener() {
		return new IEntityModifierListener() {
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	private ILoopEntityModifierListener getILoopShapeModifierListener() {
		return new ILoopEntityModifierListener() {
			@Override
			public void onLoopFinished(LoopModifier<IEntity> pLoopModifier,
					int pLoopsRemaining) {
				// TODO Auto-generated method stub
				
			}
		};
	}	
}

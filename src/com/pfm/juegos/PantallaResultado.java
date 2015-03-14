package com.pfm.juegos;

import min3d.core.Object3dContainer;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

import com.pfm.core.RendererActivityResultado;


/**
 * How to load a model from a .obj file
 * 
 * @author dennis.ippel
 * 
 */
public class PantallaResultado extends RendererActivityResultado {
	private Object3dContainer objModel;

//    private float mAngleX;
//    private float mAngleY;
//    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
//    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
//    private float mPreviousX;
//    private float mPreviousY;
	
	@Override
	public void initScene() {
		 // !important
//        scene.backgroundColor().setAll(0x00000000);
		scene.backgroundColor().setAll((short)43, (short)200, (short)51, (short)0);
        
		scene.lights().add(new Light());
		
		IParser parser = Parser.createParser(Parser.Type.OBJ,getResources(), "com.pfm.cuento:raw/letrar_obj", true);
		parser.parse();

		scene.backgroundTransparent();
		objModel = parser.getParsedObject();
		objModel.scale().x = objModel.scale().y = objModel.scale().z = 2.5f;
//		objModel.position().x++;
		scene.addChild(objModel);		
	}

	@Override
	public void updateScene() {
		objModel.rotation().x++;
		objModel.rotation().y++;
		objModel.rotation().z++;
//		objModel.rotation().rotateX(mAngleX);
//		objModel.rotation().rotateY(mAngleY);
	}
	
//	@Override
//	public boolean onTrackballEvent(MotionEvent $e)
//	{
//		 mAngleX += $e.getX() * TRACKBALL_SCALE_FACTOR;
//		 mAngleY += $e.getY() * TRACKBALL_SCALE_FACTOR;
//		return true;
//	}
//	
//	  @Override public boolean onTrackballEvent(MotionEvent e) {
//	        mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
//	        mRenderer.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR;
//	        requestRender();
//	        return true;
//	    }
//
//	    @Override public boolean onTouchEvent(MotionEvent $e) {
//	        float x = $e.getX();
//	        float y = $e.getY();
//	        switch ($e.getAction()) {
//	        case MotionEvent.ACTION_MOVE:
//	            float dx = x - mPreviousX;
//	            float dy = y - mPreviousY;
//	            mAngleX += dx * TOUCH_SCALE_FACTOR;
//	            mAngleY += dy * TOUCH_SCALE_FACTOR;
//	        }
//	        mPreviousX = x;
//	        mPreviousY = y;
//	        return true;
//	    }
}

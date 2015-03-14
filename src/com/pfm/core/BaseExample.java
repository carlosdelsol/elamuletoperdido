package com.pfm.core;

import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.pfm.cuento.R;
import com.pfm.menu.Indice;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * @author Nicolas Gramlich
 * @since 22:10:28 - 11.04.2010
 */
public abstract class BaseExample extends BaseGameActivity {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final int MENU_TRACE = Menu.FIRST;

        // ===========================================================
        // Fields
        // ===========================================================

        // ===========================================================
        // Constructors
        // ===========================================================

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================
//
//        @Override
//        public boolean onCreateOptionsMenu(final Menu pMenu) {
//    		MenuInflater inflater = getMenuInflater();
//    		inflater.inflate(R.menu.menu, pMenu);
//    		return super.onCreateOptionsMenu(pMenu);
//        }

//
//        @Override
//        public boolean onPrepareOptionsMenu(final Menu pMenu) {
//        		Log.v("#PFM#", "Entramos en onPrepareOptionsMenu");
//                pMenu.findItem(MENU_TRACE).setTitle(this.mEngine.isMethodTracing() ? "Stop Method Tracing" : "Start Method Tracing");
//                return super.onPrepareOptionsMenu(pMenu);
//        }

//        @Override
//        public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
//    		Log.v("#PFM#", "Entramos en onMenuItemSelected");
//			switch(pItem.getItemId()) {
//                    case MENU_TRACE:
//                            if(this.mEngine.isMethodTracing()) {
//                                    this.mEngine.stopMethodTracing();
//                            } else {
//                                    this.mEngine.startMethodTracing("AndEngine_" + System.currentTimeMillis() + ".trace");
//                            }
//                            return true;
//                    default:
//                            return super.onMenuItemSelected(pFeatureId, pItem);
//            }
//        }

    	public boolean onKeyDown(int keyCode, KeyEvent event) {
    		//Para bloquear botones del telefono
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//            }
            return false;

//            return super.onKeyDown(keyCode, event);
        }
        // ===========================================================
        // Methods
        // ===========================================================

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
}
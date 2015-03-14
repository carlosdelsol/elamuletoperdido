/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pfm.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;

import com.pfm.cuento.R;
import com.pfm.juegos.JuegoReciclaje;
import com.pfm.juegos.JuegoReducir;
import com.pfm.juegos.JuegoReutilizar;
import com.pfm.libro.Fin;
import com.pfm.libro.Page1;
import com.pfm.libro.Page2;
import com.pfm.libro.Page3;
import com.pfm.libro.Page4;
import com.pfm.libro.Page5;
import com.pfm.libro.Page6;
import com.pfm.libro.Page7;


public class Indice extends Activity implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
	
	private SharedPreferences settings;
	private String[] ejemplos;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.indice);

        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        
        ejemplos = getResources().getStringArray(R.array.ejemplos_array);

        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);
        g.setOnItemClickListener(new OnItemClickListener() 
						        {
						            public void onItemClick(AdapterView parent, 
						            View v, int position, long id) 
						            {                
//						                Toast.makeText(getBaseContext(),ejemplos[position],Toast.LENGTH_SHORT).show();
						                
					                	Intent i;
					        			settings = getSharedPreferences("puntos", MODE_PRIVATE);
					        	        String puntosReutilizar = settings.getString("puntosReutilizar", "Empty");
					        	        String puntosReciclar = settings.getString("puntosReciclaje", "Empty");
						        
						        		    //Iniciamos una nueva actividad dependiendo de la posición en el menu que este
						        	      switch(position)
						        	      {	        
						        	      		case 0: //Página 1
						        	              	i = new Intent(v.getContext(), Page1.class);
						        	      			startActivity(i);
						        	              	finish();
						        	      			break;
						        	      		case 1:	 //Página 2
						        	      			i = new Intent(v.getContext(), Page2.class);
						        	      			startActivity(i);
						        	      			finish();
						        	      			break;
						        	      		case 2:	 //Página 3
						        	      			i = new Intent(v.getContext(), Page3.class);
						        	      			startActivity(i);
						        	      			finish();
						        	      			break;
						        	      		case 3:	 //Página 4
						        	      			i = new Intent(v.getContext(), Page4.class);
						        	      			startActivity(i);
						        	      			finish();
						        	      			break;
						        	      		case 4:	 //Página 5
						            				i = new Intent(v.getContext(), Page5.class);
						            				startActivity(i);
						            				finish();
						        	      			break;
						        	      		case 5:	 //Juego Reutilizar
						        	    		      i = new Intent(v.getContext(), JuegoReutilizar.class);
						        	    		      startActivity(i);
						        	    		      finish();
						        	      			break;
						        	      		case 6:	 //Página 6
						        	      			  if(!puntosReutilizar.trim().equalsIgnoreCase("0")){
						        		    		      i = new Intent(v.getContext(), Page6.class);
						        		    		      startActivity(i);
						        		    		      finish();
						        	    		      }else{
						        	    		        Toast.makeText(Indice.this, "Antes tienes que encontrar la primera R", Toast.LENGTH_SHORT).show();
						        	    		      }
						        	      			break;
						        	      		case 7:	 //Juego Reciclaje
						        	      			  if(!puntosReutilizar.trim().equalsIgnoreCase("0")){
						        		    		      i = new Intent(v.getContext(), JuegoReciclaje.class);
						        		    		      startActivity(i);
						        		    		      finish();
						        	      			}else{
						        	      				Toast.makeText(Indice.this, "Antes tienes que encontrar la primera R", Toast.LENGTH_SHORT).show();
						        	    		      }
						        	      			break;	
						        	      		case 8:	//Página 7
						        	      			if(!puntosReciclar.trim().equalsIgnoreCase("0")){
						        	    		      i = new Intent(v.getContext(), Page7.class);
						        	    		      startActivity(i);
						        	    		      finish();
						        	      			}else{
						        	      				Toast.makeText(Indice.this, "Antes tienes que encontrar la segunda R", Toast.LENGTH_SHORT).show();
						        	    		      }
						        	    		      break;
						        	      		case 9:	 //Juego Reducir
//						        	      			if(!puntosReciclar.trim().equalsIgnoreCase("0")){
						        	    		      i = new Intent(v.getContext(), JuegoReducir.class);
						        	    		      startActivity(i);
						        	    		      finish();
//						        	      			}else{
//						        	      				Toast.makeText(Indice.this, "Antes tienes que encontrar la segunda R", Toast.LENGTH_SHORT).show();
//						        	    		      }
						        	    		      break;
//						        	      		case 10:	 //Fin
////						        	      			if(!puntosReducir.trim().equalsIgnoreCase("0")){
//						        	    		      i = new Intent(v.getContext(), Fin.class);
//						        	    		      startActivity(i);
//						        	    		      finish();
////						        	      			}else{
////						        	      				Toast.makeText(Indice.this, "Antes tienes que encontrar la tercera R", Toast.LENGTH_SHORT).show();
////						        	    		      }
//						        	    		      break;
						        	    		      
						        	      		default:
						        	      			break;
						        	      }
						            }
						        });
        
		Typeface fontBangers = Typeface.createFromAsset(getAssets(), "font/Bangers.ttf");
		TextView txtIndice = (TextView) findViewById(R.id.textoIndice);
		txtIndice.setTypeface(fontBangers);
    }

    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        mSwitcher.setImageResource(mImageIds[position]);
//        Toast.makeText(getBaseContext(),ejemplos[position],Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView parent) {
    }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        return i;
    }

    private ImageSwitcher mSwitcher;

    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);

            i.setImageResource(mThumbIds[position]);
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            i.setBackgroundResource(R.drawable.c1g);
            return i;
        }

        private Context mContext;

    }

    private Integer[] mThumbIds = {
            R.drawable.c1g, R.drawable.c2g,
            R.drawable.c3g, R.drawable.c4g,
            R.drawable.c5g, R.drawable.c6g,
            R.drawable.c7g, R.drawable.c8g,
            R.drawable.c9g, R.drawable.c11g};

    private Integer[] mImageIds = {
            R.drawable.c1g, R.drawable.c2g, R.drawable.c3g,
            R.drawable.c4g, R.drawable.c5g, R.drawable.c6g,
            R.drawable.c7g, R.drawable.c8g, R.drawable.c9g, 
            R.drawable.c11g};
    

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.salir:
			this.finish(); 
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menusalir, menu);
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}

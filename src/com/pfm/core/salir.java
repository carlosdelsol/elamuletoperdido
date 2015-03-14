package com.pfm.core;

import android.app.Activity;
import android.os.Bundle;

public class salir extends Activity{
	 public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       System.exit(1);
	       System.exit(2);
	       android.os.Process.killProcess(android.os.Process.myPid());
	   }
}

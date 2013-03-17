package com.netthreads.gdx.android;

import com.netthreads.gdx.core.Box2DBumpers;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class Box2DBumpersActivity extends AndroidApplication {
	
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
       config.useGL20 = true;
       initialize(new Box2DBumpers(), config);
   }
}

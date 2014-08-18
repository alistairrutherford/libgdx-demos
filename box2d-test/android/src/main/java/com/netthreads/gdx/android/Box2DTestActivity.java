package com.netthreads.gdx.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.netthreads.gdx.app.Box2DTest;

/**
 * Android Application Activity.
 *
 */
public class Box2DTestActivity extends AndroidApplication
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		initialize(new Box2DTest(), config);
	}
}

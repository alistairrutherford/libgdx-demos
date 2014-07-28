package com.netthreads.gdx.android;

import com.netthreads.gdx.app.core.SimpleShooter;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class SimpleShooterActivity extends AndroidApplication
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		initialize(new SimpleShooter(), config);
	}
}

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
		config.useGL20 = true;
		initialize(new SimpleShooter(), config);
	}
}

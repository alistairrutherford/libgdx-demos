package com.netthreads.gdx.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.netthreads.gdx.app.core.SimpleShooter;

public class SimpleShooterHtml extends GwtApplication
{
	@Override
	public ApplicationListener getApplicationListener()
	{
		return new SimpleShooter();
	}

	@Override
	public GwtApplicationConfiguration getConfig()
	{
		return new GwtApplicationConfiguration(480, 320);
	}
}

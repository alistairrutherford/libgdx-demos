package com.netthreads.gdx.html;

import com.netthreads.gdx.core.Box2DTest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class Box2DTestHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Box2DTest();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}

package com.netthreads.gdx.html;

import com.netthreads.gdx.core.Box2DBumpers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class Box2DBumpersHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Box2DBumpers();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}
}

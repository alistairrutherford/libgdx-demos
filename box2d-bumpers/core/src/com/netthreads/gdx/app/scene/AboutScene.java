package com.netthreads.gdx.app.scene;

import com.netthreads.gdx.app.layer.AboutLayer;
import com.netthreads.gdx.app.layer.ControlLayer;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.scene.Scene;

/**
 * About scene.
 * 
 */
public class AboutScene extends Scene
{
	private Layer controlLayer;
	private Layer aboutLayer;
	
	public AboutScene()
	{
		// ---------------------------------------------------------------
		// Control layer
		// ---------------------------------------------------------------
		controlLayer = new ControlLayer();
		getInputMultiplexer().addProcessor(controlLayer);
		addLayer(controlLayer);
		
		// ---------------------------------------------------------------
		// About layer
		// ---------------------------------------------------------------
		aboutLayer = new AboutLayer(getWidth(), getHeight());
		
		addLayer(aboutLayer);
	}
}

package com.netthreads.gdx.app.scene;

import com.netthreads.gdx.app.layer.ControlLayer;
import com.netthreads.gdx.app.layer.FpsLayer;
import com.netthreads.gdx.app.layer.main.MainLayer;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.scene.Scene;

/**
 * Main Application scene.
 * 
 */
public class MainScene extends Scene
{
	private Layer controlLayer;
	private Layer fpsLayer;
	private MainLayer mainLayer;

	/**
	 * Application scene.
	 * 
	 */
	public MainScene()
	{
		// ---------------------------------------------------------------
		// Control layer
		// ---------------------------------------------------------------
		controlLayer = new ControlLayer();

		getInputMultiplexer().addProcessor(controlLayer);

		addLayer(controlLayer);

		// ---------------------------------------------------------------
		// FPS layer.
		// ---------------------------------------------------------------
		fpsLayer = new FpsLayer(getWidth(), getHeight());

		addLayer(fpsLayer);

		// ---------------------------------------------------------------
		// Main layer.
		// ---------------------------------------------------------------
		mainLayer = new MainLayer(getWidth(), getHeight());

		// Route input events to layer.
		getInputMultiplexer().addProcessor(mainLayer);

		addLayer(mainLayer);

	}

	/**
	 * Dispose of stuff which might have been created and required for application but needs to be cleaned up now.
	 * 
	 */
	public void cleanup()
	{
		mainLayer.cleanupView();
	}
}

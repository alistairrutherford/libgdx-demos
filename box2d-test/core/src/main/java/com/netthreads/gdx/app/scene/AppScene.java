/*
 * -----------------------------------------------------------------------
 * Copyright 2012 - Alistair Rutherford - www.netthreads.co.uk
 * -----------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.netthreads.gdx.app.scene;

import com.netthreads.gdx.app.layer.ControlLayer;
import com.netthreads.gdx.app.layer.FpsLayer;
import com.netthreads.gdx.app.layer.LabelLayer;
import com.netthreads.gdx.app.layer.SimulationLayer;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.scene.Scene;

/**
 * Application scene.
 * 
 */
public class AppScene extends Scene
{
	private Layer controlLayer;
	private SimulationLayer simulationLayer;
	private Layer labelLayer;
	private Layer fpsLayer;
	
	/**
	 * Application scene.
	 * 
	 */
	public AppScene()
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
		// Label layer.
		// ---------------------------------------------------------------
		labelLayer = new LabelLayer(getWidth(), getHeight());
		
		// Route input events to layer.
		getInputMultiplexer().addProcessor(labelLayer);
		
		addLayer(labelLayer);
		
		// ---------------------------------------------------------------
		// Simulation layer.
		// ---------------------------------------------------------------
		simulationLayer = new SimulationLayer(getWidth(), getHeight());
		
		// Route input events to layer.
		getInputMultiplexer().addProcessor(labelLayer);
		
		addLayer(simulationLayer);
	}
	
	/**
	 * Dispose of stuff which might have been created and required for
	 * application but needs to be cleaned up now.
	 * 
	 */
	public void cleanup()
	{
		simulationLayer.cleanupView(true);
	}
}

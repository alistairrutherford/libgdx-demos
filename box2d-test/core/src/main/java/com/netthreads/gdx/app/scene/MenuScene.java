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

import com.netthreads.gdx.app.layer.MenuLayer;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.scene.Scene;

/**
 * Menu scene.
 * 
 */
public class MenuScene extends Scene
{
	private Layer menuLayer;

	public MenuScene()
	{
		// ---------------------------------------------------------------
		// Main menu layer.
		// ---------------------------------------------------------------
		menuLayer = new MenuLayer(getWidth(), getHeight());

		addLayer(menuLayer);
	}

}

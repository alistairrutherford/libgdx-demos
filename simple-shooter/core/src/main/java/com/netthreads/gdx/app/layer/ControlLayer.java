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

package com.netthreads.gdx.app.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.netthreads.gdx.app.definition.AppActorEvents;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Capture back/ESC key layer.
 * 
 */
public class ControlLayer extends Layer
{
	/**
	 * The one and only director.
	 */
	private Director director;

	
	/**
	 * Create layer.
	 * 
	 * @param stage
	 */
	public ControlLayer()
	{
		director = AppInjector.getInjector().getInstance(Director.class);
		
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Catch escape key.
	 * 
	 */
	@Override
	public boolean keyDown(int keycode)
	{
		boolean handled = false;

		if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
		{
			director.sendEvent(AppActorEvents.EVENT_TRANSITION_TO_MENU_SCENE, this);

			handled = true;
		}

		return handled;
	}

}

/*
 * -----------------------------------------------------------------------
 * Copyright 2014 - Alistair Rutherford - www.netthreads.co.uk
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

package com.netthreads.gdx.app.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.sprite.FrameSprite;

/**
 * Represent a pulse sprite.
 * 
 */
public class ExplosionSprite extends FrameSprite
{
	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Create sprite.
	 * 
	 * @param texture
	 *            The animation texture.
	 * @param rows
	 *            The animation texture rows.
	 * @param cols
	 *            The animation texture rows.
	 * @param frameDuration
	 *            The animation frame duration.
	 */
	public ExplosionSprite(TextureRegion textureRegion, int rows, int cols, float frameDuration)
	{
		super(textureRegion, rows, cols, frameDuration, false);
		
		// Since our Sprite objects  are pooled it is okay to have overhead of fetching singleton instance.
		director = AppInjector.getInjector().getInstance(Director.class);		
	}

	/**
	 * Draw sprite.
	 * 
	 * Handle detection of animation complete.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (isAnimationFinished())
		{
			// Ensure you have a handler for this or the event pool will fill
			// up.
			director.sendEvent(AppEvents.EVENT_END_EXPLOSION, this);
		}
	}

}

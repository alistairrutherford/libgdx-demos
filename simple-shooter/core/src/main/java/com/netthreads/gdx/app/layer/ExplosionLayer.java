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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.sprite.ExplosionSprite;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Scene layer.
 * 
 */
public class ExplosionLayer extends Layer implements ActorEventObserver
{
	private static final int MAX_SPRITES = 30;

	public static final float FRAME_DURATION = 0.05f;

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<ExplosionSprite> pool = new Pool<ExplosionSprite>(MAX_SPRITES)
	{
		@Override
		protected ExplosionSprite newObject()
		{
			return createSprite();
		}
	};


	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Singletons.
	 */
	private TextureCache textureCache;
	
	/**
	 * Create pulse group layer.
	 * 
	 * @param stage
	 */
	public ExplosionLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);
		
		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
	}

	/**
	 * Called when layer is part of visible view but not yet displayed.
	 * 
	 */
	@Override
	public void enter()
	{
		// Add this as an event observer.
		director.registerEventHandler(this);
	}

	/**
	 * Called when layer is no longer part of visible view.
	 * 
	 */
	@Override
	public void exit()
	{
		// Remove this as an event observer.
		director.deregisterEventHandler(this);
	}
	
	/**
	 * Create new sprite.
	 * 
	 * @return A new sprite.
	 */
	private ExplosionSprite createSprite()
	{
		TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_EXPLOSION);
		TextureRegion textureRegion = textureCache.getTexture(definition);

		ExplosionSprite sprite = new ExplosionSprite(textureRegion, definition.getRows(), definition.getCols(), FRAME_DURATION);

		return sprite;
	}

	/**
	 * Event handler will listen for pulse fire event and kick off pulse when
	 * one is received.
	 * 
	 * Event handler will listen for signal that pulse is finished and remove it
	 * from view.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case AppEvents.EVENT_START_ASTEROID_EXPLOSION:
			handleStart(event.getActor());
			handled = true;
			break;
		case AppEvents.EVENT_END_EXPLOSION:
			handleEnd(event.getActor());
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Add sprite to view.
	 * 
	 * @param source
	 *            The source actor.
	 */
	private void handleStart(Actor source)
	{
		// The starting position of pulse is source of 'fire pulse' i.e. our
		// ship.
		float x = source.getX();
		float y = source.getY();

		// Get free sprite from pool.
		ExplosionSprite sprite = pool.obtain();
		sprite.setX(x);
		sprite.setY(y);
		
		// Add to view.
		addActor(sprite);
		
		// Ensure we play-back from the start.
		sprite.resetAnimation();
	}

	/**
	 * Remove sprite from view.
	 * 
	 * @param source
	 *            The source actor.
	 */
	public void handleEnd(Actor source)
	{
		// Free this from pool so it can be re-used.
		pool.free((ExplosionSprite) source);

		// Remove from view.
		source.remove();
	}

}

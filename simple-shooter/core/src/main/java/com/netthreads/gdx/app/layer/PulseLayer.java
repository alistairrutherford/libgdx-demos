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
import com.badlogic.gdx.utils.SnapshotArray;
import com.netthreads.gdx.app.definition.AppActorEvents;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.sprite.PulseSprite;
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
public class PulseLayer extends Layer implements ActorEventObserver
{
	private static final int INITIAL_SPRITE_CAPACITY = 20;

	public static final float FRAME_DURATION = 0.2f;

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<PulseSprite> pool = new Pool<PulseSprite>(INITIAL_SPRITE_CAPACITY)
	{
		@Override
		protected PulseSprite newObject()
		{
			TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_PULSE);
			TextureRegion textureRegion = textureCache.getTexture(definition);

			PulseSprite sprite = new PulseSprite(textureRegion, definition.getRows(), definition.getCols(), FRAME_DURATION);

			return sprite;
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
	public PulseLayer(float width, float height)
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
		cleanup();

		// Remove this as an event observer.
		director.deregisterEventHandler(this);
	}

	/**
	 * Pooled layers need cleanup view elements.
	 * 
	 */
	private void cleanup()
	{
		SnapshotArray<Actor> list = this.getChildren();

		for (Actor actor : list)
		{
			this.removeActor(actor);
		}
	}

	/**
	 * Event handler will listen for pulse fire event and kick off pulse when one is received.
	 * 
	 * Event handler will listen for signal that pulse is finished and remove it from view.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case AppActorEvents.EVENT_START_PULSE:
			handleStartPulse(event.getActor());
			handled = true;
			break;
		case AppActorEvents.EVENT_END_PULSE:
			handleEndPulse(event.getActor());
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Launch a sprite from pool (if one available).
	 * 
	 * @param source
	 *            The source actor.
	 */
	private void handleStartPulse(Actor source)
	{
		// Get free sprite from pool.
		PulseSprite sprite = pool.obtain();

		// The starting position of pulse is source of 'fire pulse' i.e. our
		// ship.
		float x = source.getX() + source.getWidth() / 2 - sprite.getWidth() / 2;
		float y = source.getY() + source.getHeight() / 2;

		if ((x > 0 && y > 0) && (x < this.getWidth() && y < this.getHeight()))
		{
			// Add to view.
			addActor(sprite);

			// DO THIS AFTER ADDING TO VIEW.
			sprite.run(x, y);
		}
		else
		{
			pool.free(sprite);
		}
	}

	/**
	 * Handles pulse action complete.
	 * 
	 * @param source
	 *            The source actor.
	 */
	public void handleEndPulse(Actor source)
	{
		// This will trigger a "removeActor" call.
		this.removeActor(source);
	}

}

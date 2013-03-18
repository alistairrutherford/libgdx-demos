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
import com.netthreads.gdx.app.core.AppStats;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.sprite.AsteroidSprite;
import com.netthreads.libgdx.action.ActionCallBack;
import com.netthreads.libgdx.action.CallBackDelayAction;
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
public class AsteroidLayer extends Layer implements ActorEventObserver, ActionCallBack
{
	private static final int INITIAL_SPRITE_CAPACITY = 20;

	public static final float FRAME_DURATION = 0.2f; // Animation frame duration.
	private static final float CONTROL_INTERVAL = 0.75f; // How often to start.

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<AsteroidSprite> pool = new Pool<AsteroidSprite>(INITIAL_SPRITE_CAPACITY)
	{
		@Override
		protected AsteroidSprite newObject()
		{
			TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_ASTEROID);
			TextureRegion textureRegion = textureCache.getTexture(definition);

			AsteroidSprite sprite = new AsteroidSprite(textureRegion, definition.getRows(), definition.getCols(), AsteroidLayer.FRAME_DURATION);

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
	private AppStats appStats;

	/**
	 * Create asteroid layer.
	 * 
	 * @param stage
	 */
	public AsteroidLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);

		appStats = AppInjector.getInjector().getInstance(AppStats.class);

		buildActions();
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
		int size = getChildren().size;
		while (size > 0)
		{
			Actor actor = getChildren().get(--size);

			removeActor(actor);
		}

	}

	/**
	 * Create timer action to start layer elements.
	 * 
	 */
	private void buildActions()
	{
		CallBackDelayAction callBackDelay = CallBackDelayAction.$(-1, CONTROL_INTERVAL, this);

		addAction(callBackDelay);
	}

	/**
	 * Every delay duration make a decision about what to do next.
	 * 
	 */
	@Override
	public void onCallBack()
	{
		// Launch an asteroid.
		this.director.sendEvent(AppEvents.EVENT_START_ASTEROID, this);
	}

	/**
	 * Handle events.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case AppEvents.EVENT_START_ASTEROID:
			handleStartAsteroid();
			handled = true;
			break;
		case AppEvents.EVENT_COLLISION_ASTEROID_PULSE:
			handlePulseCollision(event.getActor());
			handled = true;
			break;
		case AppEvents.EVENT_END_ASTEROID:
			handleEndAsteroid(event.getActor());
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
	 */
	private void handleStartAsteroid()
	{
		// Get free sprite from pool.
		AsteroidSprite sprite = pool.obtain();

		// Add to view.
		addActor(sprite);

		// DO THIS AFTER ADDING TO VIEW.
		sprite.run();
	}

	/**
	 * Handle pulse hitting asteroid.
	 * 
	 * @param actor
	 */
	private void handlePulseCollision(Actor actor)
	{
		// Run explosion sprite
		this.director.sendEvent(AppEvents.EVENT_START_ASTEROID_EXPLOSION, actor);

		handleEndAsteroid(actor);

		// Update score.
		appStats.incScore();
	}

	/**
	 * Handle asteroid end.
	 * 
	 * @param event
	 *            The actor event.
	 */
	private void handleEndAsteroid(Actor source)
	{
		removeActor(source);
	}

	/**
	 * We override the removeActor to ensure we clear actions and re-pool item.
	 * 
	 */
	@Override
	public boolean removeActor(Actor actor)
	{
		super.removeActor(actor);

		actor.clearActions();

		pool.free((AsteroidSprite) actor);

		return true;
	}

}

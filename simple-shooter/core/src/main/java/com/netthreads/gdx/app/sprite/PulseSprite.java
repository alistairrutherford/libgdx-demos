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

package com.netthreads.gdx.app.sprite;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.libgdx.action.TweenAction;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.sprite.FrameSprite;
import com.netthreads.libgdx.tween.ActorAccessor;

/**
 * Represent a pulse sprite.
 * 
 */
public class PulseSprite extends FrameSprite implements TweenCallback
{
	private static final float PIXELS_PER_SEC_FACTOR = 1 / 250.0f;

	private TweenAction tweenAction;

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
	public PulseSprite(TextureRegion textureRegion, int rows, int cols, float frameDuration)
	{
		super(textureRegion, rows, cols, frameDuration, true);
		
		// Since our Sprite objects  are pooled it is okay to have overhead of fetching singleton instance.
		director = AppInjector.getInjector().getInstance(Director.class);		
	}

	/**
	 * Run actions for actor.
	 * 
	 * ALWAYS DO THIS AFTER ADDING TO VIEW.
	 * 
	 * @param x
	 * @param y
	 * 
	 * @return The main actions object.
	 */
	public void run(float x, float y)
	{
		// Set initial position
		setPosition(x, y);

		// Calculate the duration to cover distance at pixels-per-second.
		float height = director.getHeight();
		float distance = height - y;
		int duration = (int) (PIXELS_PER_SEC_FACTOR * distance * 1000);

		// Note it is NOT associated with the TweenManager.
		Tween tween = Tween.to(this, ActorAccessor.POSITION_XY, duration)
							.target(x, height).ease(Quad.OUT)
							.setCallbackTriggers(TweenCallback.COMPLETE)
							.setCallback(this)
							.start();
		tweenAction = TweenAction.$(tween);

		addAction(tweenAction);
	}

	/**
	 * Handles pulse action complete.
	 * 
	 * Technically we should test the 'eventType' but we know it is always 'complete'.
	 */
	@Override
	public void onEvent(int eventType, BaseTween<?> source)
	{
		// Send notification that pulse has completed.
		director.sendEvent(AppEvents.EVENT_END_PULSE, this);
	}

}

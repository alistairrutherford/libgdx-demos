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
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppStates;
import com.netthreads.libgdx.action.ActionCallBack;
import com.netthreads.libgdx.action.CallBackDelayAction;
import com.netthreads.libgdx.action.TimelineAction;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.SceneHelper;
import com.netthreads.libgdx.sprite.AnimatedSprite;
import com.netthreads.libgdx.tween.ActorAccessor;
import com.netthreads.libgdx.tween.GroupAccessor;

/**
 * Represent an asteroid sprite.
 * 
 * An asteroid rotates at a random speed as it moves from it's starting point to the bottom.
 * 
 * An asteroid checks for collision with other actors and generates the appropriate event.
 * 
 */
public class AsteroidSprite extends AnimatedSprite implements TweenCallback, ActionCallBack
{
	private static final int MIN_DURATION = 2;
	private static final int MAX_DURATION = 10;
	private static final float CALLBACK_INTERVAL = 0.05f; // 50 milliseconds

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
	public AsteroidSprite(TextureRegion textureRegion, int rows, int cols, float frameDuration)
	{
		super(textureRegion, rows, cols, frameDuration);

		// Since our Sprite objects  are pooled it is okay to have overhead of fetching singleton instance.
		director = AppInjector.getInjector().getInstance(Director.class);
		
		// Move origin to the centre so that rotation works from there.
		setOriginX(getWidth() / 2);
		setOriginY(getHeight() / 2);
	}

	/**
	 * Run actions for actor.
	 * 
	 * 
	 * @return The main actions object.
	 */
	public void run()
	{
		float width = director.getWidth();
		float height = director.getHeight();
		float randomX = (float) (Math.random() * width);

		// From the top and random point across the width.
		this.setX(randomX);
		this.setY(height);

		// ---------------------------------------------------------------
		// Animation.
		// ---------------------------------------------------------------
		int durationMove = (int) ((Math.random() * MAX_DURATION + MIN_DURATION) * 1000);
		int rotate = (int) (Math.random() * 1440) - 720;

		Timeline timeline = Timeline.createParallel()
				.beginParallel()
					.push(Tween.to(this, ActorAccessor.POSITION_XY, durationMove).setCallbackTriggers(TweenCallback.BEGIN).target(getX(), 0 - this.getHeight()).ease(Linear.INOUT))
					.beginSequence()
						.push(Tween.to(this, GroupAccessor.ROTATION, 0).target(0))
						.push(Tween.to(this, GroupAccessor.ROTATION, durationMove).target(rotate))
					.end()
				.end()
				.setCallbackTriggers(TweenCallback.COMPLETE)
				.setCallback(this)
				.start();

		TimelineAction timelineAction = TimelineAction.$(timeline);

		addAction(timelineAction);

		// ---------------------------------------------------------------
		// CallBack
		// ---------------------------------------------------------------
		CallBackDelayAction callBackDelay = CallBackDelayAction.$(-1, CALLBACK_INTERVAL, this);

		addAction(callBackDelay);
	}

	/**
	 * Action callback.
	 * 
	 */
	@Override
	public void onCallBack()
	{
		handleCheck();
	}

	/**
	 * Handles pulse action complete.
	 * 
	 */
	@Override
	public void onEvent(int eventType, BaseTween<?> source)
	{
		switch (eventType)
		{
		case BEGIN:
			this.setRotation(0);
			break;
		case COMPLETE:
			handleComplete();
			break;
		default:
			break;
		}
	}

	/**
	 * Handles pulse action complete.
	 * 
	 */
	private void handleComplete()
	{
		// Send notification that pulse has completed.
		this.director.sendEvent(AppEvents.EVENT_END_ASTEROID, this);
	}

	/**
	 * Triggered on call back action.
	 * 
	 */
	private void handleCheck()
	{
		// Check for collision with other actor.
		Actor target = collisionCheck();

		if (target != null)
		{
			if (target instanceof ShipSprite)
			{
				// Don't run collision or anything else if target is busy.
				if (!AppStates.shipSpriteBusy)
				{
					// Ship collision.
					this.director.sendEvent(AppEvents.EVENT_COLLISION_ASTEROID_SHIP, this);
				}
			}
			else if (target instanceof PulseSprite)
			{
				// Asteroid explode.
				this.director.sendEvent(AppEvents.EVENT_COLLISION_ASTEROID_PULSE, this);

				// Pulse finished.
				this.director.sendEvent(AppEvents.EVENT_END_PULSE, target);
			}
		}
	}

	/**
	 * Check for collision with another actor on the stage.
	 * 
	 * @return Target object if collision or null if none.
	 */
	private Actor collisionCheck()
	{
		Actor actor = null;

		Stage stage = getStage();

		if (stage != null)
		{
			// Have we hit a pulse?
			actor = SceneHelper.intersects(this.getX(), this.getY(), this.getWidth(), this.getHeight(), stage.getRoot(), PulseSprite.class);

			// Have we hit the ship?
			if (actor == null)
			{
				actor = SceneHelper.intersects(this.getX(), this.getY(), this.getWidth(), this.getHeight(), stage.getRoot(), ShipSprite.class);
			}
		}

		return actor;
	}

	/**
	 * Override the hit method here to ensure our collision detection helper class {@link SceneHelper} can make a match
	 * against an actual game element like this Asteroid class. I.e if you call the super-class method you will get a
	 * match against a FrameSprite which isn't what we want.
	 */
	@Override
	public Actor hit(float x, float y, boolean touchable)
	{
		return x > 0 && x < getWidth()&& y > 0 && y < getHeight()? this : null;
	}
}

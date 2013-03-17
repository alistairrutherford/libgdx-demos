package com.netthreads.gdx.app.layer.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppSounds;
import com.netthreads.gdx.app.properties.AppProperties;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.sound.SoundCache;

/**
 * An element which will give any body which come into contact a 'kick'.
 * 
 */
public class DisplayContactElement extends WorldElement
{
	private AppProperties appProperties;
	private SoundCache soundCache;
	private Director director;

	private Actor actor;

	/**
	 * Construct element.
	 * 
	 * @param body
	 */
	public DisplayContactElement()
	{
		director = AppInjector.getInjector().getInstance(Director.class);

		appProperties = AppInjector.getInjector().getInstance(AppProperties.class);

		soundCache = AppInjector.getInjector().getInstance(SoundCache.class);

		actor = null;
	}

	public Actor getActor()
	{
		return actor;
	}

	public void setActor(Actor actor)
	{
		this.actor = actor;
	}

	/**
	 * Handle bumper collision.
	 * 
	 * @param collisionBody
	 *            The colliding body.
	 */
	@Override
	public void handleCollision(Body collisionBody)
	{

		float pointX = collisionBody.getWorldCenter().x;
		float pointY = collisionBody.getWorldCenter().y;

		if (collisionBody.getUserData() != null && collisionBody.getUserData() instanceof Actor)
		{
			// Override in super-class
			Gdx.app.log("DisplayContactElement", "x:" + pointX + ", y:" + pointY);

			// Dampen!
			collisionBody.applyAngularImpulse(0);

			// Play clonk.
			if (appProperties.isAudioOn())
			{
				soundCache.get(AppSounds.SOUND_CLONK).play(appProperties.getVolume());
			}

			// Flare
			if (actor != null)
			{
				director.sendEvent(AppEvents.EVENT_POINTER_FLARE, actor);
			}
		}
	}
}

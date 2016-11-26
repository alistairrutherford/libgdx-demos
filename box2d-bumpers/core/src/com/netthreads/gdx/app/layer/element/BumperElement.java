package com.netthreads.gdx.app.layer.element;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.netthreads.gdx.app.definition.AppSounds;
import com.netthreads.gdx.app.properties.AppProperties;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.sound.SoundCache;

/**
 * An element which will give any body which come into contact a 'kick'.
 * 
 */
public class BumperElement extends WorldElement
{
	private static float IMPULSE_BUMPER = 10.0f;

	private Vector2 impulse;
	private float centreX;
	private float centreY;

	private AppProperties appProperties;
	private SoundCache soundCache;

	/**
	 * Construct bumper.
	 * 
	 * @param body
	 * @param centreX
	 * @param centreY
	 */
	public BumperElement(Body body, float centreX, float centreY)
	{
		super(body);

		this.centreX = centreX;
		this.centreY = centreY;

		appProperties = AppInjector.getInjector().getInstance(AppProperties.class);
		soundCache = AppInjector.getInjector().getInstance(SoundCache.class);

		impulse = new Vector2(IMPULSE_BUMPER, IMPULSE_BUMPER);
	}

	/**
	 * Calculate impulse for 'kick'. This is pretty experimental but the idea is that the further away from the centre
	 * of the bumper the greater the kick.
	 * 
	 * @param collisionBody
	 *            The colliding body.
	 */
	private void impulseForBody(Body collisionBody)
	{
		Vector2 bodyPos = collisionBody.getWorldCenter();

		float ix = Math.abs(centreX - bodyPos.x);
		float iy = Math.abs(centreY - bodyPos.y);

		float hyp = (float) Math.sqrt(ix * ix + iy * iy);

		Vector2 linearVelocity = collisionBody.getLinearVelocity();

		if (linearVelocity.x < IMPULSE_BUMPER)
		{
			impulse.x = linearVelocity.x + hyp;
		}

		if (linearVelocity.y < IMPULSE_BUMPER)
		{
			impulse.y = linearVelocity.y + hyp;
		}
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
		float impulseX = impulse.x;
		float impulseY = impulse.y;

		float pointX = collisionBody.getWorldCenter().x;
		float pointY = collisionBody.getWorldCenter().y;

		impulseForBody(collisionBody);

		// Kick!
		collisionBody.applyLinearImpulse(impulseX, impulseY, pointX, pointY, true);

		// Dampen!
		// collisionBody.applyAngularImpulse(0);

		// Play clink.
		if (appProperties.isAudioOn())
		{
			soundCache.get(AppSounds.SOUND_CLINK).play(appProperties.getVolume());
		}
	}
}

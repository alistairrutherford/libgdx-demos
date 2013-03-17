package com.netthreads.gdx.app.layer.main;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.layer.ManagedLayer;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Box2D Touch layer
 * 
 */
public class TouchLayer extends Layer implements ManagedLayer
{
	private World world;
	private float pixelsPerMetre;

	// Touch handling.
	private MouseJoint mouseJoint = null;
	private Body groundBody;
	private HitInfo hitInfo;

	// The one and only director.
	private Director director;

	private boolean sendTouchDown = false;
	private boolean sendTouchDragged = false;
	private boolean sendTouchUp = false;

	// -------------------------------------------------------------------
	// Touch hit info.
	// -------------------------------------------------------------------
	private class HitInfo
	{
		public Body hitBody = null;

		public final Vector2 worldXY;

		public HitInfo()
		{
			worldXY = new Vector2();
		}

		/**
		 * Reset hit info.
		 */
		public void reset()
		{
			hitBody = null;
			worldXY.set(0.0f, 0.0f);
		}

		/**
		 * Return true if body is valid hit.
		 * 
		 * @return True if valid hit.
		 */
		public boolean validHit()
		{
			boolean valid = false;
			if (hitBody != null && hitBody.getType() != BodyType.KinematicBody)
			{
				valid = true;
			}

			return valid;
		}
	}

	/**
	 * Construct layer.
	 * 
	 * @param width
	 * @param height
	 */
	public TouchLayer(float width, float height, World world, float pixelsPerMetre)
	{
		setWidth(width);
		setHeight(height);

		this.world = world;
		this.pixelsPerMetre = pixelsPerMetre;

		// Tracks hit/touch info.
		hitInfo = new HitInfo();

		director = AppInjector.getInjector().getInstance(Director.class);

		// Ground body.
		BodyDef bodyDef = new BodyDef();
		groundBody = world.createBody(bodyDef);
	}

	@Override
	public void cleanupView(boolean all)
	{
		hitInfo.reset();
		mouseJoint = null;
	}

	// -------------------------------------------------------------------
	// Collision detection and touch handling.
	// -------------------------------------------------------------------

	/**
	 * Handler for body hit.
	 * 
	 * @param body
	 */
	private void generateMouseJoint(HitInfo hitInfo)
	{
		MouseJointDef def = new MouseJointDef();
		def.bodyA = groundBody;
		def.bodyB = hitInfo.hitBody;
		def.collideConnected = true;
		def.target.set(hitInfo.worldXY.x, hitInfo.worldXY.y);
		def.maxForce = 1000.0f * hitInfo.hitBody.getMass();

		mouseJoint = (MouseJoint) world.createJoint(def);
		hitInfo.hitBody.setAwake(true);
	}

	/**
	 * Collision detection callback for world.
	 * 
	 */
	private QueryCallback callback = new QueryCallback()
	{
		@Override
		public boolean reportFixture(Fixture fixture)
		{
			boolean notFound = true;

			// if the hit point is inside the fixture of the body
			// we report it
			if (fixture.testPoint(hitInfo.worldXY.x, hitInfo.worldXY.y))
			{
				hitInfo.hitBody = fixture.getBody();
				notFound = false;
			}

			return notFound;
		}
	};

	/**
	 * Handle touch down.
	 * 
	 * @param x
	 *            The x coordinate, origin is in the upper left corner
	 * @param y
	 *            The y coordinate, origin is in the upper left corner pointer the pointer for the event.
	 * @param newParam
	 *            the button
	 */
	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		hitInfo.reset();

		// Convert touch coordinates to world coordinates. Note, in Box2D the y
		// axis is around the other way.
		hitInfo.worldXY.x = x / pixelsPerMetre;
		hitInfo.worldXY.y = (getHeight() - y) / pixelsPerMetre;

		// Ask the world which bodies are within the given bounding box around
		// the mouse pointer.
		world.QueryAABB(callback, hitInfo.worldXY.x - 0.1f, hitInfo.worldXY.y - 0.1f, hitInfo.worldXY.x + 0.1f, hitInfo.worldXY.y + 0.1f);

		if (hitInfo.validHit())
		{
			generateMouseJoint(hitInfo);

			if (isSendTouchDown())
			{
				Actor actor = (Actor) mouseJoint.getBodyB().getUserData();
				director.sendEvent(AppEvents.EVENT_BODY_TOUCH_DOWN, actor);
			}
		}

		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		if (mouseJoint != null)
		{
			hitInfo.worldXY.x = x / pixelsPerMetre;
			hitInfo.worldXY.y = (getHeight() - y) / pixelsPerMetre;
			mouseJoint.setTarget(hitInfo.worldXY);

			if (isSendTouchDragged())
			{
				Actor actor = (Actor) mouseJoint.getBodyB().getUserData();
				director.sendEvent(AppEvents.EVENT_BODY_TOUCH_DRAGGED, actor);
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		// if a mouse joint exists we simply destroy it
		if (mouseJoint != null)
		{
			if (isSendTouchUp())
			{
				Actor actor = (Actor) mouseJoint.getBodyB().getUserData();
				director.sendEvent(AppEvents.EVENT_BODY_TOUCH_UP, actor);
			}

			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}

		return false;
	}

	/**
	 * @return
	 */
	public boolean isSendTouchDown()
	{
		return sendTouchDown;
	}

	/**
	 * @param sendTouchDown
	 */
	public void setSendTouchDown(boolean sendTouchDown)
	{
		this.sendTouchDown = sendTouchDown;
	}

	/**
	 * @return
	 */
	public boolean isSendTouchDragged()
	{
		return sendTouchDragged;
	}

	/**
	 * @param sendTouchDragged
	 */
	public void setSendTouchDragged(boolean sendTouchDragged)
	{
		this.sendTouchDragged = sendTouchDragged;
	}

	/**
	 * @return
	 */
	public boolean isSendTouchUp()
	{
		return sendTouchUp;
	}

	/**
	 * @param sendTouchUp
	 */
	public void setSendTouchUp(boolean sendTouchUp)
	{
		this.sendTouchUp = sendTouchUp;
	}

}

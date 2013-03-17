package com.netthreads.gdx.app.layer.element;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * World element.
 * 
 */
public class WorldElement
{
	private Body body;
	private int contactCount;

	/**
	 * Create element with associated body.
	 * 
	 */
	public WorldElement()
	{
		this.body = null;
	}
	
	/**
	 * Create element with associated body.
	 * 
	 * @param body
	 */
	public WorldElement(Body body)
	{
		this.body = body;
	}

	/**
	 * Set associated body.
	 * 
	 * @param body
	 */
	public void setBody(Body body)
    {
    	this.body = body;
    }

	/**
	 * Return element body.
	 * 
	 * @return The element body.
	 */
	public Body getBody()
	{
		return body;
	}

	public void handleBeginContact()
	{
		contactCount++;
	}

	public void handleEndContact()
	{
		contactCount--;
	}

	public int getContactCount()
	{
		return contactCount;
	}

	/**
	 * Handle collision.
	 * 
	 * @param collisionBody
	 */
	public void handleCollision(Body collisionBody)
	{
		// Override in super-class
	}
}

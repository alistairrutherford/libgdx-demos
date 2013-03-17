package com.netthreads.gdx.app.layer.element;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Contact Listener
 * 
 */
public class WorldElementContactListener implements ContactListener
{
	private List<WorldElement> elements;

	public WorldElementContactListener()
	{
		elements = new LinkedList<WorldElement>();
	}

	public void addElement(WorldElement element)
	{
		elements.add(element);
	}

	public void removeElement(WorldElement element)
	{
		elements.remove(element);
	}

	@Override
	public void beginContact(Contact contact)
	{
		handleBeginContact(contact);
	}

	@Override
	public void endContact(Contact contact)
	{
		// Nothing.
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// Nothing.
	}

	/**
	 * Process contact.
	 * 
	 * @param contact
	 */
	private void handleBeginContact(Contact contact)
	{
		int size = elements.size();
		for (int i = 0; i < size; i++)
		{
			WorldElement element = elements.get(i);

			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();

			Body elementBody = element.getBody();

			// Collision normal normal points from fixtureA to fixtureB.
			if (bodyA == elementBody)
			{
				element.handleCollision(bodyB);
			}
			else if (bodyB == elementBody)
			{
				element.handleCollision(bodyA);
			}
		}
	}
}
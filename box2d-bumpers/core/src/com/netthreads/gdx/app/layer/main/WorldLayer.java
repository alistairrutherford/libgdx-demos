package com.netthreads.gdx.app.layer.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.netthreads.gdx.app.definition.AppSize;
import com.netthreads.gdx.app.layer.ManagedLayer;
import com.netthreads.gdx.app.layer.element.BumperElement;
import com.netthreads.gdx.app.layer.element.WorldElementContactListener;
import com.netthreads.libgdx.scene.Layer;

/**
 * Main Box2D layer.
 * 
 */
public class WorldLayer extends Layer implements ManagedLayer
{
	public static final float WIDTH_WALL = 0.1f;

	private static final float GRAVITY_X = 0.0f;
	private static final float GRAVITY_Y = -1.0f;

	// The Box2D world.
	private World world;

	// Conversion factor.
	private float pixelsPerMetre;

	// Flag to indicate world is still rendering.
	private boolean running = false;

	// Element collision handling.
	private WorldElementContactListener contactListener;

	/**
	 * Construct layer.
	 * 
	 * @param width
	 * @param height
	 */
	public WorldLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		// Seems to be sufficient.
		pixelsPerMetre = getWidth() / AppSize.WORLD_SIZE.x;

		createView();
	}

	/**
	 * Called when layer is part of visible view but not yet displayed.
	 * 
	 */
	@Override
	public void enter()
	{
		running = true;
	}

	/**
	 * Called when layer is no longer part of visible view.
	 * 
	 */
	@Override
	public void exit()
	{
		running = false;
	}

	/**
	 * Build Box2D world.
	 * 
	 */
	public void createView()
	{
		// create the world
		world = new World(new Vector2(GRAVITY_X, GRAVITY_Y), true);

		contactListener = new WorldElementContactListener();

		createWallModels();
	}

	/**
	 * Cleanup view elements. This gets called when the layer exits to remove non-static elements and it also gets
	 * called when the application exits to clean up the Box2D world associated with the layer.
	 * 
	 */
	@Override
	public void cleanupView()
	{
		// NOTE WE DO THE MAIN DISPOSE 
	}

	/**
	 * Draw layer contents.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (running)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		}
	}

	/**
	 * Create walls in the view.
	 * 
	 */
	private void createWallModels()
	{
		// ---------------------------------------------------------------
		// Bottom
		// ---------------------------------------------------------------

		Body wall = addWall(0, 0, getWidth(), WIDTH_WALL);

		float centreX = getWidth() / 2;
		float centreY = WIDTH_WALL / 2;
		contactListener.addElement(new BumperElement(wall, centreX, centreY));

		// ---------------------------------------------------------------
		// Left
		// ---------------------------------------------------------------
		wall = addWall(0, 0, WIDTH_WALL, getHeight());

		centreX = WIDTH_WALL / 2;
		centreY = getHeight() / 2;
		contactListener.addElement(new BumperElement(wall, centreX, centreY));

		// ---------------------------------------------------------------
		// Right
		// ---------------------------------------------------------------
		wall = addWall(getWidth() - WIDTH_WALL, 0, WIDTH_WALL, getHeight());

		centreX = getWidth() - (WIDTH_WALL / 2);
		centreY = getHeight() / 2;
		contactListener.addElement(new BumperElement(wall, centreX, centreY));

		// ---------------------------------------------------------------
		// Top
		// ---------------------------------------------------------------
		wall = addWall(0, getHeight() - WIDTH_WALL, getWidth(), WIDTH_WALL);

		centreX = getWidth() / 2;
		centreY = getHeight() - (WIDTH_WALL / 2);
		contactListener.addElement(new BumperElement(wall, centreX, centreY));

		// Add contact listener to do something is wall is hit.
		world.setContactListener(contactListener);
	}

	/**
	 * Add wall to model.
	 * 
	 * @param x
	 *            The bottom left hand corner of wall.
	 * @param y
	 *            The bottom left hand corner of wall.
	 * @param width
	 *            The wall width.
	 * @param height
	 *            The wall height.
	 */
	private Body addWall(float x, float y, float width, float height)
	{
		PolygonShape wallShape = new PolygonShape();

		float halfWidth = (width / pixelsPerMetre) / 2;
		float halfHeight = (height / pixelsPerMetre) / 2;
		wallShape.setAsBox(halfWidth, halfHeight);

		FixtureDef wallFixture = new FixtureDef();
		wallFixture.density = 1;
		wallFixture.friction = 1;
		wallFixture.restitution = 0.5f;
		wallFixture.shape = wallShape;

		BodyDef wallBodyDef = new BodyDef();
		wallBodyDef.type = BodyType.StaticBody;

		float posX = x / pixelsPerMetre;
		float posY = y / pixelsPerMetre;
		wallBodyDef.position.set(posX + halfWidth, posY + halfHeight);

		Body wall = world.createBody(wallBodyDef);
		wall.createFixture(wallFixture);

		return wall;
	}

	/**
	 * Return pixels to metre conversion factor.
	 * 
	 * @return The pixels to metre conversion factor.
	 */
	public float getPixelsPerMetre()
	{
		return pixelsPerMetre;
	}

	/**
	 * Return world reference.
	 * 
	 * @return The Box2D world reference.
	 */
	public World getWorld()
	{
		return world;
	}

	/**
	 * Contact listener for world elements.
	 * 
	 * @return The element contact listener.
	 */
	public WorldElementContactListener getContactListener()
	{
		return contactListener;
	}

}

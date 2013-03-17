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

import java.util.Random;

import aurelienribon.box2deditor.FixtureAtlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.sprite.BallSprite;
import com.netthreads.libgdx.action.BodyUpdateAction;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.sprite.SimpleSprite;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Simulation layer.
 * 
 */
public class SimulationLayer extends Layer implements ActorEventObserver
{
	private static final Vector2 WORLD_SIZE = new Vector2(10, 15);
	private static final Vector2 CENTRE_BODY_SIZE = new Vector2(8, 8);
	private static final Vector2 BALL_BODY_SIZE = new Vector2(0.4f, 0.4f);

	private static final int BALL_POOL = 20;

	private float pixelsPerMetre;

	private World world;
	private Body fixedBody;
	private Body centreBody;

	private final Random rand = new Random();
	private final Vector2 tmpVec = new Vector2();

	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Singletons.
	 */
	private TextureCache textureCache;

	private boolean running = false;

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<BallSprite> pool = new Pool<BallSprite>(BALL_POOL)
	{
		@Override
		protected BallSprite newObject()
		{
			TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_BALL);
			TextureRegion textureRegion = textureCache.getTexture(definition);

			BallSprite sprite = new BallSprite(textureRegion);

			return sprite;
		}
	};

	/**
	 * Create pulse group layer.
	 * 
	 * @param stage
	 */
	public SimulationLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		// Uhmmmmmm....hmmmmmmm...
		pixelsPerMetre = getWidth() / WORLD_SIZE.x;

		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);

		createView();
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

		this.running = true;
	}

	/**
	 * Called when layer is no longer part of visible view.
	 * 
	 */
	@Override
	public void exit()
	{
		this.running = false;

		cleanupView(false);

		// Remove this as an event observer.
		director.deregisterEventHandler(this);
	}

	/**
	 * Build View elements.
	 * 
	 */
	private void createView()
	{
		// create the world
		world = new World(new Vector2(0, -5), true);

		createWallModels();

		createCentralModel();

		createCentralSprite();
	}

	/**
	 * Cleanup view elements. This gets called when the layer exits to remove non-static elements and it also gets
	 * called when the application exits to clean up the Box2D world associated with the layer.
	 * 
	 */
	public void cleanupView(boolean all)
	{
		SnapshotArray<Actor> list = getChildren();

		for (Actor actor : list)
		{
			if (all)
			{
				removeActor(actor);
			}
			else
			{
				if (actor instanceof BallSprite)
				{
					removeActor(actor);
				}
			}
		}

		if (all)
		{
			world.dispose();
		}
	}

	/**
	 * Draw layer contents.
	 * 
	 */
	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (this.running)
		{
			world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		}
	}

	/**
	 * Handle actor being removed from layer.
	 * 
	 * NOTE: We can call clearActions from here as it is not called from within an action.
	 * 
	 * @return Success.
	 * 
	 */
	@Override
	public boolean removeActor(Actor actor)
	{
		super.removeActor(actor);

		// This will call 'finish' on Actor associated actions.
		SimpleSprite sprite = (SimpleSprite) actor;
		sprite.clearActions();

		if (actor instanceof BallSprite)
		{
			BallSprite ballSprite = (BallSprite) sprite;
			pool.free(ballSprite);
		}

		return true;
	}

	/**
	 * Create model in view.
	 * 
	 */
	private void createCentralModel()
	{
		// Create a FixtureAtlas which will automatically load the fixture
		// list for every body defined with the editor.
		FixtureAtlas atlas = new FixtureAtlas(Gdx.files.internal("data/drawing.bin"));

		// DEFINE CENTRAL BODY
		BodyDef centralBodyDef = new BodyDef();

		// PIN BODY IN CENTRE
		centralBodyDef.type = BodyType.StaticBody;
		float centreX = (getWidth() / pixelsPerMetre) / 2;
		float centreY = (getHeight() / pixelsPerMetre) / 2;
		centralBodyDef.position.set(centreX, centreY);
		centralBodyDef.fixedRotation = false;
		fixedBody = world.createBody(centralBodyDef);

		// PIN FIXTURE
		CircleShape pivot = new CircleShape();
		pivot.setRadius(CENTRE_BODY_SIZE.x / 2 - 0.9f);
		fixedBody.createFixture(pivot, 0.1f);
		pivot.dispose();

		// BODY WHICH WE WILL CONNECT TO CENTRAL PIN BODY
		centralBodyDef.type = BodyType.DynamicBody;
		float x = centreX - CENTRE_BODY_SIZE.x / 2;
		float y = centreY - CENTRE_BODY_SIZE.y / 2;
		centralBodyDef.position.set(x, y);
		centralBodyDef.fixedRotation = false;
		centreBody = world.createBody(centralBodyDef);

		// Fixture definition is required to make joint work.
		FixtureDef fd = new FixtureDef();
		fd.friction = 0.6f;
		fd.density = 2.0f;

		// CENTRAL FIXTURE
		atlas.createFixtures(centreBody, "cogA.png", CENTRE_BODY_SIZE.x, CENTRE_BODY_SIZE.y, fd);

		// JOINT
		RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
		revoluteJointDef.initialize(fixedBody, centreBody, fixedBody.getWorldCenter());

		revoluteJointDef.enableMotor = true;
		revoluteJointDef.motorSpeed = 1.0f;
		revoluteJointDef.maxMotorTorque = 500;

		world.createJoint(revoluteJointDef);
	}

	/**
	 * Create model sprite.
	 * 
	 */
	private void createCentralSprite()
	{
		TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_COG);
		TextureRegion textureRegion = textureCache.getTexture(definition);
		textureRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		SimpleSprite sprite = new SimpleSprite(textureRegion);

		Vector2 centrePos = centreBody.getPosition();
		sprite.setX(centrePos.x * pixelsPerMetre);
		sprite.setY(centrePos.y * pixelsPerMetre);
		sprite.setScaleX((CENTRE_BODY_SIZE.x * pixelsPerMetre) / sprite.getWidth());
		sprite.setScaleY((CENTRE_BODY_SIZE.y * pixelsPerMetre) / sprite.getHeight());

		addActor(sprite);

		// -----------------------
		// Create Action
		// -----------------------
		BodyUpdateAction updateAction = BodyUpdateAction.$(world, centreBody, pixelsPerMetre, false);

		// -----------------------
		// Run action on actor.
		// -----------------------
		sprite.addAction(updateAction);
	}

	/**
	 * Create walls in the view.
	 * 
	 */
	private void createWallModels()
	{
		addWall(0, 0, getWidth(), 0.1f);
		addWall(0, 0, 0.1f, getHeight());
		addWall(getWidth() - 0.1f, 0, getWidth() - 0.1f, getHeight());
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
	private void addWall(float x, float y, float width, float height)
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
		case AppEvents.EVENT_DROP_SHAPE:
			Actor actor = event.getActor();
			handleDropShape(actor.getX(), actor.getY());
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Drop ball into centre.
	 * 
	 */
	private void handleDropShape(float x, float y)
	{

		// Assign random starting position and angle.
		float tx = x / pixelsPerMetre;
		float ty = y / pixelsPerMetre;
		float angle = rand.nextFloat() * MathUtils.PI * 2;

		// -----------------------
		// Body
		// -----------------------
		BodyDef ballBodyDef = new BodyDef();
		ballBodyDef.position.set(tx, ty);
		ballBodyDef.angle = angle;
		ballBodyDef.type = BodyType.DynamicBody;

		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(BALL_BODY_SIZE.x / 2);

		Body body = world.createBody(ballBodyDef);
		body.createFixture(ballShape, 1);
		body.setUserData(BALL_BODY_SIZE);

		// Properties
		body.setActive(true);
		body.setAwake(true);
		body.setLinearVelocity(tmpVec.set(0, 0));
		body.setAngularVelocity(0);

		// -----------------------
		// Get sprite.
		// -----------------------
		SimpleSprite sprite = pool.obtain();

		// Size.
		sprite.setScaleX((BALL_BODY_SIZE.x * pixelsPerMetre) / sprite.getWidth());
		sprite.setScaleY((BALL_BODY_SIZE.y * pixelsPerMetre) / sprite.getHeight());

		// Centre of rotation.
		sprite.setOriginX(sprite.getWidth() / 2);
		sprite.setOriginY(sprite.getHeight() / 2);

		// -----------------------
		// Add actor to the view.
		// -----------------------
		addActor(sprite);

		// -----------------------
		// Create Action
		// -----------------------
		BodyUpdateAction updateAction = BodyUpdateAction.$(world, body, pixelsPerMetre, true);

		// -----------------------
		// Run action on actor.
		// -----------------------
		sprite.addAction(updateAction);
	}

}

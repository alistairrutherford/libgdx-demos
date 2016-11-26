package com.netthreads.gdx.app.layer.main;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppSize;
import com.netthreads.gdx.app.definition.AppTextures;
import com.netthreads.gdx.app.layer.ManagedLayer;
import com.netthreads.gdx.app.layer.element.DisplayContactElement;
import com.netthreads.gdx.app.layer.element.WorldElementContactListener;
import com.netthreads.gdx.app.sprite.LabeledSprite;
import com.netthreads.libgdx.action.BodyGravityAction;
import com.netthreads.libgdx.action.BodyUpdateAction;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Tumbling elements.
 * 
 */
public class ElementLayer extends Layer implements ManagedLayer, ActorEventObserver
{
	public static final float LIGHTER_THAN_AIR_GRAVITY_Y = 30.0f;
	public static final float WIDTH_WALL = 0.1f;
	private static final int ELEMENT_COUNT = 4;
	private static final int ITEM_POOL = 20;

	private World world;
	private float pixelsPerMetre;
	private WorldElementContactListener contactListener;

	private Random randomGenerator = new Random();

	/**
	 * Singletons.
	 */
	private TextureCache textureCache;

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<LabeledSprite> itemPool = new Pool<LabeledSprite>(ITEM_POOL)
	{
		@Override
		protected LabeledSprite newObject()
		{
			TextureDefinition definition = textureCache.getDefinition(AppTextures.TEXTURE_BLOCK_B);
			TextureRegion textureRegion = textureCache.getTexture(definition);

			LabeledSprite sprite = new LabeledSprite(textureRegion);

			return sprite;
		}
	};

	// -------------------------------------------------------------------
	// Collision handler object pool.
	// -------------------------------------------------------------------
	private Pool<DisplayContactElement> worldElementPool = new Pool<DisplayContactElement>(ITEM_POOL)
	{
		@Override
		protected DisplayContactElement newObject()
		{
			DisplayContactElement contactElement = new DisplayContactElement();

			return contactElement;
		}
	};

	// The one and only director.
	private Director director;

	/**
	 * Construct layer.
	 * 
	 * @param width
	 * @param height
	 */
	public ElementLayer(float width, float height, World world, float pixelsPerMetre, WorldElementContactListener contactListener)
	{
		setWidth(width);
		setHeight(height);

		this.world = world;
		this.pixelsPerMetre = pixelsPerMetre;
		this.contactListener = contactListener;

		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
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

		// Add some elements
		for (int i = 0; i < ELEMENT_COUNT; i++)
		{
			// Add some elements
			director.sendEvent(AppEvents.EVENT_ITEM_ADD, null);
		}
	}

	/**
	 * Called when layer is no longer part of visible view.
	 * 
	 */
	@Override
	public void exit()
	{
		// Remove this as an event observer.
		director.deregisterEventHandler(this);

		cleanupView();
	}

	/**
	 * Cleanup view elements.
	 * 
	 */
	@Override
	public void cleanupView()
	{
		int size = getChildren().size;
		while (size > 0)
		{
			Actor actor = getChildren().get(--size);
			removeActor(actor);
		}
	}

	/**
	 * Remove actor.
	 * 
	 * @param actor
	 *            The actor.
	 */
	@Override
	public boolean removeActor(Actor actor)
	{
		super.removeActor(actor);

		actor.clearActions();

		if (actor instanceof LabeledSprite)
		{
			// Free from pool.
			LabeledSprite ballSprite = (LabeledSprite) actor;
			itemPool.free(ballSprite);

			// Remove collision handler.
			DisplayContactElement element = ballSprite.getContactElement();
			contactListener.removeElement(element);
			worldElementPool.free(element);
		}

		return true;
	}

	/**
	 * Launch labelled sprite.
	 * 
	 */
	public LabeledSprite launchLabeledSprite(float xPos, float yPos, Character text)
	{
		// Check data available.
		// -----------------------------------------------------------
		// Sprite.
		// -----------------------------------------------------------
		LabeledSprite sprite = itemPool.obtain();

		// Size.
		float scaleX = (AppSize.BODY_SIZE.x * pixelsPerMetre) / sprite.getWidth();
		float scaleY = (AppSize.BODY_SIZE.y * pixelsPerMetre) / sprite.getHeight();
		sprite.setScaleX(scaleX);
		sprite.setScaleY(scaleY);

		// Centre of rotation.
		sprite.setOriginX(sprite.getWidth() / 2);
		sprite.setOriginY(sprite.getHeight() / 2);

		// Get Latest label
		sprite.setText(text);

		// Position
		sprite.setPosition(xPos, yPos);

		// -----------------------------------------------------------
		// Body
		// -----------------------------------------------------------

		// Assign random starting position and angle.
		float tx = xPos / pixelsPerMetre;
		float ty = yPos / pixelsPerMetre;
		float angle = 0;
		float radius = AppSize.BODY_SIZE.x / 2;

		Body body = createCircle(tx, ty, radius, angle, sprite);

		// -----------------------
		// Collision handler.
		// -----------------------
		DisplayContactElement contactElement = worldElementPool.obtain();

		// Linkage.
		contactElement.setBody(body);
		contactElement.setActor(sprite);
		sprite.setContactElement(contactElement);

		// Add to Box2D collision handlers.
		contactListener.addElement(contactElement);

		// -----------------------
		// Add actor to the view.
		// -----------------------
		addActor(sprite);

		// -----------------------
		// Update actor from Box2D body.
		// -----------------------
		BodyUpdateAction action = BodyUpdateAction.$(world, body, pixelsPerMetre, true);

		// -----------------------
		// Run action on actor.
		// -----------------------
		sprite.addAction(action);

		// -----------------------
		// Make the body lighter than air so it rises up by default.
		// -----------------------
		BodyGravityAction gravityAction = BodyGravityAction.$(world, body, LIGHTER_THAN_AIR_GRAVITY_Y, false);

		// -----------------------
		// Run action on actor.
		// -----------------------
		sprite.addAction(gravityAction);

		return sprite;
	}

	/**
	 * Create Box2D primitive.
	 * 
	 * @param tx
	 * @param ty
	 * @param radius
	 * @param angle
	 * @param sprite
	 * 
	 * @return The shape.
	 */
	public Body createCircle(float tx, float ty, float radius, float angle, Actor sprite)
	{
		// Shape
		CircleShape ballShape = new CircleShape();
		ballShape.setRadius(radius);

		// Fixture
		FixtureDef fdef = new FixtureDef();
		fdef.shape = ballShape;
		fdef.density = 1.0f;
		fdef.friction = 0.3f;
		fdef.restitution = 0.6f;

		// Body
		BodyDef bdef = new BodyDef();
		bdef.position.set(tx, ty);
		bdef.angle = angle;
		bdef.type = BodyType.DynamicBody;

		Body body = world.createBody(bdef);
		body.createFixture(fdef);

		body.setUserData(sprite);

		ballShape.dispose();

		// Properties
		body.setActive(true);
		body.setAwake(true);

		return body;
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
		case AppEvents.EVENT_ITEM_ADD:
			handleItemAdd();
			handled = true;
			break;
		case AppEvents.EVENT_ITEM_CLEAR:
			handleItemClear(event.getActor());
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Handle adding numbered element to view.
	 * 
	 */
	private void handleItemAdd()
	{
		float minX = AppSize.BODY_SIZE.x * pixelsPerMetre;
		float maxX = getWidth() - minX;

		float letterHeight = AppSize.BODY_SIZE.y * pixelsPerMetre;

		int value = randomGenerator.nextInt(9);

		// / Take item from our pool of items.
		Character sequence = String.valueOf(value).charAt(0);

		// Message height.
		float y = letterHeight;

		float x = ((float) Math.random() * getWidth());

		if (x < minX)
		{
			x = minX;
		}

		if (x > maxX)
		{
			x = maxX;
		}

		launchLabeledSprite(x, y, sequence);
	}

	/**
	 * Handle clearing letter element.
	 * 
	 */
	private void handleItemClear(Actor actor)
	{
		removeActor(actor);
	}

}

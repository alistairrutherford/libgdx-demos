package com.netthreads.gdx.app.layer.main;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppTextures;
import com.netthreads.gdx.app.sprite.FlareSprite;
import com.netthreads.gdx.app.sprite.PositionLabelSprite;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.sprite.AnimatedSprite;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Pointer layer handles animated pointers.
 * 
 */
public class PointerLayer extends Layer implements ActorEventObserver
{
	private static final float POINTER_ANIMATION_FRAME_DURATION = 0.25f;
	private static final float FLARE_ANIMATION_FRAME_DURATION = 0.1f;
	private static final float FLARE_ANIMATION_LOOP_DURATION = 1.0f;
	private static final int ITEM_POOL = 5;

	// -------------------------------------------------------------------
	// Sprite pool.
	// -------------------------------------------------------------------
	private Pool<FlareSprite> pool = new Pool<FlareSprite>(ITEM_POOL)
	{

		@Override
		protected FlareSprite newObject()
		{
			TextureDefinition definition = textureCache.getDefinition(AppTextures.TEXTURE_BLOOM);
			TextureRegion textureRegion = textureCache.getTexture(definition);
			FlareSprite sprite = new FlareSprite(textureRegion, 1, 4, FLARE_ANIMATION_FRAME_DURATION, FLARE_ANIMATION_LOOP_DURATION);

			// Lets add a position label.
			sprite.addActor(new PositionLabelSprite());

			return sprite;
		}
	};

	// Director of the action.
	private Director director;

	private TextureCache textureCache;

	// Animated pointer sprite
	private AnimatedSprite pointer;
	private Actor selected;

	/**
	 * Create main layer which composes all the main application layers.
	 * 
	 * @param width
	 * @param height
	 */
	public PointerLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);

		// ---------------------------------------------------------------
		// Animated pointer sprite.
		// ---------------------------------------------------------------
		TextureDefinition definition = textureCache.getDefinition(AppTextures.TEXTURE_POINTER);
		TextureRegion textureRegion = textureCache.getTexture(definition);
		pointer = new AnimatedSprite(textureRegion, 1, 4, POINTER_ANIMATION_FRAME_DURATION);
		pointer.setOriginX(pointer.getWidth() / 2);
		pointer.setOriginY(pointer.getHeight() / 2);

		// Initially not visible.
		pointer.setVisible(false);

		addActor(pointer);
	}

	/**
	 * Enter scene handler.
	 * 
	 */
	@Override
	public void enter()
	{
		super.enter();

		// Add this as an event observer.
		director.registerEventHandler(this);
	}

	/**
	 * Enter scene handler.
	 * 
	 */
	@Override
	public void exit()
	{
		super.exit();

		// Remove this as an event observer.
		director.deregisterEventHandler(this);
	}

	/**
	 * Adjust pointer position.
	 * 
	 * @param pointer
	 * @param selected
	 */
	private void position(Actor pointer, Actor selected)
	{
		pointer.setX(selected.getX() - (selected.getWidth() / 2));
		pointer.setY(selected.getY() - (selected.getHeight() / 2));
		pointer.setRotation(selected.getRotation());
	}

	/**
	 * Remove item from layer then free it from pool.
	 * 
	 */
	@Override
	public boolean removeActor(Actor actor)
	{
		super.removeActor(actor);

		actor.clearActions();

		FlareSprite sprite = (FlareSprite) actor;

		pool.free(sprite);

		return true;
	}

	/**
	 * Handle events.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		Actor actor = event.getActor();

		switch (event.getId())
		{
		case AppEvents.EVENT_BODY_TOUCH_UP:
			handleTouchUp(actor);
			handled = false;
			break;
		case AppEvents.EVENT_BODY_TOUCH_DRAGGED:
			handleTouchDragged(actor);
			handled = true;
			break;
		case AppEvents.EVENT_BODY_TOUCH_DOWN:
			handleTouchDown(actor);
			handled = true;
			break;
		case AppEvents.EVENT_POINTER_FLARE:
			handleFlare(actor);
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Override the draw to position to place our element pointer at the appropriate spot on the view.
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		if (pointer.isVisible())
		{
			position(pointer, selected);
		}

	}

	/**
	 * Handle touch down event from the TouchLayer.
	 * 
	 * @param actor
	 */
	private void handleTouchDown(Actor actor)
	{
		pointer.setVisible(true);
		selected = actor;

		position(pointer, selected);
	}

	/**
	 * Handle touch down event from the TouchLayer.
	 * 
	 * @param actor
	 */
	private void handleTouchDragged(Actor actor)
	{
		if (pointer.isVisible())
		{
			selected = actor;

			position(pointer, selected);
		}
	}

	/**
	 * Handle touch up event from the TouchLayer.
	 * 
	 * @param actor
	 */
	private void handleTouchUp(Actor actor)
	{
		// Hide the pointer sprite.
		pointer.setVisible(false);
	}

	/**
	 * Run flare sprite.
	 * 
	 * @param actor
	 */
	private void handleFlare(Actor actor)
	{
		FlareSprite flareSprite = pool.obtain();
		flareSprite.setOriginX(flareSprite.getWidth() / 2);
		flareSprite.setOriginY(flareSprite.getHeight() / 2);
		flareSprite.setScaleX(0.5f);
		flareSprite.setScaleY(0.5f);
		// We have to adjust the position to take into account odd positioning of world body.
		flareSprite.setX(actor.getX() - actor.getWidth() / 2);
		flareSprite.setY(actor.getY() - actor.getHeight() / 2);
		flareSprite.setRotation(actor.getRotation());

		addActor(flareSprite);

		flareSprite.run();
	}
}

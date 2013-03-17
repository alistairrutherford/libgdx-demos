package com.netthreads.gdx.core;

import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppSounds;
import com.netthreads.gdx.app.definition.AppTextures;
import com.netthreads.gdx.app.scene.AboutScene;
import com.netthreads.gdx.app.scene.MainScene;
import com.netthreads.gdx.app.scene.MenuScene;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Scene;
import com.netthreads.libgdx.scene.transition.MoveInBTransitionScene;
import com.netthreads.libgdx.scene.transition.MoveInTTransitionScene;
import com.netthreads.libgdx.scene.transition.TransitionScene;
import com.netthreads.libgdx.sound.SoundCache;
import com.netthreads.libgdx.texture.TextureCache;

/**
 * Application controller.
 * 
 */
public class Box2DBumpers extends InputAdapter implements ApplicationListener, ActorEventObserver
{
	public static final String VERSION_TEXT = "1.0.1";

	public static final String APPLICATION_NAME = "Scene2D - Box2D - Bumpers";

	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 480;

	private static final int DURATION_TRANSITION_TO_MENU = 500;
	private static final int DURATION_TRANSITION_TO_MAIN = 500;
	private static final int DURATION_TRANSITION_TO_ABOUT = 500;

	// Our 'scenes'.
	private MenuScene menuScene;
	private AboutScene aboutScene;
	private MainScene mainScene;

	// The one and only director.
	private Director director;

	private TextureCache textureCache;
	private SoundCache soundCache;

	/**
	 * Create components.
	 * 
	 */
	@Override
	public void create()
	{
		// Group.enableDebugging("data/group-debug.png");

		// ---------------------------------------------------------------
		// Director
		// ---------------------------------------------------------------

		// Requires Graphics context.
		director = AppInjector.getInjector().getInstance(Director.class);

		// Set initial width and height.
		director.setWidth(DEFAULT_WIDTH);
		director.setHeight(DEFAULT_HEIGHT);

		// Add this as an event observer.
		director.registerEventHandler(this);

		// ---------------------------------------------------------------
		// Textures
		// ---------------------------------------------------------------
		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);

		// Load/Re-load textures
		textureCache.load(AppTextures.TEXTURES);

		// ---------------------------------------------------------------
		// Sound
		// ---------------------------------------------------------------
		soundCache = AppInjector.getInjector().getInstance(SoundCache.class);

		// Load/Re-load sounds.
		soundCache.load(AppSounds.SOUNDS);

		// Set up director screen clear colour.
		director.setClearColourR(0.4f);
		director.setClearColourG(0.4f);
		director.setClearColourB(0.4f);

		// Set initial scene.
		director.setScene(getMenuScene());
	}

	/**
	 * Resize.
	 * 
	 */
	@Override
	public void resize(int width, int height)
	{
		// Recalculate scale factors for touch events.
		director.recalcScaleFactors(width, height);
	}

	/**
	 * Render view.
	 * 
	 */
	@Override
	public void render()
	{
		// Update director
		director.update();
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose()
	{
		// Graphics.
		textureCache.dispose();

		// Sounds.
		soundCache.dispose();

		// Views.
		director.setScene(null);

		// Cleanup application view.
		if (mainScene != null)
		{
			mainScene.cleanup();
		}
	}

	/**
	 * Generate scene.
	 * 
	 * @return The target scene.
	 */
	public MenuScene getMenuScene()
	{
		if (menuScene == null)
		{
			menuScene = new MenuScene();
		}

		return menuScene;
	}

	/**
	 * Generate scene.
	 * 
	 * @return The target scene.
	 */
	public AboutScene getAboutScene()
	{
		if (aboutScene == null)
		{
			aboutScene = new AboutScene();
		}

		return aboutScene;
	}

	/**
	 * Generate scene.
	 * 
	 * @return The target scene.
	 */
	public MainScene getMainScene()
	{
		if (mainScene == null)
		{
			mainScene = new MainScene();
		}

		return mainScene;
	}

	/**
	 * Handle controller events.
	 * 
	 * @param event
	 *            The actor event.
	 * 
	 * @return True if handled.
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case AppEvents.EVENT_TRANSITION_TO_MENU_SCENE:
			handleTransitionToMenuScene();
			handled = true;
			break;
		case AppEvents.EVENT_TRANSITION_TO_MAIN_SCENE:
			handleTransitionToMainScene();
			handled = true;
			break;
		case AppEvents.EVENT_TRANSITION_TO_ABOUT_SCENE:
			handleTransitionToAboutScene();
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Run transition.
	 * 
	 */
	private void handleTransitionToMenuScene()
	{
		Scene inScene = getMenuScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = MoveInTTransitionScene.$(inScene, outScene, DURATION_TRANSITION_TO_MENU, Linear.INOUT);

		director.setScene(transitionScene);
	}

	/**
	 * Run transition.
	 * 
	 */
	private void handleTransitionToMainScene()
	{
		Scene inScene = getMainScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = MoveInBTransitionScene.$(inScene, outScene, DURATION_TRANSITION_TO_MAIN, Linear.INOUT);

		director.setScene(transitionScene);
	}

	/**
	 * Run transition.
	 * 
	 */
	private void handleTransitionToAboutScene()
	{
		Scene inScene = getAboutScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = MoveInBTransitionScene.$(inScene, outScene, DURATION_TRANSITION_TO_ABOUT, Linear.INOUT);

		director.setScene(transitionScene);
	}

}

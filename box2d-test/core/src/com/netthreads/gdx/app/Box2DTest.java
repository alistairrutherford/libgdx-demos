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

package com.netthreads.gdx.app;

import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppSoundDefinitions;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.scene.AboutScene;
import com.netthreads.gdx.app.scene.AppScene;
import com.netthreads.gdx.app.scene.MenuScene;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.event.ActorEvent;
import com.netthreads.libgdx.event.ActorEventObserver;
import com.netthreads.libgdx.scene.Scene;
import com.netthreads.libgdx.scene.transition.MoveInBTransitionScene;
import com.netthreads.libgdx.scene.transition.MoveInLTransitionScene;
import com.netthreads.libgdx.scene.transition.MoveInRTransitionScene;
import com.netthreads.libgdx.scene.transition.MoveInTTransitionScene;
import com.netthreads.libgdx.scene.transition.TransitionScene;
import com.netthreads.libgdx.sound.SoundCache;
import com.netthreads.libgdx.texture.TextureCache;

/**
 * Simple Test application for LibGDX.
 * 
 */
public class Box2DTest extends InputAdapter implements ApplicationListener, ActorEventObserver
{
	public static final String VERSION_TEXT = "1.0.1";
	
	private static final float DEFAULT_CLEAR_COLOUR_RED = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_BLUE = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_GREEN = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_ALPHA = 1.0f;

	public static final String APPLICATION_NAME = "Box2D Test";
	
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 480;

	private static final int DURATION_ABOUT_TRANSITION = 500;
	private static final int DURATION_MENU_TRANSITION = 500;

	// Our 'stages' for groups of actors.
	private MenuScene menuScene;
	private AboutScene aboutScene;
	private AppScene appScene;

	/**
	 * The one and only director.
	 */
	private Director director;
	
	/**
	 * Singletons.
	 */
	private TextureCache textureCache;
	private SoundCache soundCache;

	/**
	 * Create components.
	 * 
	 */
	@Override
	public void create()
	{
		// Requires Graphics context.
		director = AppInjector.getInjector().getInstance(Director.class);
		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
		soundCache = AppInjector.getInjector().getInstance(SoundCache.class);
		
		director.setClearColourA(DEFAULT_CLEAR_COLOUR_ALPHA);
		director.setClearColourR(DEFAULT_CLEAR_COLOUR_RED);
		director.setClearColourG(DEFAULT_CLEAR_COLOUR_GREEN);
		director.setClearColourB(DEFAULT_CLEAR_COLOUR_BLUE);

		// Set initial width and height.
		director.setWidthHeight(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// Add this as an event observer.
		director.registerEventHandler(this);

		// Load/Re-load textures
		textureCache.load(AppTextureDefinitions.TEXTURES);

		// Load/Re-load sounds.
		soundCache.load(AppSoundDefinitions.SOUNDS);

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
		director.resize(width, height);
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
		
		// Graphics
		director.dispose();

		// Cleanup application view.
		if (appScene != null)
		{
			appScene.cleanup();
		}
	}

	/**
	 * Handle screen events.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		case AppEvents.EVENT_TRANSITION_TO_MENU_SCENE:
			transitionToMenuScene();
			handled = true;
			break;
		case AppEvents.EVENT_TRANSITION_TO_SIMULATION_SCENE:
			transitionToSimulationScene();
			handled = true;
			break;
		case AppEvents.EVENT_TRANSITION_TO_ABOUT_SCENE:
			transitionToAboutScene();
			handled = true;
			break;
		default:
			break;
		}

		return handled;
	}

	/**
	 * Run transition to menu.
	 * 
	 */
	private void transitionToMenuScene()
	{
		Scene inScene = getMenuScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = null;
		if (outScene instanceof AppScene)
		{
			transitionScene = MoveInTTransitionScene.$(inScene, outScene, DURATION_MENU_TRANSITION, Linear.INOUT);
		}
		else
		{
			transitionScene = MoveInLTransitionScene.$(inScene, outScene, DURATION_ABOUT_TRANSITION, Bounce.OUT);
		}

		director.setScene(transitionScene);
	}

	/**
	 * Run transition.
	 * 
	 */
	private void transitionToSimulationScene()
	{
		Scene inScene = getAppScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = MoveInBTransitionScene.$(inScene, outScene, DURATION_MENU_TRANSITION, Linear.INOUT);

		director.setScene(transitionScene);
	}

	/**
	 * Run transition.
	 * 
	 */
	private void transitionToAboutScene()
	{
		Scene inScene = getAboutScene();
		Scene outScene = director.getScene();

		TransitionScene transitionScene = MoveInRTransitionScene.$(inScene, outScene, DURATION_ABOUT_TRANSITION, Bounce.OUT);

		director.setScene(transitionScene);
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
	public AppScene getAppScene()
	{
		if (appScene == null)
		{
			appScene = new AppScene();
		}

		return appScene;
	}

}

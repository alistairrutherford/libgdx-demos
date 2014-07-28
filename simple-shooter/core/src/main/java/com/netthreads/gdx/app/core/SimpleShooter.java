/*
 * -----------------------------------------------------------------------
 * Copyright 2014 - Alistair Rutherford - www.netthreads.co.uk
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

package com.netthreads.gdx.app.core;

import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.InputAdapter;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppSoundDefinitions;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.scene.AboutScene;
import com.netthreads.gdx.app.scene.GameScene;
import com.netthreads.gdx.app.scene.MenuScene;
import com.netthreads.gdx.app.scene.SettingsScene;
import com.netthreads.gdx.app.scene.SplashScene;
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
 * Simple Shooter application for LibGDX.
 * 
 */
public class SimpleShooter extends InputAdapter implements ApplicationListener, ActorEventObserver
{
	public static final String VERSION_TEXT = "1.0.8";
	
	public static final int DEFAULT_WIDTH = 320;
	public static final int DEFAULT_HEIGHT = 480;
	
	private static final int DURATION_SETTINGS_TRANSITION = 700;
	private static final int DURATION_ABOUT_TRANSITION = 700;
	private static final int DURATION_GAME_TRANSITION = 900;
	private static final int DURATION_MENU_TRANSITION = 900;
	
	// Our 'stages' for groups of actors.
	private SplashScene splashScene;
	private AboutScene aboutScene;
	private MenuScene menuScene;
	private SettingsScene settingsScene;
	private GameScene gameScene;
	
	/**
	 * The one and only director.
	 */
	private Director director;
	
	/**
	 * Singletons.
	 */
	private TextureCache textureCache;
	private SoundCache soundCache;
	private AppStats appStats;
	
	/**
	 * Create components.
	 * 
	 */
	@Override
	public void create()
	{
		// Instantiate our singletons through Guice.
		director = AppInjector.getInjector().getInstance(Director.class);
		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
		soundCache = AppInjector.getInjector().getInstance(SoundCache.class);
		appStats = AppInjector.getInjector().getInstance(AppStats.class);
		
		// Set initial width and height.
		director.setWidth(DEFAULT_WIDTH);
		director.setHeight(DEFAULT_HEIGHT);
		
		// Add this as an event observer.
		director.registerEventHandler(this);
		
		// Load/Re-load textures
		textureCache.load(AppTextureDefinitions.TEXTURES);
		
		// Load/Re-load sounds.
		soundCache.load(AppSoundDefinitions.SOUNDS);
		
		// Get initial scene
		menuScene = getMenuScene();
		
		director.setScene(menuScene);
	}
	
	/**
	 * Resize.
	 * 
	 */
	@Override
	public void resize(int width, int height)
	{
		// Resize view-port and recalculate scale factors for touch events.
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
			case AppEvents.EVENT_DISPLAY_SPLASH_SCREEN:
				displaySplashScene();
				handled = true;
				break;
			case AppEvents.EVENT_TRANSITION_TO_MENU_SCENE:
				transitionToMenuScene();
				handled = true;
				break;
			case AppEvents.EVENT_TRANSITION_TO_SETTINGS_SCENE:
				transitionToSettingsScene();
				handled = true;
				break;
			case AppEvents.EVENT_TRANSITION_TO_GAME_SCENE:
				transitionToGameScene();
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
	 * Run transition.
	 * 
	 */
	private void displaySplashScene()
	{
		director.setScene(getSplashScene());
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
		if (outScene instanceof GameScene)
		{
			transitionScene = MoveInTTransitionScene.$(inScene, outScene, DURATION_MENU_TRANSITION, Linear.INOUT);
		}
		else
		{
			if (outScene instanceof SettingsScene)
			{
				transitionScene = MoveInRTransitionScene.$(inScene, outScene, DURATION_SETTINGS_TRANSITION, Bounce.OUT);
			}
			else
			{
				transitionScene = MoveInLTransitionScene.$(inScene, outScene, DURATION_ABOUT_TRANSITION, Bounce.OUT);
			}
		}
		
		director.setScene(transitionScene);
	}
	
	/**
	 * Run transition.
	 * 
	 */
	private void transitionToSettingsScene()
	{
		Scene inScene = getSettingsScene();
		Scene outScene = director.getScene();
		
		TransitionScene transitionScene = MoveInLTransitionScene.$(inScene, outScene, DURATION_SETTINGS_TRANSITION, Bounce.OUT);
		
		director.setScene(transitionScene);
	}
	
	/**
	 * Run transition.
	 * 
	 */
	private void transitionToGameScene()
	{
		// Reset scores.
		appStats.resetScore();
		
		Scene inScene = getGameScene();
		Scene outScene = director.getScene();
		
		TransitionScene transitionScene = MoveInBTransitionScene.$(inScene, outScene, DURATION_GAME_TRANSITION, Linear.INOUT);
		
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
	public SettingsScene getSettingsScene()
	{
		if (settingsScene == null)
		{
			settingsScene = new SettingsScene();
		}
		
		return settingsScene;
	}
	
	/**
	 * Generate scene.
	 * 
	 * @return The target scene.
	 */
	public GameScene getGameScene()
	{
		if (gameScene == null)
		{
			gameScene = new GameScene();
		}
		
		return gameScene;
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
	public SplashScene getSplashScene()
	{
		if (splashScene == null)
		{
			splashScene = new SplashScene();
		}
		
		return splashScene;
	}
}

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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.sprite.FadeLabelSprite;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Fading label layer.
 * 
 */
public class FadeLabelLayer extends Layer
{
	private static final int INITIAL_POOL_CAPACITY = 20;
	
	private static final String TEXT_POSITION_SEPARATOR = ",";
	
	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);
	
	/**
	 * The one and only director.
	 */
	private Director director;
	
	// -------------------------------------------------------------------
	// Label pool.
	// -------------------------------------------------------------------
	private Pool<FadeLabelSprite> pool = new Pool<FadeLabelSprite>(INITIAL_POOL_CAPACITY)
	{
		@Override
		protected FadeLabelSprite newObject()
		{
			FadeLabelSprite sprite = new FadeLabelSprite();
			
			return sprite;
		}
	};
	
	/**
	 * Create layer which displays the FPS in the bottom corner.
	 * 
	 * @param width
	 * @param height
	 */
	public FadeLabelLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);
		
		director = AppInjector.getInjector().getInstance(Director.class);
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		float touchX = (x * director.getScaleFactorX());
		float touchY = director.getHeight() - (y * director.getScaleFactorY());
		
		// Get free sprite from pool.
		FadeLabelSprite sprite = pool.obtain();
		
		// Make text.
		textStringBuilder.setLength(0);
		textStringBuilder.append(Math.round(x));
		textStringBuilder.append(TEXT_POSITION_SEPARATOR);
		textStringBuilder.append(Math.round(y));
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);
		
		// Position.
		sprite.setText(textStringBuilder);
		sprite.setX(touchX);
		sprite.setY(touchY);
		
		// Send an event to drop a ball at touch position
		director.sendEvent(AppEvents.EVENT_DROP_SHAPE, sprite);
		
		// Add actor to view will run until 'done'.
		addActor(sprite);
		
		// DO THIS AFTER ADDING TO VIEW.
		sprite.run();
		
		// Returning true will stop further event propogation.
		return true;
	}
	
	/**
	 * When the label 'fade' action has run it's course the completion handler
	 * will mark it for removal. This method will then catch the removal and
	 * also free it from the associated pool to be used again.
	 * 
	 * @return
	 * 
	 */
	@Override
	public boolean removeActor(Actor actor)
	{
		super.removeActor(actor);
		
		FadeLabelSprite sprite = (FadeLabelSprite) actor;
		sprite.clearActions();
		
		pool.free(sprite);
		
		return true;
	}
}

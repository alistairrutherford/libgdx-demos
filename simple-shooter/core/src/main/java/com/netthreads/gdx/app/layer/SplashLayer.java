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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Scene layer.
 * 
 */
public class SplashLayer extends Layer
{
	private TextureRegion background;

	/**
	 * Singletons.
	 */
	private TextureCache textureCache;

	/**
	 * Construct layer.
	 * 
	 * @param width
	 * @param height
	 */
	public SplashLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
		
		loadTextures();

		buildElements();
	}

	/**
	 * Load view textures.
	 * 
	 */
	private void loadTextures()
	{
		TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_SPLASH);
		background = textureCache.getTexture(definition);
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// ---------------------------------------------------------------
		// Background.
		// ---------------------------------------------------------------
		Image image = new Image(background);
		image.setWidth(getWidth());
		image.setHeight(getHeight());

		addActor(image);
	}
}

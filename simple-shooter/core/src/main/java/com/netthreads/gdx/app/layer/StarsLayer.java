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

import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.sprite.BackgroundSprite;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Scene layer.
 * 
 */
public class StarsLayer extends Layer
{
	private static final float BACKGROUND_FRAME_DURATION = 5.0f;

	private Texture background;
	private BackgroundSprite backGroundSprite;

	/**
	 * Singletons.
	 */
	private TextureCache textureCache;

	/**
	 * Create layer.
	 * 
	 * @param stage
	 */
	public StarsLayer(float width, float height)
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
		TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_BACKGROUND);

		background = textureCache.getTexture(definition).getTexture();
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
		backGroundSprite = new BackgroundSprite(background, this.getWidth(), this.getHeight(), BACKGROUND_FRAME_DURATION, Orientation.Portrait, -1);
	}

	/**
	 * Draw background.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		backGroundSprite.draw(batch, parentAlpha);
	}

}

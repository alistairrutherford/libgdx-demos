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

package com.netthreads.gdx.app.definition;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Singleton;
import com.netthreads.libgdx.texture.TextureDefinition;
import com.netthreads.libgdx.texture.TextureDefinitions;

/**
 * You can populate this if you are not using a pre-defined packed texture
 * created using the TexturePacker class.
 * 
 */
@Singleton
@SuppressWarnings("serial")
public class AppTextureDefinitions implements TextureDefinitions 
{
	public static final String TEXTURE_PATH = "data";
	
	public static final String TEXTURE_ASTEROID = "asteroid.png";
	public static final String TEXTURE_EXPLOSION = "explosion.png";
	public static final String TEXTURE_BACKGROUND = "background.png";
	public static final String TEXTURE_MENU_BACKGROUND = "menubackground.png";
	public static final String TEXTURE_PULSE = "pulse.png";
	public static final String TEXTURE_SHIPS = "ships.png";
	public static final String TEXTURE_SPLASH = "splash.png";
	public static final String TEXTURE_LIBGDX_LOGO = "libgdx.png";
	

	public static final List<TextureDefinition> TEXTURES = new LinkedList<TextureDefinition>()
	{
		{
			add(new TextureDefinition(TEXTURE_ASTEROID, TEXTURE_PATH + "/" + TEXTURE_ASTEROID));
			add(new TextureDefinition(TEXTURE_EXPLOSION, TEXTURE_PATH + "/" + TEXTURE_EXPLOSION, 1, 4));
			add(new TextureDefinition(TEXTURE_BACKGROUND, TEXTURE_PATH + "/" + TEXTURE_BACKGROUND));
			add(new TextureDefinition(TEXTURE_MENU_BACKGROUND, TEXTURE_PATH + "/" + TEXTURE_MENU_BACKGROUND));
			add(new TextureDefinition(TEXTURE_PULSE, TEXTURE_PATH + "/" + TEXTURE_PULSE, 1, 8));
			add(new TextureDefinition(TEXTURE_SHIPS, TEXTURE_PATH + "/" + TEXTURE_SHIPS, 1, 4));
			add(new TextureDefinition(TEXTURE_SPLASH, TEXTURE_PATH + "/" + TEXTURE_SPLASH));
			add(new TextureDefinition(TEXTURE_LIBGDX_LOGO, TEXTURE_PATH + "/" + TEXTURE_LIBGDX_LOGO));
		}
	};


	/**
	 * Return texture definitions.
	 * 
	 */
	@Override
    public List<TextureDefinition> getDefinitions()
    {
	    return TEXTURES;
    }

}

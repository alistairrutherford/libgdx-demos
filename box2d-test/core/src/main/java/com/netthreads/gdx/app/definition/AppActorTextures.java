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

import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * You can populate this if you are not using a pre-defined packed texture
 * created using the TexturePacker class.
 * 
 */
@SuppressWarnings("serial")
public class AppActorTextures
{
	public static final String TEXTURE_PATH = "data/gfx";
	
	public static final String TEXTURE_BALL = "ball.png";
	public static final String TEXTURE_COG = "cogA.png";
	public static final String TEXTURE_LIBGDX_LOGO = "libgdx.png";
	
	public static final List<TextureDefinition> TEXTURES = new LinkedList<TextureDefinition>()
	{
		{
			add(new TextureDefinition(TEXTURE_BALL, TEXTURE_PATH + "/" + TEXTURE_BALL));
			add(new TextureDefinition(TEXTURE_COG, TEXTURE_PATH + "/" + TEXTURE_COG));
			add(new TextureDefinition(TEXTURE_LIBGDX_LOGO, TEXTURE_PATH + "/" + TEXTURE_LIBGDX_LOGO));
		}
	};
	
}

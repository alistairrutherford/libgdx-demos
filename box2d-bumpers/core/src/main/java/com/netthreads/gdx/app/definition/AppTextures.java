package com.netthreads.gdx.app.definition;

import java.util.LinkedList;
import java.util.List;

import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * You can populate this if you are not using a predefined packed texture created using the TexturePacker class.
 * 
 */
@SuppressWarnings("serial")
public class AppTextures
{
	public static final String TEXTURE_PATH = "data/gfx";
	public static final String TEXTURE_LIBGDX_LOGO = "libgdx.png";
	public static final String TEXTURE_BLOCK_A = "blockA.png";
	public static final String TEXTURE_BLOCK_B = "blockB.png";
	public static final String TEXTURE_BLOCK_C = "blockC.png";
	public static final String TEXTURE_POINTER = "blowupW64.png";
	public static final String TEXTURE_BLOOM = "blowupC64.png";

	public static final List<TextureDefinition> TEXTURES = new LinkedList<TextureDefinition>()
	{
		{
			add(new TextureDefinition(TEXTURE_LIBGDX_LOGO, TEXTURE_PATH + "/" + TEXTURE_LIBGDX_LOGO));
			add(new TextureDefinition(TEXTURE_BLOCK_A, TEXTURE_PATH + "/" + TEXTURE_BLOCK_A));
			add(new TextureDefinition(TEXTURE_BLOCK_B, TEXTURE_PATH + "/" + TEXTURE_BLOCK_B));
			add(new TextureDefinition(TEXTURE_BLOCK_C, TEXTURE_PATH + "/" + TEXTURE_BLOCK_C));
			add(new TextureDefinition(TEXTURE_POINTER, TEXTURE_PATH + "/" + TEXTURE_POINTER));
			add(new TextureDefinition(TEXTURE_BLOOM, TEXTURE_PATH + "/" + TEXTURE_BLOOM));
		}
	};
}

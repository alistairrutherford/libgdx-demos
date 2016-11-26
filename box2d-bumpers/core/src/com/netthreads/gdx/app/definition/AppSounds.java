package com.netthreads.gdx.app.definition;

import java.util.LinkedList;
import java.util.List;

import com.netthreads.libgdx.sound.SoundDefinition;

/**
 * Populate this with sound definitions.
 * 
 * NOTE: This is not a very efficient way of doing this.
 * 
 */
@SuppressWarnings("serial")
public class AppSounds
{
	public static final String SOUND_PATH = "data/sounds";

	public static final String SOUND_CLINK = "clink.wav";
	public static final String SOUND_CLONK = "clonk.wav";

	public static final List<SoundDefinition> SOUNDS = new LinkedList<SoundDefinition>()
	{
		{
			add(new SoundDefinition(SOUND_CLINK, SOUND_PATH + "/" + SOUND_CLINK));
			add(new SoundDefinition(SOUND_CLONK, SOUND_PATH + "/" + SOUND_CLONK));
		}
	};
}

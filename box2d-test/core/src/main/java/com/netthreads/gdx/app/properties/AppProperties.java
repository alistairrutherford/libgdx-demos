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

package com.netthreads.gdx.app.properties;

/**
 * Global settings.
 * 
 */
public class AppProperties
{
	private static final float DEFAULT_OFFSET_TOUCH = 10.0f;
	private static final boolean DEFAULT_AUDIO_ON = false;
	private static final float DEFAULT_VOLUME = 0.1f;

	private static AppProperties gameProperties = null;

	private float touchOffset = DEFAULT_OFFSET_TOUCH;
	private boolean audioOn = DEFAULT_AUDIO_ON;
	private float volume = DEFAULT_VOLUME;

	/**
	 * Return singleton instance.
	 * 
	 * @return A single instance.
	 */
	public static synchronized AppProperties instance()
	{
		if (gameProperties == null)
		{
			gameProperties = new AppProperties();
		}

		return gameProperties;
	}

	public float getTouchOffset()
	{
		return touchOffset;
	}

	public void setTouchOffset(float touchOffset)
	{
		this.touchOffset = touchOffset;
	}

	public boolean isAudioOn()
	{
		return audioOn;
	}

	public void setAudioOn(boolean audioOn)
	{
		this.audioOn = audioOn;
	}

	public float getVolume()
	{
		return volume;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

}

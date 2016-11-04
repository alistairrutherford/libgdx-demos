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

import com.google.inject.Singleton;

/**
 * Global settings.
 * 
 */
@Singleton
public class ApplicationProperties
{
	private static final float DEFAULT_OFFSET_TOUCH = 10.0f;
	private static final boolean DEFAULT_AUDIO_ON = false;
	private static final float DEFAULT_VOLUME = 0.5f;

	private float touchOffset = DEFAULT_OFFSET_TOUCH;
	private boolean audioOn = DEFAULT_AUDIO_ON;
	private float volume = DEFAULT_VOLUME;

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

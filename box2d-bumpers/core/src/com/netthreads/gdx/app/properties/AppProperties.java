package com.netthreads.gdx.app.properties;

/**
 * Global settings.
 * 
 */
public class AppProperties
{
	private static final float DEFAULT_OFFSET_TOUCH = 10.0f;
	private static final boolean DEFAULT_AUDIO_ON = true;
	private static final float DEFAULT_VOLUME = 0.5f;

	private static AppProperties _instance = null;

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
		if (_instance == null)
		{
			_instance = new AppProperties();
		}

		return _instance;
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

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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.netthreads.gdx.app.core.AppStats;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.scene.Layer;

/**
 * Game statistics view.
 * 
 * Note: I have reused the fps StringBuilder technique from Robert Greens website:
 * http://www.rbgrn.net/content/290-light-racer-20-days-32-33-getting-great -game-performance
 * 
 */
public class StatsLayer extends Layer
{
	private static final String FONT_FILE_LARGE = "data/digital-dream-32.fnt";
	private static final String FONT_IMAGE_LARGE = "data/digital-dream-32.png";

	private static final String TEXT_SCORE = "Score:";

	private BitmapFont largeFont;

	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);

	private AppStats appStats;

	public StatsLayer(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);

		appStats = AppInjector.getInjector().getInstance(AppStats.class);
		
		buildElements();
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		largeFont = new BitmapFont(Gdx.files.internal(FONT_FILE_LARGE), Gdx.files.internal(FONT_IMAGE_LARGE), false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		textStringBuilder.setLength(0);
		textStringBuilder.append(TEXT_SCORE);
		textStringBuilder.append(appStats.getScore());
		textStringBuilder.getChars(0, textStringBuilder.length(), chars, 0);

		largeFont.draw(batch, textStringBuilder, 10, this.getHeight() - 20);
	}
}

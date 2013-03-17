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

package com.netthreads.gdx.app.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.netthreads.libgdx.action.ActionCallBack;
import com.netthreads.libgdx.action.CallBackDelayAction;
import com.netthreads.libgdx.sprite.AnimatedSprite;

/**
 * Represent a pulse sprite.
 * 
 */
public class FlareSprite extends AnimatedSprite implements ActionCallBack
{
	/**
	 * How long to run the animation loop for
	 */
	private float loopDuration;

	/**
	 * Create sprite.
	 * 
	 * @param texture
	 *            The animation texture.
	 * @param rows
	 *            The animation texture rows.
	 * @param cols
	 *            The animation texture rows.
	 * @param frameDuration
	 *            The animation frame duration.
	 * @param loopDuration
	 *            How long to run animation loop for.
	 * 
	 */
	public FlareSprite(TextureRegion textureRegion, int rows, int cols, float frameDuration, float loopDuration)
	{
		super(textureRegion, rows, cols, frameDuration);

		this.loopDuration = loopDuration;
	}

	/**
	 * Run action.
	 * 
	 */
	public void run()
	{
		// Let animation run for stated time then we'll trigger callback to set mark for removal
		CallBackDelayAction callBackDelayAction = CallBackDelayAction.$(1, loopDuration, this);

		addAction(callBackDelayAction);
	}

	/**
	 * Set mark to remove.
	 * 
	 */
	@Override
	public void onCallBack()
	{
		getParent().removeActor(this);
	}

}

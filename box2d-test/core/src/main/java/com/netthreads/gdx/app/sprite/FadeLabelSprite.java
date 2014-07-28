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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.netthreads.libgdx.action.ActionCallBack;
import com.netthreads.libgdx.action.CallBackAction;

/**
 * Label sprite which actions can run on.
 * 
 */
public class FadeLabelSprite extends Actor implements ActionCallBack
{
	private static final String FONT_FILE_SMALL = "data/font.fnt";
	private static final String FONT_IMAGE_SMALL = "data/font.png";
	
	private BitmapFont touchMessage;
	
	private static final float DEFAULT_FADE_TIME = 1.0f;
	private static float START_ALPHA = 1;
	private static float END_ALPHA = 0;
	private float fadeTime;
	
	private StringBuilder text = new StringBuilder(100);
	
	/**
	 * Create layer which displays the FPS in the bottom corner.
	 * 
	 * @param width
	 * @param height
	 */
	public FadeLabelSprite()
	{
		fadeTime = DEFAULT_FADE_TIME;
		
		buildElements();
	}
	
	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		touchMessage = new BitmapFont(Gdx.files.internal(FONT_FILE_SMALL), Gdx.files.internal(FONT_IMAGE_SMALL), false);
	}
	
	/**
	 * Run fade action.
	 * 
	 */
	public void run()
	{
		// Initialise alpha.
		getColor().a = START_ALPHA;
		
		AlphaAction fadeOut = Actions.alpha(END_ALPHA, fadeTime);
		
		// We will use a callback action to remove our label from the view. As
		// noted in removeActor we can't clear out the associated actions
		// because we are calling from within an action. Not sure that the
		// solution to that is.
		CallBackAction callBackAction = CallBackAction.$(this);
		
		addAction(Actions.sequence(fadeOut, callBackAction));
	}
	
	/**
	 * Draw touch message.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		// Fade action is running against the Actor colour so assign it to the
		// text.
		touchMessage.setColor(getColor());
		
		touchMessage.setScale(getScaleX(), getScaleY());
		
		touchMessage.draw(batch, text, getX(), getY());
	}
	
	/**
	 * Assign text.
	 * 
	 * @param text
	 */
	public void setText(CharSequence text)
	{
		this.text.setLength(0);
		this.text.append(text);
	}
	
	/**
	 * On callback remove the actor.
	 * 
	 * TODO: Find a better way to do this.
	 */
	@Override
	public void onCallBack()
	{
		getParent().removeActor(this);
	}
	
}

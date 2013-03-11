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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.definition.AppTextureDefinitions;
import com.netthreads.gdx.app.properties.GameProperties;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinition;

/**
 * Scene layer.
 * 
 */
public class SettingsLayer extends Layer
{
	private static final String UI_FILE = "data/uiskin60.json";
	private static final String URL_LABEL_FONT = "default-font";

	private Table table;
	private TextureRegion background;
	private Skin skin;

	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Singletons
	 */
	private GameProperties gameProperties;
	private TextureCache textureCache;

	/**
	 * Construct layer.
	 * 
	 * @param width
	 * @param height
	 */
	public SettingsLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		director = AppInjector.getInjector().getInstance(Director.class);

		textureCache = AppInjector.getInjector().getInstance(TextureCache.class);
		
		gameProperties = AppInjector.getInjector().getInstance(GameProperties.class);
		
		Gdx.input.setCatchBackKey(true);

		loadTextures();

		buildElements();
	}

	/**
	 * Load view textures.
	 * 
	 */
	private void loadTextures()
	{
		TextureDefinition definition = textureCache.getDefinition(AppTextureDefinitions.TEXTURE_MENU_BACKGROUND);
		background = textureCache.getTexture(definition);

		skin = new Skin(Gdx.files.internal(UI_FILE));
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// ---------------------------------------------------------------
		// Background.
		// ---------------------------------------------------------------
		Image image = new Image(background);

		image.setWidth(getWidth());
		image.setHeight(getHeight());

		addActor(image);

		// ---------------------------------------------------------------
		// Elements
		// ---------------------------------------------------------------
		final Label titleLabel = new Label("Settings", skin, URL_LABEL_FONT, Color.YELLOW);
		
		final CheckBox checkBox = new CheckBox("", skin);
		checkBox.setChecked(gameProperties.isAudioOn());
		checkBox.size(20, 20);

		final Label soundLabel = new Label("Sound", skin);

		final Slider slider = new Slider(0, 10, 1, false, skin);
		slider.setValue(gameProperties.getVolume());
		final Label volumeLabel = new Label("Volume", skin);
		
		// ---------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------
		table = new Table();

		table.setSize(getWidth(), getHeight());

		table.row();
		table.add(titleLabel).expandY().expandX();
		table.row();
		table.add(soundLabel).expandY();
		table.row();
		table.add(checkBox);
		table.row();
		table.add(volumeLabel).expandY().expandX();
		table.row();
		table.add(slider);
		table.row();

		table.setFillParent(true);

		table.pack();

		addActor(table);

		// Handlers
		checkBox.addListener(new ClickListener()
		{

			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				boolean setting = checkBox.isChecked();

				gameProperties.setAudioOn(setting);
			}

		});

		slider.addListener(new ChangeListener()
		{

			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				Slider slider = (Slider) actor;

				if (slider.getValue() == 0)
				{
					gameProperties.setVolume(0);
				}
				else
				{
					gameProperties.setVolume(slider.getValue() / 10);
				}
			}

		});
	}

	/**
	 * Catch escape key.
	 * 
	 */
	@Override
	public boolean keyUp(int keycode)
	{
		boolean handled = false;

		if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
		{
			director.sendEvent(AppEvents.EVENT_TRANSITION_TO_MENU_SCENE, this);

			handled = true;
		}

		return handled;
	}

}

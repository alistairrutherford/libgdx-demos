/*
 * -----------------------------------------------------------------------
 * Copyright 2014 - Alistair Rutherford - www.netthreads.co.uk
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.app.properties.ApplicationProperties;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Scene layer.
 * 
 */
public class SettingsLayer extends Layer
{
	private static final String UI_FILE = "data/uiskin.json";
	private static final String URL_LABEL_FONT_NORMAL_BOLD = "medium-font-bold";
	private static final String URL_LABEL_FONT_NORMAL = "medium-font";

	private static final String TEXT_SETTINGS = "Settings";
	private static final String TEXT_SOUND = "SOUND";
	private static final String TEXT_VOLUME = "Volume";

	private Table table;
	private Skin skin;

	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Singletons
	 */
	private ApplicationProperties applicationProperties;

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

		applicationProperties = AppInjector.getInjector().getInstance(ApplicationProperties.class);

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
		skin = new Skin(Gdx.files.internal(UI_FILE));
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// ---------------------------------------------------------------
		// Elements
		// ---------------------------------------------------------------
		final Label titleLabel = new Label(TEXT_SETTINGS, skin, URL_LABEL_FONT_NORMAL_BOLD, Color.YELLOW);

		final CheckBox checkBox = new CheckBox("", skin);
		checkBox.setChecked(applicationProperties.isAudioOn());
		checkBox.setSize(20, 20);

		final Label soundLabel = new Label(TEXT_SOUND, skin, URL_LABEL_FONT_NORMAL, Color.WHITE);

		final Slider slider = new Slider(0, 10, 1, false, skin);
		slider.setValue(applicationProperties.getVolume());
		final Label volumeLabel = new Label(TEXT_VOLUME, skin, URL_LABEL_FONT_NORMAL, Color.WHITE);

		final Label padding = new Label("", skin);
		padding.setHeight(20);

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
		for (int i = 0; i < 20; i++)
		{
			table.add(padding);
			table.row();
		}

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

				applicationProperties.setAudioOn(setting);
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
					applicationProperties.setVolume(0);
				}
				else
				{
					applicationProperties.setVolume(slider.getValue() / 10);
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
